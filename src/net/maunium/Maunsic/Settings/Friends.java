package net.maunium.Maunsic.Settings;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import net.minecraft.entity.EntityLivingBase;

public class Friends {
	private static List<Class<? extends EntityLivingBase>> targets;
	private static Set<String> friends;
	
	public static boolean isFriend(String name) {
		return friends.contains(name.toLowerCase(Locale.ENGLISH));
	}
	
	public static List<Class<? extends EntityLivingBase>> getTargets() {
		return targets;
	}
}
