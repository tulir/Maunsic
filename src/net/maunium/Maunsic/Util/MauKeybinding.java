package net.maunium.Maunsic.Util;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.util.IntHashMap;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MauKeybinding implements Comparable<MauKeybinding> {
	private static final List<MauKeybinding> keybindArray = Lists.newArrayList();
	private static final IntHashMap hash = new IntHashMap();
	private final String keyDescription;
	private final int keyCodeDefault;
	private int keyCode;
	/** Is the key held down? */
	private boolean pressed;
	private int pressTime;
	
	public static void onTick(int keyCode) {
		if (keyCode != 0) {
			MauKeybinding keybinding = (MauKeybinding) hash.lookup(keyCode);
			if (keybinding != null) ++keybinding.pressTime;
		}
	}
	
	public static void setKeyBindState(int keyCode, boolean pressed) {
		if (keyCode != 0) {
			MauKeybinding keybinding = (MauKeybinding) hash.lookup(keyCode);
			if (keybinding != null) keybinding.pressed = pressed;
		}
	}
	
	public static void unPressAllKeys() {
		Iterator<MauKeybinding> iterator = keybindArray.iterator();
		
		while (iterator.hasNext()) {
			MauKeybinding keybinding = iterator.next();
			keybinding.unpressKey();
		}
	}
	
	public static void resetKeyBindingArrayAndHash() {
		hash.clearMap();
		Iterator<MauKeybinding> iterator = keybindArray.iterator();
		
		while (iterator.hasNext()) {
			MauKeybinding keybinding = iterator.next();
			hash.addKey(keybinding.keyCode, keybinding);
		}
	}
	
	public MauKeybinding(String description, int keyCode) {
		keyDescription = description;
		this.keyCode = keyCode;
		keyCodeDefault = keyCode;
		keybindArray.add(this);
		hash.addKey(keyCode, this);
	}
	
	/**
	 * Returns true if the key is pressed (used for continuous querying). Should be used in tickers.
	 */
	public boolean isKeyDown() {
		return pressed;
	}
	
	/**
	 * Returns true on the initial key press. For continuous querying use {@link isKeyDown()}. Should be used in key events.
	 */
	public boolean isPressed() {
		if (pressTime == 0) {
			return false;
		} else {
			--pressTime;
			return true;
		}
	}
	
	private void unpressKey() {
		pressTime = 0;
		pressed = false;
	}
	
	public String getKeyDescription() {
		return keyDescription;
	}
	
	public int getKeyCodeDefault() {
		return keyCodeDefault;
	}
	
	public int getKeyCode() {
		return keyCode;
	}
	
	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}
	
	@Override
	public int compareTo(MauKeybinding p_compareTo_1_) {
		return I18n.translate(keyDescription, new Object[0]).compareTo(I18n.translate(p_compareTo_1_.keyDescription, new Object[0]));
	}
}