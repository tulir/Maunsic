package com.mcf.davidee.guilib.vanilla.extended;

import com.mcf.davidee.guilib.core.Scrollbar.Shiftable;
import com.mcf.davidee.guilib.vanilla.ButtonVanilla;

public class ShiftableButton extends ButtonVanilla implements Shiftable {
	
	public ShiftableButton(int width, int height, String text, ButtonHandler handler) {
		super(width, height, text, handler);
	}
	
	public ShiftableButton(String text, ButtonHandler handler) {
		super(text, handler);
	}
	
	@Override
	public void shiftY(int dy) {
		y += dy;
	}
}
