package net.maunium.Maunsic.Actions;

import net.maunium.Maunsic.Actions.Util.TickAction;
import net.maunium.Maunsic.Util.MaunsiConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

public class ActionFullbright implements TickAction {
	private boolean active = false;
	private float gamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
	
	@Override
	public boolean isActive() {
		return true;
	}
	
	@Override
	public void setActive(boolean active) {
		if (!active) Minecraft.getMinecraft().gameSettings.gammaSetting = gamma;
		else gamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
		this.active = active;
	}
	
	@Override
	public void saveData(MaunsiConfig conf) {}
	
	@Override
	public void loadData(MaunsiConfig conf) {}
	
	@Override
	public String[] getStatusText() {
		return active ? new String[] { "Fullbright " + EnumChatFormatting.GREEN + "ON" } : null;
	}
	
	@Override
	public void execute() {
		if (active) {
			if (Minecraft.getMinecraft().gameSettings.gammaSetting < 16F) Minecraft.getMinecraft().gameSettings.gammaSetting += 0.25F;
		} else if (Minecraft.getMinecraft().gameSettings.gammaSetting > gamma) {
			Minecraft.getMinecraft().gameSettings.gammaSetting -= 0.5F;
		}
	}
	
}
