package net.maunium.Maunsic.Actions;

import java.util.List;

import net.maunium.Maunsic.Actions.Util.TickAction;
import net.maunium.Maunsic.Settings.Attacking;
import net.maunium.Maunsic.Util.GLHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

/**
 * Action for displaying lines from self to players within render distance. Code in execute() was imported from Maucros.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class ActionTracer extends TickAction {
	@Override
	public String[] getStatusText() {
		return new String[] { "Tracer " + EnumChatFormatting.GREEN + "ON" };
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute() {
		EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
		for (EntityPlayer e : (List<EntityPlayer>) Minecraft.getMinecraft().theWorld.playerEntities) {
			if (e.getUniqueID().equals(p.getUniqueID())) continue;
			if (Attacking.isFriend(e.getName())) GLHelper.drawLine(p.posX, p.posY, p.posZ, p.posX, p.posY + p.getEyeHeight(), p.posZ, e.posX,
					e.posY + e.getEyeHeight(), e.posZ, 2, 0, 255, 0, 1.0F);
			else GLHelper.drawLine(p.posX, p.posY, p.posZ, p.posX, p.posY + p.getEyeHeight(), p.posZ, e.posX, e.posY + e.getEyeHeight(), e.posZ, 2, 255, 0, 0,
					1.0F);
		}
	}
	
}
