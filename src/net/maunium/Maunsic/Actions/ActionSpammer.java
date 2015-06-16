package net.maunium.Maunsic.Actions;

import java.util.List;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Actions.Util.IntervalAction;
import net.maunium.Maunsic.Util.MaunsiConfig;

import net.minecraft.client.Minecraft;
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
	public boolean isActive() {
		return active;
	}
	
	@Override
	public void setActive(boolean active) {
		List<?> msgs = Minecraft.getMinecraft().ingameGUI.getChatGUI().getSentMessages();
		spam = (String) msgs.get(msgs.size());
		this.active = active;
	}
	
	public void setInterval(int interval) {
		this.interval = interval;
	}
	
	@Override
	public void saveData(MaunsiConfig conf) {
		conf.set("actions.spammer.interval", interval);
	}
	
	@Override
	public void loadData(MaunsiConfig conf) {
		interval = conf.getInt("actions.spammer.interval", interval);
	}
}
