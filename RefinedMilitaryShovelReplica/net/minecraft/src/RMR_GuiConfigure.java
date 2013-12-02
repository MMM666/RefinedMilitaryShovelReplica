package net.minecraft.src;

public class RMR_GuiConfigure extends GuiScreen {

	public MMM_GuiSlider sliderDig;
	public MMM_GuiSlider sliderMine;
	public MMM_GuiSlider sliderCut;


	public RMR_GuiConfigure() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initGui() {
		MMM_GuiToggleButton lgbt;
		
		sliderDig = new MMM_GuiSlider(10, 100, 30, "Range", mod_RMR_RefinedMilitaryShovelReplica.cfg_digLimit / 10F);
		sliderDig.sliderMultiply = 20;
		sliderDig.sliderOffset = 1;
		sliderDig.sliderStep = 0.1F;
		sliderDig.setStrFormat("%s : %.0f");
		buttonList.add(sliderDig.setDisplayString());
		lgbt = new MMM_GuiToggleButton(11, 300, 30, 100, 20, "ON");
		lgbt.isDown = mod_RMR_RefinedMilitaryShovelReplica.cfg_digUnder;
		lgbt.displayString = lgbt.isDown ? "ON" : "OFF";
		buttonList.add(lgbt);
		
		sliderMine = new MMM_GuiSlider(20, 100, 70, "Range", mod_RMR_RefinedMilitaryShovelReplica.cfg_mineLimit / 10F);
		sliderMine.sliderMultiply = 20;
		sliderMine.sliderOffset = 1;
		sliderMine.sliderStep = 0.1F;
		sliderMine.setStrFormat("%s : %.0f");
		buttonList.add(sliderMine.setDisplayString());
		lgbt = new MMM_GuiToggleButton(21, 300, 70, 100, 20, "ON");
		lgbt.isDown = mod_RMR_RefinedMilitaryShovelReplica.cfg_mineUnder;
		lgbt.displayString = lgbt.isDown ? "ON" : "OFF";
		buttonList.add(lgbt);
		
		sliderCut = new MMM_GuiSlider(30, 100, 110, "Range", mod_RMR_RefinedMilitaryShovelReplica.cfg_cutLimit / 10F);
		sliderCut.sliderMultiply = 20;
		sliderCut.sliderOffset = 1;
		sliderCut.sliderStep = 0.1F;
		sliderCut.setStrFormat("%s : %.0f");
		buttonList.add(sliderCut.setDisplayString());
		lgbt = new MMM_GuiToggleButton(31, 300, 110, 100, 20, "ON");
		lgbt.isDown = mod_RMR_RefinedMilitaryShovelReplica.cfg_cutUnder;
		lgbt.displayString = lgbt.isDown ? "ON" : "OFF";
		buttonList.add(lgbt);
		lgbt = new MMM_GuiToggleButton(32, 300, 130, 100, 20, "ON");
		lgbt.isDown = mod_RMR_RefinedMilitaryShovelReplica.cfg_cutOtherDirection;
		lgbt.displayString = lgbt.isDown ? "ON" : "OFF";
		buttonList.add(lgbt);
		lgbt = new MMM_GuiToggleButton(33, 300, 150, 100, 20, "ON");
		lgbt.isDown = mod_RMR_RefinedMilitaryShovelReplica.cfg_cutBreakLeaves;
		lgbt.displayString = lgbt.isDown ? "ON" : "OFF";
		buttonList.add(lgbt);
		lgbt = new MMM_GuiToggleButton(34, 300, 170, 100, 20, "ON");
		lgbt.isDown = mod_RMR_RefinedMilitaryShovelReplica.cfg_cutTheBig;
		lgbt.displayString = lgbt.isDown ? "ON" : "OFF";
		buttonList.add(lgbt);
		
		buttonList.add(new GuiSmallButton(100, 240, 200, "Save"));
	}

