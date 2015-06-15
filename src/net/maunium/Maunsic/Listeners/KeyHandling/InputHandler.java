package net.maunium.Maunsic.Listeners.KeyHandling;

import org.lwjgl.input.Keyboard;

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
		KeyRegistry.registerKeybind(Keybinds.config);
		KeyRegistry.registerKeybind(Keybinds.fly);
		KeyRegistry.registerKeybind(Keybinds.inc_speed);
		KeyRegistry.registerKeybind(Keybinds.dec_speed);
	}
	
	public static void input(int keycode, boolean pressed) {
		// Check if usage is allowed
		if (!ServerHandler.canUse()) return;
		// Execute the precheck key maucros, but only if the input handler is enabled.
		if (!disabled) for (KeyMaucro km : KeyMaucro.getKeyMaucros()) {
			KeyMaucro.ExecPhase ep = pressed ? KeyMaucro.ExecPhase.PRECHECKS_DOWN : KeyMaucro.ExecPhase.PRECHECKS_UP;
			if (km.getExecutionPhase().equals(ep)) km.checkAndExecute();
		}
		
		// Check if the chat gui is open. If it is, return.
		if (FMLClientHandler.instance().isGUIOpen(GuiChat.class)) return;
		// Check if the player exists. If not, return.
		if (Minecraft.getMinecraft().thePlayer == null) return;
		
		// Execute the prekeys key maucros, but only if the input handler is enabled.
		if (!disabled) for (KeyMaucro km : KeyMaucro.getKeyMaucros()) {
			KeyMaucro.ExecPhase ep = pressed ? KeyMaucro.ExecPhase.PREKEYS_DOWN : KeyMaucro.ExecPhase.PREKEYS_UP;
			if (km.getExecutionPhase().equals(ep)) km.checkAndExecute();
		}
		
		// Check for the input handler toggle combination.
		if (Keyboard.isKeyDown(Keyboard.KEY_M) && Keyboard.isKeyDown(Keyboard.KEY_A) && Keyboard.isKeyDown(Keyboard.KEY_U)) {
			disabled = !disabled;
			Maunsic.printChat("conf.keys." + (disabled ? "disable" : "enable"));
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
		
		for (KeyMaucro km : KeyMaucro.getKeyMaucros()) {
			KeyMaucro.ExecPhase ep = pressed ? KeyMaucro.ExecPhase.POSTKEYS_DOWN : KeyMaucro.ExecPhase.POSTKEYS_UP;
			if (km.getExecutionPhase().equals(ep)) km.checkAndExecute();
		}
	}
	
	public static boolean toggleDisable() {
		disabled = !disabled;
		return disabled;
	}
	
	public static boolean isDisabled() {
		return disabled;
	}
	
	public static class Keybinds {
		public static final MauKeybind config = new MauKeybind("Configuration", Keyboard.KEY_F4);
		public static final MauKeybind fly = new MauKeybind("Fly", Keyboard.KEY_F);
		public static final MauKeybind inc_speed = new MauKeybind("Increase Speed", Keyboard.KEY_PRIOR);
		public static final MauKeybind dec_speed = new MauKeybind("Decrease Speed", Keyboard.KEY_NEXT);
	}
}
