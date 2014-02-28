package mmm.RefinedMilitaryShovelReplica;

import mmm.lib.destroyAll.DestroyAllData;
import mmm.lib.destroyAll.DestroyAllIdentificator;
import mmm.lib.destroyAll.DestroyAllManager;
import mmm.lib.destroyAll.IDestroyAll;
import net.minecraft.block.Block;
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
		if (RefinedMilitaryShovelReplica.isDestroyEnable && player.worldObj.isRemote) {
			RefinedMilitaryShovelReplica.Debug("world:" + player.getClass().toString());
			Block lblock = player.worldObj.getBlock(X, Y, Z);
			int lmetadata = player.worldObj.getBlockMetadata(X, Y, Z);
			for (int li = 0; li < targetBlocks.length; li++) {
				if (targetBlocks[li].isTargetBlock(lblock, lmetadata)) {
					RefinedMilitaryShovelReplica.Debug("Start DigAll.");
					DestroyAllData ldd = new DestroyAllData();
					ldd.rangeWidth = RefinedMilitaryShovelReplica.digLimit;
					ldd.rangeHeight = RefinedMilitaryShovelReplica.digLimit;
					ldd.ox = X;
					ldd.oy = Y;
					ldd.oz = Z;
					ldd.block = lblock;
					ldd.metadata = lmetadata;
					ldd.isUnder = RefinedMilitaryShovelReplica.digUnder;
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
