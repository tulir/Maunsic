package net.maunium.Maunsic.Actions;

import net.maunium.Maunsic.Actions.Util.TickAction;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.EnumChatFormatting;

/**
 * Regenerate health super quickly
 * 
 * @author Tulir293
 * @since 0.1
 */
public class ActionRegen extends TickAction {
	@Override
	public String[] getStatusText() {
		return new String[] { "Regen Hack " + EnumChatFormatting.GREEN + "ON" };
	}
	
	@Override
	public void execute() {
		EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
		if (!p.capabilities.isCreativeMode && p.getFoodStats().getFoodLevel() > 17 && p.getHealth() < 20 && p.getHealth() != 0) {
			for (int i = 0; i < 1000; i++)
				Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
		}
	}
	
}
