package mmm.lib.DestroyAll;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.LinkedList;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

/**
 * 一括破壊対象を判定するマネージャ。
 *
 */
public class DestroyAllManager {

	public static final byte Flag_isUnder			= 0x01;
	public static final byte Flag_isOtherDirection	= 0x02;
	public static final byte Flag_isBreakLeaves		= 0x04;
	public static final byte Flag_isTheBig			= 0x08;


	public static DestroyAllManager instance;
	public static String packetChannel = "MMM|DAM";
	public static FMLEventChannel serverEventChannel;


	private class DestroyPos {
		public int X;
		public int Y;
		public int Z;
		
		public DestroyPos(int pX, int pY, int pZ) {
			X = pX;
			Y = pY;
			Z = pZ;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof DestroyPos) {
				DestroyPos ldo = (DestroyPos)obj;
				return (X == ldo.X) && (Y == ldo.Y) && (Z == ldo.Z);
			}
			return false;
		}
		
	}
	public static LinkedList<DestroyPos> destroyList = new LinkedList<DestroyPos>();
	public static int rangeWidth;
	public static int rangeHeight;
	public static int ox;
	public static int oy;
	public static int oz;


	public static void init() {
		if (instance instanceof DestroyAllManager) return;
		
		// ネットワークのハンドラを登録
		instance = new DestroyAllManager();
		serverEventChannel = NetworkRegistry.INSTANCE.newEventDrivenChannel(packetChannel);
		serverEventChannel.register(instance);
	}

	/**
	 * 一括破壊用パケットを送る。
	 * @param pEntity
	 * @param pWidth
	 * @param pHeight
	 * @param pX
	 * @param pY
	 * @param pZ
	 * @param pFlags
	 * @param pIdentificator
	 */
	public static void sendDestroyAllPacket(Entity pEntity, int pWidth, int pHeight,
			int pX, int pY, int pZ, int pFlags, DestroyAllIdentificator pIdentificator) {
		System.out.println("Start DestroyAll.");
		ByteBuf lbuf = Unpooled.buffer();
		// get block
		Block lblock = pEntity.worldObj.func_147439_a(pX, pY, pZ);
		int lblockid = Block.func_149682_b(lblock);
		int lmetadata = pEntity.worldObj.getBlockMetadata(pX, pY, pZ);
		
		// EntityID
		lbuf.writeInt(pEntity.func_145782_y());
		// Range W H
		lbuf.writeInt(pWidth);
		lbuf.writeInt(pHeight);
		// Target Pos
		lbuf.writeInt(pX);
		lbuf.writeInt(pY);
		lbuf.writeInt(pZ);
		// Target Block
		lbuf.writeInt(lblockid);
		lbuf.writeByte(lmetadata);
		// Flags
		lbuf.writeByte(pFlags);
		// TargetBlocks
		if (pIdentificator != null) {
			lbuf.writeBytes(pIdentificator.toByte());
		}
		// ChainBlocks
		if (pIdentificator.chain != null) {
			lbuf.writeBytes(pIdentificator.chain.toByte());
		}
		
		FMLProxyPacket lpacket = new FMLProxyPacket(lbuf, packetChannel);
		serverEventChannel.sendToServer(lpacket);
	}

	@SubscribeEvent
	public void onGetPacketServer(FMLNetworkEvent.ServerCustomPacketEvent pEvent) {
		// パケット受信
		System.out.println("get DestroyAll Packet from Client." + pEvent.packet.channel());
		if (pEvent.packet.channel().contentEquals(packetChannel)) {
			System.out.println("get DestroyAll Packet from Client.");
			ByteBuf lbuf = Unpooled.wrappedBuffer(pEvent.packet.payload());
			
			lbuf.resetReaderIndex();
			int lblockid;
			System.out.println(String.format("EntityID: %d; W/H: %d/ %d; Pos: %d, %d, %d; Block: %d-%d; Flag: %x;",
					lbuf.readInt(), lbuf.readInt(), lbuf.readInt(),
					lbuf.readInt(), lbuf.readInt(), lbuf.readInt(),
					lblockid = lbuf.readInt(), lbuf.readByte(), lbuf.readByte()
					));
			System.out.println(Block.func_149729_e(lblockid).toString());

			
			lbuf.resetReaderIndex();
			if (pEvent.handler instanceof NetHandlerPlayServer) {
				// プレーヤー以外が発動するのを考慮すべき？
				EntityPlayerMP lentity;
				lentity = ((NetHandlerPlayServer)pEvent.handler).field_147369_b;
//				lentity = lentity.worldObj.getEntityByID(lbuf.readInt());
				lbuf.readInt();
				defaultDestroyAll(lentity, lbuf.readInt(), lbuf.readInt(),
						lbuf.readInt(), lbuf.readInt(), lbuf.readInt(),
						Block.func_149729_e(lbuf.readInt()), (int)lbuf.readByte(),
						(int)lbuf.readByte(), new DestroyAllIdentificator(lbuf));
			}
			
		}
	}

	public void defaultDestroyAll(EntityPlayerMP pEntity, int pWidth, int pHeight,
			int pX, int pY, int pZ, Block pBlock, int pMetadata, int pFlags,
			DestroyAllIdentificator pIdentificator) {
		// 標準破壊処理
		destroyList.clear();
		
		ox = pX;
		oy = pY;
		oz = pZ;
		rangeWidth = pWidth;
		rangeHeight = pHeight;
		
		checkAround(pEntity, pX, pY, pZ, pBlock, pMetadata, pIdentificator, false);
		
		DestroyPos ldp;
		while ((ldp = destroyList.poll()) != null) {
			checkAround(pEntity, ldp.X, ldp.Y, ldp.Z, pBlock, pMetadata, pIdentificator, false);
		}
		
		
		
	}

	/**
	 * フラグ作成用
	 * @param pDestroyUnder
	 * @param pOtherDirection
	 * @param pChain
	 * @param pTheBig
	 * @return
	 */
	public static int getFlags(boolean pDestroyUnder, boolean pOtherDirection, boolean pChain, boolean pTheBig) {
		int lflags = 0;
		lflags |= pDestroyUnder ? Flag_isUnder : 0;
		lflags |= pOtherDirection ? Flag_isOtherDirection : 0;
		lflags |= pChain ? Flag_isBreakLeaves : 0;
		lflags |= pTheBig ? Flag_isTheBig : 0;
		
		return lflags;
	}

	private void checkAround(EntityPlayerMP pEntity, int pX, int pY, int pZ, Block pBlock, int pMeta, DestroyAllIdentificator pDAI, boolean pTheBig) {
		// 周囲をチェックして作業キューへ入れる。
		if ((pX - ox) < rangeWidth) {
			checkBlock(pEntity, pX + 1, pY, pZ, pBlock, pMeta, pDAI, pTheBig);
		}
		if ((ox - pX) < rangeWidth) {
			checkBlock(pEntity, pX - 1, pY, pZ, pBlock, pMeta, pDAI, pTheBig);
		}
		if ((pZ - oz) < rangeWidth) {
			checkBlock(pEntity, pX, pY, pZ + 1, pBlock, pMeta, pDAI, pTheBig);
		}
		if ((oz - pZ) < rangeWidth) {
			checkBlock(pEntity, pX, pY, pZ - 1, pBlock, pMeta, pDAI, pTheBig);
		}
		if ((pY - oy) < rangeWidth) {
			checkBlock(pEntity, pX, pY + 1, pZ, pBlock, pMeta, pDAI, pTheBig);
		}
		if ((oy - pY) < rangeWidth) {
			checkBlock(pEntity, pX, pY - 1, pZ, pBlock, pMeta, pDAI, pTheBig);
		}
		
	}

	private boolean checkBlock(EntityPlayerMP pEntity, int pX, int pY, int pZ, Block pBlock, int pMeta, DestroyAllIdentificator pDAI, boolean pTheBig) {
		if (pDAI.isTargetBlock(pEntity.worldObj, pX, pY, pZ)) {
			addDestroyList(pX, pY, pZ);
			pEntity.theItemInWorldManager.uncheckedTryHarvestBlock(pX, pY, pX);
			return true;
		}
		return false;
	}

	private boolean addDestroyList(int pX, int pY, int pZ) {
		// 重複チェックしながらスタック
		DestroyPos lpos = new DestroyPos(pX, pY, pZ);
		if (destroyList.contains(lpos)) {
			return false;
		}
		destroyList.offer(lpos);
		return true;
	}

}
