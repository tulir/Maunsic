package net.maunium.Maunsic;

import org.lwjgl.input.Keyboard;

import net.maunium.Maunsic.Gui.GuiMaunsic;
import net.maunium.Maunsic.KeyMaucros.KeyMaucro;
import net.maunium.Maunsic.Server.ServerHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class InputHandler {
	private boolean disabled = false;
	private Keybinds kbs;
	private Maunsic host;
	
	public InputHandler(Maunsic host) {
		this.host = host;
		kbs = new Keybinds();
	}
	
	public Keybinds getKeybinds() {
		return kbs;
	}
	
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent evt) {
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
		if (disabled) {
			// If it is, call the isPressed method of each keybind so they won't get "stuck" and fire after enabling the input listener.
			kbs.config.isPressed();
			// Return, since anything beyond shouldn't be executed when disabled.
			return;
		}
		
		if (kbs.config.isPressed()) Minecraft.getMinecraft().displayGuiScreen(new GuiMaunsic(host));
		
		for (KeyMaucro km : KeyMaucro.getKeyMaucros())
			if (km.getExecutionPhase().equals(KeyMaucro.ExecPhase.POSTKEYS)) km.checkAndExecute();
	}
	
	public void enable() {
		disabled = false;
	}
	
	public void disable() {
		disabled = true;
	}
	
	public class Keybinds {
		public final KeyBinding config = new KeyBinding("Configuration", Keyboard.KEY_F4, null);
	}
}
