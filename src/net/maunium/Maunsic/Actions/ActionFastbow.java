package net.maunium.Maunsic.Actions;

import net.maunium.Maunsic.Actions.Util.TickAction;
import net.maunium.Maunsic.Util.MaunsiConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;

public class ActionFastbow implements TickAction {
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
		return new String[] { "Fastbow " + EnumChatFormatting.GREEN + "ON" };
	}
	
	@Override
	public void execute() {
		EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
		if (p.inventory.getCurrentItem() != null && p.inventory.getCurrentItem().getItem() instanceof ItemBow
				&& Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown()) {
			Minecraft.getMinecraft().playerController.sendUseItem(p, Minecraft.getMinecraft().theWorld, p.inventory.getCurrentItem());
			p.inventory.getCurrentItem().getItem().onItemRightClick(p.inventory.getCurrentItem(), Minecraft.getMinecraft().theWorld, p);
			for (int i = 0; i < 20; i++)
				p.sendQueue.addToSendQueue(new C03PacketPlayer(false));
			Minecraft.getMinecraft().getNetHandler()
					.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
			p.inventory.getCurrentItem().getItem().onPlayerStoppedUsing(p.inventory.getCurrentItem(), Minecraft.getMinecraft().theWorld, p, 10);
		}
	}
	
}
