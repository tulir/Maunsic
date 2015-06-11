package net.maunium.Maunsic.KeyMaucros.Commands;

import net.maunium.Maunsic.Maunsic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

/**
 * The command to make the user move forward.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class CommandForward implements MaucroCommand {
	@Override
	public String getOwner() {
		return "Maunsic";
	}
	
	@Override
	public String getName() {
		return "Forward";
	}
	
	@Override
	public void execute(String label, String[] args) {
		if (args.length > 0) {
			final int time;
			try {
				time = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				Maunsic.printChatError_static("The MaucroCommand Move takes one integer.");
				return;
			}
			new Thread() {
				@Override
				public void run() {
					KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), true);
					try {
						Thread.sleep(time);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), false);
				}
			}.start();
		} else Maunsic.printChatError_static("The MaucroCommand Move takes one integer.");
	}
	
}
