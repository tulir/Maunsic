package net.maunium.Maunsic.Listeners;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;

import net.maunium.Maunsic.Maunsic;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

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
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onChat(ClientChatReceivedEvent evt) {
		if (evt.message.getUnformattedText().contains("Ⅿᴮ")) {
			String[] tmp = evt.message.getFormattedText().split(Pattern.quote("Ⅿᴮ"), 2);
			tmp[1] = tmp[1].substring(0, tmp[1].length() - 2);
			String decoded = new String(Base64.decodeBase64(tmp[1].getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
			
			evt.message = new ChatComponentText(tmp[0]);
			evt.message.appendText(decoded.replace("&", "§"));
			evt.message.appendSibling(new ChatComponentText(" [B64]").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true)));
		}
		Maunsic.getChatLogger().in(evt.message.getUnformattedText());
	}
}
