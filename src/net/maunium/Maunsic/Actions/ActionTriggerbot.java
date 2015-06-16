package net.maunium.Maunsic.Actions;

import java.util.Random;

import net.maunium.Maunsic.Actions.Util.TickAction;
import net.maunium.Maunsic.Util.MaunsiConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;

public class ActionTriggerbot implements TickAction {
	private boolean active = false;
	private long lastAttack = 0;
	private Random r = new Random(System.nanoTime());
	private int delay = r.nextInt(250);
	
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
	
	@Override
	public String[] getStatusText() {
		return new String[] { "Trigger bot" + EnumChatFormatting.GREEN + " ON" };
	}
	
	@Override
	public void saveData(MaunsiConfig conf) {}
	
	@Override
	public void loadData(MaunsiConfig conf) {}
}
