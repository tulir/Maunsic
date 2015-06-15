package com.mcf.davidee.guilib.vanilla.extended;

import org.lwjgl.opengl.GL11;

import com.mcf.davidee.guilib.core.Scrollbar.Shiftable;
import com.mcf.davidee.guilib.core.Widget;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class ExtendedIntSlider extends Widget implements Shiftable {
	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/widgets.png");
	
	protected final int minValue, maxValue;
	protected float value;
	protected boolean dragging, hover;
	protected Format format;
	
	public ExtendedIntSlider(int width, int height, Format format, int value, int minValue, int maxValue) {
		super(width, height);
		
		this.value = MathHelper.clamp_float(getFloatValue(value, minValue, maxValue), 0, 1);
		
		this.format = format;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	public ExtendedIntSlider(Format format, int value, int minValue, int maxValue) {
		this(150, 20, format, value, minValue, maxValue);
	}
	
	public ExtendedIntSlider(int width, int height, String name, int value, int minValue, int maxValue) {
		this(width, height, new GenericFormat(name), value, minValue, maxValue);
	}
	
	public ExtendedIntSlider(String name, int value, int minValue, int maxValue) {
		this(150, 20, new GenericFormat(name), value, minValue, maxValue);
	}
	
	@Override
	public void handleClick(int mx, int my, int code) {
		value = (float) (mx - (x + 4)) / (float) (width - 8);
		value = MathHelper.clamp_float(value, 0, 1);
		dragging = true;
		mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
	}
	
	@Override
	public void draw(int mx, int my) {
		hover = inBounds(mx, my);
		
		if (dragging) {
			value = (float) (mx - (x + 4)) / (float) (width - 8);
			value = MathHelper.clamp_float(value, 0, 1);
		}
		
		mc.renderEngine.bindTexture(TEXTURE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(x, y, 0, 46, width / 2, height);
		drawTexturedModalRect(x + width / 2, y, 200 - width / 2, 46, width / 2, height);
		drawTexturedModalRect(x + (int) (value * (width - 8)), y, 0, 66, 4, 20);
		drawTexturedModalRect(x + (int) (value * (width - 8)) + 4, y, 196, 66, 4, 20);
		drawCenteredString(mc.fontRendererObj, format.format(getIntValue()), x + width / 2, y + (height - 8) / 2, inBounds(mx, my) ? 16777120 : 0xffffff);
	}
	
	@Override
	public boolean mouseWheel(int delta) {
		if (hover && !dragging) {
			value = getFloatValue(getIntValue() + (int) Math.signum(delta), minValue, maxValue);
			return true;
		}
		return false;
	}
	
	public static float getFloatValue(int value, int min, int max) {
		value = MathHelper.clamp_int(value, min, max);
		return (float) (value - min) / (max - min);
	}
	
	public void setIntValue(int value) {
		this.value = MathHelper.clamp_float(getFloatValue(value, minValue, maxValue), 0, 1);
	}
	
	public int getIntValue() {
		return Math.round(value * (maxValue - minValue) + minValue);
	}
	
	public float getFloatValue() {
		return value;
	}
	
	public void setFloatValue(float value) {
		this.value = value;
	}
	
	@Override
	public boolean click(int mx, int my, int code) {
		if (code == 0 && inBounds(mx, my)) {
			value = (float) (mx - (x + 4)) / (float) (width - 8);
			value = MathHelper.clamp_float(value, 0, 1);
			dragging = true;
			return true;
		}
		return false;
	}
	
	@Override
	public void mouseReleased(int mx, int my) {
		dragging = false;
	}
	
	public static interface Format {
		public String format(int amount);
	}
	
	public static class GenericFormat implements Format {
		private String name;
		
		public GenericFormat(String name) {
			this.name = name;
		}
		
		@Override
		public String format(int amount) {
			return name + ": " + amount;
		}
	}
	
	@Override
	public void shiftY(int dy) {
		y += dy;
	}
}