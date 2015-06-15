package net.maunium.Maunsic.TickActions;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;

public class ActionNofall implements TickAction {
	private boolean active = false;
	
	@Override
	public String getName() {
		return "Nofall";
	}
	
	@Override
	public boolean isActive() {
		return active;
	}
	
	@Override
	public void setActive(boolean active) {
		this.active = active;
	}
	
	@Override
	public void execute() {
		Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
	}
}
