package net.maunium.Maunsic.Actions;

import net.maunium.Maunsic.Actions.Util.TickAction;
import net.maunium.Maunsic.Settings.Attacking;
import net.maunium.Maunsic.Util.EntityUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;

/**
 * Action for following a player. Base of this action was taken from Wurst Client by Alexander01998, which is licenced under MPL v2.0
 * 
 * @author Tulir293
 * @since 0.1
 */
public class ActionFollow extends TickAction {
	public EntityOtherPlayerMP entity = null;
	
	@Override
	public void activate() {
		entity = (EntityOtherPlayerMP) EntityUtils.getClosestEntity(false, EntityUtils.getEntitiesAABB(EntityOtherPlayerMP.class, Attacking.range));
		if (entity != null) super.activate();
	}
	
	@Override
	public void deactivate() {
		super.deactivate();
		KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), false);
	}
	
	@Override
	public void execute() {
		EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
		if (entity == null || entity.isDead || p.isDead) {
			entity = null;
			deactivate();
			return;
		}
		
		double dx = Math.abs(p.posX - entity.posX);
		double dz = Math.abs(p.posZ - entity.posZ);
		EntityUtils.faceEntityClient(entity);
		
		if (dx > 1D || dz > 1D) KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), true);
		else KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), false);
		
		if (entity.isSprinting()) p.setSprinting(true);
		else p.setSprinting(false);
		
		if (p.isCollidedHorizontally && p.onGround) p.jump();
		if (p.isInWater() && p.posY < entity.posY) p.motionY += 0.04;
	}
	
	@Override
	public String[] getStatusText() {
		return new String[] { "Following " + EnumChatFormatting.GREEN + entity.getName() };
	}
	
}
