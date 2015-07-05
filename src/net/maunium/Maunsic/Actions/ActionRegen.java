package net.maunium.Maunsic.Actions;

import net.maunium.Maunsic.Actions.Util.TickAction;
import net.maunium.Maunsic.Util.MaunsiConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.EnumChatFormatting;

public class ActionRegen implements TickAction {
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
		return new String[] { "Regen Hack " + EnumChatFormatting.GREEN + "ON" };
	}
	
	@Override
	public void execute() {
		EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
		if (!p.capabilities.isCreativeMode && p.getFoodStats().getFoodLevel() > 17 && p.getHealth() < 20 && p.getHealth() != 0) {
			for (int i = 0; i < 1000; i++)
				Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
		}
	}
	
}
