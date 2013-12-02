package net.minecraft.src;

public interface RMR_IDestroyAll {

	/**
	 * �ꊇ�j��̃V�O�i�����󂯎�������Ɏ��s���鏈���B
	 * �T�[�o�[���̓���B
	 * @return
	 */
	public boolean destroyAll(RMR_DestroyData pDData);

	/**
	 * �A�C�e���̃_���[�W�����p��super���L�q����֐��B
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
