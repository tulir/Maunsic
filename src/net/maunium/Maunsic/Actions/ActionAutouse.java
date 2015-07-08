package net.maunium.Maunsic.Actions;

import net.maunium.Maunsic.Actions.Util.IntervalAction;
import net.maunium.Maunsic.Util.MaunsiConfig;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;

public class ActionAutouse extends IntervalAction {
	public double range = 5.0D;
	
	@Override
	public String[] getStatusText() {
		return new String[] { "Autouse " + EnumChatFormatting.GREEN + "ON" };
	}
	
	@Override
	public void saveData(MaunsiConfig conf) {
		interval = conf.getInt("autouse.interval", interval);
		range = conf.getDouble("autouse.range", range);
	}
	
	@Override
	public void loadData(MaunsiConfig conf) {
		conf.set("autouse.interval", interval);
		conf.set("autouse.range", range);
	}
	
	@Override
	public void executeInterval() {
		EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
		PlayerControllerMP pc = Minecraft.getMinecraft().playerController;
		MovingObjectPosition moprt = p.rayTrace(range, 1);
		if (moprt != null && !p.worldObj.getBlockState(new BlockPos(moprt.hitVec)).getBlock().getMaterial().equals(Material.air)) pc.func_178890_a(p,
				Minecraft.getMinecraft().theWorld, p.inventory.getCurrentItem(), new BlockPos(moprt.hitVec), moprt.sideHit, moprt.hitVec);
		else if (p.inventory.getCurrentItem() != null) pc.sendUseItem(p, p.worldObj, p.inventory.getCurrentItem());
	}
	
}
