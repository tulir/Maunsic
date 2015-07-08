package net.maunium.Maunsic.Actions;

import java.util.ArrayList;
import java.util.List;

import net.maunium.Maunsic.Actions.Util.StatusAction;
import net.maunium.Maunsic.Util.MaunsiConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumChatFormatting;

/**
 * Don't even blink
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class ActionBlink extends StatusAction {
	public static boolean automated = true, active = false;
	public static int safetyLevel = 3;
	
	private static List<Packet> packets = new ArrayList<Packet>();
	private static long blinkStart = 0;
	
	public static void startBlinking() {
		blinkStart = Minecraft.getSystemTime();
		active = true;
	}
	
	public static void blink(Packet p) {
		if (automated && safetyLevel > 0) {
			if (blinkStart + safetyLevel * 1000 < Minecraft.getSystemTime()) {
				releaseBlink();
				startBlinking();
			}
			packets.add(p);
		} else if (safetyLevel > 0) {
			if (blinkStart + safetyLevel * 1000 < Minecraft.getSystemTime()) releaseBlink();
			else packets.add(p);
		} else packets.add(p);
	}
	
	public static void releaseBlink() {
		active = false;
		NetHandlerPlayClient nhpc = Minecraft.getMinecraft().thePlayer.sendQueue;
		for (Packet p : packets)
			nhpc.addToSendQueue(p);
		packets.clear();
	}
	
	@Override
	public void activate() {
		startBlinking();
	}
	
	@Override
	public void deactivate() {
		releaseBlink();
	}
	
	@Override
	public void toggle() {
		if (active) activate();
		else deactivate();
	}
	
	@Override
	public boolean isActive() {
		return active;
	}
	
	@Override
	public String[] getStatusText() {
		String[] rtrn = new String[2];
		if (automated && safetyLevel != 0) {
			rtrn[0] = "Blink " + EnumChatFormatting.GREEN + "Automated";
			if (safetyLevel < 5) rtrn[1] = " Release delay: " + EnumChatFormatting.GREEN + safetyLevel + " seconds";
			else rtrn[1] = " Release delay: " + EnumChatFormatting.RED + safetyLevel + " seconds";
		} else {
			rtrn[0] = "Blink " + EnumChatFormatting.RED + "Manual";
			if (safetyLevel > 4) rtrn[1] = " Release delay: " + EnumChatFormatting.RED + safetyLevel + " seconds";
			else if (safetyLevel > 0) rtrn[1] = " Release delay: " + EnumChatFormatting.GREEN + safetyLevel + " seconds";
			else rtrn[1] = " Safety: " + EnumChatFormatting.RED + "Disabled";
		}
		return rtrn;
	}
	
	@Override
	public void saveData(MaunsiConfig conf) {
		conf.set("actions.blink.automated", automated);
		conf.set("actions.blink.safetylevel", safetyLevel);
	}
	
	@Override
	public void loadData(MaunsiConfig conf) {
		automated = conf.getBoolean("actions.blink.automated", automated);
		safetyLevel = conf.getInt("actions.blink.safetylevel", safetyLevel);
	}
}
