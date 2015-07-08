package net.maunium.Maunsic.Actions;

import net.maunium.Maunsic.Actions.Util.TickAction;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;

/**
 * Fastbow. Part of the code from Wurst Client licenced under MPL v2.0 by Alexander01998.
 * 
 * @author Tulir293
 * @author Alexander01998
 * @since 0.1
 */
public class ActionFastbow extends TickAction {
	@Override
	public String[] getStatusText() {
		return new String[] { "Fastbow " + EnumChatFormatting.GREEN + "ON" };
	}
	
	@Override
	public void execute() {
		EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
		// Make sure the player has a bow in the hand and is pressing the use button.
		if (p.inventory.getCurrentItem() != null && p.inventory.getCurrentItem().getItem() instanceof ItemBow
				&& Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown()) {
			// Send use item packet.
			Minecraft.getMinecraft().playerController.sendUseItem(p, Minecraft.getMinecraft().theWorld, p.inventory.getCurrentItem());
			// Send an item right click event.
			p.inventory.getCurrentItem().getItem().onItemRightClick(p.inventory.getCurrentItem(), Minecraft.getMinecraft().theWorld, p);
			// Spam player position packets which claim that the player is not on the ground.
			for (int i = 0; i < 20; i++)
				p.sendQueue.addToSendQueue(new C03PacketPlayer(false));
			// Send a release using item packet.
			Minecraft.getMinecraft().getNetHandler()
					.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
			// Send an release using item event.
			p.inventory.getCurrentItem().getItem().onPlayerStoppedUsing(p.inventory.getCurrentItem(), Minecraft.getMinecraft().theWorld, p, 10);
		}
	}
	
}
