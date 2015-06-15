package com.mcf.davidee.guilib.vanilla.extended;

import com.mcf.davidee.guilib.core.Scrollbar.Shiftable;
import com.mcf.davidee.guilib.vanilla.TextFieldVanilla;

public class ShiftableTextField extends TextFieldVanilla implements Shiftable{
	public ShiftableTextField(int width, int height, CharacterFilter filter) {
		super(width, height, filter);
	}

	public ShiftableTextField(CharacterFilter filter) {
		super(200, 20, filter);
	}
	
	public ShiftableTextField(int width, int height, int innerColor, int outerColor, CharacterFilter filter) {
		super(width, height, innerColor, outerColor, filter);
	}

	@Override
	public void shiftY(int dy) {
		this.y += dy;
	}
}
