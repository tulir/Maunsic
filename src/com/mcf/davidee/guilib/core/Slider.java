package com.mcf.davidee.guilib.core;

import net.minecraft.util.MathHelper;

/**
 * 
 * Abstract representation of a minecraft slider.
 *
 */
public abstract class Slider extends Widget {
	
	public interface SliderFormat {
		String format(Slider slider);
	}
	
	protected SliderFormat format;
	protected float value;
	protected boolean dragging;
	
	public Slider(int width, int height, float value, SliderFormat format) {
		super(width, height);
		
		this.value = MathHelper.clamp_float(value, 0, 1);
		this.format = format;
	}
	
	@Override
	public boolean click(int mx, int my, int code) {
		if (inBounds(mx, my) && code == 0) {
			value = (float) (mx - (x + 4)) / (float) (width - 8);
			value = MathHelper.clamp_float(value, 0, 1);
			dragging = true;
			return true;
		}
		return false;
	}
	
	@Override
	public void handleClick(int mx, int my, int code) {
		value = (float) (mx - (x + 4)) / (float) (width - 8);
		value = MathHelper.clamp_float(value, 0, 1);
		dragging = true;
	}
	
	@Override
	public void mouseReleased(int mx, int my) {
		dragging = false;
	}
	
	public float getValue() {
		return value;
	}
	
}
