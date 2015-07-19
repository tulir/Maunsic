package net.maunium.Maunsic.Listeners.KeyHandling;

import org.lwjgl.input.Keyboard;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Actions.ActionFly;
import net.maunium.Maunsic.Events.RawInputEvent;
import net.maunium.Maunsic.Gui.GuiMaunsic;
import net.maunium.Maunsic.Gui.Alts.GuiChangeUsername;
import net.maunium.Maunsic.Gui.XRay.GuiXrayBlocks;
import net.maunium.Maunsic.KeyMaucros.KeyMaucro;
import net.maunium.Maunsic.Server.ServerHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Handles all key and mouse input for Maunsic and also stores keybinds.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class InputHandler {
	private boolean disabled = false;
	private Maunsic host;
	
	public int[] forceOpen = { Keyboard.KEY_UP, Keyboard.KEY_UP, Keyboard.KEY_DOWN, Keyboard.KEY_DOWN, Keyboard.KEY_LEFT, Keyboard.KEY_RIGHT, Keyboard.KEY_LEFT,
			Keyboard.KEY_RIGHT, Keyboard.KEY_B, Keyboard.KEY_A };
	private int forceOpenStatus = 0;
	private boolean forceOpenDown = true;
	
	public InputHandler(Maunsic host) {
		this.host = host;
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
		KeyRegistry.registerKeybind(Keybinds.trajectories);
		KeyRegistry.registerKeybind(Keybinds.follow);
	}
	
	@SubscribeEvent
	public void onKeyInput(RawInputEvent evt) {
		if (evt.getCode() == -101 || evt.getCode() == Keyboard.KEY_NONE) return;
		// Check if usage is allowed
		if (!ServerHandler.canUse()) return;
		// Execute the precheck key maucros, but only if the input handler is enabled.
		if (!disabled) for (KeyMaucro km : KeyMaucro.getKeyMaucros()) {
			KeyMaucro.ExecPhase ep = evt.isPressed() ? KeyMaucro.ExecPhase.PRECHECKS_DOWN : KeyMaucro.ExecPhase.PRECHECKS_UP;
			if (evt.getCode() == km.getKeyCode() && km.shiftKeysDown() && km.getExecutionPhase().equals(ep)) km.executeMacro();
		}
		
		// Check if the chat gui is open. If it is, return.
		if (FMLClientHandler.instance().isGUIOpen(GuiChat.class)) return;
		// Check if the player exists. If not, return.
		if (Minecraft.getMinecraft().thePlayer == null) return;
		
		if (evt.getCode() == forceOpen[forceOpenStatus] && evt.isPressed() == forceOpenDown) {
			if (!forceOpenDown) forceOpenStatus++;
			forceOpenDown = !forceOpenDown;
			
			if (forceOpenStatus >= forceOpen.length) {
				Minecraft.getMinecraft().displayGuiScreen(new GuiMaunsic(host));
				forceOpenStatus = 0;
				forceOpenDown = true;
			}
			return;
		} else {
			forceOpenStatus = 0;
			forceOpenDown = true;
		}
		
		// Execute the prekeys key maucros, but only if the input handler is enabled.
		if (!disabled) for (KeyMaucro km : KeyMaucro.getKeyMaucros()) {
			KeyMaucro.ExecPhase ep = evt.isPressed() ? KeyMaucro.ExecPhase.PREKEYS_DOWN : KeyMaucro.ExecPhase.PREKEYS_UP;
			if (evt.getCode() == km.getKeyCode() && km.shiftKeysDown() && km.getExecutionPhase().equals(ep)) km.executeMacro();
		}
		
		if (evt.isPressed() && !disabled) {
			if (evt.getCode() == Keybinds.config.getKeyCode()) Minecraft.getMinecraft().displayGuiScreen(new GuiMaunsic(host));
			else if (evt.getCode() == Keybinds.fly.getKeyCode()) {
				if (!host.actionFly.isActive()) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) host.actionFly.setType(ActionFly.TYPE_WALK);
					else host.actionFly.setType(ActionFly.TYPE_FLY);
				} else host.actionFly.setType(ActionFly.TYPE_DISABLED);
			} else if (evt.getCode() == Keybinds.inc_speed.getKeyCode()) {
				if (Keybinds.dec_speed.isDown()) host.actionFly.setSpeed(ActionFly.DEFAULT_SPEED);
				else host.actionFly.changeSpeed(true);
			} else if (evt.getCode() == Keybinds.dec_speed.getKeyCode()) {
				if (Keybinds.inc_speed.isDown()) host.actionFly.setSpeed(ActionFly.DEFAULT_SPEED);
				else host.actionFly.changeSpeed(false);
			} else if (evt.getCode() == Keybinds.xray.getKeyCode()) {
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) Minecraft.getMinecraft().displayGuiScreen(new GuiXrayBlocks(host));
				else host.actionXray.toggle();
			} else if (evt.getCode() == Keybinds.alts.getKeyCode()) Minecraft.getMinecraft().displayGuiScreen(new GuiChangeUsername(host));
			else if (evt.getCode() == Keybinds.nofall.getKeyCode()) host.actionNofall.toggle();
			else if (evt.getCode() == Keybinds.blink.getKeyCode()) host.actionBlink.toggle();
			else if (evt.getCode() == Keybinds.attackaura.getKeyCode()) host.actionAttackaura.toggle();
			else if (evt.getCode() == Keybinds.spammer.getKeyCode()) host.actionSpammer.toggle();
			else if (evt.getCode() == Keybinds.triggerbot.getKeyCode()) host.actionTriggerbot.toggle();
			else if (evt.getCode() == Keybinds.autosoup.getKeyCode()) host.actionAutosoup.toggle();
			else if (evt.getCode() == Keybinds.aimbot.getKeyCode()) host.actionAimbot.toggle();
			else if (evt.getCode() == Keybinds.tracer.getKeyCode()) host.actionTracer.toggle();
			else if (evt.getCode() == Keybinds.esp.getKeyCode()) host.actionEsp.toggle();
			else if (evt.getCode() == Keybinds.autouse.getKeyCode()) host.actionAutouse.toggle();
			else if (evt.getCode() == Keybinds.fastbow.getKeyCode()) host.actionFastbow.toggle();
			else if (evt.getCode() == Keybinds.regen.getKeyCode()) host.actionRegen.toggle();
			else if (evt.getCode() == Keybinds.freecam.getKeyCode()) host.actionFreecam.toggle();
			else if (evt.getCode() == Keybinds.trajectories.getKeyCode()) host.actionTrajectories.toggle();
			else if (evt.getCode() == Keybinds.fullbright.getKeyCode()) host.actionFullbright.toggle();
			else if (evt.getCode() == Keybinds.follow.getKeyCode()) host.actionFollow.toggle();
			else if (evt.getCode() == Keybinds.phase.getKeyCode()) {
				if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) host.actionPhase.deactivate();
				else host.actionPhase.activate();
			}
//			else if (evt.getCode() == Keybinds.antikb.getKeyCode()) host.actionAntiKB.setActive(!host.actionAntiKB.isActive());
		}
		
		if (!disabled) for (KeyMaucro km : KeyMaucro.getKeyMaucros()) {
			KeyMaucro.ExecPhase ep = evt.isPressed() ? KeyMaucro.ExecPhase.POSTKEYS_DOWN : KeyMaucro.ExecPhase.POSTKEYS_UP;
			if (evt.getCode() == km.getKeyCode() && km.shiftKeysDown() && km.getExecutionPhase().equals(ep)) km.executeMacro();
		}
	}
	
	public boolean toggleDisable() {
		disabled = !disabled;
		return disabled;
	}
	
	public boolean isDisabled() {
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
		public static final MauKeybind trajectories = new MauKeybind("trajectories", Keyboard.KEY_Y);
		public static final MauKeybind follow = new MauKeybind("follow", Keyboard.KEY_NONE);
	}
}
