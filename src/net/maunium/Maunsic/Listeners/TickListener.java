package net.maunium.Maunsic.Listeners;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Gui.Alts.GuiChangeUsername;
import net.maunium.Maunsic.Listeners.KeyHandling.InputHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TickListener {
	private Maunsic host;
	
	public TickListener(Maunsic host) {
		this.host = host;
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent evt) {
		if (InputHandler.Keybinds.alts.isDown()) {
			GuiScreen cur = Minecraft.getMinecraft().currentScreen;
			if (Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatOpen()) return;
			if (cur != null && !(cur instanceof GuiIngameMenu) && !(cur instanceof GuiMainMenu) && !(cur instanceof GuiMultiplayer)
					&& !(cur instanceof GuiSelectWorld) && !(cur instanceof GuiOptions)) return;
			Minecraft.getMinecraft().displayGuiScreen(new GuiChangeUsername(host));
		}
	}
}
