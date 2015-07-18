package net.maunium.Maunsic.Actions;

import java.util.ArrayList;
import java.util.List;

import net.maunium.Maunsic.Actions.Util.IntervalAction;
import net.maunium.Maunsic.Settings.Attacking;
import net.maunium.Maunsic.Util.EntityUtils;
import net.maunium.Maunsic.Util.MaunsiConfig;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.EnumChatFormatting;

/**
 * Aimbot.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class ActionAimbot extends IntervalAction {
	@Override
	public void executeInterval() {
		List<Entity> e = new ArrayList<Entity>(EntityUtils.getEntitiesAABB(EntityFireball.class, Attacking.range));
		for (Class<?> c : Attacking.getTargets())
			e.addAll(EntityUtils.getEntitiesAABB(c, Attacking.range));
			
		Entity c = EntityUtils.getClosestEntity(true, e);
		if (c == null) return;
		EntityUtils.faceEntityClient(c);
	}
	
	@Override
	public String[] getStatusText() {
		String[] rtrn = new String[3];
		rtrn[0] = "Aimbot " + EnumChatFormatting.GREEN + "ON";
		if (Attacking.range <= 6) rtrn[1] = " Range: " + EnumChatFormatting.GREEN + Attacking.range;
		else rtrn[1] = " Range: " + EnumChatFormatting.GREEN + EnumChatFormatting.ITALIC + Attacking.range;
		
		if (interval >= 40) rtrn[2] = " Speed (ms/ref): " + EnumChatFormatting.GREEN + interval;
		else rtrn[2] = " Speed (ms/ref): " + EnumChatFormatting.GREEN + EnumChatFormatting.ITALIC + interval;
		return rtrn;
	}
	
	@Override
	public void saveData(MaunsiConfig conf) {
		conf.set("actions.aimbot.interval", interval);
	}
	
	@Override
	public void loadData(MaunsiConfig conf) {
		interval = conf.getInt("actions.aimbot.interval", interval);
	}
}
