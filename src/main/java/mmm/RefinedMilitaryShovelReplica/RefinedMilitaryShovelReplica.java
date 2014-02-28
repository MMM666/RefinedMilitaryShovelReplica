package mmm.RefinedMilitaryShovelReplica;

import java.io.File;

import mmm.lib.ProxyCommon;
import mmm.lib.destroyAll.DestroyAllIdentificator;
import mmm.lib.destroyAll.DestroyAllManager;
import mmm.lib.destroyAll.IDestroyAll;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(
		modid	= "refinedMilitaryShovelReplica",
		name	= "RefinedMilitaryShovelReplica",
		version	= "1.7.x-srg-1"
		)
public class RefinedMilitaryShovelReplica {

	@SidedProxy(clientSide = "mmm.RefinedMilitaryShovelReplica.ProxyClient", serverSide = "mmm.lib.ProxyCommon")
	public static ProxyCommon proxy;
	
	public static boolean isDebugMessage = true;
	public static boolean isDestroyEnable;
	public static boolean isStarting;
	/*
	 * 一括対象の指定。
	 * 同一文字列内で「,」で区切った場合は同一グループとして処理され、異なるブロックでも一括破壊される。
	 * 「:」はメタデータの値であり、個別の識別を行う。
	 * メタデータ識別は上位４ビットがマスク指定となっており反転したものがマスク値として使用される。
	 */
	public static String[] DigBlock;
	public static String[] MineBlock;
	public static String[] CutBlock;
	public static int digLimit;
	public static boolean digUnder;
	public static int mineLimit;
	public static boolean mineUnder;
	public static int cutLimit;
	public static boolean cutUnder;
	public static boolean cutOtherDirection;
	public static boolean cutBreakLeaves;
	public static boolean cutTheBig;

	public static int MaxDamage = 2;

	public static Item RMRSpadeI;
	public static Item RMRPickaxeI;
	public static Item RMRAxeI;
	public static Item RMRSpadeD;
	public static Item RMRPickaxeD;
	public static Item RMRAxeD;
	protected static File configFile;



	public static void Debug(String pText, Object... pData) {
		// デバッグメッセージ
		if (isDebugMessage) {
			System.out.println(String.format("RMR-" + pText, pData));
		}
	}

