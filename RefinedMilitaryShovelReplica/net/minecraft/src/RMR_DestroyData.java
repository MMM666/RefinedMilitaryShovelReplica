package net.minecraft.src;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.LinkedList;

public class RMR_DestroyData {

	public static final byte RMR_Flag_isUnder			= 0x01;
	public static final byte RMR_Flag_isOtherDirection	= 0x02;
	public static final byte RMR_Flag_isBreakLeaves		= 0x04;
	public static final byte RMR_Flag_isTheBig			= 0x08;

	public byte mode;
	public EntityLivingBase entity;
	public ItemStack itemstack;
	public int maxRangeW;
	public int maxRangeH;
	public int baseX;
	public int baseY;
	public int baseZ;
	public World world;
	public int[] blockIDs;
	public int[] metadatas;
	public int[] masks;
	public RMR_IDestroyAll destroyAll;
	public LinkedList<int[]> destroyList = new LinkedList<int[]>();
	public boolean checkUnder;
	public int flags;


	public RMR_DestroyData(World pWorld, byte[] pData) {
		/**
		 * byte mode
		 * int EntityID
		 * int range
		 * int x
		 * int y
		 * int z
		 * 	int BlockID
		 * 	int Metadata
		 * 		|
		 */
		DataInputStream ldi = new DataInputStream(new ByteArrayInputStream(pData));
		try {
			world = pWorld;
			mode = ldi.readByte();
			if ((mode & 0x80) > 0) {
				Entity le = pWorld.getEntityByID(ldi.readInt());
				if (le instanceof EntityLivingBase) {
					entity = (EntityLivingBase)le;
					itemstack = entity.getCurrentItemOrArmor(0);
					if (itemstack != null && itemstack.getItem() instanceof RMR_IDestroyAll) {
						destroyAll = (RMR_IDestroyAll)itemstack.getItem();
					}
				}
				world = entity.worldObj;
				maxRangeW = maxRangeH = ldi.readInt();
				flags = ldi.readByte();
				checkUnder = (flags & 0x01) > 0;
				baseX = ldi.readInt();
				baseY = ldi.readInt();
				baseZ = ldi.readInt();
				int li = ldi.available() / 5;
				blockIDs = new int[li];
				metadatas = new int[li];
				masks = new int[li];
				for (int lj = 0; lj < li; lj++) {
					blockIDs[lj] = ldi.readInt();
					metadatas[lj] = ldi.readByte();
					masks[lj] = 0x0f ^ (metadatas[lj] >>> 4);
					metadatas[lj] = metadatas[lj] & 0x0f;
				}
			}
			ldi.close();
		} catch (Exception e) {
			
		}
	}

