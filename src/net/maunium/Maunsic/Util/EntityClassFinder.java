package net.maunium.Maunsic.Util;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Used to find entity classes by name
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class EntityClassFinder {
	/**
	 * Find an entity class by name
	 * 
	 * @return The entity class, or null if not found.
	 */
	@SuppressWarnings("unchecked")
	public static Class<? extends EntityLivingBase> getEntityClass(String s) {
		if (equals(s, "all", "living", "livings", "livingbase", "livingbases")) return EntityLivingBase.class;
		else if (equals(s, "player", "others")) return EntityPlayer.class;
		else if (equals(s, "mob", "mobs", "monsters", "monster")) return EntityMob.class;
		else if (equals(s, "creature", "creatures")) return EntityCreature.class;
		else if (equals(s, "ambientcreature", "ambientcreatures", "ambient", "ambients")) return EntityAmbientCreature.class;
		else if (equals(s, "animal", "animals")) return EntityAnimal.class;
		else if (equals(s, "tameable", "tameables")) return EntityTameable.class;
		else if (equals(s, "water", "watermob", "waters", "watermobs")) return EntityWaterMob.class;
		else if (equals(s, "villager", "villagers")) return EntityVillager.class;
		else {
			for (Class<?> c : passives) {
				try {
					if (c.getSimpleName().substring(6).equalsIgnoreCase(s)) return (Class<? extends EntityLivingBase>) c;
				} catch (Throwable t) {
					continue;
				}
			}
			for (Class<?> c : monsters) {
				try {
					if (c.getSimpleName().substring(6).equalsIgnoreCase(s)) return (Class<? extends EntityLivingBase>) c;
				} catch (Throwable t) {
					continue;
				}
			}
			for (Class<?> c : bosses) {
				try {
					if (c.getSimpleName().substring(6).equalsIgnoreCase(s)) return (Class<? extends EntityLivingBase>) c;
				} catch (Throwable t) {
					continue;
				}
			}
		}
		try {
			return (Class<? extends EntityLivingBase>) Class.forName(s);
		} catch (Exception e) {
			return null;
		}
	}
	
	private static boolean equals(String s, String... ct) {
		for (String c : ct)
			if (s.equalsIgnoreCase(c)) return true;
		return false;
	}
	
	private static Class<?>[] passives = { EntityWolf.class, EntitySquid.class, EntitySheep.class, EntityPig.class, EntityOcelot.class, EntityMooshroom.class,
			EntityHorse.class, EntityCow.class, EntityChicken.class, EntityBat.class };
	private static Class<?>[] monsters = { EntitySpider.class, EntityZombie.class, EntityWitch.class, EntitySnowman.class, EntitySlime.class,
			EntitySkeleton.class, EntitySilverfish.class, EntityPigZombie.class, EntityMagmaCube.class, EntityIronGolem.class, EntityGolem.class,
			EntityGiantZombie.class, EntityGhast.class, EntityEnderman.class, EntityCreeper.class, EntityCaveSpider.class, EntityBlaze.class };
	private static Class<?>[] bosses = { EntityDragon.class, EntityDragonPart.class, EntityWither.class };
	
}
