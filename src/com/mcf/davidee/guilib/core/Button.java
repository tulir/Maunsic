package com.mcf.davidee.guilib.core;

/**
 * 
 * Abstract representation of a minecraft button. The buttons calls handler.buttonClicked(this) when it is pressed.
 *
 */
public abstract class Button extends Widget {
	
	public interface ButtonHandler {
		void buttonClicked(Button button, int code);
	}
	
	protected ButtonHandler handler;
	
	public Button(int width, int height, ButtonHandler handler) {
		super(width, height);
		
		this.handler = handler;
	}
	
	@Override
	public boolean click(int mx, int my, int code) {
		return enabled && inBounds(mx, my);
	}
	
	@Override
	public void handleClick(int mx, int my, int code) {
		if (handler != null) handler.buttonClicked(this, code);
	}
	
	public void setEnabled(boolean flag) {
		enabled = flag;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public String getText() {
		return "";
	}
	
	public void setText(String str) {}
}
