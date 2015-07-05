/*
 * Copyright � 2014 - 2015 | Alexander01998 | All rights reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a
 * copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.maunium.Maunsic.Util;

import java.util.Collection;

import net.maunium.Maunsic.Settings.Attacking;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
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
			Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch,
					Minecraft.getMinecraft().thePlayer.onGround));
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
			float neededYaw = Minecraft.getMinecraft().thePlayer.rotationYaw - neededRotations[0], neededPitch = Minecraft.getMinecraft().thePlayer.rotationPitch
					- neededRotations[1];
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
//	public static ArrayList<EntityLivingBase> getCloseEntities(boolean ignoreFriends, float range) {
//		ArrayList<EntityLivingBase> closeEntities = new ArrayList<EntityLivingBase>();
//		for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList)
//			if (isCorrectEntity(o, ignoreFriends)) {
//				EntityLivingBase en = (EntityLivingBase) o;
//				if (!(o instanceof EntityPlayerSP) && !en.isDead && en.getHealth() > 0 && Minecraft.getMinecraft().thePlayer.canEntityBeSeen(en)
//						&& !en.getName().equals(Minecraft.getMinecraft().thePlayer.getName())
//						&& Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en) <= range) closeEntities.add(en);
//			}
//		return closeEntities;
//	}
//	
//	public static EntityLivingBase getClosestEntityRaw(boolean ignoreFriends) {
//		EntityLivingBase closestEntity = null;
//		for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList)
//			if (isCorrectEntity(o, ignoreFriends)) {
//				EntityLivingBase en = (EntityLivingBase) o;
//				if (!(o instanceof EntityPlayerSP) && !en.isDead && en.getHealth() > 0)
//					if (closestEntity == null
//							|| Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en) < Minecraft.getMinecraft().thePlayer
//									.getDistanceToEntity(closestEntity)) closestEntity = en;
//			}
//		return closestEntity;
//	}
//	
//	public static EntityLivingBase getClosestEnemy(EntityLivingBase friend) {
//		EntityLivingBase closestEnemy = null;
//		for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList)
//			if (isCorrectEntity(o, true)) {
//				EntityLivingBase en = (EntityLivingBase) o;
//				if (!(o instanceof EntityPlayerSP) && o != friend && !en.isDead && en.getHealth() <= 0 == false
//						&& Minecraft.getMinecraft().thePlayer.canEntityBeSeen(en))
//					if (closestEnemy == null
//							|| Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en) < Minecraft.getMinecraft().thePlayer
//									.getDistanceToEntity(closestEnemy)) closestEnemy = en;
//			}
//		return closestEnemy;
//	}
//	
//	public static EntityLivingBase searchEntityByIdRaw(UUID ID) {
//		EntityLivingBase newEntity = null;
//		for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList)
//			if (isCorrectEntity(o, false)) {
//				EntityLivingBase en = (EntityLivingBase) o;
//				if (!(o instanceof EntityPlayerSP) && !en.isDead) if (newEntity == null && en.getUniqueID().equals(ID)) newEntity = en;
//			}
//		return newEntity;
//	}
//	
//	public static EntityLivingBase searchEntityByName(String name) {
//		EntityLivingBase newEntity = null;
//		for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList)
//			if (isCorrectEntity(o, false)) {
//				EntityLivingBase en = (EntityLivingBase) o;
//				if (!(o instanceof EntityPlayerSP) && !en.isDead && Minecraft.getMinecraft().thePlayer.canEntityBeSeen(en))
//					if (newEntity == null && en.getName().equals(name)) newEntity = en;
//			}
//		return newEntity;
//	}
//	
//	public static EntityLivingBase searchEntityByNameRaw(String name) {
//		EntityLivingBase newEntity = null;
//		for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList)
//			if (isCorrectEntity(o, false)) {
//				EntityLivingBase en = (EntityLivingBase) o;
//				if (!(o instanceof EntityPlayerSP) && !en.isDead) if (newEntity == null && en.getName().equals(name)) newEntity = en;
//			}
//		return newEntity;
//	}
}
