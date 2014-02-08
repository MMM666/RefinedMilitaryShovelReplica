package mmm.RefinedMilitaryShovelReplica;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;

public class RefinedMilitaryShovelReplicaEventHandler {

	@SubscribeEvent
	public void keyboardEvent(InputEvent.KeyInputEvent pEvent) {
		if (RefinedMilitaryShovelReplica.eventKey.getIsKeyPressed()) {
			System.out.println("onKeyPress");
			// GUIを開く
			Minecraft lmc = Minecraft.getMinecraft();
			if (lmc.thePlayer.isSneaking()) {
//				lmc.thePlayer.openGui(this, 0, lmc.theWorld, 0, 0, 0);
//				ModLoader.openGUI(MMM_Helper.mc.thePlayer, new GuiRMRConfigure());
				lmc.displayGuiScreen(new GuiRMRConfigure());
			} else {
				RefinedMilitaryShovelReplica.isDestroyEnable =
						!RefinedMilitaryShovelReplica.isDestroyEnable;
			}
		}
	}

}
