package mmm.RefinedMilitaryShovelReplica;

import mmm.lib.ProxyCommon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;

/**
 * イベントハンドラも一緒に入れる
 *
 */
public class ProxyClient extends ProxyCommon {

	public static KeyBinding eventKey;


	@Override
	public void init() {
		// destroyAll切り替えボタン
		String ls = "key.rmr";
		eventKey = new KeyBinding(ls, 46, "");
		ClientRegistry.registerKeyBinding(eventKey);
		FMLCommonHandler.instance().bus().register(this);

	}

	@SubscribeEvent
	public void keyboardEvent(InputEvent.KeyInputEvent pEvent) {
		if (eventKey.getIsKeyPressed()) {
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
