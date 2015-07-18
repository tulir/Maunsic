package net.maunium.Maunsic.Actions;

import java.util.ArrayList;
import java.util.List;

import net.maunium.Maunsic.Maunsic;
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
	public static int safetyLevel = 15;
	
	private static List<Packet> packets = new ArrayList<Packet>();
	private static long blinkStart = 0;
	
	public static void startBlinking() {
		if (Minecraft.getMinecraft().theWorld.isRemote) {
			Maunsic.printChatError("message.blink.sp");
			return;
		}
		// Set the start timestamp
		blinkStart = Minecraft.getSystemTime();
		// Set the active status to true
		active = true;
	}
	
	public static void blink(Packet p) {
		// Check if safety level is above zero and the safety point has passed
		if (safetyLevel > 0 && blinkStart + safetyLevel * 100 < Minecraft.getSystemTime()) {
			// Stop blinking
			releaseBlink();
			
			// If blink is automated, start blinking again.
			if (automated) startBlinking();
			// Otherwise return.
			else return;
		}
		// Add the packet to the blink queue
		packets.add(p);
	}
	
	public static void releaseBlink() {
		// Set active status to false
		active = false;
		NetHandlerPlayClient nhpc = Minecraft.getMinecraft().thePlayer.sendQueue;
		// Send all packets in the blink queue.
		for (Packet p : packets)
			nhpc.addToSendQueue(p);
		// Clear the blink queue
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
	public boolean isActive() {
		return active;
	}
	
	@Override
	public String[] getStatusText() {
		String[] rtrn = new String[2];
		if (automated && safetyLevel > 0) {
			rtrn[0] = "Blink " + EnumChatFormatting.GREEN + "Automated";
			if (safetyLevel <= 20) rtrn[1] = " Release delay: " + EnumChatFormatting.GREEN + safetyLevel / 10.0 + " seconds";
			else rtrn[1] = " Release delay: " + EnumChatFormatting.RED + safetyLevel / 10.0 + " seconds";
		} else {
			rtrn[0] = "Blink " + EnumChatFormatting.RED + "Manual";
			if (safetyLevel > 20) rtrn[1] = " Release delay: " + EnumChatFormatting.RED + safetyLevel / 10.0 + " seconds";
			else if (safetyLevel > 0) rtrn[1] = " Release delay: " + EnumChatFormatting.GREEN + safetyLevel / 10.0 + " seconds";
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
