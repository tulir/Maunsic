package net.maunium.Maunsic.Actions;

import net.maunium.Maunsic.Actions.Util.TickAction;
import net.maunium.Maunsic.Util.MaunsiConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

/**
 * Phasing through nonsolid blocks. Updated to be a lot quicker.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class ActionPhase extends TickAction {
	public boolean automated = true, noclip = true, ground = true, fall = true;
	public int horizontalPackets = 1, verticalPackets = 1;
	public double startX, startZ, movHorizontal = 0.6, movVertical = 0.6;
	
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
		
		double phaseX = p.posX, phaseY = p.posX - movVertical, phaseZ = p.posZ;
		if (look == 0) phaseZ += movHorizontal;
		else if (look == 1) phaseX -= movHorizontal;
		else if (look == 2) phaseZ -= movHorizontal;
		else if (look == 3) phaseX += movHorizontal;
		
		if (p.worldObj.isAirBlock(new BlockPos(phaseX, phaseY, phaseZ))) phaseY -= 1;
		
		for (int i = 0; i < horizontalPackets; i++)
			p.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(phaseX, p.posY, phaseZ, true));
		for (int i = 0; i < verticalPackets; i++)
			p.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(phaseX, phaseY, phaseZ, true));
			
		if (look == 0 && p.posZ > startZ + 1) deactivate();
		else if (look == 1 && p.posX < startX - 1) deactivate();
		else if (look == 2 && p.posZ < startZ - 1) deactivate();
		else if (look == 3 && p.posX > startX + 1) deactivate();
		else if (!automated) deactivate();
		
		if (noclip) p.noClip = true;
		if (ground) p.onGround = true;
		if (fall) p.fallDistance = 0;
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
		conf.set("actions.phase.noclip", noclip);
		conf.set("actions.phase.ground", ground);
		conf.set("actions.phase.fall", fall);
		conf.set("actions.phase.packets.horizontal", horizontalPackets);
		conf.set("actions.phase.packets.vertical", verticalPackets);
		conf.set("actions.phase.move.horizontal", movHorizontal);
		conf.set("actions.phase.move.vertical", movVertical);
	}
	
	@Override
	public void loadData(MaunsiConfig conf) {
		automated = conf.getBoolean("actions.phase.automated", automated);
		noclip = conf.getBoolean("actions.phase.noclip", noclip);
		ground = conf.getBoolean("actions.phase.ground", ground);
		fall = conf.getBoolean("actions.phase.fall", fall);
		horizontalPackets = conf.getInt("actions.phase.packets.horizontal", horizontalPackets);
		verticalPackets = conf.getInt("actions.phase.packets.vertical", verticalPackets);
		movHorizontal = conf.getDouble("actions.phase.move.horizontal", movHorizontal);
		movVertical = conf.getDouble("actions.phase.move.vertical", movVertical);
	}
}
