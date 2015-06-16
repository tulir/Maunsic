package net.maunium.Maunsic.Actions;

import net.maunium.Maunsic.Actions.Util.TickAction;
import net.maunium.Maunsic.Util.MaunsiConfig;

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
public class ActionNofall implements TickAction {
	private boolean active = false;
	
	@Override
	public boolean isActive() {
		return active;
	}
	
	@Override
	public void setActive(boolean active) {
		this.active = active;
	}
	
	@Override
	public void execute() {
		Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
	}
	
	@Override
	public String[] getStatusText() {
		return new String[] { "Nofall" + EnumChatFormatting.GREEN + " ON" };
	}
	
	@Override
	public void saveData(MaunsiConfig conf) {}
	
	@Override
	public void loadData(MaunsiConfig conf) {}
}
