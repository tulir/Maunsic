package net.maunium.Maunsic.Actions;

import java.util.Random;

import net.maunium.Maunsic.Actions.Util.TickAction;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MovingObjectPosition;

public class ActionTriggerbot implements TickAction {
	private boolean active = false;
	private long lastAttack = 0;
	private Random r = new Random(System.nanoTime());
	private int delay = r.nextInt(250);
	
	@Override
	public String getName() {
		return "Triggerbot";
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
		Minecraft mc = Minecraft.getMinecraft();
		MovingObjectPosition moprt = mc.objectMouseOver;
		if (Minecraft.getSystemTime() - lastAttack > delay && moprt != null && moprt.entityHit != null) {
			mc.playerController.attackEntity(mc.thePlayer, moprt.entityHit);
			mc.thePlayer.swingItem();
			delay = r.nextInt(200) + 50;
			lastAttack = Minecraft.getSystemTime();
		}
	}
}
