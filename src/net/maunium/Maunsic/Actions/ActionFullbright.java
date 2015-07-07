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
	
	public boolean isActuallyActive() {
		return active;
	}
	
	@Override
	public void setActive(boolean active) {
		if (gamma > 1.0F) gamma = 1.0F;
		this.active = active;
	}
	
	@Override
	public void saveData(MaunsiConfig conf) {}
	
	@Override
	public void loadData(MaunsiConfig conf) {}
	
	@Override
	public String[] getStatusText() {
		return isActuallyActive() ? new String[] { "Fullbright " + EnumChatFormatting.GREEN + "ON" } : null;
	}
	
	@Override
	public void execute() {
		if (isActuallyActive()) {
			if (Minecraft.getMinecraft().gameSettings.gammaSetting < 16F) Minecraft.getMinecraft().gameSettings.gammaSetting += 0.5F;
		} else if (Minecraft.getMinecraft().gameSettings.gammaSetting > gamma) {
			Minecraft.getMinecraft().gameSettings.gammaSetting -= 0.5F;
		}
	}
	
}
