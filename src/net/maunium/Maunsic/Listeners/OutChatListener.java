package net.maunium.Maunsic.Listeners;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.apache.commons.codec.binary.Base64;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Core.ClientChatSendEvent;
import net.maunium.Maunsic.Util.Calculator;

import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Currently used for logging outgoing chat and encoding messages marked with base64.
 * 
 * @author Tulir293
 * @since 0.1
 */
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
		} else if (m.startsWith("calculate ") || m.startsWith("calc ")) {
			evt.setCanceled(true);
			String s = Calculator.processCalculation(evt.getMessage().split(" ", 2)[1]);
			if (s.equals("varname")) Maunsic.printChatError("message.calculator.variable.spacename");
			else if (!s.equals("syntax")) Maunsic.printChat_static(s);
			return;
		}
		Maunsic.getChatLogger().out(evt.getMessage());
	}
}
