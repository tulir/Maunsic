package net.maunium.Maunsic.Actions;

import net.maunium.Maunsic.Actions.Util.TickAction;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.EnumChatFormatting;

/**
 * The TickAction to remove fall damage.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class ActionNofall extends TickAction {
	@Override
	public void execute() {
		Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
	}
	
	@Override
	public String[] getStatusText() {
		return new String[] { "Nofall" + EnumChatFormatting.GREEN + " ON" };
	}
}
