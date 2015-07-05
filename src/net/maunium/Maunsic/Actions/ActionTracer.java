package net.maunium.Maunsic.Actions;

import java.util.List;

import net.maunium.Maunsic.Actions.Util.TickAction;
import net.maunium.Maunsic.Settings.Attacking;
import net.maunium.Maunsic.Util.GLHelper;
import net.maunium.Maunsic.Util.MaunsiConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;

public class ActionTracer implements TickAction {
	private boolean active = false;
	
	@Override
	public boolean isActive() {
		return active;
	}
	
	@Override
	public void setActive(boolean active) {
		this.active = active;
	}
	
	@Override
	public void saveData(MaunsiConfig conf) {}
	
	@Override
	public void loadData(MaunsiConfig conf) {}
	
	@Override
	public String[] getStatusText() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute() {
		EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
		for (EntityPlayer e : (List<EntityPlayer>) Minecraft.getMinecraft().theWorld.playerEntities) {
			if (e.equals(p)/* || e instanceof EntityFreecam */) continue;
			if (Attacking.isFriend(e.getName())) GLHelper.drawLine(p.posX, p.posY, p.posZ, p.posX, p.posY + p.getEyeHeight(), p.posZ, e.posX,
					e.posY + e.getEyeHeight(), e.posZ, 2, 0, 255, 0, 1.0F);
			else GLHelper.drawLine(p.posX, p.posY, p.posZ, p.posX, p.posY + p.getEyeHeight(), p.posZ, e.posX, e.posY + e.getEyeHeight(), e.posZ, 2, 255, 0, 0,
					1.0F);
		}
	}
	
}
