package mmm.lib.gui;

import net.minecraft.client.gui.GuiButton;


public class GuiToggleButton extends GuiButton {

	public boolean isDown = false;

	public GuiToggleButton(int par1, int par2, int par3, String par4Str) {
		super(par1, par2, par3, par4Str);
	}

	public GuiToggleButton(int par1, int par2, int par3, int par4,
			int par5, String par6Str) {
		super(par1, par2, par3, par4, par5, par6Str);
	}

	@Override
	protected int func_146114_a(boolean par1) {
		if (!field_146124_l || isDown) {
			return 0;
		} else if (par1) {
			return 2;
		}
		return 1;
	}

}
