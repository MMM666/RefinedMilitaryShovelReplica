package mmm.RefinedMilitaryShovelReplica;

import mmm.lib.DestroyAll.DestroyAllIdentificator;
import mmm.lib.DestroyAll.IDestroyAll;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class ItemMilitaryPickaxe extends ItemPickaxe implements IDestroyAll {

	protected static DestroyAllIdentificator[] targetBlocks;
	protected Item nextItem;


	@Override
	public void setTargets(DestroyAllIdentificator[] pBlocks) {
		targetBlocks = pBlocks;
	}

	protected ItemMilitaryPickaxe(ToolMaterial par2EnumToolMaterial) {
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
	public boolean superOnBlockStartBreak(ItemStack itemstack,
			int X, int Y, int Z, EntityPlayer player) {
		return super.onBlockStartBreak(itemstack, X, Y, Z, player);
	}
/*
	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z,
			EntityPlayer player) {
		if (RefinedMilitaryShovelReplica.isDestroyEnable && player.isClientWorld()) {
			for (int li = 0; li < targetBlockIDs.length; li++) {
				if (targetBlockIDs[li][0] == RefinedMilitaryShovelReplica.selectedblockID) {
					if (targetMetadatas[li][0] > 15 ||
							targetMetadatas[li][0] == RefinedMilitaryShovelReplica.selectedMetadata) {
						byte lflag = RefinedMilitaryShovelReplica.mineUnder ? DestroyData.RMR_Flag_isUnder : 0;
						DestroyData.sendMessage(0x81, player,
								RefinedMilitaryShovelReplica.mineLimit,
								lflag, X, Y, Z, targetBlockIDs[li], targetMetadatas[li]);
						RefinedMilitaryShovelReplica.Debug("Start MineAll.");
					}
				}
			}
		}
		
		return super.onBlockStartBreak(itemstack, X, Y, Z, player);
	}

	@Override
	public boolean destroyAll(DestroyData pDData) {
		pDData.destroyList.clear();
		pDData.checkAround(pDData.baseX, pDData.baseY, pDData.baseZ, 32000);
		
		int lpos[];
		while ((lpos = pDData.destroyList.poll()) != null) {
			destroyAround(pDData, lpos[0], lpos[1], lpos[2]);
		}
		
		return true;
	}

	protected boolean destroyAround(DestroyData pDData, int pX, int pY, int pZ) {
		if (pDData.destroyBlock(pX, pY, pZ, true)) {
			pDData.checkAround(pX, pY, pZ, 32000);
			return true;
		}
		return false;
	}
*/
}
