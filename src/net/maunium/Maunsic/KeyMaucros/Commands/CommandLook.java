package net.maunium.Maunsic.KeyMaucros.Commands;

import net.maunium.Maunsic.Maunsic;

import net.minecraft.client.Minecraft;

/**
 * The "Look" command for command chain key maucros.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class CommandLook implements MaucroCommand {
	@Override
	public String getOwner() {
		return "Maucros";
	}
	
	@Override
	public String getName() {
		return "Look";
	}
	
	@Override
	public void execute(String label, String[] args) {
		if (args.length > 1) {
			int yaw, pitch;
			try {
				yaw = Integer.parseInt(args[0]);
				pitch = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				Maunsic.printChatError_static("The MaucroCommand Look takes two integers.");
				return;
			}
			Minecraft.getMinecraft().thePlayer.rotationYaw = yaw;
			Minecraft.getMinecraft().thePlayer.rotationPitch = pitch;
		} else Maunsic.printChatError_static("The MaucroCommand Look takes two integers.");
	}
	
}
