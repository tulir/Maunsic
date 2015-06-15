package com.mcf.davidee.guilib.vanilla.extended;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.mcf.davidee.guilib.core.Button.ButtonHandler;
import com.mcf.davidee.guilib.vanilla.focusable.FocusableButton;

import net.minecraft.util.ResourceLocation;

public class KeySelectButton extends FocusableButton {
	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/widgets.png");
	
	private int kc;
	private KSBFormat format;
	
	public KeySelectButton(ButtonHandler handler, int keycode) {
		this(150, 20, handler, keycode, new DefaultKSBFormat());
	}
	
	public KeySelectButton(int width, int height, ButtonHandler handler, int keycode) {
		this(width, height, handler, keycode, new DefaultKSBFormat());
	}
	
	public KeySelectButton(int width, int height, ButtonHandler handler) {
		this(width, height, handler, -1, new DefaultKSBFormat());
	}
	
	public KeySelectButton(ButtonHandler handler) {
		this(150, 20, handler, -1, new DefaultKSBFormat());
	}
	
	public KeySelectButton(ButtonHandler handler, int keycode, KSBFormat format) {
		this(150, 20, handler, keycode, format);
	}
	
	public KeySelectButton(int width, int height, ButtonHandler handler, int keycode, KSBFormat format) {
		super(width, height, format.format(keycode, false), handler);
		kc = keycode;
		this.format = format;
	}
	
	public KeySelectButton(int width, int height, ButtonHandler handler, KSBFormat format) {
		this(width, height, handler, -1, format);
	}
	
	public KeySelectButton(ButtonHandler handler, KSBFormat format) {
		this(150, 20, handler, -1, format);
	}
	
	@Override
	public void draw(int mx, int my) {
		mc.renderEngine.bindTexture(KeySelectButton.TEXTURE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		boolean hover;
		str = format.format(kc, isFocused());
		hover = isFocused() ? true : inBounds(mx, my);
		int u = 0, v = 46 + getStateOffset(hover);
		
		if (width == 200 && height == 20) drawTexturedModalRect(x, y, u, v, width, height);
		else {
			drawTexturedModalRect(x, y, u, v, width / 2, height / 2);
			drawTexturedModalRect(x + width / 2, y, u + 200 - width / 2, v, width / 2, height / 2);
			drawTexturedModalRect(x, y + height / 2, u, v + 20 - height / 2, width / 2, height / 2);
			drawTexturedModalRect(x + width / 2, y + height / 2, u + 200 - width / 2, v + 20 - height / 2, width / 2, height / 2);
		}
		drawCenteredString(mc.fontRendererObj, str, x + width / 2, y + (height - 8) / 2, getTextColor(hover));
	}
	
	private int getStateOffset(boolean hover) {
		return enabled ? hover ? 40 : 20 : 0;
	}
	
	private int getTextColor(boolean hover) {
		return enabled ? hover ? 16777120 : 14737632 : 6250336;
	}
	
	public int getKeycode() {
		return kc;
	}
	
	public void setKeycode(int kc) {
		this.kc = kc;
	}
	
	@Override
	public boolean keyTyped(char k, int kc) {
		if (isFocused()) {
			setKeycode(kc);
			focusLost();
			return true;
		} else return false;
	}
	
	@Override
	public void handleClick(int mx, int my, int code) {
		if (code == 0) super.handleClick(mx, my, code);
		else if (isFocused() && code > 1) {
			setKeycode(code - 100);
			focusLost();
		}
	}
	
	@Override
	public boolean click(int mx, int my, int code) {
		return enabled && inBounds(mx, my);
	}
	
	public static interface KSBFormat {
		public String format(int keyCode, boolean pressed);
	}
	
	public static class DefaultKSBFormat implements KSBFormat {
		@Override
		public String format(int keyCode, boolean pressed) {
			if (keyCode < 1) return "Not set";
			else if (pressed) return "> " + Keyboard.getKeyName(keyCode) + " <";
			else return Keyboard.getKeyName(keyCode);
		}
	}
}
