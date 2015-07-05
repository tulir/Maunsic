package net.maunium.Maunsic.Actions;

import java.util.HashSet;
import java.util.Set;

import net.maunium.Maunsic.Actions.Util.StatusAction;
import net.maunium.Maunsic.Util.MaunsiConfig;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

public class ActionXray implements StatusAction {
	public static boolean enabled = false;
	private static Set<String> disabledBlocks = new HashSet<String>();
	
	public static boolean isDisabled(Block b) {
		return disabledBlocks.contains(Block.blockRegistry.getNameForObject(b).toString());
	}
	
	public static boolean disableBlock(Block b) {
		return disabledBlocks.add(Block.blockRegistry.getNameForObject(b).toString());
	}
	
	public static boolean enableBlock(Block b) {
		return disabledBlocks.remove(Block.blockRegistry.getNameForObject(b).toString());
	}
	
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
