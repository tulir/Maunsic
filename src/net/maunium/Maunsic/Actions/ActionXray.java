package net.maunium.Maunsic.Actions;

import java.util.HashSet;
import java.util.Set;

import net.maunium.Maunsic.Actions.Util.StatusAction;
import net.maunium.Maunsic.Util.MaunsiConfig;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

/**
 * X-Ray vision. This is just an action wrapper. All the actual changes happen in the block rendering classes. They call the isDisabled method to see which
 * blocks should be hidden.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class ActionXray implements StatusAction {
	public static boolean enabled = false;
	private static Set<String> disabledBlocks = new HashSet<String>();
	
	/**
	 * Check if the given block should be rendered.
	 * 
	 * @param b The block to check.
	 * @return False if the block should be rendered, true if it should be hidden.
	 */
	public static boolean isDisabled(Block b) {
		return disabledBlocks.contains(Block.blockRegistry.getNameForObject(b).toString());
	}
	
	/**
	 * Mark the given block as one which should not be rendered.
	 * 
	 * @param b The block to mark.
	 * @return see {@link HashSet#add(Object)}
	 */
	public static boolean disableBlock(Block b) {
		return disabledBlocks.add(Block.blockRegistry.getNameForObject(b).toString());
	}
	
	/**
	 * Unmark the given block as one which should not be rendered.
	 * 
	 * @param b The block to unmark.
	 * @return see {@link HashSet#remove(Object)}
	 */
	public static boolean enableBlock(Block b) {
		return disabledBlocks.remove(Block.blockRegistry.getNameForObject(b).toString());
	}
	
	/**
	 * Get a hash set of the registry names of the disabled blocks (disabled = don't render)
	 */
	public static Set<String> getDisabledBlocks() {
		return disabledBlocks;
	}
	
	@Override
	public String[] getStatusText() {
		return new String[] { "X-Ray " + EnumChatFormatting.GREEN + "ON" };
	}
	
	@Override
	public boolean isActive() {
		return enabled;
	}
	
	@Override
	public void setActive(boolean active) {
		enabled = active;
		Minecraft.getMinecraft().renderGlobal.loadRenderers();
	}
	
	@Override
	public void saveData(MaunsiConfig conf) {
		conf.set("xray.blocks", disabledBlocks);
	}
	
	@Override
	public void loadData(MaunsiConfig conf) {
		disabledBlocks = new HashSet<String>(conf.getStringList("xray.blocks"));
	}
}
