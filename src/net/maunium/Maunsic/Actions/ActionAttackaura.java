package net.maunium.Maunsic.Actions;

import net.maunium.Maunsic.Actions.Util.IntervalAction;
import net.maunium.Maunsic.Settings.Attacking;
import net.maunium.Maunsic.Util.EntityUtils;
import net.maunium.Maunsic.Util.MaunsiConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

/**
 * Attack aura.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class ActionAttackaura extends IntervalAction {
	private boolean swing = true;
	
	@Override
	public void executeInterval() {
		EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
		boolean hasSwung = false;
		// Loop through the attacking target classes.
		for (Class<?> c : Attacking.getTargets()) {
			// Loop through the entities of the given class within the attacking range.
			for (Entity e : EntityUtils.getEntitiesAABB(c, Attacking.range)) {
				// If the entity is a player, do some checks.
				if (e instanceof EntityPlayer) {
					EntityPlayer b = (EntityPlayer) e;
					// If the entity is a friend or the entity is the player, ignore it.
					if (Attacking.isFriend(b.getName()) || b == p) continue;
				}
				
				// If the entity is hiding, ignore it.
				if (!p.canEntityBeSeen(e)) continue;
				// Attack the entity
				Minecraft.getMinecraft().playerController.attackEntity(p, e);
				// If the player hasn't swung yet and the swing flag is set to true...
				if (!hasSwung && swing) {
					// ... swing the hand ...
					p.swingItem();
					// ... and set hasSwung to true.
					hasSwung = true;
				}
			}
		}
	}
	
	@Override
	public String[] getStatusText() {
		String[] rtrn = new String[3];
		rtrn[0] = "Attack Aura " + EnumChatFormatting.GREEN + "ON";
		if (Attacking.range <= 6) rtrn[1] = " Range: " + EnumChatFormatting.GREEN + Attacking.range;
		else rtrn[1] = " Range: " + EnumChatFormatting.GREEN + EnumChatFormatting.ITALIC + Attacking.range;
		if (interval >= 20) rtrn[2] = " Speed (ms/hit): " + EnumChatFormatting.GREEN + interval;
		else rtrn[2] = " Speed (ms/hit): " + EnumChatFormatting.GREEN + EnumChatFormatting.ITALIC + interval;
		return rtrn;
	}
	
	public void setInterval(int interval) {
		this.interval = interval;
	}
	
	@Override
	public void saveData(MaunsiConfig conf) {
		conf.set("actions.attackaura.swing", swing);
		conf.set("actions.attackaura.interval", interval);
	}
	
	@Override
	public void loadData(MaunsiConfig conf) {
		swing = conf.getBoolean("actions.attackaura.swing", swing);
		interval = conf.getInt("actions.attackaura.interval", interval);
	}
}
