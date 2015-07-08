package net.maunium.Maunsic.Actions;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Actions.Util.TickAction;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.util.EnumChatFormatting;

/**
 * Freecam. Part of the code from Wurst Client licenced under MPL v2.0 by Alexander01998.
 * 
 * @author Tulir293
 * @author Alexander01998
 * @since 0.1
 */
public class ActionFreecam extends TickAction {
	private Maunsic host;
	public static boolean active = false;
	private double oldX, oldY, oldZ;
	private EntityOtherPlayerMP fake = null;
	
	public ActionFreecam(Maunsic host) {
		this.host = host;
	}
	
	@Override
	public void activate() {
		super.activate();
		oldX = Minecraft.getMinecraft().thePlayer.posX;
		oldY = Minecraft.getMinecraft().thePlayer.posY;
		oldZ = Minecraft.getMinecraft().thePlayer.posZ;
		
		fake = new EntityOtherPlayerMP(Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getGameProfile());
		fake.clonePlayer(Minecraft.getMinecraft().thePlayer, true);
		fake.copyLocationAndAnglesFrom(Minecraft.getMinecraft().thePlayer);
		fake.rotationYawHead = Minecraft.getMinecraft().thePlayer.rotationYawHead;
		
		Minecraft.getMinecraft().theWorld.addEntityToWorld(-293, fake);
		
	}
	
	@Override
	public void deactivate() {
		super.deactivate();
		Minecraft.getMinecraft().thePlayer.setPositionAndRotation(oldX, oldY, oldZ, Minecraft.getMinecraft().thePlayer.rotationYaw,
				Minecraft.getMinecraft().thePlayer.rotationPitch);
		
		Minecraft.getMinecraft().theWorld.removeEntityFromWorld(-293);
		fake = null;
		
		Minecraft.getMinecraft().renderGlobal.loadRenderers();
	}
	
	@Override
	public void execute() {
		if (host.actionFly.isActive()) Minecraft.getMinecraft().thePlayer.noClip = true;
		else Minecraft.getMinecraft().thePlayer.noClip = false;
	}
	
	@Override
	public String[] getStatusText() {
		return new String[] { "Freecam " + EnumChatFormatting.GREEN + "ON" };
	}
}
