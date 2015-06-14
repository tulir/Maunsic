package net.maunium.Maunsic.Listeners;

import net.maunium.Maunsic.Maunsic;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class InChatListener {
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onChat(ClientChatReceivedEvent evt) {
		Maunsic.getChatLogger().in(evt.message.getUnformattedText());
	}
}
