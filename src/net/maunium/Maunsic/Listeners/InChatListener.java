package net.maunium.Maunsic.Listeners;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;

import net.maunium.Maunsic.Maunsic;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Currently used for logging incoming chat and decoding base64 input.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class InChatListener {
	private String lastMessage = "";
	private int countOfSpam = 1, prevId = 0;
	public static int antispam = 0;
	
	public InChatListener(Maunsic host) {
		antispam = host.getConfig().getInt("antispam");
	}
	
	@SuppressWarnings("unchecked")
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onChat(ClientChatReceivedEvent evt) {
		if (evt.message.getUnformattedText().contains("Ⅿᴮ")) {
			for (int i = 0; i < evt.message.getSiblings().size(); i++) {
				IChatComponent icc = (IChatComponent) evt.message.getSiblings().get(i);
				String s = icc.getUnformattedTextForChat();
				
				Matcher matcher = Pattern.compile("(Ⅿᴮ)[^(Ⅿᴮ)]+(ᴮⅯ)").matcher(s);
				while (matcher.find()) {
					String ss = matcher.group(0);
					byte[] decoded = Base64.decodeBase64(ss.substring(2, ss.length() - 2).getBytes(StandardCharsets.UTF_8));
					s = s.replace(ss, ("&7&n&ob64&7&o{&f" + new String(decoded, StandardCharsets.UTF_8) + "&7&o}" + icc.getChatStyle().getFormattingCode())
							.replace('&', '§'));
				}
				IChatComponent ncc = new ChatComponentText(s);
				ncc.setChatStyle(icc.getChatStyle());
				evt.message.getSiblings().set(i, ncc);
			}
//			String[] tmp = evt.message.getFormattedText().split(Pattern.quote("Ⅿᴮ"), 2);
//			if (evt.message.getUnformattedText().contains("ᴮⅯ")) {
//				String[] a = tmp[1].split(Pattern.quote("ᴮⅯ"));
//				tmp = new String[] { tmp[0], a[0], a[1] };
//			}
//			tmp[1] = tmp[1].substring(0, tmp[1].length() - 2);
//			String decoded = new String(Base64.decodeBase64(tmp[1].getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
//			
//			evt.message = new ChatComponentText(tmp[0]);
//			evt.message.appendText(decoded.replace("&", "§"));
//			evt.message.appendSibling(new ChatComponentText(" [B64]").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true)));
//			if (tmp.length > 2) evt.message.appendText(tmp[2]);
		}
		
		String message = evt.message.getUnformattedText();
		if (antispam == 2) {
			evt.setCanceled(true);
			if (message.equalsIgnoreCase(lastMessage)) {
				countOfSpam++;
				IChatComponent spamCount = new ChatComponentText(" [" + countOfSpam + "]")
						.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true));
				evt.message = evt.message.appendSibling(spamCount);
			} else {
				prevId++;
				countOfSpam = 1;
			}
			Minecraft.getMinecraft().ingameGUI.getChatGUI().setChatLine(evt.message, prevId, Minecraft.getMinecraft().ingameGUI.getUpdateCounter(), false);
		} else if (antispam == 1 && message.equalsIgnoreCase(lastMessage)) evt.setCanceled(true);
		
		Maunsic.getChatLogger().in(message);
		// Set the last message to this message.
		lastMessage = message;
	}
}
