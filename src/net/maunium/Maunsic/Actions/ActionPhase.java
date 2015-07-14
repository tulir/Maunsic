package net.maunium.Maunsic.Actions;

import net.maunium.Maunsic.Actions.Util.TickAction;
import net.maunium.Maunsic.Util.MaunsiConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

/**
 * Phasing through nonsolid blocks. Updated to be a lot quicker.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class ActionPhase extends TickAction {
	public boolean automated = true;
	private double startX, startZ;
	
	@Override
	public void activate() {
		super.activate();
		startX = Minecraft.getMinecraft().thePlayer.posX;
		startZ = Minecraft.getMinecraft().thePlayer.posZ;
	}
	
	@Override
	public void execute() {
		EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
		int look = MathHelper.floor_double(p.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		
		double phaseX = p.posX, phaseZ = p.posZ;
		if (look == 0) phaseZ += 0.6;
		else if (look == 1) phaseX -= 0.6;
		else if (look == 2) phaseZ -= 0.6;
		else if (look == 3) phaseX += 0.6;
		
		for (int i = 0; i < 4; i++)
			p.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(phaseX, p.posY, phaseZ, true));
		p.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(phaseX, p.posY - 0.5, phaseZ, true));
		
		if (look == 0 && p.posZ > startZ + 1) deactivate();
		else if (look == 1 && p.posX < startX - 1) deactivate();
		else if (look == 2 && p.posZ < startZ - 1) deactivate();
		else if (look == 3 && p.posX > startX + 1) deactivate();
		else if (!automated) deactivate();
		
		p.noClip = true;
		p.onGround = true;
		p.fallDistance = 0;
	}
	
	@Override
	public String[] getStatusText() {
		String[] rtrn = new String[2];
		rtrn[0] = "Automated Phase " + EnumChatFormatting.GREEN + "ON";
		return rtrn;
	}
	
	@Override
	public void saveData(MaunsiConfig conf) {
		conf.set("actions.phase.automated", automated);
	}
	
	@Override
	public void loadData(MaunsiConfig conf) {
		automated = conf.getBoolean("actions.phase.automated", automated);
	}
}
