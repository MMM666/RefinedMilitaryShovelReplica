package mmm.RefinedMilitaryShovelReplica;

import mmm.lib.gui.GuiRangedSlider;
import mmm.lib.gui.GuiToggleButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiRMRConfigure extends GuiScreen {

	public GuiRangedSlider sliderDig;
	public GuiRangedSlider sliderMine;
	public GuiRangedSlider sliderCut;


	public GuiRMRConfigure() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		GuiToggleButton lgbt;
		
		sliderDig = new GuiRangedSlider(10, 100, 30, "Range", RefinedMilitaryShovelReplica.digLimit / 10F);
		sliderDig.sliderMultiply = 20;
		sliderDig.sliderOffset = 1;
		sliderDig.sliderStep = 0.1F;
		sliderDig.setStrFormat("%s : %.0f");
		field_146292_n.add(sliderDig.setDisplayString());
		lgbt = new GuiToggleButton(11, 300, 30, 100, 20, "ON");
		lgbt.isDown = RefinedMilitaryShovelReplica.digUnder;
		lgbt.field_146126_j = lgbt.isDown ? "ON" : "OFF";
		field_146292_n.add(lgbt);
		
		sliderMine = new GuiRangedSlider(20, 100, 70, "Range", RefinedMilitaryShovelReplica.mineLimit / 10F);
		sliderMine.sliderMultiply = 20;
		sliderMine.sliderOffset = 1;
		sliderMine.sliderStep = 0.1F;
		sliderMine.setStrFormat("%s : %.0f");
		field_146292_n.add(sliderMine.setDisplayString());
		lgbt = new GuiToggleButton(21, 300, 70, 100, 20, "ON");
		lgbt.isDown = RefinedMilitaryShovelReplica.mineUnder;
		lgbt.field_146126_j = lgbt.isDown ? "ON" : "OFF";
		field_146292_n.add(lgbt);
		
		sliderCut = new GuiRangedSlider(30, 100, 110, "Range", RefinedMilitaryShovelReplica.cutLimit / 10F);
		sliderCut.sliderMultiply = 20;
		sliderCut.sliderOffset = 1;
		sliderCut.sliderStep = 0.1F;
		sliderCut.setStrFormat("%s : %.0f");
		field_146292_n.add(sliderCut.setDisplayString());
		lgbt = new GuiToggleButton(31, 300, 110, 100, 20, "ON");
		lgbt.isDown = RefinedMilitaryShovelReplica.cutUnder;
		lgbt.field_146126_j = lgbt.isDown ? "ON" : "OFF";
		field_146292_n.add(lgbt);
		lgbt = new GuiToggleButton(32, 300, 130, 100, 20, "ON");
		lgbt.isDown = RefinedMilitaryShovelReplica.cutOtherDirection;
		lgbt.field_146126_j = lgbt.isDown ? "ON" : "OFF";
		field_146292_n.add(lgbt);
		lgbt = new GuiToggleButton(33, 300, 150, 100, 20, "ON");
		lgbt.isDown = RefinedMilitaryShovelReplica.cutBreakLeaves;
		lgbt.field_146126_j = lgbt.isDown ? "ON" : "OFF";
		field_146292_n.add(lgbt);
		lgbt = new GuiToggleButton(34, 300, 170, 100, 20, "ON");
		lgbt.isDown = RefinedMilitaryShovelReplica.cutTheBig;
		lgbt.field_146126_j = lgbt.isDown ? "ON" : "OFF";
		field_146292_n.add(lgbt);
		
		field_146292_n.add(new GuiButton(100, 240, 200, 100, 20, "Save"));
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
		drawString(field_146289_q, "DigAll", 30, 15, 0xffffff);
		drawString(field_146289_q, "MaxRange", 30, 35, 0xffffff);
		drawString(field_146289_q, "DigUnder", 220, 35, 0xffffff);
		
		drawString(field_146289_q, "MineAll", 30, 55, 0xffffff);
		drawString(field_146289_q, "MaxRange", 30, 75, 0xffffff);
		drawString(field_146289_q, "MineUnder", 220, 75, 0xffffff);
		
		drawString(field_146289_q, "CutAll", 30, 95, 0xffffff);
		drawString(field_146289_q, "MaxRange", 30, 115, 0xffffff);
		drawString(field_146289_q, "CutUnder", 220, 115, 0xffffff);
		drawString(field_146289_q, "OtherDirection", 220, 135, 0xffffff);
		drawString(field_146289_q, "BreakLeaves", 220, 155, 0xffffff);
		drawString(field_146289_q, "TheBig", 220, 175, 0xffffff);
	}

	@Override
	protected void func_146284_a(GuiButton par1GuiButton) {
		// TODO Auto-generated method stub
		super.func_146284_a(par1GuiButton);
		
		if (par1GuiButton.field_146127_k == 11) {
			GuiToggleButton lgb = (GuiToggleButton)par1GuiButton;
			lgb.isDown = (RefinedMilitaryShovelReplica.digUnder = !RefinedMilitaryShovelReplica.digUnder);
			lgb.field_146126_j = lgb.isDown ? "ON" : "OFF";
		}
		
		if (par1GuiButton.field_146127_k == 21) {
			GuiToggleButton lgb = (GuiToggleButton)par1GuiButton;
			lgb.isDown = (RefinedMilitaryShovelReplica.mineUnder = !RefinedMilitaryShovelReplica.mineUnder);
			lgb.field_146126_j = lgb.isDown ? "ON" : "OFF";
		}
		
		if (par1GuiButton.field_146127_k == 31) {
			GuiToggleButton lgb = (GuiToggleButton)par1GuiButton;
			lgb.isDown = (RefinedMilitaryShovelReplica.cutUnder = !RefinedMilitaryShovelReplica.cutUnder);
			lgb.field_146126_j = lgb.isDown ? "ON" : "OFF";
		}
		if (par1GuiButton.field_146127_k == 32) {
			GuiToggleButton lgb = (GuiToggleButton)par1GuiButton;
			lgb.isDown = (RefinedMilitaryShovelReplica.cutOtherDirection = !RefinedMilitaryShovelReplica.cutOtherDirection);
			lgb.field_146126_j = lgb.isDown ? "ON" : "OFF";
		}
		if (par1GuiButton.field_146127_k == 33) {
			GuiToggleButton lgb = (GuiToggleButton)par1GuiButton;
			lgb.isDown = (RefinedMilitaryShovelReplica.cutBreakLeaves = !RefinedMilitaryShovelReplica.cutBreakLeaves);
			lgb.field_146126_j = lgb.isDown ? "ON" : "OFF";
		}
		if (par1GuiButton.field_146127_k == 34) {
			GuiToggleButton lgb = (GuiToggleButton)par1GuiButton;
			lgb.isDown = (RefinedMilitaryShovelReplica.cutTheBig = !RefinedMilitaryShovelReplica.cutTheBig);
			lgb.field_146126_j = lgb.isDown ? "ON" : "OFF";
		}
		
		if (par1GuiButton.field_146127_k == 100) {
			RefinedMilitaryShovelReplica.saveConfig();
		}
	}
/*
	public static void keyboardEvent(KeyBinding keybinding) {
		// GUIを開く
		if (MMM_Helper.isClient && MMM_Helper.mc.currentScreen == null) {
			if (MMM_Helper.mc.thePlayer.isSneaking()) {
				ModLoader.openGUI(MMM_Helper.mc.thePlayer, new GuiRMRConfigure());
			} else {
				RefinedMilitaryShovelReplica.isDestroyEnable =
						!RefinedMilitaryShovelReplica.isDestroyEnable;
			}
		}
	}
*/
	@Override
	protected void func_146286_b(int par1, int par2, int par3) {
		super.func_146286_b(par1, par2, par3);
		
		RefinedMilitaryShovelReplica.digLimit	= (int)(sliderDig.sliderValue * 10);
		RefinedMilitaryShovelReplica.mineLimit	= (int)(sliderMine.sliderValue * 10);
		RefinedMilitaryShovelReplica.cutLimit	= (int)(sliderCut.sliderValue * 10);
	}

}
