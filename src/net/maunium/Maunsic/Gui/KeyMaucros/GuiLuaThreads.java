package net.maunium.Maunsic.Gui.KeyMaucros;

import org.lwjgl.input.Keyboard;

import com.mcf.davidee.guilib.basic.BasicScreen;
import com.mcf.davidee.guilib.basic.Label;
import com.mcf.davidee.guilib.core.Button;
import com.mcf.davidee.guilib.core.Container;
import com.mcf.davidee.guilib.core.Scrollbar.Shiftable;
import com.mcf.davidee.guilib.core.Widget;
import com.mcf.davidee.guilib.vanilla.ButtonVanilla;
import com.mcf.davidee.guilib.vanilla.ScrollbarVanilla;

import mauluam.MauThreadLib;

import net.maunium.Maunsic.Gui.GuiMaunsic;
import net.maunium.Maunsic.Util.I18n;

import net.minecraft.client.gui.Gui;

/**
 * Gui for managing lua threads
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class GuiLuaThreads extends BasicScreen {
	public GuiLuaThreads(GuiMaunsic parent) {
		super(parent);
	}
	
	private Container c, c2;
	private ScrollbarVanilla sb;
	private ButtonVanilla back, killAll;
	private Label title;
	
	@Override
	public void onInit() {
		title = new Label(I18n.translate("conf.lua.threads.title"));
		back = new ButtonVanilla(150, 20, I18n.translate("conf.back"), this);
		killAll = new ButtonVanilla(150, 20, I18n.translate("conf.lua.threads.killall"), this);
		
		sb = new ScrollbarVanilla(10);
		c2 = new Container(sb, 14, 12);
		
		c = new Container();
		c.addWidgets(title, back, killAll);
		
		containers.add(c);
		containers.add(c2);
	}
	
	@Override
	public void onRevalidate() {
		title.setPosition(width / 2, 15);
		int y = 16;
		c2.getWidgets().clear();
		for (String s : MauThreadLib.getThreads()) {
			y += 20;
			LuaThreadWidget ltw = new LuaThreadWidget(s);
			ltw.setPosition(width / 2 - ltw.getWidth() / 2, y);
			c2.addWidgets(ltw);
		}
		sb.setPosition(width - 90, 30);
		back.setPosition(width / 2 - 150 - 2, 200);
		killAll.setPosition(width / 2 + 2, 200);
		c.revalidate(0, 0, width, height);
		c2.revalidate(80, 30, width - 160, back.getY() - 35);
	}
	
	@Override
	public void onButtonClicked(Button b, int code) {
		if (code != 0) return;
		if (b.equals(back)) close();
		else if (b.equals(killAll)) {
			MauThreadLib.stopThreads();
			onRevalidate();
		}
	}
	
	@Override
	protected void mouseClicked(int mx, int my, int code) {
		if (code == 0) {
			c.mouseClicked(mx, my, code);
			c2.mouseClicked(mx, my, code);
		}
	}
	
	@Override
	public void onKeyTyped(char c, int kc) {
		if (kc == Keyboard.KEY_DELETE) {
			for (Widget w : c2.getWidgets()) {
				if (w instanceof LuaThreadWidget) {
					LuaThreadWidget ltw = (LuaThreadWidget) w;
					if (ltw.isSelected()) {
						MauThreadLib.stopThread(ltw.getName());
						onRevalidate();
						return;
					}
				}
			}
		}
	}
	
	public class LuaThreadWidget extends Widget implements Shiftable {
		protected final String name;
		protected boolean hover, selected;
		
		public String getName() {
			return name;
		}
		
		public LuaThreadWidget(int width, int height, String name) {
			super(width, height);
			this.name = name;
		}
		
		public LuaThreadWidget(String name) {
			super(150, 15);
			this.name = name;
		}
		
		@Override
		public void draw(int mx, int my) {
			hover = inBounds(mx, my);
			Gui.drawRect(x, y, x + width, y + height, toInt(68, 68, 68, 155));
			drawString(mc.fontRendererObj, name, x + 4, y + 4,
					selected ? toInt(150, 255, 150, 255) : hover ? toInt(255, 255, 185, 230) : toInt(255, 255, 255, 255));
		}
		
		private int toInt(int red, int green, int blue, int alpha) {
			return (alpha & 0xFF) << 24 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | (blue & 0xFF) << 0;
		}
		
		public boolean isSelected() {
			return selected;
		}
		
		public void setSelected(boolean selected) {
			this.selected = selected;
		}
		
		@Override
		public void shiftY(int dy) {
			y += dy;
		}
		
		@Override
		public boolean click(int mx, int my, int code) {
			if (code == 0 && inBounds(mx, my)) {
				if (selected) selected = false;
				else selected = true;
				return true;
			} else selected = false;
			return false;
		}
	}
}
