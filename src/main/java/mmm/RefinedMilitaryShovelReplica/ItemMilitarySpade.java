package mmm.RefinedMilitaryShovelReplica;

import mmm.lib.DestroyAll.DestroyAllIdentificator;
import mmm.lib.DestroyAll.DestroyAllManager;
import mmm.lib.DestroyAll.IDestroyAll;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.ReflectionHelper;


public class ItemMilitarySpade extends ItemSpade implements IDestroyAll {

	protected static DestroyAllIdentificator[] targetBlocks;
	protected Item nextItem;


	@Override
	public void setTargets(DestroyAllIdentificator[] pBlocks) {
		targetBlocks = pBlocks;
	}

	public ItemMilitarySpade(Item.ToolMaterial par2EnumToolMaterial) {
		super(par2EnumToolMaterial);
		setMaxDamage(par2EnumToolMaterial.getMaxUses() * RefinedMilitaryShovelReplica.MaxDamage);
		
		// シャベルモードの攻撃力は剣と一緒
		// damageVsEntity
		ReflectionHelper.setPrivateValue(ItemTool.class, this,
				Float.valueOf(4.0F + par2EnumToolMaterial.getDamageVsEntity()), "damageVsEntity");
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

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityPlayer player) {
		System.out.println("world:" + player.getClass().toString());
		if (RefinedMilitaryShovelReplica.isDestroyEnable && (player instanceof EntityPlayerSP)) {
			Block lblock = player.worldObj.func_147439_a(X, Y, Z);
			int lmetadata = player.worldObj.getBlockMetadata(X, Y, Z);
			for (int li = 0; li < targetBlocks.length; li++) {
				if (targetBlocks[li].isTargetBlock(lblock, lmetadata)) {
					RefinedMilitaryShovelReplica.Debug("Start DigAll.");
					int llimit = RefinedMilitaryShovelReplica.digLimit;
					boolean lunder = RefinedMilitaryShovelReplica.digUnder;
					DestroyAllManager.sendDestroyAllPacket(player, llimit, llimit, X, Y, Z,
							DestroyAllManager.getFlags(lunder, false, false, false), targetBlocks[li]);
				}
			}
		}
		
		return super.onBlockStartBreak(itemstack, X, Y, Z, player);
	}
/*
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
