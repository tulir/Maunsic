package net.maunium.Maunsic.Actions;

import java.util.Random;

import net.maunium.Maunsic.Actions.Util.TickAction;
import net.maunium.Maunsic.Util.MaunsiConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;

/**
 * Attack if looking at an entity.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class ActionTriggerbot extends TickAction {
	private long lastAttack = 0;
	private Random r = new Random(System.nanoTime());
	public int delay = 0, minDelay = 50, maxDelay = 125;
	
	@Override
	public void execute() {
		Minecraft mc = Minecraft.getMinecraft();
		MovingObjectPosition moprt = mc.objectMouseOver;
		if (Minecraft.getSystemTime() - lastAttack > delay && moprt != null && moprt.entityHit != null) {
			mc.playerController.attackEntity(mc.thePlayer, moprt.entityHit);
			mc.thePlayer.swingItem();
			delay = r.nextInt(Math.abs(maxDelay - minDelay)) + minDelay;
			lastAttack = Minecraft.getSystemTime();
		}
	}
	
	@Override
	public String[] getStatusText() {
		return new String[] { "Trigger bot" + EnumChatFormatting.GREEN + " ON" };
	}
	
	@Override
	public void saveData(MaunsiConfig conf) {
		conf.set("actions.triggerbot.mindelay", minDelay);
		conf.set("actions.triggerbot.maxdelay", maxDelay);
	}
	
	@Override
	public void loadData(MaunsiConfig conf) {
		minDelay = conf.getInt("actions.triggerbot.mindelay", minDelay);
		maxDelay = conf.getInt("actions.triggerbot.maxdelay", maxDelay);
	}
}
