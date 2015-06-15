package net.maunium.Maunsic.TickActions;

import java.util.List;

import net.maunium.Maunsic.Settings.Friends;
import net.maunium.Maunsic.TickActions.Util.IntervalAction;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;

public class ActionAttackaura extends IntervalAction {
	private boolean active = false;
	private double range;
	
	@Override
	public void executeInterval() {
		EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
		List<Entity> entities = getEntitiesAABB(EntityFireball.class, range);
		for (Class<? extends EntityLivingBase> c : Friends.getTargets())
			entities.addAll(getEntitiesAABB(c, range));
		boolean swing = false;
		for (Entity e : entities) {
			if (e instanceof EntityPlayer) {
				EntityPlayer b = (EntityPlayer) e;
				if (Friends.isFriend(b.getName()) || b == p) continue;
			}
			Minecraft.getMinecraft().playerController.attackEntity(p, e);
			if (!swing) {
				p.swingItem();
				swing = true;
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
	public String getName() {
		return "Attack Aura";
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
}