	@Override
	public void drawBackground(int par1) {
		// TODO Auto-generated method stub
		super.drawBackground(par1);
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
		drawString(fontRenderer, "DigAll", 30, 15, 0xffffff);
		drawString(fontRenderer, "MaxRange", 30, 35, 0xffffff);
		drawString(fontRenderer, "DigUnder", 220, 35, 0xffffff);
		
		drawString(fontRenderer, "MineAll", 30, 55, 0xffffff);
		drawString(fontRenderer, "MaxRange", 30, 75, 0xffffff);
		drawString(fontRenderer, "MineUnder", 220, 75, 0xffffff);
		
		drawString(fontRenderer, "CutAll", 30, 95, 0xffffff);
		drawString(fontRenderer, "MaxRange", 30, 115, 0xffffff);
		drawString(fontRenderer, "CutUnder", 220, 115, 0xffffff);
		drawString(fontRenderer, "OtherDirection", 220, 135, 0xffffff);
		drawString(fontRenderer, "BreakLeaves", 220, 155, 0xffffff);
		drawString(fontRenderer, "TheBig", 220, 175, 0xffffff);
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		// TODO Auto-generated method stub
		super.actionPerformed(par1GuiButton);
		
		if (par1GuiButton.id == 11) {
			MMM_GuiToggleButton lgb = (MMM_GuiToggleButton)par1GuiButton;
			lgb.isDown = (mod_RMR_RefinedMilitaryShovelReplica.cfg_digUnder = !mod_RMR_RefinedMilitaryShovelReplica.cfg_digUnder);
			lgb.displayString = lgb.isDown ? "ON" : "OFF";
		}
		
		if (par1GuiButton.id == 21) {
			MMM_GuiToggleButton lgb = (MMM_GuiToggleButton)par1GuiButton;
			lgb.isDown = (mod_RMR_RefinedMilitaryShovelReplica.cfg_mineUnder = !mod_RMR_RefinedMilitaryShovelReplica.cfg_mineUnder);
			lgb.displayString = lgb.isDown ? "ON" : "OFF";
		}
		
		if (par1GuiButton.id == 31) {
			MMM_GuiToggleButton lgb = (MMM_GuiToggleButton)par1GuiButton;
			lgb.isDown = (mod_RMR_RefinedMilitaryShovelReplica.cfg_cutUnder = !mod_RMR_RefinedMilitaryShovelReplica.cfg_cutUnder);
			lgb.displayString = lgb.isDown ? "ON" : "OFF";
		}
		if (par1GuiButton.id == 32) {
			MMM_GuiToggleButton lgb = (MMM_GuiToggleButton)par1GuiButton;
			lgb.isDown = (mod_RMR_RefinedMilitaryShovelReplica.cfg_cutOtherDirection = !mod_RMR_RefinedMilitaryShovelReplica.cfg_cutOtherDirection);
			lgb.displayString = lgb.isDown ? "ON" : "OFF";
		}
		if (par1GuiButton.id == 33) {
			MMM_GuiToggleButton lgb = (MMM_GuiToggleButton)par1GuiButton;
			lgb.isDown = (mod_RMR_RefinedMilitaryShovelReplica.cfg_cutBreakLeaves = !mod_RMR_RefinedMilitaryShovelReplica.cfg_cutBreakLeaves);
			lgb.displayString = lgb.isDown ? "ON" : "OFF";
		}
		if (par1GuiButton.id == 34) {
			MMM_GuiToggleButton lgb = (MMM_GuiToggleButton)par1GuiButton;
			lgb.isDown = (mod_RMR_RefinedMilitaryShovelReplica.cfg_cutTheBig = !mod_RMR_RefinedMilitaryShovelReplica.cfg_cutTheBig);
			lgb.displayString = lgb.isDown ? "ON" : "OFF";
		}
		
		if (par1GuiButton.id == 100) {
			MMM_Config.saveConfig(mod_RMR_RefinedMilitaryShovelReplica.class);
		}
	}

	public static void keyboardEvent(KeyBinding keybinding) {
		// GUI‚ðŠJ‚­
		if (MMM_Helper.isClient && MMM_Helper.mc.currentScreen == null) {
			if (MMM_Helper.mc.thePlayer.isSneaking()) {
				ModLoader.openGUI(MMM_Helper.mc.thePlayer, new RMR_GuiConfigure());
			} else {
				mod_RMR_RefinedMilitaryShovelReplica.isDestroyEnable =
						!mod_RMR_RefinedMilitaryShovelReplica.isDestroyEnable;
			}
		}
	}

	@Override
	protected void mouseMovedOrUp(int par1, int par2, int par3) {
		super.mouseMovedOrUp(par1, par2, par3);
		
		mod_RMR_RefinedMilitaryShovelReplica.cfg_digLimit = (int)(sliderDig.sliderValue * 10);
		mod_RMR_RefinedMilitaryShovelReplica.cfg_mineLimit = (int)(sliderMine.sliderValue * 10);
		mod_RMR_RefinedMilitaryShovelReplica.cfg_cutLimit = (int)(sliderCut.sliderValue * 10);
	}

}
