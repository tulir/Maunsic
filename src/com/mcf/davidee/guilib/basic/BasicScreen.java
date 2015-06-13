package com.mcf.davidee.guilib.basic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.mcf.davidee.guilib.core.Button;
import com.mcf.davidee.guilib.core.Button.ButtonHandler;
import com.mcf.davidee.guilib.core.Container;
import com.mcf.davidee.guilib.core.Widget;
import com.mcf.davidee.guilib.vanilla.focusable.FocusableButton;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Server.ServerHandler;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;

/**
 * 
 * The core GuiScreen - use this class for your GUIs.
 * 
 */
public abstract class BasicScreen extends GuiScreen implements ButtonHandler {
	private GuiScreen parent;
	private boolean hasInit, closed;
	protected List<Container> containers;
	protected Container selectedContainer;
	
	public BasicScreen(GuiScreen parent) {
		this.parent = parent;
		containers = new ArrayList<Container>();
	}
	
	/**
	 * Revalidate this GUI. Reset your widget locations/dimensions here.
	 */
	protected abstract void onRevalidate();
	
	/**
	 * Called ONCE to create this GUI. Create your containers and widgets here.
	 */
	protected abstract void onInit();
	
	/**
	 * Called when this GUI is reopened after being closed.
	 */
	protected void onReopen() {};
	
	protected void onButtonClicked(Button b) {}
	
	/**
	 * Called when this GUI is closed.
	 * 
	 * @return True if {@link #save()} should be called, false otherwise.
	 */
	protected boolean onClose() {
		return true;
	}
	
	protected void save() {}
	
	protected void onKeyTyped(char c, int kc) {
		boolean handled = selectedContainer != null ? selectedContainer.keyTyped(c, kc) : false;
		if (!handled) unhandledKeyTyped(c, kc);
	}
	
	public GuiScreen getParent() {
		return parent;
	}
	
	public List<Container> getContainers() {
		return containers;
	}
	
	public void close() {
		// START MAUNSIC-DEPENDANT
		if (!ServerHandler.canUse()) return;
		Maunsic.getLogger().debug("Closing GUI", this);
		// END MAUNSIC-DEPENDANT
		if (onClose()) save();
		mc.displayGuiScreen(parent);
	}
	
	/**
	 * Called when the selectedContainer did not capture this keyboard event.
	 * 
	 * @param c Character typed (if any)
	 * @param code Keyboard.KEY_ code for this key
	 */
	protected void unhandledKeyTyped(char c, int code) {}
	
	/**
	 * Called to draw this screen's background ScaledResolution
	 */
	protected void drawBackground() {
		drawDefaultBackground();
	}
	
	@Override
	public void drawScreen(int mx, int my, float f) {
		drawBackground();
		List<Widget> overlays = new ArrayList<Widget>();
		int scale = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight).getScaleFactor();
		for (Container c : containers)
			overlays.addAll(c.draw(mx, my, scale));
		for (Widget w : overlays)
			w.draw(mx, my);
	}
	
	@Override
	public void updateScreen() {
		for (Container c : containers)
			c.update();
	}
	
	@Override
	protected void mouseClicked(int mx, int my, int code) {
		if (code == 0) {
			for (Container c : containers) {
				if (c.mouseClicked(mx, my)) {
					selectedContainer = c;
					break;
				}
			}
			for (Container c : containers)
				if (c != selectedContainer) c.setFocused(null);
		}
	}
	
	@Override
	protected void mouseReleased(int mx, int my, int code) {
		if (code == 0) {
			for (Container c : containers)
				c.mouseReleased(mx, my);
		}
	}
	
	/**
	 * See {@link GuiScreen#handleMouseInput} for more information about mx and my.
	 */
	@Override
	public void handleMouseInput() {
		try {
			super.handleMouseInput();
		} catch (IOException e) {}
		int delta = Mouse.getEventDWheel();
		if (delta != 0) {
			int mx = Mouse.getEventX() * width / mc.displayWidth;
			int my = height - Mouse.getEventY() * height / mc.displayHeight - 1;
			boolean handled = false;
			delta = MathHelper.clamp_int(delta, -5, 5);
			
			for (Container c : containers) {
				if (c.inBounds(mx, my)) {
					c.mouseWheel(delta);
					handled = true;
					break;
				}
			}
			if (!handled && selectedContainer != null) selectedContainer.mouseWheel(delta);
		}
	}
	
	@Override
	public final void keyTyped(char c, int kc) {
		// START MAUNSIC-DEPENDANT
		if (!ServerHandler.canUse()) return;
		Maunsic.getLogger().trace("Key typed: " + c + ", " + kc + ", " + Keyboard.getKeyName(kc), this);
		// END MAUNSIC-DEPENDANT
		if (kc == Keyboard.KEY_ESCAPE) close();
		onKeyTyped(c, kc);
	}
	
	@Override
	public final void buttonClicked(Button b) {
		// START MAUNSIC-DEPENDANT
		if (!ServerHandler.canUse()) return;
		Maunsic.getLogger().trace("Button " + b.getText() + " clicked", this);
		// END MAUNSIC-DEPENDANT
		onButtonClicked(b);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
	
	@Override
	public void initGui() {
		if (!ServerHandler.canUse()) return;
		Keyboard.enableRepeatEvents(true);
		
		if (!hasInit) {
			Maunsic.getLogger().debug("Creating GUI", this); // MAUNSIC-DEPENDANT
			onInit();
			hasInit = true;
		}
		Maunsic.getLogger().debug("Revalidating GUI", this); // MAUNSIC-DEPENDANT
		onRevalidate();
		if (closed) {
			Maunsic.getLogger().debug("Reopening GUI", this); // MAUNSIC-DEPENDANT
			onReopen();
			closed = false;
		}
	}
	
	public void drawCenteredStringNoShadow(FontRenderer ft, String str, int cx, int y, int color) {
		ft.drawString(str, cx - ft.getStringWidth(str) / 2, y, color);
	}
	
	@Override
	public void onGuiClosed() {
		closed = true;
		Keyboard.enableRepeatEvents(false);
	}
	
	public void setEnabled(boolean enabled, Button... ws) {
		for (Button w : ws)
			w.setEnabled(enabled);
	}
	
	public void setEnabled(boolean enabled, FocusableButton... ws) {
		for (FocusableButton w : ws)
			w.setEnabled(enabled);
	}
}
