package net.maunium.Maunsic.Actions;

import java.util.ArrayList;
import java.util.List;

import net.maunium.Maunsic.Actions.Util.IntervalAction;
import net.maunium.Maunsic.Settings.Attacking;
import net.maunium.Maunsic.Util.MaunsiConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;

/**
 * Aimbot.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class ActionAimbot extends IntervalAction {
	private boolean active = false;
	
	@Override
	public void executeInterval() {
		EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
		List<Entity> entities = getEntitiesAABB(EntityFireball.class, Attacking.range);
		for (Class<? extends EntityLivingBase> c : Attacking.getTargets())
			entities.addAll(getEntitiesAABB(c, Attacking.range));
		Entity c = null;
		double cd = Double.MAX_VALUE;
		for (Entity e : entities) {
			if (e instanceof EntityPlayer) {
				EntityPlayer b = (EntityPlayer) e;
				if (Attacking.isFriend(b.getName()) || b.equals(p)) continue;
			}
			double dX = p.posX - e.posX, dY = p.posY - e.posY, dZ = p.posZ - e.posZ;
			double d = dX * dX + dY * dY + dZ * dZ;
			if (d < cd) {
				c = e;
				cd = d;
			}
		}
		if (c != null) {
			double dX = Math.abs(c.posX) - Math.abs(p.posX), dY = Math.abs(c.posY) - Math.abs(p.posY), dZ = Math.abs(c.posZ) - Math.abs(p.posZ);
			double yaw = Math.toDegrees(Math.atan(dY / dX));
			double pitch = Math.toDegrees(Math.atan(Math.sqrt(dX * dX + dY * dY) / dZ));
			if (c.posX < p.posX) yaw -= 180;
			p.rotationYaw = (float) yaw - 90;
			p.rotationPitch = (float) pitch - 90;
		}
	}
	
	private List<Entity> getEntitiesAABB(Class<?> c, double range) {
		EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
		List<?> l = p.worldObj.getEntitiesWithinAABB(c,
				AxisAlignedBB.fromBounds(p.posX - range, p.posY - range, p.posZ - range, p.posX + range, p.posY + range, p.posZ + range));
		List<Entity> rtrn = new ArrayList<Entity>();
		for (Object o : l)
			if (o instanceof Entity) rtrn.add((Entity) o);
		return rtrn;
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
	public boolean isActive() {
		return active;
	}
	
	@Override
	public void setActive(boolean active) {
		this.active = active;
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
