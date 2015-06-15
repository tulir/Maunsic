package com.mcf.davidee.guilib.basic;

import com.mcf.davidee.guilib.core.Widget;

import net.minecraft.client.Minecraft;

public class Tooltip extends Widget {
	
	protected int color, txtColor;
	private String str;
	
	public Tooltip(String text) {
		super(Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) + 4, 12);
		
		zLevel = 1.0f;
		str = text;
		color = 0xff000000;
		txtColor = 0xffffff;
	}
	
	public Tooltip(String text, int color, int txtColor) {
		super(Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) + 4, 12);
		
		zLevel = 1.0f;
		str = text;
		this.color = color;
		this.txtColor = txtColor;
	}
	
	public void setBackgroundColor(int color) {
		this.color = color;
	}
	
	public void setTextColor(int color) {
		txtColor = color;
	}
	
	@Override
	public void draw(int mx, int my) {
		drawRect(x, y, x + width, y + height, color);
		drawCenteredString(mc.fontRendererObj, str, x + width / 2, y + (height - 8) / 2, txtColor);
	}
	
	@Override
	public boolean click(int mx, int my, int code) {
		return false;
	}
	
}
