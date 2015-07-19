package net.maunium.Maunsic.Listeners;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Events.ClientChatSendEvent;
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
		
		Base64: if (m.contains("b64")) {
			Matcher matcher = Pattern.compile("(b64)[ ]*\\{[^\\}]+\\}").matcher(evt.getMessage());
			boolean matched = false;
			String msg = evt.getMessage();
			while (matcher.find()) {
				String s = matcher.group(0);
				s = s.substring(4, s.length() - 1);
				byte[] encoded = Base64.encodeBase64(s.getBytes(StandardCharsets.UTF_8));
				s = new String(encoded, StandardCharsets.UTF_8);
				msg = msg.replaceFirst("(b64)[ ]*\\{[^\\}]+\\}", "Ⅿᴮ" + s + "ᴮⅯ");
				matched = true;
			}
			
			if (!matched) {
				if (m.startsWith("b64 ")) msg = "Ⅿᴮ"
						+ new String(Base64.encodeBase64(evt.getMessage().split(" ", 2)[1].getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8) + "ᴮⅯ";
				else break Base64;
			}
			
			if (msg.length() > 100) {
				Maunsic.printChatError("message.encoding.toolong", msg.length());
				evt.setCanceled(true);
				return;
			}
			
			evt.setMessage(msg);
		}
		
//		if (m.startsWith("b64 ") || m.startsWith("base64 ")) {
//			String s = evt.getMessage().split(" ", 2)[1];
//			String msg = "Ⅿᴮ" + new String(Base64.encodeBase64(s.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
//			if (msg.length() > 100) {
//				Maunsic.printChatError("message.encoding.toolong", msg.length());
//				evt.setCanceled(true);
//				return;
//			}
//			evt.setMessage(msg);
//			return;
//		} else if (m.contains("b64")) {
//			String[] ss = evt.getMessage().split("(b64)[ ]?", 2);
//			String msg;
//			if (ss[1].contains("}")) {
//				String[] ss2 = ss[1].split(Pattern.quote("}"), 2);
//				msg = ss[0] + "Ⅿᴮ" + new String(Base64.encodeBase64(ss2[0].getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8) + "ᴮⅯ" + ss2[1];
//			} else msg = ss[0] + "Ⅿᴮ" + new String(Base64.encodeBase64(ss[1].getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
//			if (msg.length() > 100) {
//				Maunsic.printChatError("message.encoding.toolong", msg.length());
//				evt.setCanceled(true);
//				return;
//			}
//			evt.setMessage(msg);
//			return;
		/* } */ else if (m.startsWith("calculate ") || m.startsWith("calc ")) {
			evt.setCanceled(true);
			String s = Calculator.processCalculation(evt.getMessage().split(" ", 2)[1]);
			if (s.equals("varname")) Maunsic.printChatError("message.calculator.variable.spacename");
			else if (!s.equals("syntax")) Maunsic.printChatError("message.calculator.unknownerror", s);
			return;
		}
		Maunsic.getChatLogger().out(evt.getMessage());
	}
}
