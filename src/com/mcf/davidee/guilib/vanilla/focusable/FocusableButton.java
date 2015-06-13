package com.mcf.davidee.guilib.vanilla.focusable;

import org.lwjgl.opengl.GL11;

import com.mcf.davidee.guilib.core.Button.ButtonHandler;
import com.mcf.davidee.guilib.core.FocusableWidget;
import com.mcf.davidee.guilib.core.Scrollbar.Shiftable;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

public class FocusableButton extends FocusableWidget implements Shiftable {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/widgets.png");
	protected ButtonHandler handler;
	protected String str;
	private boolean focused;
	
	public FocusableButton(int width, int height, String text, ButtonHandler handler) {
		super(width, height);
		
		this.handler = handler;
		
		str = text;
	}
	
	public FocusableButton(String text, ButtonHandler handler) {
		this(200, 20, text, handler);
	}
	
	@Override
	public void draw(int mx, int my) {
		mc.renderEngine.bindTexture(TEXTURE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		boolean hover = inBounds(mx, my);
		int u = 0, v = 46 + getStateOffset(hover);
		
		if (width == 200 && height == 20) // Full size button
		drawTexturedModalRect(x, y, u, v, width, height);
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
	
	public String getText() {
		return str;
	}
	
	public void setText(String str) {
		this.str = str;
	}
	
	@Override
	public void handleClick(int mx, int my) {
		mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
		super.handleClick(mx, my);
	}
	
	@Override
	public boolean click(int mx, int my) {
		return enabled && inBounds(mx, my);
	}
	
	public void setEnabled(boolean flag) {
		enabled = flag;
	}
	
	public boolean isFocused() {
		return focused;
	}
	
	@Override
	public void focusGained() {
		focused = true;
	}
	
	@Override
	public void focusLost() {
		focused = false;
	}
	
	@Override
	public void shiftY(int dy) {
		y += dy;
	}
}
