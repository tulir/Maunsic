package net.maunium.Maunsic.Actions;

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
		List<Entity> entities = getEntitiesAABB(EntityFireball.class, Attacking.range);
		for (Class<? extends EntityLivingBase> c : Attacking.getTargets())
			entities.addAll(getEntitiesAABB(c, Attacking.range));
		boolean hasSwung = false;
		for (Entity e : entities) {
			if (e instanceof EntityPlayer) {
				EntityPlayer b = (EntityPlayer) e;
				if (Attacking.isFriend(b.getName()) || b == p) continue;
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
