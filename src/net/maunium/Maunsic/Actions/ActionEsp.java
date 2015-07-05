package net.maunium.Maunsic.Actions;

import java.util.List;

import net.maunium.Maunsic.Actions.Util.TickAction;
import net.maunium.Maunsic.Settings.Attacking;
import net.maunium.Maunsic.Util.GLHelper;
import net.maunium.Maunsic.Util.MaunsiConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

public class ActionEsp implements TickAction {
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
			
			float hW = e.width / 2.0F;
			AxisAlignedBB bb = AxisAlignedBB.fromBounds(e.posX - hW, e.posY, e.posZ - hW, e.posX + hW, e.posY + 1.68, e.posZ + hW);
			
			if (Attacking.isFriend(e.getName())) GLHelper.drawBoundingBox(bb, 0.1, 0.7, 0.1);
			else GLHelper.drawBoundingBox(bb, 0.7, 0.1, 0.1);
		}
	}
}
