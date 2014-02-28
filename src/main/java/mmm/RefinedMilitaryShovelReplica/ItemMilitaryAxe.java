package mmm.RefinedMilitaryShovelReplica;

import java.util.LinkedList;

import mmm.lib.destroyAll.DestroyAllData;
import mmm.lib.destroyAll.DestroyAllIdentificator;
import mmm.lib.destroyAll.DestroyAllManager;
import mmm.lib.destroyAll.IDestroyAll;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class ItemMilitaryAxe extends ItemAxe implements IDestroyAll {

	protected static DestroyAllIdentificator[] targetBlocks;
	protected LinkedList<int[]> destroyLeaveList = new LinkedList<int[]>();
	protected Item nextItem;


	@Override
	public void setTargets(DestroyAllIdentificator[] pBlocks) {
		targetBlocks = pBlocks;
	}

	protected ItemMilitaryAxe(ToolMaterial par2EnumToolMaterial) {
		super(par2EnumToolMaterial);
		setMaxDamage(par2EnumToolMaterial.getMaxUses() * RefinedMilitaryShovelReplica.MaxDamage);
		setCreativeTab(null);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,
			EntityPlayer par3EntityPlayer) {
		par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
		return super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World,
			EntityPlayer par3EntityPlayer, int par4) {
		if (nextItem != null) {
			par1ItemStack.func_150996_a(nextItem);
		}
		super.onPlayerStoppedUsing(par1ItemStack, par2World, par3EntityPlayer, par4);
	}

	@Override
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		if (par2 == 1) {
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			return  0xff8888;
//			return  RefinedMilitaryShovelReplica.isDestroyEnable ? 0xff8888 : 0xffffff;
		} else {
			return 0xffffff;
		}
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return RefinedMilitaryShovelReplica.isDestroyEnable;
	}

	@Override
	public boolean superOnBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityPlayer player) {
		return super.onBlockStartBreak(itemstack, X, Y, Z, player);
	}

	/*
	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z,
			EntityPlayer player) {
		if (RefinedMilitaryShovelReplica.isDestroyEnable && player.isClientWorld()) {
			for (int li = 0; li < targetBlockIDs.length; li++) {
				if (targetBlockIDs[li][0] == RefinedMilitaryShovelReplica.selectedblockID) {
					byte lflag = 0;
					lflag |= RefinedMilitaryShovelReplica.cutUnder ? DestroyData.RMR_Flag_isUnder : 0;
					lflag |= RefinedMilitaryShovelReplica.cutOtherDirection ? DestroyData.RMR_Flag_isOtherDirection : 0;
					lflag |= RefinedMilitaryShovelReplica.cutBreakLeaves ? DestroyData.RMR_Flag_isBreakLeaves : 0;
					lflag |= RefinedMilitaryShovelReplica.cutTheBig ? DestroyData.RMR_Flag_isTheBig : 0;
							
					Block lblock = Block.blocksList[targetBlockIDs[li][0]];
					int lmmask = targetMetadatas[li][0];
					if (RefinedMilitaryShovelReplica.cutOtherDirection) {
						lmmask = (lmmask == 0 && lblock instanceof BlockLog) ? 0xC0 : lmmask;
					}
					int[] lmetas = new int[targetMetadatas[li].length];
					if (lmmask > 0) {
						int lmmeta = RefinedMilitaryShovelReplica.selectedMetadata & 0x0f;
						for (int  lj = 0; lj < lmetas.length; lj++) {
							lmetas[lj] = lmmask | lmmeta;
						}
						DestroyData.sendMessage(0x82, player,
								RefinedMilitaryShovelReplica.cutLimit,
								lflag, X, Y, Z, targetBlockIDs[li], lmetas);
						RefinedMilitaryShovelReplica.Debug("Start CutAll.");
					} else 
					if (targetMetadatas[li][0] == RefinedMilitaryShovelReplica.selectedMetadata) {
						DestroyData.sendMessage(0x82, player,
								RefinedMilitaryShovelReplica.cutLimit,
								lflag, X, Y, Z, targetBlockIDs[li], targetMetadatas[li]);
						RefinedMilitaryShovelReplica.Debug("Start CutAll.");
					}
				}
			}
		}
		return super.onBlockStartBreak(itemstack, X, Y, Z, player);
	}

	@Override
	public boolean destroyAll(DestroyData pDData) {
		destroyLeaveList.clear();
		pDData.destroyList.clear();
		checkAroundLog(pDData, pDData.baseX, pDData.baseY, pDData.baseZ);
		checkAroundLeaves(pDData, pDData.baseX, pDData.baseY, pDData.baseZ, 5);
		
		int lpos[];
		while ((lpos = pDData.destroyList.poll()) != null) {
			destroyAround(pDData, lpos[0], lpos[1], lpos[2]);
		}
		while ((lpos = destroyLeaveList.poll()) != null) {
			destroyAroundLeaves(pDData, lpos[0], lpos[1], lpos[2], lpos[3] - 1);
		}
		
		return true;
	}

	protected boolean destroyAround(DestroyData pDData, int pX, int pY, int pZ) {
		// 幹の破壊及び周囲のチェック
		if (pDData.destroyBlock(pX, pY, pZ, true)) {
			if (pDData.isFlag(DestroyData.RMR_Flag_isTheBig)) {
				checkAroundLog(pDData, pX, pY, pZ);
			} else {
				pDData.checkAround(pX, pY, pZ, 1);
			}
			checkAroundLeaves(pDData, pX, pY, pZ, 5);
			return true;
		}
		return false;
	}

	protected boolean destroyAroundLeaves(DestroyData pDData, int pX, int pY, int pZ, int pRange) {
		// 葉の破壊及び周囲のチェック
		if (pDData.destroyBlock(pX, pY, pZ, false)) {
			checkAroundLeaves(pDData, pX, pY, pZ, pRange);
			return true;
		}
		return false;
	}

	protected void checkAroundLog(DestroyData pDData, int pX, int pY, int pZ) {
		for (int ly = -1; ly < 2; ly++) {
			for (int lz = -1; lz < 2; lz++) {
				for (int lx = -1; lx < 2; lx++) {
					if (pDData.checkBlockRect(pX + lx, pY + ly, pZ + lz, 1)) {
						pDData.destroyList.add(new int[] { pX + lx, pY + ly, pZ + lz});
					}
				}
			}
		}
	}

	protected void checkAroundLeaves(DestroyData pDData, int pX, int pY, int pZ, int pRange) {
		if (!pDData.isFlag(DestroyData.RMR_Flag_isBreakLeaves)) {
			return;
		}
		if (pDData.checkBlockRange(pX - 1, pY, pZ, pRange)) {
			destroyLeaveList.add(new int[] { pX - 1, pY, pZ, pRange});
		}
		if (pDData.checkBlockRange(pX + 1, pY, pZ, pRange)) {
			destroyLeaveList.add(new int[] { pX + 1, pY, pZ, pRange});
		}
		if (pDData.checkBlockRange(pX, pY, pZ - 1, pRange)) {
			destroyLeaveList.add(new int[] { pX, pY, pZ - 1, pRange});
		}
		if (pDData.checkBlockRange(pX, pY, pZ + 1, pRange)) {
			destroyLeaveList.add(new int[] { pX, pY, pZ + 1, pRange});
		}
		if (pDData.checkBlockRange(pX, pY - 1, pZ, pRange)) {
			destroyLeaveList.add(new int[] { pX, pY - 1, pZ, pRange});
		}
		if (pDData.checkBlockRange(pX, pY + 1, pZ, pRange)) {
			destroyLeaveList.add(new int[] { pX, pY + 1, pZ, pRange});
		}
	}
*/

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityPlayer player) {
		if (RefinedMilitaryShovelReplica.isDestroyEnable && player.worldObj.isRemote) {
			RefinedMilitaryShovelReplica.Debug("world:" + player.getClass().toString());
			Block lblock = player.worldObj.getBlock(X, Y, Z);
			int lmetadata = player.worldObj.getBlockMetadata(X, Y, Z);
			int lmet = lmetadata & 0xfc;
			for (int li = 0; li < targetBlocks.length; li++) {
				if (targetBlocks[li].isTargetBlock(lblock, lmet)) {
					RefinedMilitaryShovelReplica.Debug("Start CutgAll.");
					DestroyAllData ldd = new DestroyAllData();
					ldd.rangeWidth = RefinedMilitaryShovelReplica.cutLimit;
					ldd.rangeHeight = 32000;
					ldd.maxChain = 5;
					ldd.ox = X;
					ldd.oy = Y;
					ldd.oz = Z;
					ldd.block = lblock;
					ldd.metadata = lmetadata & 0x07;
					ldd.isUnder = RefinedMilitaryShovelReplica.cutUnder;
					ldd.isOtherDirection = RefinedMilitaryShovelReplica.cutOtherDirection;
					ldd.isBreakLeaves =  RefinedMilitaryShovelReplica.cutBreakLeaves;
					ldd.isTheBig = RefinedMilitaryShovelReplica.cutTheBig;
					ldd.isAdvMeta = true;
					ldd.identificator = targetBlocks[li];
					DestroyAllManager.sendDestroyAllPacket(ldd);
				}
			}
		}
		
		return super.onBlockStartBreak(itemstack, X, Y, Z, player);
	}

	@Override
	public DestroyAllData getDestroyAllData() {
		return new DestroyAllData();
	}

}
