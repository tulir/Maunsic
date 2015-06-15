package com.mcf.davidee.guilib.vanilla.extended;

import com.mcf.davidee.guilib.basic.Label;
import com.mcf.davidee.guilib.core.Scrollbar.Shiftable;
import com.mcf.davidee.guilib.core.Widget;

public class ShiftableLabel extends Label implements Shiftable {
	public ShiftableLabel(String text, int color, int hoverColor, Widget... tooltips) {
		super(text, color, hoverColor, true, tooltips);
	}
	
	public ShiftableLabel(String text, Widget... tooltips) {
		super(text, 0xffffff, 0xffffff, true, tooltips);
	}
	
	public ShiftableLabel(String text, int color, int hoverColor, boolean center, Widget... tooltips) {
		super(text, color, hoverColor, center, tooltips);
	}
	
	public ShiftableLabel(String text, boolean center, Widget... tooltips) {
		super(text, 0xffffff, 0xffffff, center, tooltips);
	}
	
	@Override
	public void shiftY(int dy) {
		y += dy;
	}
	
}
