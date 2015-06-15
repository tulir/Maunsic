package net.maunium.Maunsic.Listeners.KeyHandling;

import java.util.HashSet;
import java.util.Set;

import net.maunium.Maunsic.Util.MaunsiConfig;

public class KeyRegistry {
	private static Set<MauKeybind> keybinds = new HashSet<MauKeybind>();
	
	public static void registerKeybind(MauKeybind kb) {
		keybinds.add(kb);
	}
	
	public static void unregisterKeybind(MauKeybind kb) {
		keybinds.remove(kb);
	}
	
	public static Set<MauKeybind> getKeybinds() {
		return keybinds;
	}
	
	public static void save(MaunsiConfig conf) {
		for (MauKeybind kb : keybinds)
			kb.save(conf);
	}
	
	public static void load(MaunsiConfig conf) {
		for (MauKeybind kb : keybinds)
			kb.load(conf);
	}
}
