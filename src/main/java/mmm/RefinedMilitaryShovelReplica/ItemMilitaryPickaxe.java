package mmm.RefinedMilitaryShovelReplica;

import mmm.lib.DestroyAll.DestroyAllData;
import mmm.lib.DestroyAll.DestroyAllIdentificator;
import mmm.lib.DestroyAll.DestroyAllManager;
import mmm.lib.DestroyAll.IDestroyAll;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
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

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityPlayer player) {
		System.out.println("world:" + player.getClass().toString());
		if (RefinedMilitaryShovelReplica.isDestroyEnable && (player instanceof EntityPlayerSP)) {
			Block lblock = player.worldObj.getBlock(X, Y, Z);
			int lmetadata = player.worldObj.getBlockMetadata(X, Y, Z);
			for (int li = 0; li < targetBlocks.length; li++) {
				if (targetBlocks[li].isTargetBlock(lblock, lmetadata)) {
					RefinedMilitaryShovelReplica.Debug("Start MineAll.");
					DestroyAllData ldd = new DestroyAllData();
					ldd.rangeWidth = RefinedMilitaryShovelReplica.mineLimit;
					ldd.rangeHeight = RefinedMilitaryShovelReplica.mineLimit;
					ldd.ox = X;
					ldd.oy = Y;
					ldd.oz = Z;
					ldd.block = lblock;
					ldd.metadata = lmetadata;
					ldd.isUnder = RefinedMilitaryShovelReplica.mineUnder;
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
