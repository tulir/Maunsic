package net.maunium.Maunsic.Listeners.KeyHandling;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.maunium.Maunsic.Util.I18n;
import net.maunium.Maunsic.Util.MaunsiConfig;

/**
 * A simplified key bind for Maunsic
 * 
 * @author Tulir293
 * @since 0.1
 */
public class MauKeybind implements Comparable<MauKeybind> {
	private int keyCode;
	private final int defaultKeyCode;
	private final String name;
	
	public MauKeybind(String name, int keyCode) {
		this.keyCode = keyCode;
		defaultKeyCode = keyCode;
		this.name = name;
	}
	
	/**
	 * Get the key code of this key binding. Key codes above 256 are characters and to get the actual character, use {@code (char) (getKeyCode() - 256)}. Key
	 * codes below -1 are mouse keys and to get the actual mouse key, use {@code getKeyCode() + 100}.
	 */
	public int getKeyCode() {
		return keyCode;
	}
	
	/**
	 * Get the default key code of this key binding.
	 */
	public int getDefaultKeyCode() {
		return defaultKeyCode;
	}
	
	/**
	 * Get the name of this key binding.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the key code for this key binding.
	 */
	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}
	
	/**
	 * Check if this key binding is currently pressed. Please note that this does not properly check the key bind status if it has been bound to a key that
	 * LWJGL does not directly recognize (raw key code over 256).
	 */
	public boolean isDown() {
		return isDown(keyCode);
	}
	
	public static boolean isDown(int keyCode) {
		return keyCode < 0 ? Mouse.isButtonDown(keyCode + 100) : keyCode > 256 ? Keyboard.getEventKeyState() && Keyboard.getEventCharacter() + 256 == keyCode
				: Keyboard.isKeyDown(keyCode);
	}
	
	/**
	 * Get the key name of the key code of this key binding.
	 * 
	 * @see {@link #getKeyName(int)}
	 */
	public String getKeyName() {
		return getKeyName(getKeyCode());
	}
	
	/**
	 * Get the key name of the given key code. If the key code is under -1, this will return {@code Mouse.getButtonName(keyCode + 100)}. If it the key code is
	 * above 256, the return value will be {@code Character.toString((char) (keyCode - 256))}. In any other case (above -1, below 256) this will return
	 * {@code Keyboard.getKeyName(keyCode)}.
	 */
	public static String getKeyName(int keyCode) {
		return keyCode < -1 ? Mouse.getButtonName(keyCode + 100) : keyCode > 256 ? Character.toString((char) (keyCode - 256)) : Keyboard.getKeyName(keyCode);
	}
	
	@Override
	public int compareTo(MauKeybind o) {
		return I18n.translate(getName()).compareTo(I18n.translate(o.getName()));
	}
	
	/**
	 * Save the key code to the given MaunsiConfig.
	 */
	public void save(MaunsiConfig conf) {
		conf.set("key." + getName(), getKeyCode());
	}
	
	/**
	 * Load the key code from the given MaunsiConfig.
	 */
	public void load(MaunsiConfig conf) {
		setKeyCode(conf.getInt("key." + getName(), getKeyCode()));
	}
}
