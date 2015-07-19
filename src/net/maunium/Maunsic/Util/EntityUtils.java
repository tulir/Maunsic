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
	/**
	 * Makes the player face the given entity on the client. Minecraft will soon after send the changes to the server.
	 */
	public synchronized static void faceEntityClient(Entity entity) {
		RotationsNeeded rn = getRotationsNeeded(entity);
		if (rn != null) {
			Minecraft.getMinecraft().thePlayer.rotationYaw = limitAngleChange(Minecraft.getMinecraft().thePlayer.prevRotationYaw, rn.yaw, 55);
			Minecraft.getMinecraft().thePlayer.rotationPitch = rn.pitch;
		}
	}
	
	/**
	 * Sends a packet that makes the player face the given entity. This may not be visible on the client.
	 */
	public synchronized static void faceEntityPacket(Entity entity) {
		RotationsNeeded rn = getRotationsNeeded(entity);
		if (rn != null) {
			Minecraft.getMinecraft().thePlayer.sendQueue
					.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(rn.yaw, rn.pitch, Minecraft.getMinecraft().thePlayer.onGround));
		}
	}
	
	private static RotationsNeeded getRotationsNeeded(Entity entity) {
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
		return new RotationsNeeded(
				Minecraft.getMinecraft().thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw),
				Minecraft.getMinecraft().thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch));
				
	}
	
	private final static float limitAngleChange(final float current, final float intended, final float maxChange) {
		float change = intended - current;
		if (change > maxChange) change = maxChange;
		else if (change < -maxChange) change = -maxChange;
		return current + change;
	}
	
	/**
	 * Get the distance from the mouse to the given entity.
	 */
	public static int getDistanceFromMouse(Entity e) {
		RotationsNeeded rn = getRotationsNeeded(e);
		if (rn != null) {
			float neededYaw = Minecraft.getMinecraft().thePlayer.rotationYaw - rn.yaw,
					neededPitch = Minecraft.getMinecraft().thePlayer.rotationPitch - rn.pitch;
			float distanceFromMouse = MathHelper.sqrt_float(neededYaw * neededYaw + neededPitch * neededPitch);
			return (int) distanceFromMouse;
		}
		return -1;
	}
	
	private static class RotationsNeeded {
		public RotationsNeeded(float yaw, float pitch) {
			this.yaw = yaw;
			this.pitch = pitch;
		}
		
		public float yaw, pitch;
	}
	
	/**
	 * Get the closest entity from the given collection of entities. If an entity in the list is dead or can't be seen, it will be ignored. If the ignoreFriend
	 * argument is true, all friends will be ignored. If thePlayer is in the list, it will also be ignored.
	 * 
	 * @param ignoreFriends True if friends should be ignored.
	 * @param entities The collection of entities to search.
	 * @return The closest entity, or null if none of the entities in the collection matched the required specifications.
	 */
	public static Entity getClosestEntity(boolean ignoreFriends, Collection<Entity> entities) {
		Entity closestEntity = null;
		EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
		for (Entity e : entities) {
			if (e.isDead) continue;
			else if (e instanceof EntityPlayer && (e.getUniqueID().equals(p.getUniqueID()) || Attacking.isFriend(((EntityPlayer) e).getName()))) continue;
			else if (!p.canEntityBeSeen(e)) continue;
			else if (closestEntity == null || p.getDistanceSqToEntity(closestEntity) > p.getDistanceSqToEntity(e)) closestEntity = e;
		}
		return closestEntity;
	}
	
	/**
	 * Get all entities of the given type within the given range from the player.
	 */
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
