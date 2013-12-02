package net.minecraft.src;

public class mod_RMR_RefinedMilitaryShovelReplica extends BaseMod {

	
	public static int cfg_ItemID = 22280;
	public static boolean cfg_isStarting = true;
	public static boolean cfg_isDebugMessage = true;
	public static String cfg_DigBlock = "2-0,3-0; 3-0,2-0; 12;13; 82; 110;";
	public static String cfg_MineBlock = "1-0;14;15;16;21;24; 49;56;73; 87;88;89; 129; 153;";
	public static String cfg_CutBlock = "17,18;";
	
	public static int cfg_digLimit = 5;
	public static boolean cfg_digUnder = true;
	public static int cfg_mineLimit = 1;
	public static boolean cfg_mineUnder = true;
	public static int cfg_cutLimit = 10;
	public static boolean cfg_cutUnder = false;
	public static boolean cfg_cutOtherDirection = true;
	public static boolean cfg_cutBreakLeaves = true;
	public static boolean cfg_cutTheBig = true;

	public static int MaxDamage = 2;

	public static Item RMRSpadeI;
	public static Item RMRPickaxeI;
	public static Item RMRAxeI;
	public static Item RMRSpadeD;
	public static Item RMRPickaxeD;
	public static Item RMRAxeD;
	
	public static String packetChannel = "RMR";
	
	public static boolean isDestroyEnable;
	protected static int selectedblockID;
	protected static int selectedMetadata;


	public static void Debug(String pText, Object... pData) {
		// デバッグメッセージ
		if (cfg_isDebugMessage) {
			System.out.println(String.format("RMR-" + pText, pData));
		}
	}

	@Override
	public String getName() {
		return "RefinedMilitaryShovelReplica";
	}

	@Override
	public String getVersion() {
		return "1.6.2-1";
	}

	@Override
	public String getPriorities() {
		return "required-after:mod_MMM_MMMLib";
	}

	@Override
	public void load() {
		// MMMLibのRevisionチェック
		MMM_Helper.checkRevision("7");
		MMM_Config.checkConfig(this.getClass());
		
		// アイテムの登録
		RMRSpadeI	= new RMR_ItemSpade(cfg_ItemID + 0 - 256, EnumToolMaterial.IRON).setUnlocalizedName("RMRSpadeI").func_111206_d("ms");
		RMRPickaxeI	= new RMR_ItemPickaxe(cfg_ItemID + 1 - 256, EnumToolMaterial.IRON).setUnlocalizedName("RMRPickaxeI").func_111206_d("ms2");
		RMRAxeI		= new RMR_ItemAxe(cfg_ItemID + 2 - 256, EnumToolMaterial.IRON).setUnlocalizedName("RMRAxeI").func_111206_d("ms3");
		RMRSpadeD	= new RMR_ItemSpade(cfg_ItemID + 3 - 256, EnumToolMaterial.EMERALD).setUnlocalizedName("RMRSpadeD").func_111206_d("ms_d");
		RMRPickaxeD	= new RMR_ItemPickaxe(cfg_ItemID + 4 - 256, EnumToolMaterial.EMERALD).setUnlocalizedName("RMRPickaxeD").func_111206_d("ms2_d");
		RMRAxeD		= new RMR_ItemAxe(cfg_ItemID + 5 - 256, EnumToolMaterial.EMERALD).setUnlocalizedName("RMRAxeD").func_111206_d("ms3_d");
		
		ModLoader.addRecipe(new ItemStack(RMRSpadeI),
				" # ",
				"#X#",
				'#', Item.ingotIron,
				'X', Item.shovelIron
			);
		ModLoader.addRecipe(new ItemStack(RMRSpadeD, 1, 0),
				" # ",
				"#X#",
				'#', Item.diamond,
				'X', Item.shovelDiamond
			);
		
		ModLoader.addName(RMRSpadeI,	"IronMilitaryShovel Spade");
		ModLoader.addName(RMRPickaxeI,	"IronMilitaryShovel Pickax");
		ModLoader.addName(RMRAxeI,		"IronMilitaryShovel Axe");
		ModLoader.addName(RMRSpadeD,	"DiamondMilitaryShovel Spade");
		ModLoader.addName(RMRPickaxeD,	"DiamondMilitaryShovel Pickax");
		ModLoader.addName(RMRAxeD,		"DiamondMilitaryShovel Axe");
		
		// destroyAll切り替えボタン
		if (MMM_Helper.isClient) {
			String ls = "key.rmr";
			ModLoader.registerKey(this, new KeyBinding(ls, 46), false);
			ModLoader.addLocalization(ls, "RMR Config");
		}
		
		// コンフィグの解析・設定
		isDestroyEnable = cfg_isStarting;
		int li;
		int lj;
		String[] lsd;
		// Dig
		analizeString((RMR_IDestroyAll)RMRSpadeI, cfg_DigBlock);
		// Mine
		analizeString((RMR_IDestroyAll)RMRPickaxeD, cfg_MineBlock);
		// Cut
		analizeString((RMR_IDestroyAll)RMRAxeI, cfg_CutBlock);
		
		// カスタムパケットの登録
		ModLoader.registerPacketChannel(this, packetChannel);
		ModLoader.setInGameHook(this, true, true);
	}

