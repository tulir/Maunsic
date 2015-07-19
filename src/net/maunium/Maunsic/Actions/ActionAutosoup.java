package net.maunium.Maunsic.Actions;

import java.util.Random;

import net.maunium.Maunsic.Actions.Util.IntervalAction;
import net.maunium.Maunsic.Util.MaunsiConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;

/**
 * Autosoup v3 with functional legit mode.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class ActionAutosoup extends IntervalAction {
	public boolean fullRefill = false, randomizeSoup = false, randomizeRefill = false;
	private int prevSlot = 0;
	private Task task = Task.CHECKSTATUS;
	private Random r = new Random(System.currentTimeMillis());
	
	@Override
	public void executeInterval() {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayerSP p = mc.thePlayer;
		if (task == Task.CHECKSTATUS) {
			// If there is no soup in the player inventory ignore everything.
			if (countSoup() == 0) return;
			int emptySlot = -1;
			// Save the previous slot the player had selected.
			prevSlot = p.inventory.currentItem;
			
			// Loop through the hotbar slots.
			for (int i = 0; i < 9; i++) {
				// Get the itemstack in the slot.
				ItemStack is = p.inventory.getStackInSlot(i);
				// Check if it's empty or doesn't have soup in.
				if (is == null || is.getItem() == null || is.stackSize == 0) {
					// Empty slot, save it.
					if (emptySlot == -1) emptySlot = i;
					// and then ignore it.
					continue;
				} else if (!is.getItem().equals(Items.mushroom_stew)) continue; // No soup, just ignore it.
				
				// If the randomize soup flag is set, the hotbar slot isn't the last slot and the appropriate randomness came, try to hop over the slot.
				if (randomizeSoup && i != 8 && r.nextInt(5) == 2) {
					is = p.inventory.getStackInSlot(i + 1);
					// If there is soup in the slot, confirm the hop over.
					if (is != null && is.getItem() != null && is.stackSize != 0 && is.getItem().equals(Items.mushroom_stew)) i++;
					// Otherwise just cancel the hop over.
				}
				
				// Check if the player is low on health.
				// This is done at this point so that the hotbar can be refilled even when the player is at full health.
				if (p.getHealth() < p.getMaxHealth() - 7) {
					// If yes, select the specified slot in the hotbar...
					p.inventory.currentItem = i;
					// ... and move to the next task.
					task = Task.EATSOUP;
				}
				
				// Skip the next step of this task (refilling), since soup was found.
				return;
			}
			
			// If the full refill flag has been set, move to the full refill task.
			if (fullRefill) task = Task.FULLREFILL;
			// Otherwise just refill a single soup.
			else {
				// Loop through the inventory.
				for (int i = 9; i < 36; i++) {
					ItemStack is = p.inventory.getStackInSlot(i);
					// Check if there is soup in the slot
					if (is != null && is.getItem() != null && is.getItem().equals(Items.mushroom_stew) && is.stackSize != 0) {
						// Fake a click of the soup slot...
						mc.playerController.windowClick(0, i, 0, 0, p);
						// ... and then fake a click of the hotbar slot that is empty.
						mc.playerController.windowClick(0, emptySlot + 36, 0, 0, p);
						// Nothing more needs to be done, return.
						return;
					}
				}
			}
		} else if (task == Task.EATSOUP) {
			// Send a use item packet...
			if (p.inventory.getCurrentItem() != null) mc.playerController.sendUseItem(p, p.worldObj, p.inventory.getCurrentItem());
			// ... and move to the next task.
			task = Task.DROPSOUP;
		} else if (task == Task.DROPSOUP) {
			// Send a drop item packet...
			p.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ALL_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
			// ... select the previously selected slot...
			p.inventory.currentItem = prevSlot;
			// ... and move to the first task
			task = Task.CHECKSTATUS;
		} else if (task == Task.FULLREFILL) {
			int soupIn = -1;
			// Loop through the inventory to find soup.
			for (int i = 9; i < 36; i++) {
				ItemStack is = p.inventory.getStackInSlot(i);
				// Check if the stack in the slot is soup.
				if (is != null && is.getItem() != null && is.getItem().equals(Items.mushroom_stew) && is.stackSize != 0) {
					// Save the slot...
					soupIn = i;
					// ... and stop searching.
					break;
				}
			}
			// If no soup found, go back to the check status task.
			if (soupIn == -1) task = Task.CHECKSTATUS;
			
			// If refilling should be randomized and the appropriate randomization matched, try to move one slot right or down.
			if (randomizeRefill && r.nextInt(3) == 1) {
				int ns = -1;
				if (soupIn < 27 && r.nextBoolean()) ns = soupIn + 9;
				else if (soupIn % 9 != 8) ns = soupIn + 1;
				if (ns != -1) {
					ItemStack is = p.inventory.getStackInSlot(ns);
					if (is != null && is.getItem() != null && is.getItem().equals(Items.mushroom_stew) && is.stackSize != 0) soupIn = ns;
				}
			}
			
			// Loop through the hotbar to find an empty slot
			for (int i = 0; i < 9; i++) {
				ItemStack is = p.inventory.getStackInSlot(i);
				if (is == null || is.getItem() == null || is.stackSize == 0) {
					// Move the soup found earlier to the empty slot
					mc.playerController.windowClick(0, soupIn, 0, 0, p);
					mc.playerController.windowClick(0, i + 36, 0, 0, p);
					// Return to keep the task same. This task will keep executing till there's no soup or the hotbar is full.
					return;
				}
			}
			// Hotbar full, go to first task
			task = Task.CHECKSTATUS;
		}
	}
	
	@Override
	public String[] getStatusText() {
		String[] rtrn = new String[4];
		rtrn[0] = "Autosoup " + EnumChatFormatting.GREEN + "ON";
		if (fullRefill) rtrn[1] = " Legit mode " + EnumChatFormatting.GREEN + "ON";
		else rtrn[1] = null;
		if (interval != 20) rtrn[2] = " Tick delay: " + EnumChatFormatting.AQUA + interval;
		else rtrn[2] = null;
		int soup = countSoup();
		
		if (soup > 18) rtrn[3] = EnumChatFormatting.GREEN + " " + soup + " Soup left";
		else if (soup > 8) rtrn[3] = EnumChatFormatting.YELLOW + " " + soup + " Soup left";
		else if (soup > 0) rtrn[3] = EnumChatFormatting.RED + " " + soup + " Soup left";
		else rtrn[3] = EnumChatFormatting.DARK_RED + " " + EnumChatFormatting.BOLD + "No soup left!";
		return rtrn;
	}
	
	public int countSoup() {
		int soup = 0;
		for (ItemStack is : Minecraft.getMinecraft().thePlayer.inventory.mainInventory) {
			if (is == null) continue;
			if (is.getItem() == null) continue;
			if (is.getItem().equals(Items.mushroom_stew)) soup++;
		}
		return soup;
	}
	
	@Override
	public void saveData(MaunsiConfig conf) {
		conf.set("actions.autosoup.interval", interval);
		conf.set("actions.autosoup.fullrefill", fullRefill);
		conf.set("actions.autosoup.randomize.soup", randomizeSoup);
		conf.set("actions.autosoup.randomize.refill", randomizeRefill);
	}
	
	@Override
	public void loadData(MaunsiConfig conf) {
		interval = conf.getInt("actions.autosoup.interval", interval);
		fullRefill = conf.getBoolean("actions.autosoup.fullrefill", fullRefill);
		randomizeSoup = conf.getBoolean("actions.autosoup.randomize.soup", randomizeSoup);
		randomizeRefill = conf.getBoolean("actions.autosoup.randomize.refill", randomizeRefill);
	}
	
	public int getInterval() {
		return interval;
	}
	
	public void setInterval(int i) {
		interval = i;
	}
	
	public static enum Task {
		CHECKSTATUS, EATSOUP, DROPSOUP, FULLREFILL;
	}
}
