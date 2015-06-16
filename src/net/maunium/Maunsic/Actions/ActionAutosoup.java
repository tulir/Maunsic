package net.maunium.Maunsic.Actions;

import java.util.Random;

import net.maunium.Maunsic.Actions.Util.IntervalAction;
import net.maunium.Maunsic.Util.MaunsiConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.util.EnumChatFormatting;

/**
 * Updated version of AutoSoup.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class ActionAutosoup extends IntervalAction {
	private boolean legit = false, active = false;
	private int nextTask = 0, taskModifier = 0, nextSlot = 0, prevSlot = 0, emptySlots = 0;
	private Random mslr = new Random(System.currentTimeMillis());
	
	@Override
	public void executeInterval() {
		EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
		if (nextTask == 0) {
			prevSlot = p.inventory.currentItem;
			for (int i = 0; i < 9; i++) {
				if (legit && i != 8 && mslr.nextInt(5) == 3) i++;
				if (p.inventory.getStackInSlot(i) == null || p.inventory.getStackInSlot(i).getItem() == null || p.inventory.getStackInSlot(i).stackSize == 0
						|| !p.inventory.getStackInSlot(i).getItem().equals(Items.mushroom_stew)) continue;
				
				if (p.getHealth() < p.getMaxHealth() - 7) {
					p.inventory.currentItem = i;
					nextTask = 1;
				}
				return;
			}
			if (taskModifier == 1) {
				for (int i = 9; i < 36; i++) {
					ItemStack is = p.inventory.getStackInSlot(i);
					if (is != null && is.getItem() != null && is.getItem().equals(Items.mushroom_stew) && is.stackSize != 0) {
						p.inventoryContainer.transferStackInSlot(p, i);
						p.sendQueue.addToSendQueue(new C0EPacketClickWindow(0, i, 0, 1, p.inventory.mainInventory[i], (short) i));
						emptySlots--;
						if (emptySlots == 0) taskModifier = 0;
						else taskModifier = 2;
						return;
					}
				}
				taskModifier = 0;
			} else if (taskModifier == 2) taskModifier++;
			else if (taskModifier == 3) taskModifier++;
			else if (taskModifier == 4) taskModifier++;
			else if (taskModifier == 5) taskModifier = 1;
			
			if (legit) {
				emptySlots = 0;
				for (int i = 0; i < 9; i++) {
					ItemStack is = p.inventory.getStackInSlot(nextSlot);
					if (is == null || is.getItem() == null || is.stackSize == 0) emptySlots++;
				}
				taskModifier++;
			} else {
				for (int i = 9; i < 36; i++) {
					ItemStack is = p.inventory.getStackInSlot(i);
					if (is != null && is.getItem() != null && is.getItem().equals(Items.mushroom_stew) && is.stackSize != 0) {
						p.inventoryContainer.transferStackInSlot(p, i);
						p.sendQueue.addToSendQueue(new C0EPacketClickWindow(0, i, 0, 1, p.inventory.mainInventory[i], (short) i));
						return;
					}
				}
			}
		} else if (nextTask == 1) {
			if (p.inventory.getCurrentItem() != null) Minecraft.getMinecraft().playerController.sendUseItem(p, p.worldObj, p.inventory.getCurrentItem());
			nextTask = 2;
		} else if (nextTask == 2) {
			p.dropOneItem(true);
			p.inventory.currentItem = prevSlot;
			nextTask = 0;
		} else if (nextTask == 10) {
			emptySlots = 0;
			for (int i = 0; i < 9; i++) {
				ItemStack is = p.inventory.getStackInSlot(nextSlot);
				if (is == null || is.getItem() == null || is.stackSize == 0) emptySlots++;
			}
			nextTask = 12;
		} else if (nextTask == 12) {
			for (int i = 9; i < 36; i++) {
				ItemStack is = p.inventory.getStackInSlot(i);
				if (is != null && is.getItem() != null && is.getItem().equals(Items.mushroom_stew) && is.stackSize != 0) {
					p.inventoryContainer.transferStackInSlot(p, i);
					p.sendQueue.addToSendQueue(new C0EPacketClickWindow(0, i, 0, 1, p.inventory.mainInventory[i], (short) i));
					emptySlots--;
					if (emptySlots == 0) nextTask = 0;
					else nextTask = 13;
					break;
				}
			}
		} else if (nextTask == 13) nextTask = 14;
		else if (nextTask == 14) nextTask = 15;
		else if (nextTask == 15) nextTask = 16;
		else if (nextTask == 16) nextTask = 12;
	}
	
	@Override
	public String[] getStatusText() {
		String[] rtrn = new String[4];
		rtrn[0] = "Autosoup " + EnumChatFormatting.GREEN + "ON";
		if (legit) rtrn[1] = " Legit mode " + EnumChatFormatting.GREEN + "ON";
		else rtrn[1] = null;
		if (interval != 20) rtrn[2] = " Tick delay: " + EnumChatFormatting.AQUA + interval;
		else rtrn[2] = null;
		int soup = 0;
		
		for (ItemStack is : Minecraft.getMinecraft().thePlayer.inventory.mainInventory) {
			if (is == null) continue;
			if (is.getItem() == null) continue;
			if (is.getItem().equals(Items.mushroom_stew)) soup++;
		}
		
		if (soup > 18) rtrn[3] = EnumChatFormatting.GREEN + " " + soup + " Soup left";
		else if (soup > 8) rtrn[3] = EnumChatFormatting.YELLOW + " " + soup + " Soup left";
		else if (soup > 0) rtrn[3] = EnumChatFormatting.RED + " " + soup + " Soup left";
		else rtrn[3] = EnumChatFormatting.DARK_RED + " " + EnumChatFormatting.BOLD + "No soup left!";
		return rtrn;
	}
	
	@Override
	public boolean isActive() {
		return active;
	}
	
	@Override
	public void setActive(boolean active) {
		this.active = active;
	}
	
	@Override
	public void saveData(MaunsiConfig conf) {
		conf.set("actions.autosoup.legit", legit);
		conf.set("actions.autosoup.interval", interval);
	}
	
	@Override
	public void loadData(MaunsiConfig conf) {
		legit = conf.getBoolean("actions.autosoup.legit", legit);
		interval = conf.getInt("actions.autosoup.interval", interval);
	}
}
