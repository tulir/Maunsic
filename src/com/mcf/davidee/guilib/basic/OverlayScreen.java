package com.mcf.davidee.guilib.basic;

public abstract class OverlayScreen extends BasicScreen {

	protected BasicScreen bg;

	public OverlayScreen(BasicScreen bg) {
		super(bg);

		this.bg = bg;
	}

	@Override
	public void drawBackground() {
		bg.drawScreen(-1, -1, 0);
	}

	@Override
	protected void onRevalidate() {
		bg.width = width;
		bg.height = height;
		bg.onRevalidate();
	}

	@Override
	protected void onReopen() { }

}
