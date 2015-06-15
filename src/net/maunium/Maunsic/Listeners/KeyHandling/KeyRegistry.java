package net.maunium.Maunsic.Listeners.KeyHandling;

import java.util.HashSet;
import java.util.Set;

import net.maunium.Maunsic.Util.MaunsiConfig;

/**
 * Registering for MauKeybinds.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class KeyRegistry {
	private static Set<MauKeybind> keybinds = new HashSet<MauKeybind>();
	
	/**
	 * Register the given key binding.
	 */
	public static void registerKeybind(MauKeybind kb) {
		keybinds.add(kb);
	}
	
	/**
	 * Unregister the given key binding.
	 */
	public static void unregisterKeybind(MauKeybind kb) {
		keybinds.remove(kb);
	}
	
	/**
	 * Get all the registered key bindings.
	 */
	public static Set<MauKeybind> getKeybinds() {
		return keybinds;
	}
	
	/**
	 * Save key codes of all the key bindings registered to the given MaunsiConfig.
	 */
	public static void save(MaunsiConfig conf) {
		for (MauKeybind kb : keybinds)
			kb.save(conf);
	}
	
	/**
	 * Load key codes of all the key bindings registered from the given MaunsiConfig.
	 */
	public static void load(MaunsiConfig conf) {
		for (MauKeybind kb : keybinds)
			kb.load(conf);
	}
}
