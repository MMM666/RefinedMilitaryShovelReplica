package mmm.lib.DestroyAll;

import io.netty.buffer.ByteBuf;

import java.util.LinkedList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;

public class DestroyAllData {

	public EntityPlayerMP breaker;
	public int rangeWidth;
	public int rangeHeight;
	public int x;
	public int y;
	public int z;
	public Block block;
	public int metadata;
	public int flag;
	
	public DestroyAllIdentificator identificator;
	
	public LinkedList<int[]> targets;


	public DestroyAllData(EntityPlayerMP pPlayer, ByteBuf pBuf) {
		breaker = pPlayer;
		rangeWidth	= pBuf.readInt();
		rangeHeight	= pBuf.readInt();
		x = pBuf.readInt();
		y = pBuf.readInt();
		z = pBuf.readInt();
		block = Block.getBlockById(pBuf.readInt());
		metadata = (int)pBuf.readByte();
		flag = pBuf.readByte();
		identificator = new DestroyAllIdentificator(pBuf);
	}

	@Override
	public String toString() {
		return String.format("%s; w:%d, h:%d; pos:%d, %d, %d; %s:0x%02x; flag:%02x; %s",
				breaker.toString(), rangeWidth, rangeHeight, x, y, z,
				block.toString(), metadata, flag, identificator.toString());
	}

	public boolean isUnder() {
		return (flag & DestroyAllManager.Flag_isUnder) > 0;
	}

	public boolean isOtherDirection() {
		return (flag & DestroyAllManager.Flag_isOtherDirection) > 0;
	}

	public boolean isBreakLeaves() {
		return (flag & DestroyAllManager.Flag_isBreakLeaves) > 0;
	}

	public boolean isTheBig() {
		return (flag & DestroyAllManager.Flag_isTheBig) > 0;
	}

}
