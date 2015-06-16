package net.maunium.Maunsic.Actions;

import java.util.List;

import net.maunium.Maunsic.Actions.Util.IntervalAction;
import net.maunium.Maunsic.Settings.Friends;
import net.maunium.Maunsic.Util.MaunsiConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;

public class ActionAttackaura extends IntervalAction {
	private boolean active = false, swing = true;
	private double range = 4.0D;
	
	@Override
	public void executeInterval() {
		EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
		List<Entity> entities = getEntitiesAABB(EntityFireball.class, range);
		for (Class<? extends EntityLivingBase> c : Friends.getTargets())
			entities.addAll(getEntitiesAABB(c, range));
		boolean hasSwung = false;
		for (Entity e : entities) {
			if (e instanceof EntityPlayer) {
				EntityPlayer b = (EntityPlayer) e;
				if (Friends.isFriend(b.getName()) || b == p) continue;
			}
			if (!p.canEntityBeSeen(e)) continue;
			Minecraft.getMinecraft().playerController.attackEntity(p, e);
			if (!hasSwung && swing) {
				p.swingItem();
				hasSwung = true;
			}
		}
	}
	
	private List<Entity> getEntitiesAABB(Class<?> c, double range) {
		EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
		@SuppressWarnings("unchecked")
		List<Entity> l = p.worldObj.getEntitiesWithinAABB(c,
				AxisAlignedBB.fromBounds(p.posX - range, p.posY - range, p.posZ - range, p.posX + range, p.posY + range, p.posZ + range));
		return l;
	}
	
	@Override
	public String[] getStatusText() {
		String[] rtrn = new String[3];
		rtrn[0] = "Attack Aura " + EnumChatFormatting.GREEN + "ON";
		if (range <= 6) rtrn[1] = " Range: " + EnumChatFormatting.GREEN + range;
		else rtrn[1] = " Range: " + EnumChatFormatting.GREEN + EnumChatFormatting.ITALIC + range;
		if (interval >= 20) rtrn[2] = " Speed (ms/hit): " + EnumChatFormatting.GREEN + interval;
		else rtrn[2] = " Speed (ms/hit): " + EnumChatFormatting.GREEN + EnumChatFormatting.ITALIC + interval;
		return rtrn;
	}
	
	@Override
	public boolean isActive() {
		return active;
	}
	
	@Override
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void setInterval(int interval) {
		this.interval = interval;
	}
	
	@Override
	public void saveData(MaunsiConfig conf) {
		conf.set("actions.attackaura.swing", swing);
		conf.set("actions.attackaura.range", range);
		conf.set("actions.attackaura.interval", interval);
	}
	
	@Override
	public void loadData(MaunsiConfig conf) {
		swing = conf.getBoolean("actions.attackaura.swing", swing);
		range = conf.getDouble("actions.attackaura.range", range);
		interval = conf.getInt("actions.attackaura.interval", interval);
	}
}
