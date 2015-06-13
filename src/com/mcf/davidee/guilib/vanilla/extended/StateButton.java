package com.mcf.davidee.guilib.vanilla.extended;

import com.mcf.davidee.guilib.core.Scrollbar.Shiftable;
import com.mcf.davidee.guilib.vanilla.ButtonVanilla;

public class StateButton extends ButtonVanilla implements Shiftable {
	private int state = 0;
	private Format format;
	
	public StateButton(int width, int height, ButtonHandler handler, int state, Format format) {
		super(width, height, "", handler);
		this.state = state;
		this.format = format;
	}
	
	public StateButton(int width, int height, ButtonHandler handler, Format format) {
		this(width, height, handler, 0, format);
	}
	
	public StateButton(int width, int height, int state, Format format) {
		this(width, height, null, state, format);
	}
	
	public StateButton(ButtonHandler handler, int state, Format format) {
		this(150, 20, handler, state, format);
	}
	
	public StateButton(ButtonHandler handler, Format format) {
		this(150, 20, handler, 0, format);
	}
	
	public StateButton(int state, Format format) {
		this(150, 20, null, state, format);
	}
	
	public StateButton(Format format) {
		this(150, 20, null, 0, format);
	}
	
	public StateButton(int width, int height, String text, ButtonHandler handler, int state) {
		this(width, height, handler, state, new DefaultFormat(text));
	}
	
	public StateButton(int width, int height, String text, ButtonHandler handler) {
		this(width, height, handler, 0, new DefaultFormat(text));
	}
	
	public StateButton(int width, int height, String text, int state) {
		this(width, height, null, state, new DefaultFormat(text));
	}
	
	public StateButton(String text, ButtonHandler handler, int state) {
		this(150, 20, handler, state, new DefaultFormat(text));
	}
	
	public StateButton(String text, ButtonHandler handler) {
		this(150, 20, handler, 0, new DefaultFormat(text));
	}
	
	public StateButton(String text, int state) {
		this(150, 20, null, state, new DefaultFormat(text));
	}
	
	public StateButton(String text) {
		this(150, 20, null, 0, new DefaultFormat(text));
	}
	
	public void setFormat(Format format) {
		if (format != null) this.format = format;
		else format = new DefaultFormat("SButton");
	}
	
	public int getState() {
		return state;
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	@Override
	public String getText() {
		return format.getText();
	}
	
	/**
	 * Does nothing in StateButtons. use {@link #setFormat(StateButton.Format)}
	 */
	@Deprecated
	@Override
	public void setText(String text) {}
	
	@Override
	public void draw(int mx, int my) {
		str = format.format(state);
		super.draw(mx, my);
	}
	
	public Format getFormat() {
		return format;
	}
	
	@Override
	public void handleClick(int mx, int my) {
		state++;
		if (state >= format.stateCount()) state = 0;
		super.handleClick(mx, my);
	}
	
	public static interface Format {
		public String format(int state);
		
		public int stateCount();
		
		public String getText();
	}
	
	public static class DefaultFormat extends GenericFormat {
		private static String[] states = { "Off", "On" };
		
		public static String[] getDefaultStates() {
			return states;
		}
		
		public static void setDefaultStates(String off, String on) {
			states[0] = off;
			states[1] = on;
		}
		
		public DefaultFormat(String text) {
			super(text, states);
		}
	}
	
	public static class GenericFormat implements Format {
		private String[] states;
		private String text;
		
		public GenericFormat(String text, String... states) {
			this.states = states;
			this.text = text;
		}
		
		@Override
		public int stateCount() {
			return states.length;
		}
		
		@Override
		public String format(int state) {
			if (states.length > state) return text + ": " + states[state];
			return null;
		}
		
		@Override
		public String getText() {
			return text;
		}
		
		public String[] getStates() {
			return states;
		}
		
		public void setStates(String... states) {
			if (states != null && states.length > 1) this.states = states;
			else states = DefaultFormat.states;
		}
		
		public void setText(String text) {
			this.text = text;
		}
	}
	
	@Override
	public void shiftY(int dy) {
		y += dy;
	}
}
