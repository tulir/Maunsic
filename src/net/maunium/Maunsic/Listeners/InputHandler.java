package net.maunium.Maunsic.Listeners;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Gui.GuiMaunsic;
import net.maunium.Maunsic.KeyMaucros.KeyMaucro;
import net.maunium.Maunsic.Server.ServerHandler;
import net.maunium.Maunsic.TickActions.ActionFly;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;

import net.minecraftforge.fml.client.FMLClientHandler;

/**
 * Handles all key and mouse input for Maunsic and also stores keybinds.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class InputHandler {
	private static boolean disabled = false;
	private static Maunsic host;
	
	public static void setHost(Maunsic newHost) {
		host = newHost;
	}
	
	public static void input(int keycode, boolean pressed) {
		// Check if usage is allowed
		if (!ServerHandler.canUse()) return;
		// Execute the precheck key maucros, but only if the input handler is enabled.
		if (!disabled) for (KeyMaucro km : KeyMaucro.getKeyMaucros())
			if (km.getExecutionPhase().equals(KeyMaucro.ExecPhase.PRECHECKS)) km.checkAndExecute();
		
		// Check if the event key was recognized. If not, return.
		if (Keyboard.getEventKey() == Keyboard.KEY_NONE) return;
		// Check if the chat gui is open. If it is, return.
		if (FMLClientHandler.instance().isGUIOpen(GuiChat.class)) return;
		// Check if the player exists. If not, return.
		if (Minecraft.getMinecraft().thePlayer == null) return;
		
		// Execute the prekeys key maucros, but only if the input handler is enabled.
		if (!disabled) for (KeyMaucro km : KeyMaucro.getKeyMaucros())
			if (km.getExecutionPhase().equals(KeyMaucro.ExecPhase.PREKEYS)) km.checkAndExecute();
		
		// Check for the input handler toggle combination.
		if (Keyboard.isKeyDown(Keyboard.KEY_M) && Keyboard.isKeyDown(Keyboard.KEY_A) && Keyboard.isKeyDown(Keyboard.KEY_U)) {
			disabled = !disabled;
			Maunsic.printChat("conf.keys." + (disabled ? "dis" : "en") + "able");
			return;
		}
		
		// Check if the input handler is disabled.
		if (disabled) return;
		
		if (keycode == Keybinds.config.getKeyCode()) Minecraft.getMinecraft().displayGuiScreen(new GuiMaunsic(host));
		else if (keycode == Keybinds.fly.getKeyCode()) {
			if (!host.actionFly.isActive()) {
				if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) host.actionFly.setType(ActionFly.TYPE_WALK);
				else host.actionFly.setType(ActionFly.TYPE_FLY);
			} else host.actionFly.setType(ActionFly.TYPE_DISABLED);
		} else if (keycode == Keybinds.inc_speed.getKeyCode()) {
			if (Keybinds.dec_speed.isDown()) host.actionFly.setSpeed(ActionFly.DEFAULT_SPEED);
			else host.actionFly.changeSpeed(true);
		} else if (keycode == Keybinds.inc_speed.getKeyCode()) {
			if (Keybinds.inc_speed.isDown()) host.actionFly.setSpeed(ActionFly.DEFAULT_SPEED);
			else host.actionFly.changeSpeed(false);
		}
		
		for (KeyMaucro km : KeyMaucro.getKeyMaucros())
			if (km.getExecutionPhase().equals(KeyMaucro.ExecPhase.POSTKEYS)) km.checkAndExecute();
	}
	
	public void enable() {
		disabled = false;
	}
	
	public void disable() {
		disabled = true;
	}
	
	public static class Keybind {
		private int keyCode;
		private final int defaultKeyCode;
		private final String name;
		
		public Keybind(String name, int keyCode) {
			this.keyCode = keyCode;
			defaultKeyCode = keyCode;
			this.name = name;
		}
		
		public int getKeyCode() {
			return keyCode;
		}
		
		public int getDefaultKeyCode() {
			return defaultKeyCode;
		}
		
		public String getName() {
			return name;
		}
		
		public void setKeyCode(int keyCode) {
			this.keyCode = keyCode;
		}
		
		public boolean isDown() {
			return Keyboard.isKeyDown(keyCode) || Mouse.isButtonDown(keyCode);
		}
	}
	
	public static class Keybinds {
		public static final Keybind config = new Keybind("Configuration", Keyboard.KEY_F4);
		public static final Keybind fly = new Keybind("Fly", Keyboard.KEY_F);
		public static final Keybind inc_speed = new Keybind("Increase Speed", Keyboard.KEY_PRIOR);
		public static final Keybind dec_speed = new Keybind("Decrease Speed", Keyboard.KEY_NEXT);
	}
}
