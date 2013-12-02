package net.minecraft.src;


public class RMR_ItemSpade extends ItemSpade implements RMR_IDestroyAll {

	protected static int[][] targetBlockIDs;
	protected static int[][] targetMetadatas;


	@Override
	public void setTargets(int[][] pBlockIDs, int[][] pMetadatas) {
		targetBlockIDs = pBlockIDs;
		targetMetadatas = pMetadatas;
	}

	public RMR_ItemSpade(int par1, EnumToolMaterial par2EnumToolMaterial) {
		super(par1, par2EnumToolMaterial);
		setMaxDamage(par2EnumToolMaterial.getMaxUses() * mod_RMR_RefinedMilitaryShovelReplica.MaxDamage);
		
		// シャベルモードの攻撃力は剣と一緒
		try {
			// damageVsEntity
			ModLoader.setPrivateValue(ItemTool.class, this, 2,
					Float.valueOf(4.0F + par2EnumToolMaterial.getDamageVsEntity()));
		} catch (Exception e) {
			
		}
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
				mod_RMR_RefinedMilitaryShovelReplica.RMRPickaxeD.itemID :
				mod_RMR_RefinedMilitaryShovelReplica.RMRPickaxeI.itemID;
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
					if (targetMetadatas[li][0] > 15 ||
							targetMetadatas[li][0] == mod_RMR_RefinedMilitaryShovelReplica.selectedMetadata) {
						byte lflag = mod_RMR_RefinedMilitaryShovelReplica.cfg_digUnder ? RMR_DestroyData.RMR_Flag_isUnder : 0;
						RMR_DestroyData.sendMessage(0x80, par7EntityLivingBase,
								mod_RMR_RefinedMilitaryShovelReplica.cfg_digLimit,
								lflag, par4, par5, par6, targetBlockIDs[li], targetMetadatas[li]);
						mod_RMR_RefinedMilitaryShovelReplica.Debug("Start DigAll.");
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
		pDData.destroyList.clear();
		pDData.checkAround(pDData.baseX, pDData.baseY, pDData.baseZ, 32000);
		
		int lpos[];
		while ((lpos = pDData.destroyList.poll()) != null) {
			destroyAround(pDData, lpos[0], lpos[1], lpos[2]);
		}
		
		return true;
	}

	protected boolean destroyAround(RMR_DestroyData pDData, int pX, int pY, int pZ) {
		if (pDData.destroyBlock(pX, pY, pZ, true)) {
			pDData.checkAround(pX, pY, pZ, 32000);
			return true;
		}
		return false;
	}

}
