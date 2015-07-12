package net.maunium.Maunsic.Events;

import net.minecraft.client.settings.KeyBinding;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Event that is fired when the {@link KeyBinding#setKeyBindState(int, boolean)} is called.
 * 
 * @author Tulir293
 * @since 0.1
 */
@Cancelable
public class RawInputEvent extends Event {
	private int code;
	private boolean pressed;
	
	/**
	 * Create a new raw input event with the given keycode and pressed status.
	 */
	public RawInputEvent(int code, boolean pressed) {
		this.code = code;
		this.pressed = pressed;
	}
	
	/**
	 * Change the key code in this event. Will change the keycode that the {@link KeyBinding#setKeyBindState(int, boolean)} method uses!
	 */
	public void setCode(int code) {
		this.code = code;
	}
	
	/**
	 * Get the key code in this event.
	 */
	public int getCode() {
		return code;
	}
	
	/**
	 * Check if this is a keyup or a keydown event.
	 */
	public boolean isPressed() {
		return pressed;
	}
	
	/**
	 * Change whether this event is a keyup or a keydown event. Will change the status that the {@link KeyBinding#setKeyBindState(int, boolean)} method uses!
	 */
	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}
}
