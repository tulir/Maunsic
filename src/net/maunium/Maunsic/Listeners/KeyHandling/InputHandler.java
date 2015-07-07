package net.maunium.Maunsic.Listeners.KeyHandling;

import org.lwjgl.input.Keyboard;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Actions.ActionFly;
import net.maunium.Maunsic.Gui.GuiMaunsic;
import net.maunium.Maunsic.Gui.Alts.GuiChangeUsername;
import net.maunium.Maunsic.Gui.XRay.GuiXrayBlocks;
import net.maunium.Maunsic.KeyMaucros.KeyMaucro;
import net.maunium.Maunsic.Server.ServerHandler;

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
	private static final int[] konami = { Keyboard.KEY_UP, Keyboard.KEY_UP, Keyboard.KEY_DOWN, Keyboard.KEY_DOWN, Keyboard.KEY_LEFT, Keyboard.KEY_RIGHT,
			Keyboard.KEY_LEFT, Keyboard.KEY_RIGHT, Keyboard.KEY_B, Keyboard.KEY_A };
	private static int konamiStatus = 0;
	private static boolean konamiDown = true;
	private static Maunsic host;
	
	public static void setHost(Maunsic newHost) {
		host = newHost;
		KeyRegistry.registerKeybind(Keybinds.config);
		KeyRegistry.registerKeybind(Keybinds.fly);
		KeyRegistry.registerKeybind(Keybinds.nofall);
		KeyRegistry.registerKeybind(Keybinds.inc_speed);
		KeyRegistry.registerKeybind(Keybinds.dec_speed);
		KeyRegistry.registerKeybind(Keybinds.antikb);
		KeyRegistry.registerKeybind(Keybinds.blink);
		KeyRegistry.registerKeybind(Keybinds.spammer);
		KeyRegistry.registerKeybind(Keybinds.phase);
		KeyRegistry.registerKeybind(Keybinds.attackaura);
		KeyRegistry.registerKeybind(Keybinds.triggerbot);
		KeyRegistry.registerKeybind(Keybinds.autosoup);
		KeyRegistry.registerKeybind(Keybinds.aimbot);
		KeyRegistry.registerKeybind(Keybinds.alts);
		KeyRegistry.registerKeybind(Keybinds.xray);
		KeyRegistry.registerKeybind(Keybinds.tracer);
		KeyRegistry.registerKeybind(Keybinds.esp);
		KeyRegistry.registerKeybind(Keybinds.autouse);
		KeyRegistry.registerKeybind(Keybinds.fastbow);
		KeyRegistry.registerKeybind(Keybinds.regen);
		KeyRegistry.registerKeybind(Keybinds.fullbright);
		KeyRegistry.registerKeybind(Keybinds.freecam);
	}
	
	public static void input(int keycode, boolean pressed) {
		if (keycode == -101 || keycode == Keyboard.KEY_NONE) return;
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
		
		if (keycode == konami[konamiStatus] && pressed == konamiDown) {
			if (!konamiDown) konamiStatus++;
			konamiDown = !konamiDown;
		} else {
			konamiStatus = 0;
			konamiDown = true;
		}
		
		if (konamiStatus == konami.length + 1) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiMaunsic(host));
			konamiStatus = 0;
			konamiDown = true;
			return;
		}
		
		if (pressed && !disabled) {
			if (keycode == Keybinds.config.getKeyCode()) Minecraft.getMinecraft().displayGuiScreen(new GuiMaunsic(host));
			else if (keycode == Keybinds.fly.getKeyCode()) {
				if (!host.actionFly.isActive()) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) host.actionFly.setType(ActionFly.TYPE_WALK);
					else host.actionFly.setType(ActionFly.TYPE_FLY);
				} else host.actionFly.setType(ActionFly.TYPE_DISABLED);
			} else if (keycode == Keybinds.inc_speed.getKeyCode()) {
				if (Keybinds.dec_speed.isDown()) host.actionFly.setSpeed(ActionFly.DEFAULT_SPEED);
				else host.actionFly.changeSpeed(true);
			} else if (keycode == Keybinds.dec_speed.getKeyCode()) {
				if (Keybinds.inc_speed.isDown()) host.actionFly.setSpeed(ActionFly.DEFAULT_SPEED);
				else host.actionFly.changeSpeed(false);
			} else if (keycode == Keybinds.xray.getKeyCode()) {
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) Minecraft.getMinecraft().displayGuiScreen(new GuiXrayBlocks(host));
				else {
					if (host.actionXray.isActive()) host.actionXray.setActive(false);
					else host.actionXray.setActive(true);
				}
			} else if (keycode == Keybinds.alts.getKeyCode()) Minecraft.getMinecraft().displayGuiScreen(new GuiChangeUsername(host));
			else if (keycode == Keybinds.nofall.getKeyCode()) host.actionNofall.setActive(!host.actionNofall.isActive());
			else if (keycode == Keybinds.blink.getKeyCode()) host.actionBlink.setActive(!host.actionBlink.isActive());
			else if (keycode == Keybinds.attackaura.getKeyCode()) host.actionAttackaura.setActive(!host.actionAttackaura.isActive());
			else if (keycode == Keybinds.spammer.getKeyCode()) host.actionSpammer.setActive(!host.actionSpammer.isActive());
			else if (keycode == Keybinds.triggerbot.getKeyCode()) host.actionTriggerbot.setActive(!host.actionTriggerbot.isActive());
			else if (keycode == Keybinds.autosoup.getKeyCode()) host.actionAutosoup.setActive(!host.actionAutosoup.isActive());
			else if (keycode == Keybinds.aimbot.getKeyCode()) host.actionAimbot.setActive(!host.actionAimbot.isActive());
			else if (keycode == Keybinds.tracer.getKeyCode()) host.actionTracer.setActive(!host.actionTracer.isActive());
			else if (keycode == Keybinds.esp.getKeyCode()) host.actionEsp.setActive(!host.actionEsp.isActive());
			else if (keycode == Keybinds.autouse.getKeyCode()) host.actionAutouse.setActive(!host.actionAutouse.isActive());
			else if (keycode == Keybinds.fastbow.getKeyCode()) host.actionFastbow.setActive(!host.actionFastbow.isActive());
			else if (keycode == Keybinds.regen.getKeyCode()) host.actionRegen.setActive(!host.actionRegen.isActive());
			else if (keycode == Keybinds.fullbright.getKeyCode()) host.actionFullbright.setActive(!host.actionFullbright.isActuallyActive());
			else if (keycode == Keybinds.freecam.getKeyCode()) host.actionFreecam.setActive(!host.actionFreecam.isActive());
			else if (keycode == Keybinds.phase.getKeyCode()) host.actionPhase.phase();
