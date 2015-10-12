package net.maunium.Maunsic.Listeners.KeyHandling;

import java.util.LinkedHashSet;
import java.util.Set;

import net.maunium.Maunsic.Util.MaunsiConfig;

/**
 * Registering for MauKeybinds.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class KeyRegistry {
	private static Set<MauKeybind> keybinds = new LinkedHashSet<MauKeybind>(24);
	
	/**
	 * Register the given key binding.
	 */
	public static void registerKeybind(MauKeybind kb) {
		if (kb != null) keybinds.add(kb);
	}
	
	/**
	 * Unregister the given key binding.
	 */
	public static void unregisterKeybind(MauKeybind kb) {
		if (kb != null) keybinds.remove(kb);
	}
	
	/**
	 * Get the keybind with the given name.
	 */
	public static MauKeybind findKeybind(String name) {
		for (MauKeybind kb : keybinds)
			if (kb.getName().equalsIgnoreCase(name)) return kb;
		return null;
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
