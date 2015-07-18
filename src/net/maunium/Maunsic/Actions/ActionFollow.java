package net.maunium.Maunsic.Actions;

import net.maunium.Maunsic.Actions.Util.TickAction;
import net.maunium.Maunsic.Settings.Attacking;
import net.maunium.Maunsic.Util.EntityUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;

/**
 * Action for following a player. Parts of the code from Wurst Client by Alexander01998, which is licenced under MPL 2.0
 * 
 * @author Tulir293
 * @author Alexander01998
 * @since 0.1
 * @from Wurst Client
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
		if (entity == null) {
			deactivate();
			return;
		}
		if (entity.isDead || Minecraft.getMinecraft().thePlayer.isDead) {
			entity = null;
			deactivate();
			return;
		}
		double xDist = Math.abs(Minecraft.getMinecraft().thePlayer.posX - entity.posX);
		double zDist = Math.abs(Minecraft.getMinecraft().thePlayer.posZ - entity.posZ);
		EntityUtils.faceEntityClient(entity);
		if (xDist > 1D || zDist > 1D) KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), true);
		else KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), false);
		if (Minecraft.getMinecraft().thePlayer.isCollidedHorizontally && Minecraft.getMinecraft().thePlayer.onGround) Minecraft.getMinecraft().thePlayer.jump();
		if (Minecraft.getMinecraft().thePlayer.isInWater() && Minecraft.getMinecraft().thePlayer.posY < entity.posY)
			Minecraft.getMinecraft().thePlayer.motionY += 0.04;
	}
	
	@Override
	public String[] getStatusText() {
		return new String[] { "Following " + EnumChatFormatting.GREEN + entity.getName() };
	}
	
}
