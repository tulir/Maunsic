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
	
	@Override
	public void activate() {
		super.activate();
		if (gamma > 1.0F) gamma = 1.0F;
		Minecraft.getMinecraft().gameSettings.gammaSetting = 20.0F;
	}
	
	@Override
	public void deactivate() {
		super.deactivate();
		if (gamma > 1.0F) gamma = 1.0F;
		Minecraft.getMinecraft().gameSettings.gammaSetting = gamma;
	}
	
	@Override
	public String[] getStatusText() {
		return new String[] { "Fullbright " + EnumChatFormatting.GREEN + "ON" };
	}
	
	@Override
	public void execute() {
		if (Minecraft.getMinecraft().gameSettings.gammaSetting < 20.0F) Minecraft.getMinecraft().gameSettings.gammaSetting = 20.0F;
	}
}