	private int getInt(String pString) {
		pString = pString.trim();
		if (pString.startsWith("0x")) {
			return Integer.parseInt(pString.substring(2), 16);
		} else {
			return Integer.parseInt(pString);
		}
	}

	/**
	 * 文字列を解析して対象ブロックを設定する。
	 * @param pItem
	 * @param pConfig
	 */
	private void analizeString(RMR_IDestroyAll pItem, String pConfig) {
		String[] lsd = pConfig.split(";");
		int[][] ltb = new int[lsd.length][];
		int[][] ltm = new int[lsd.length][];
		int li = 0;
		for (String lss : lsd) {
			String[] ltd = lss.split(",");
			ltb[li] = new int[ltd.length];
			ltm[li] = new int[ltd.length];
			int lj = 0;
			for (String ltt : ltd) {
				String[] lt = ltt.split("-");
				ltb[li][lj] = getInt(lt[0]);
				if (lt.length > 1) {
					ltm[li][lj] = getInt(lt[1]);
				} else {
					ltm[li][lj] = 0;
				}
				lj++;
			}
			li++;
		}
		pItem.setTargets(ltb, ltm);
	}

	@Override
	public void keyboardEvent(KeyBinding keybinding) {
		RMR_GuiConfigure.keyboardEvent(keybinding);
	}

	@Override
	public boolean onTickInGame(float f, Minecraft minecraft) {
		if (minecraft != null) {
			// ターゲットしているブロックを獲得
			selectedblockID = 0;
			selectedMetadata = 0;
			MovingObjectPosition lmo = minecraft.objectMouseOver;
			if (lmo != null && lmo.typeOfHit == EnumMovingObjectType.TILE) {
				selectedblockID = minecraft.theWorld.getBlockId(lmo.blockX, lmo.blockY, lmo.blockZ);
				selectedMetadata = minecraft.theWorld.getBlockMetadata(lmo.blockX, lmo.blockY, lmo.blockZ);
			}
		}
		return true;
	}

	@Override
	public void serverCustomPayload(NetServerHandler serverHandler,
			Packet250CustomPayload packet250custompayload) {
		if (!isDestroyEnable) {
			return;
		}
		RMR_DestroyData lddata = new RMR_DestroyData(serverHandler.playerEntity.worldObj, packet250custompayload.data);
		if (lddata.destroyAll == null) {
			return;
		}
		Debug("Request DestroyAll:%s", serverHandler.playerEntity.getEntityName());
		
		// サーバー側で限界を制限
		switch (lddata.mode) {
		case (byte)0x80:
			// DigAll
			lddata.maxRangeW = lddata.maxRangeH = Math.min(lddata.maxRangeW, cfg_digLimit);
			break;
		case (byte)0x81:
			// MineAll
			lddata.maxRangeW = lddata.maxRangeH = Math.min(lddata.maxRangeW, cfg_mineLimit);
			break;
		case (byte)0x82:
			// CutAll
			lddata.maxRangeW = Math.min(lddata.maxRangeW, cfg_cutLimit);
			lddata.maxRangeH = 32000;
			break;
		}
		lddata.destroyAll.destroyAll(lddata);
	}

}