	public static void sendMessage(int pMode, EntityLivingBase pEntity, int pRange,
			byte pFlags, int pX, int pY, int pZ, int[] pBlocks, int[] pMetadatas) {
		ByteArrayOutputStream lbos = new ByteArrayOutputStream();
		DataOutputStream ldo = new DataOutputStream(lbos);
		
		try {
			ldo.writeByte(pMode);
			ldo.writeInt(pEntity.entityId);
			ldo.writeInt(pRange);
			ldo.writeByte(pFlags);
			ldo.writeInt(pX);
			ldo.writeInt(pY);
			ldo.writeInt(pZ);
			for (int li = 0; li < pBlocks.length; li++) {
				ldo.writeInt(pBlocks[li]);
				ldo.writeByte(pMetadatas[li]);
			}
			ldo.close();
			ModLoader.clientSendPacket(new Packet250CustomPayload(mod_RMR_RefinedMilitaryShovelReplica.packetChannel, lbos.toByteArray()));
			lbos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 
	 * @return 一括破壊対象であったならtrueを返す。
	 */
	protected boolean checkBlockRect(int pX, int pY, int pZ, int pTargetBlocks) {
		if (Math.abs(baseX - pX) > maxRangeW) {
			return false;
		}
		if (Math.abs(baseY - pY) > maxRangeH) {
			return false;
		}
		if (Math.abs(baseZ - pZ) > maxRangeW) {
			return false;
		}
		if (!checkUnder && baseY > pY) {
			return false;
		}
		if (itemstack.stackSize <= 0) {
			return false;
		}
		int lblockid = world.getBlockId(pX, pY, pZ);
		if (lblockid == 0) {
			return false;
		}
		int lmetadata = world.getBlockMetadata(pX, pY, pZ);
		for (int li = 0; li < blockIDs.length && li < pTargetBlocks; li++) {
			int lmmask = masks[li];
			int lmmeta = metadatas[li] & lmmask;
			if (lblockid == blockIDs[li] && (lmetadata & lmmask) == lmmeta) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @return 一括破壊対象であったならtrueを返す。
	 */
	protected boolean checkBlockRange(int pX, int pY, int pZ, int pRange) {
		if (pRange <= 0) {
			return false;
		}
		if (itemstack.stackSize <= 0) {
			return false;
		}
		int lblockid = world.getBlockId(pX, pY, pZ);
		if (lblockid == 0) {
			return false;
		}
		int lmetadata = world.getBlockMetadata(pX, pY, pZ);
		for (int li = 1; li < blockIDs.length; li++) {
			int lmmask = masks[li];
			int lmmeta = metadatas[li] & lmmask;
			if (lblockid == blockIDs[li] && (lmetadata & lmmask) == lmmeta) {
				return true;
			}
		}
		return false;
	}


	/**
	 * 目標周囲のブロックをチェックする。
	 * @param pX
	 * @param pY
	 * @param pZ
	 */
	public void checkAround(int pX, int pY, int pZ, int pTargetBlocks) {
		if (checkBlockRect(pX - 1, pY, pZ, pTargetBlocks)) {
			destroyList.offer(new int[] {pX - 1, pY, pZ});
		}
		if (checkBlockRect(pX + 1, pY, pZ, pTargetBlocks)) {
			destroyList.offer(new int[] {pX + 1, pY, pZ});
		}
		if (checkBlockRect(pX, pY, pZ - 1, pTargetBlocks)) {
			destroyList.offer(new int[] {pX, pY, pZ - 1});
		}
		if (checkBlockRect(pX, pY, pZ + 1, pTargetBlocks)) {
			destroyList.offer(new int[] {pX, pY, pZ + 1});
		}
		if (checkBlockRect(pX, pY - 1, pZ, pTargetBlocks)) {
			destroyList.offer(new int[] {pX, pY - 1, pZ});
		}
		if (checkBlockRect(pX, pY + 1, pZ, pTargetBlocks)) {
			destroyList.offer(new int[] {pX, pY + 1, pZ});
		}
	}

	protected boolean destroyBlock(int pX, int pY, int pZ, boolean isConsumption) {
		int lblockid = world.getBlockId(pX, pY, pZ);
		int lmetadata = world.getBlockMetadata(pX, pY, pZ);
		if (world.getBlockId(pX, pY, pZ) == 0) {
			return false;
		}
		Block lblock = Block.blocksList[lblockid];
		if (lblock != null) {
			boolean lharvest = false;
			if (lblock.blockMaterial.isToolNotRequired()) {
				lharvest = true;
			} else {
				lharvest = itemstack.canHarvestBlock(lblock);
			}
			
			if (isConsumption) {
				// ツールの耐久力を減らす。
				destroyAll.superOnBlockDestroyed(itemstack, world, lblock.blockID, pX, pY, pZ, entity);
			}
			
			boolean lflag = world.setBlockToAir(pX, pY, pZ);
			
			if (entity instanceof EntityPlayer) {
				EntityPlayer lep = (EntityPlayer)entity;
				
				lblock.onBlockHarvested(world, pX, pY, pZ, lmetadata, lep);
				if (lflag) {
					lblock.onBlockDestroyedByPlayer(world, pX, pY, pZ, lmetadata);
				}
				if (itemstack.stackSize == 0) {
					lep.destroyCurrentEquippedItem();
				}
				if (lharvest && lflag) {
					lblock.harvestBlock(world, lep, pX, pY, pZ, lmetadata);
				}
			}
			return true;
		}
		world.setBlockToAir(pX, pY, pZ);
		return false;
	}

	public boolean isFlag(byte pFlag) {
		return (flags & pFlag) > 0;
	}

}
