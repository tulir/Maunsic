package com.mcf.davidee.guilib.vanilla.extended;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.mcf.davidee.guilib.core.Button.ButtonHandler;
import com.mcf.davidee.guilib.vanilla.focusable.FocusableButton;

import net.minecraft.util.ResourceLocation;

public class MultiKeySelectButton extends FocusableButton {
	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/widgets.png");
	
	private int[] kcs;
	
	public MultiKeySelectButton(ButtonHandler handler, int[] keycodes) {
		this(150, 20, keycodes, handler);
	}
	
	public MultiKeySelectButton(int width, int height, int[] keycodes, ButtonHandler handler) {
		super(width, height, "", handler);
		kcs = keycodes;
		updateText();
	}
	
	public MultiKeySelectButton(int width, int height, ButtonHandler handler) {
		this(width, height, new int[0], handler);
	}
	
	public MultiKeySelectButton(ButtonHandler handler) {
		this(200, 20, new int[0], handler);
	}
	
	public void updateText() {
		if (kcs.length == 0) {
			setText("Not set");
			return;
		}
		StringBuffer sb = new StringBuffer();
		for (int i : kcs)
			sb.append(Keyboard.getKeyName(i) + " + ");
		sb.delete(sb.length() - 3, sb.length());
		setText(sb.toString());
	}
	
	@Override
	public void draw(int mx, int my) {
		mc.renderEngine.bindTexture(TEXTURE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		boolean hover = inBounds(mx, my);
		
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
		if (isFocused()) {
			return toInt(100, 200, 100, 255);
		} else return enabled ? hover ? toInt(255, 255, 160, 255) : toInt(224, 224, 224, 255) : toInt(95, 95, 95, 255);
		
	}
	
	private int toInt(int red, int green, int blue, int alpha) {
		return (alpha & 0xFF) << 24 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | (blue & 0xFF) << 0;
	}
	
	public int[] getKeycodes() {
		return kcs;
	}
	
	public void setKeycodes(int... kcs) {
		this.kcs = kcs;
	}
	
	public void addKeycode(int kc) {
		int[] newKCS = new int[kcs.length + 1];
		for (int i = 0; i < kcs.length; i++) {
			if (kc == kcs[i]) return;
			newKCS[i] = kcs[i];
		}
		newKCS[newKCS.length - 1] = kc;
		kcs = newKCS;
	}
	
	@Override
	public boolean keyTyped(char k, int kc) {
		if (isFocused()) {
			if (kc == Keyboard.KEY_RETURN) focusLost();
			else addKeycode(kc);
			updateText();
			return true;
		} else return false;
	}
	
	@Override
	public void focusGained() {
		super.focusGained();
		kcs = new int[0];
		str = "Enter to stop";
	}
}
