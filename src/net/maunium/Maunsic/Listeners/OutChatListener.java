package net.maunium.Maunsic.Listeners;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.apache.commons.codec.binary.Base64;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Core.ClientChatSendEvent;

import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OutChatListener {
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onSendChat(ClientChatSendEvent evt) {
		String m = evt.getMessage().toLowerCase(Locale.ENGLISH);
		if (m.startsWith("b64 ") || m.startsWith("base64 ")) {
			String s = evt.getMessage().split(" ", 2)[1];
			String encoded = new String(Base64.encodeBase64(s.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
			String msg = "Ⅿᴮ" + encoded;
			if (msg.length() > 100) {
				Maunsic.printChatError("message.encoding.toolong", msg.length());
				evt.setCanceled(true);
				return;
			}
			evt.setMessage(msg);
			return;
		}
		Maunsic.getChatLogger().out(evt.getMessage());
	}
}