//			else if (keycode == Keybinds.antikb.getKeyCode()) host.actionAntiKB.setActive(!host.actionAntiKB.isActive());
		}
		
		if (!disabled) for (KeyMaucro km : KeyMaucro.getKeyMaucros()) {
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
	
	public final static class Keybinds {
		public static final MauKeybind config = new MauKeybind("conf", Keyboard.KEY_F4);
		public static final MauKeybind fly = new MauKeybind("fly", Keyboard.KEY_F);
		public static final MauKeybind nofall = new MauKeybind("nofall", Keyboard.KEY_N);
		public static final MauKeybind inc_speed = new MauKeybind("inc_speed", Keyboard.KEY_PRIOR);
		public static final MauKeybind dec_speed = new MauKeybind("dec_speed", Keyboard.KEY_NEXT);
		public static final MauKeybind antikb = new MauKeybind("antikb", Keyboard.KEY_NONE);
		public static final MauKeybind blink = new MauKeybind("blink", Keyboard.KEY_B);
		public static final MauKeybind spammer = new MauKeybind("spammer", Keyboard.KEY_NONE);
		public static final MauKeybind phase = new MauKeybind("phase", Keyboard.KEY_V);
		public static final MauKeybind attackaura = new MauKeybind("attackaura", Keyboard.KEY_R);
		public static final MauKeybind triggerbot = new MauKeybind("triggerbot", Keyboard.KEY_H);
		public static final MauKeybind autosoup = new MauKeybind("autosoup", Keyboard.KEY_O);
		public static final MauKeybind aimbot = new MauKeybind("aimbot", Keyboard.KEY_G);
		public static final MauKeybind alts = new MauKeybind("alts", Keyboard.KEY_U);
		public static final MauKeybind xray = new MauKeybind("xray", Keyboard.KEY_X);
		public static final MauKeybind tracer = new MauKeybind("tracer", Keyboard.KEY_NONE);
		public static final MauKeybind esp = new MauKeybind("esp", Keyboard.KEY_NONE);
		public static final MauKeybind autouse = new MauKeybind("autouse", Keyboard.KEY_PERIOD);
		public static final MauKeybind fastbow = new MauKeybind("fastbow", Keyboard.KEY_J);
		public static final MauKeybind regen = new MauKeybind("regen", Keyboard.KEY_P);
		public static final MauKeybind fullbright = new MauKeybind("fullbright", Keyboard.KEY_C);
		public static final MauKeybind freecam = new MauKeybind("freecam", Keyboard.KEY_I);
	}
}
