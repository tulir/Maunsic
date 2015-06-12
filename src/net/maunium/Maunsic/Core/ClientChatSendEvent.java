package net.maunium.Maunsic.Core;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * An event to modify or cancel outgoing messages.
 * 
 * @author Tulir293
 * @since 0.1
 */
@Cancelable
public class ClientChatSendEvent extends Event {
	/**
	 * This method is what the transformer tells the player class to use.
	 */
	public static void sendChatMessage(String message) {
		ClientChatSendEvent ccse = new ClientChatSendEvent(message);
		if (MinecraftForge.EVENT_BUS.post(ccse)) return;
		Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage(ccse.getMessage()));
	}
	
	private String message;
	
	public ClientChatSendEvent(String message) {
		this.message = message;
	}
	
	/**
	 * Get the message of this event.
	 */
	public String getMessage() {
		return this.message;
	}
	
	/**
	 * Set the message of this event.
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
