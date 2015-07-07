package net.maunium.Maunsic.Actions;

import net.maunium.Maunsic.Actions.Util.TickAction;
import net.maunium.Maunsic.Util.MaunsiConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

public class ActionFullbright implements TickAction {
	private boolean active = false;
	
	@Override
	public boolean isActive() {
		return active;
	}
	
	@Override
	public void setActive(boolean active) {
		this.active = active;
	}
	
	@Override
	public void saveData(MaunsiConfig conf) {}
	
	@Override
	public void loadData(MaunsiConfig conf) {}
	
	@Override
	public String[] getStatusText() {
		return new String[] { "Fullbright " + EnumChatFormatting.GREEN + "ON" };
	}
	
	@Override
	public void execute() {
		if (isActive()) {
			if (Minecraft.getMinecraft().gameSettings.gammaSetting < 16F) Minecraft.getMinecraft().gameSettings.gammaSetting += 0.5F;
		} else if (Minecraft.getMinecraft().gameSettings.gammaSetting > 0.5F) {
			if (Minecraft.getMinecraft().gameSettings.gammaSetting < 1F) Minecraft.getMinecraft().gameSettings.gammaSetting = 0.5F;
			else Minecraft.getMinecraft().gameSettings.gammaSetting -= 0.5F;
		}
	}
	
}
