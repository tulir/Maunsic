package net.maunium.Maunsic.Listeners;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Core.ClientChatSendEvent;

import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OutChatListener {
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onSendChat(ClientChatSendEvent evt) {
		Maunsic.getChatLogger().out(evt.getMessage());
	}
}
