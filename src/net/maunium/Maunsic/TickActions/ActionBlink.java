package net.maunium.Maunsic.TickActions;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumChatFormatting;

public class ActionBlink implements TickAction {
	private static boolean active, automated;
	private static int safetyLevel;
	
	@Override
	public void execute() {}
	
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
	public String getName() {
		return "Blink";
	}
	
	@Override
	public boolean isActive() {
		return active;
	}
	
	@Override
	public void setActive(boolean active) {
		if (active) startBlinking();
		else releaseBlink();
	}
}
