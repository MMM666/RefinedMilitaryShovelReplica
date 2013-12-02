package net.minecraft.src;

public interface RMR_IDestroyAll {

	/**
	 * 一括破壊のシグナルを受け取った時に実行する処理。
	 * サーバー側の動作。
	 * @return
	 */
	public boolean destroyAll(RMR_DestroyData pDData);

	/**
	 * アイテムのダメージ処理用にsuperを記述する関数。
	 */
	public boolean superOnBlockDestroyed(ItemStack par1ItemStack, World par2World,
			int par3, int par4, int par5, int par6, EntityLivingBase par7EntityLivingBase);

	/**
	 * 
	 * @param pBlockIDs
	 * @param pMetadatas
	 */
	public void setTargets(int[][] pBlockIDs, int[][] pMetadatas);

}
