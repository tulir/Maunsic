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
	private boolean fullRefill = false, randomizeSoup = false, randomizeRefill = false;
	private int prevSlot = 0;
	private Task task = Task.CHECKSTATUS;
	private Random r = new Random(System.currentTimeMillis());
	
	@Override
	public void executeInterval() {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayerSP p = mc.thePlayer;
		if (task == Task.CHECKSTATUS) {
			int emptySlot = -1;
			prevSlot = p.inventory.currentItem;
			
			for (int i = 0; i < 9; i++) {
				ItemStack is = p.inventory.getStackInSlot(i);
				if (is == null || is.getItem() == null || is.stackSize == 0) {
					if (emptySlot == -1) emptySlot = i;
					continue;
				} else if (!is.getItem().equals(Items.mushroom_stew)) continue;
				
				if (randomizeSoup && r.nextInt(3) == 2) {
					is = p.inventory.getStackInSlot(i + 1);
					if (is != null && is.getItem() != null && is.stackSize != 0 && is.getItem().equals(Items.mushroom_stew)) i++;
				}
				
				if (p.getHealth() < p.getMaxHealth() - 7) {
					p.inventory.currentItem = i;
					task = Task.EATSOUP;
				}
				return;
			}
			
			if (fullRefill) task = Task.LEGITREFILL;
			else {
				for (int i = 9; i < 36; i++) {
					ItemStack is = p.inventory.getStackInSlot(i);
					if (is != null && is.getItem() != null && is.getItem().equals(Items.mushroom_stew) && is.stackSize != 0) {
						mc.playerController.windowClick(0, i, 0, 0, p);
						mc.playerController.windowClick(0, emptySlot + 36, 0, 0, p);
						return;
					}
				}
			}
		} else if (task == Task.EATSOUP) {
			if (p.inventory.getCurrentItem() != null) mc.playerController.sendUseItem(p, p.worldObj, p.inventory.getCurrentItem());
			task = Task.DROPSOUP;
		} else if (task == Task.DROPSOUP) {
			p.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ALL_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
			p.inventory.currentItem = prevSlot;
			task = Task.CHECKSTATUS;
		} else if (task == Task.LEGITREFILL) {
			int soupIn = -1;
			for (int i = 9; i < 36; i++) {
				ItemStack is = p.inventory.getStackInSlot(i);
				if (is != null && is.getItem() != null && is.getItem().equals(Items.mushroom_stew) && is.stackSize != 0) {
					soupIn = i;
					break;
				}
			}
			if (soupIn == -1) task = Task.CHECKSTATUS;
			
			if (randomizeRefill && r.nextInt(2) == 1) {
				ItemStack is = p.inventory.getStackInSlot(soupIn + 9);
				if (is != null && is.getItem() != null && is.getItem().equals(Items.mushroom_stew) && is.stackSize != 0) soupIn += 9;
			}
			
			for (int i = 0; i < 9; i++) {
				ItemStack is = p.inventory.getStackInSlot(i);
				if (is == null || is.getItem() == null || is.stackSize == 0) {
					mc.playerController.windowClick(0, soupIn, 0, 0, p);
					mc.playerController.windowClick(0, i + 36, 0, 0, p);
					return;
				}
			}
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
	
	public static enum Task {
		CHECKSTATUS, EATSOUP, DROPSOUP, LEGITREFILL;
	}
}
