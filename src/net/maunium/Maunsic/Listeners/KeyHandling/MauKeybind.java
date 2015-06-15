package net.maunium.Maunsic.Listeners.KeyHandling;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.maunium.Maunsic.Util.I18n;

public class MauKeybind implements Comparable<MauKeybind> {
	private int keyCode;
	private final int defaultKeyCode;
	private final String name;
	
	public MauKeybind(String name, int keyCode) {
		this.keyCode = keyCode;
		defaultKeyCode = keyCode;
		this.name = name;
	}
	
	public int getKeyCode() {
		return keyCode;
	}
	
	public int getDefaultKeyCode() {
		return defaultKeyCode;
	}
	
	public String getName() {
		return name;
	}
	
	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}
	
	public boolean isDown() {
		return keyCode < 0 ? Mouse.isButtonDown(keyCode + 100) : Keyboard.isKeyDown(keyCode);
	}
	
	public String getKeyName() {
		return getKeyName(getKeyCode());
	}
	
	public static String getKeyName(int keyCode) {
		return keyCode < 0 ? Mouse.getButtonName(keyCode + 100) : Keyboard.getKeyName(keyCode);
	}
	
	@Override
	public int compareTo(MauKeybind o) {
		return I18n.translate(getName()).compareTo(I18n.translate(o.getName()));
	}
}