	private static String[] getTargets(DestroyAllIdentificator[] pDAI) {
		String[] ltargets = new String[pDAI.length];
		for (int li = 0; li < pDAI.length; li++) {
			ltargets[li] = "\"" + pDAI[li].toString()+ "\"";
		}
		return ltargets;
	}
	protected static void saveConfig() {
		Configuration lconf = new Configuration(configFile);
		lconf.load();
		lconf.get("RefinedMilitaryShovelReplica", "isDebugMessage", true).set(isDebugMessage);
		lconf.get("RefinedMilitaryShovelReplica", "isStarting", true).set(isStarting);
		lconf.get("RefinedMilitaryShovelReplica", "DigBlock", new String[] {""}).set(getTargets(ItemMilitarySpade.targetBlocks));
		lconf.get("RefinedMilitaryShovelReplica", "MineBlock", new String[] {""}).set(getTargets(ItemMilitaryPickaxe.targetBlocks));
		lconf.get("RefinedMilitaryShovelReplica", "CutBlock", new String[] {""}).set(getTargets(ItemMilitaryAxe.targetBlocks));
		
		lconf.get("RefinedMilitaryShovelReplica", "digLimit", 5).set(digLimit);
		lconf.get("RefinedMilitaryShovelReplica", "digUnder", true).set(digUnder);
		lconf.get("RefinedMilitaryShovelReplica", "mineLimit", 1).set(mineLimit);
		lconf.get("RefinedMilitaryShovelReplica", "mineUnder", true).set(mineUnder);
		lconf.get("RefinedMilitaryShovelReplica", "cutLimit", 10).set(cutLimit);
		lconf.get("RefinedMilitaryShovelReplica", "cutUnder", false).set(cutUnder);
		lconf.get("RefinedMilitaryShovelReplica", "cutOtherDirection", true).set(cutOtherDirection);
		lconf.get("RefinedMilitaryShovelReplica", "cutBreakLeaves", true).set(cutBreakLeaves);
		lconf.get("RefinedMilitaryShovelReplica", "cutTheBig", true).set(cutTheBig);
		lconf.save();
		Debug("Saveed configure.");
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent pEvent) {
		// コンフィグの解析・設定
		configFile = pEvent.getSuggestedConfigurationFile();
		Configuration lconf = new Configuration(configFile);
		lconf.load();
		isDebugMessage	= lconf.get("RefinedMilitaryShovelReplica", "isDebugMessage", true).getBoolean(true);
		isStarting		= lconf.get("RefinedMilitaryShovelReplica", "isStarting", true).getBoolean(true);
		isDestroyEnable = isStarting;
		DigBlock		= lconf.get("RefinedMilitaryShovelReplica", "DigBlock", new String[] {"\"grass=0;dirt=0\""}).getStringList();
		MineBlock		= lconf.get("RefinedMilitaryShovelReplica", "MineBlock", new String[] {"stone=0"}).getStringList();
		CutBlock		= lconf.get("RefinedMilitaryShovelReplica", "CutBlock", new String[] {"log=leaves/0x80", "log2=leaves2/0x80"}).getStringList();
		
		digLimit			= lconf.get("RefinedMilitaryShovelReplica", "digLimit", 5).getInt();
		digUnder			= lconf.get("RefinedMilitaryShovelReplica", "digUnder", true).getBoolean(true);
		mineLimit			= lconf.get("RefinedMilitaryShovelReplica", "mineLimit", 1).getInt();
		mineUnder			= lconf.get("RefinedMilitaryShovelReplica", "mineUnder", true).getBoolean(true);
		cutLimit			= lconf.get("RefinedMilitaryShovelReplica", "cutLimit", 10).getInt();
		cutUnder			= lconf.get("RefinedMilitaryShovelReplica", "cutUnder", false).getBoolean(false);
		cutOtherDirection	= lconf.get("RefinedMilitaryShovelReplica", "cutOtherDirection", true).getBoolean(true);
		cutBreakLeaves		= lconf.get("RefinedMilitaryShovelReplica", "cutBreakLeaves", true).getBoolean(true);
		cutTheBig			= lconf.get("RefinedMilitaryShovelReplica", "cutTheBig", true).getBoolean(true);
		lconf.save();
		
		// アイテムの登録
		RMRSpadeI	= new ItemMilitarySpade(Item.ToolMaterial.IRON).setUnlocalizedName("RMRSpadeI").setTextureName("mmm:ms");
		RMRPickaxeI	= new ItemMilitaryPickaxe(Item.ToolMaterial.IRON).setUnlocalizedName("RMRPickaxeI").setTextureName("mmm:ms2");
		RMRAxeI		= new ItemMilitaryAxe(Item.ToolMaterial.IRON).setUnlocalizedName("RMRAxeI").setTextureName("mmm:ms3");
		RMRSpadeD	= new ItemMilitarySpade(Item.ToolMaterial.EMERALD).setUnlocalizedName("RMRSpadeD").setTextureName("mmm:ms_d");
		RMRPickaxeD	= new ItemMilitaryPickaxe(Item.ToolMaterial.EMERALD).setUnlocalizedName("RMRPickaxeD").setTextureName("mmm:ms2_d");
		RMRAxeD		= new ItemMilitaryAxe(Item.ToolMaterial.EMERALD).setUnlocalizedName("RMRAxeD").setTextureName("mmm:ms3_d");
		// 変形の設定
		((ItemMilitarySpade)RMRSpadeI).nextItem = RMRPickaxeI;
		((ItemMilitaryPickaxe)RMRPickaxeI).nextItem = RMRAxeI;
		((ItemMilitaryAxe)RMRAxeI).nextItem = RMRSpadeI;
		((ItemMilitarySpade)RMRSpadeD).nextItem = RMRPickaxeD;
		((ItemMilitaryPickaxe)RMRPickaxeD).nextItem = RMRAxeD;
		((ItemMilitaryAxe)RMRAxeD).nextItem = RMRSpadeD;
		GameRegistry.registerItem(RMRSpadeI, "RMRSpadeI");
		GameRegistry.registerItem(RMRPickaxeI, "RMRPickaxeI");
		GameRegistry.registerItem(RMRAxeI, "RMRAxeI");
		GameRegistry.registerItem(RMRSpadeD, "RMRSpadeD");
		GameRegistry.registerItem(RMRPickaxeD, "RMRPickaxeD");
		GameRegistry.registerItem(RMRAxeD, "RMRAxeD");
		
		// Dig
		analizeString((IDestroyAll)RMRSpadeI, DigBlock);
		// Mine
		analizeString((IDestroyAll)RMRPickaxeD, MineBlock);
		// Cut
		analizeString((IDestroyAll)RMRAxeI, CutBlock);
		
		// SIDE用初期化
		proxy.init();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent pEvent) {
		// 一応ドキュメント上ではここでレシピとかを宣言するらしい。
		// レシピ
		GameRegistry.addRecipe(new ItemStack(RMRSpadeI),
				" # ",
				"#X#",
				'#', Items.iron_ingot,
				'X', Items.iron_shovel
			);
		GameRegistry.addRecipe(new ItemStack(RMRSpadeD, 1, 0),
				" # ",
				"#X#",
				'#', Items.diamond,
				'X', Items.diamond_shovel
			);
//		FMLCommonHandler.instance().bus().register(proxy);
		
		// MMMLibのRevisionチェック
//		MMM_Helper.checkRevision("7");
//		MMM_Config.checkConfig(this.getClass());
	}

	/**
	 * 文字列を解析して対象ブロックを設定する。
	 * @param pItem
	 * @param pConfig
	 */
	private void analizeString(IDestroyAll pItem, String[] pConfig) {
		DestroyAllIdentificator ldbi[] = new DestroyAllIdentificator[pConfig.length];
		for (int li = 0; li < pConfig.length; li++) {
			ldbi[li] = new DestroyAllIdentificator(pConfig[li]);
		}
		pItem.setTargets(ldbi);
	}

	@EventHandler
	public void serverStart(FMLServerStartingEvent pEvent) {
		// ネットワークのハンドラを登録
		DestroyAllManager.init();
	}

}
