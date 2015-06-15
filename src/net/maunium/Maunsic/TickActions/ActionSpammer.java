package net.maunium.Maunsic.TickActions;

import net.maunium.Maunsic.Maunsic;

import net.minecraft.util.EnumChatFormatting;

/**
 * Spammer.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class ActionSpammer extends IntervalAction {
	private boolean active = false;
	private String spam = "test";
	
	@Override
	public void executeInterval() {
		Maunsic.sendChat(spam);
	}
	
	@Override
	public String[] getStatusText() {
		String[] rtrn = new String[2];
		rtrn[0] = "Spammer " + EnumChatFormatting.GREEN + "ON";
		if (interval < 500) rtrn[1] = " Speed: " + EnumChatFormatting.RED + 1000 / interval + " msg/s";
		else rtrn[1] = " Speed: " + EnumChatFormatting.GREEN + 1000 / interval + " msg/s";
		return rtrn;
	}
	
	@Override
	public String getName() {
		return "Spammer";
	}
	
	@Override
	public boolean isActive() {
		return active;
	}
	
	@Override
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void setInterval(int interval) {
		this.interval = interval;
	}
}
