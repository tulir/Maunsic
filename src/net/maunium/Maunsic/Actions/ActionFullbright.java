package net.maunium.Maunsic.Actions;

import net.maunium.Maunsic.Actions.Util.TickAction;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

/**
 * Much brighter environment.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class ActionFullbright extends TickAction {
	private float gamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
	
	public boolean isActuallyActive() {
		return active;
	}
	
	@Override
	public boolean isActive() {
		return true;
	}
	
	@Override
	public void activate() {
		super.activate();
		if (gamma > 1.0F) gamma = 1.0F;
	}
	
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
