package net.maunium.Maunsic.Listeners.KeyHandling;

import java.util.HashSet;
import java.util.Set;

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
}
