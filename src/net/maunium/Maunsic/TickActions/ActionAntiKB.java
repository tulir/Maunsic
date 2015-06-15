package net.maunium.Maunsic.TickActions;

import net.maunium.Maunsic.Maunsic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class ActionAntiKB implements TickAction {
	private boolean active = false;
	
	@Override
	public String getName() {
		return "Antiknockback";
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
		EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
		if (p.hurtTime > 0) Maunsic.getLogger().info(p.hurtTime);
		if (p.hurtTime > 0) {
			p.motionX = 0;
			p.motionY = 0;
			p.motionZ = 0;
		}
	}
}
