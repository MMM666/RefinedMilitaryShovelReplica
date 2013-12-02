package net.minecraft.src;

import java.util.LinkedList;

public class RMR_ItemAxe extends ItemAxe implements RMR_IDestroyAll {

	protected static int[][] targetBlockIDs;
	protected static int[][] targetMetadatas;
	protected LinkedList<int[]> destroyLeaveList = new LinkedList<int[]>();


	@Override
	public void setTargets(int[][] pBlockIDs, int[][] pMetadatas) {
		targetBlockIDs = pBlockIDs;
		targetMetadatas = pMetadatas;
	}

	protected RMR_ItemAxe(int par1, EnumToolMaterial par2EnumToolMaterial) {
		super(par1, par2EnumToolMaterial);
		setMaxDamage(par2EnumToolMaterial.getMaxUses() * mod_RMR_RefinedMilitaryShovelReplica.MaxDamage);
		setCreativeTab(null);
	}

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
		par1ItemStack.itemID = toolMaterial == EnumToolMaterial.EMERALD ?
				mod_RMR_RefinedMilitaryShovelReplica.RMRSpadeD.itemID :
				mod_RMR_RefinedMilitaryShovelReplica.RMRSpadeI.itemID;
		super.onPlayerStoppedUsing(par1ItemStack, par2World, par3EntityPlayer, par4);
	}

	@Override
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		return mod_RMR_RefinedMilitaryShovelReplica.isDestroyEnable ? 0xff8888 : 0xffffff;
	}


	@Override
	public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World,
			int par3, int par4, int par5, int par6, EntityLivingBase par7EntityLivingBase) {
		if (mod_RMR_RefinedMilitaryShovelReplica.isDestroyEnable && par2World.isRemote) {
			for (int li = 0; li < targetBlockIDs.length; li++) {
				if (targetBlockIDs[li][0] == mod_RMR_RefinedMilitaryShovelReplica.selectedblockID) {
					byte lflag = 0;
					lflag |= mod_RMR_RefinedMilitaryShovelReplica.cfg_cutUnder ? RMR_DestroyData.RMR_Flag_isUnder : 0;
					lflag |= mod_RMR_RefinedMilitaryShovelReplica.cfg_cutOtherDirection ? RMR_DestroyData.RMR_Flag_isOtherDirection : 0;
					lflag |= mod_RMR_RefinedMilitaryShovelReplica.cfg_cutBreakLeaves ? RMR_DestroyData.RMR_Flag_isBreakLeaves : 0;
					lflag |= mod_RMR_RefinedMilitaryShovelReplica.cfg_cutTheBig ? RMR_DestroyData.RMR_Flag_isTheBig : 0;
							
					Block lblock = Block.blocksList[targetBlockIDs[li][0]];
					int lmmask = targetMetadatas[li][0];
					if (mod_RMR_RefinedMilitaryShovelReplica.cfg_cutOtherDirection) {
						lmmask = (lmmask == 0 && lblock instanceof BlockLog) ? 0xC0 : lmmask;
					}
					int[] lmetas = new int[targetMetadatas[li].length];
					if (lmmask > 0) {
						int lmmeta = mod_RMR_RefinedMilitaryShovelReplica.selectedMetadata & 0x0f;
						for (int  lj = 0; lj < lmetas.length; lj++) {
							lmetas[lj] = lmmask | lmmeta;
						}
						RMR_DestroyData.sendMessage(0x82, par7EntityLivingBase,
								mod_RMR_RefinedMilitaryShovelReplica.cfg_cutLimit,
								lflag, par4, par5, par6, targetBlockIDs[li], lmetas);
						mod_RMR_RefinedMilitaryShovelReplica.Debug("Start CutAll.");
					} else 
					if (targetMetadatas[li][0] == mod_RMR_RefinedMilitaryShovelReplica.selectedMetadata) {
						RMR_DestroyData.sendMessage(0x82, par7EntityLivingBase,
								mod_RMR_RefinedMilitaryShovelReplica.cfg_cutLimit,
								lflag, par4, par5, par6, targetBlockIDs[li], targetMetadatas[li]);
						mod_RMR_RefinedMilitaryShovelReplica.Debug("Start CutAll.");
					}
				}
			}
		}
		
		return super.onBlockDestroyed(par1ItemStack, par2World, par3, par4, par5, par6, par7EntityLivingBase);
	}

	@Override
	public boolean superOnBlockDestroyed(ItemStack par1ItemStack,
			World par2World, int par3, int par4, int par5, int par6,
			EntityLivingBase par7EntityLivingBase) {
		return super.onBlockDestroyed(par1ItemStack, par2World, par3, par4, par5, par6, par7EntityLivingBase);
	}

	@Override
	public boolean destroyAll(RMR_DestroyData pDData) {
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

	protected boolean destroyAround(RMR_DestroyData pDData, int pX, int pY, int pZ) {
		// 幹の破壊及び周囲のチェック
		if (pDData.destroyBlock(pX, pY, pZ, true)) {
			if (pDData.isFlag(RMR_DestroyData.RMR_Flag_isTheBig)) {
				checkAroundLog(pDData, pX, pY, pZ);
			} else {
				pDData.checkAround(pX, pY, pZ, 1);
			}
			checkAroundLeaves(pDData, pX, pY, pZ, 5);
			return true;
		}
		return false;
	}

	protected boolean destroyAroundLeaves(RMR_DestroyData pDData, int pX, int pY, int pZ, int pRange) {
		// 葉の破壊及び周囲のチェック
		if (pDData.destroyBlock(pX, pY, pZ, false)) {
			checkAroundLeaves(pDData, pX, pY, pZ, pRange);
			return true;
		}
		return false;
	}

	protected void checkAroundLog(RMR_DestroyData pDData, int pX, int pY, int pZ) {
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

	protected void checkAroundLeaves(RMR_DestroyData pDData, int pX, int pY, int pZ, int pRange) {
		if (!pDData.isFlag(RMR_DestroyData.RMR_Flag_isBreakLeaves)) {
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


}
