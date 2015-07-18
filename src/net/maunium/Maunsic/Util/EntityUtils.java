/*
 * Copyright � 2014 - 2015 | Alexander01998 | All rights reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a
 * copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.maunium.Maunsic.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.maunium.Maunsic.Settings.Attacking;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

/**
 * Most of the code from Wurst Client licenced under MPL v2.0 by Alexander01998
 * 
 * @author Alexander01998
 * @author Tulir293
 * @since 0.1
 * @from Wurst-Client
 */
public class EntityUtils {
	public synchronized static void faceEntityClient(Entity entity) {
		float[] rotations = getRotationsNeeded(entity);
		if (rotations != null) {
			Minecraft.getMinecraft().thePlayer.rotationYaw = limitAngleChange(Minecraft.getMinecraft().thePlayer.prevRotationYaw, rotations[0], 55);
			Minecraft.getMinecraft().thePlayer.rotationPitch = rotations[1];
		}
	}
	
	public synchronized static void faceEntityPacket(Entity entity) {
		float[] rotations = getRotationsNeeded(entity);
		if (rotations != null) {
			float yaw = rotations[0];
			float pitch = rotations[1];
			Minecraft.getMinecraft().thePlayer.sendQueue
					.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, Minecraft.getMinecraft().thePlayer.onGround));
		}
	}
	
	public static float[] getRotationsNeeded(Entity entity) {
		if (entity == null) return null;
		double diffX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
		double diffY;
		if (entity instanceof EntityLivingBase) {
			EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
			diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() * 0.9
					- (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
		} else diffY = (entity.getBoundingBox().minY + entity.getBoundingBox().maxY) / 2.0D
				- (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
		double diffZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
		return new float[] {
				Minecraft.getMinecraft().thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw),
				Minecraft.getMinecraft().thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch) };
				
	}
	
	private final static float limitAngleChange(final float current, final float intended, final float maxChange) {
		float change = intended - current;
		if (change > maxChange) change = maxChange;
		else if (change < -maxChange) change = -maxChange;
		return current + change;
	}
	
	public static int getDistanceFromMouse(EntityLivingBase entity) {
		float[] neededRotations = getRotationsNeeded(entity);
		if (neededRotations != null) {
			float neededYaw = Minecraft.getMinecraft().thePlayer.rotationYaw - neededRotations[0],
					neededPitch = Minecraft.getMinecraft().thePlayer.rotationPitch - neededRotations[1];
			float distanceFromMouse = MathHelper.sqrt_float(neededYaw * neededYaw + neededPitch * neededPitch);
			return (int) distanceFromMouse;
		}
		return -1;
	}
	
	public static Entity getClosestEntity(boolean ignoreFriends, Collection<Entity> entities) {
		Entity closestEntity = null;
		EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
		for (Entity e : entities) {
			if (e.isDead) continue;
			else if (e instanceof EntityPlayer && (e.equals(p) || Attacking.isFriend(((EntityPlayer) e).getName()))) continue;
			else if (!p.canEntityBeSeen(e)) continue;
			else if (closestEntity == null || p.getDistanceSqToEntity(closestEntity) < p.getDistanceSqToEntity(e)) closestEntity = e;
		}
		return closestEntity;
	}
	
	public static List<Entity> getEntitiesAABB(Class<?> c, double range) {
		EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
		List<?> l = p.worldObj.getEntitiesWithinAABB(c,
				AxisAlignedBB.fromBounds(p.posX - range, p.posY - range, p.posZ - range, p.posX + range, p.posY + range, p.posZ + range));
		List<Entity> rtrn = new ArrayList<Entity>();
		for (Object o : l)
			if (o instanceof Entity) rtrn.add((Entity) o);
		return rtrn;
	}
}
