package net.maunium.Maunsic.Events;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class RawInputEvent extends Event {
	public void d(int keycode, boolean pressed) {
		RawInputEvent rie = new RawInputEvent(keycode, pressed);
		if (MinecraftForge.EVENT_BUS.post(rie)) return;
		keycode = rie.getCode();
		pressed = rie.isPressed();
	}
	
	private int code;
	private boolean pressed;
	
	public RawInputEvent(int code, boolean pressed) {
		this.code = code;
		this.pressed = pressed;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
	public boolean isPressed() {
		return pressed;
	}
	
	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}
}
