package net.maunium.Maunsic.Actions;

import java.util.Random;

import net.maunium.Maunsic.Actions.Util.IntervalAction;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;

public class ActionAutoattack extends IntervalAction {
	private boolean active = false;
	private long lastAttack = 0;
	private Random r = new Random(System.nanoTime());
	private int delay = r.nextInt(250);
	
	@Override
	public String getName() {
		return "Autoattack";
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
	public String[] getStatusText() {
		String[] rtrn = new String[2];
		rtrn[0] = "Autoattack " + EnumChatFormatting.GREEN + "ON";
		if (interval <= 100) rtrn[1] = " Speed: " + EnumChatFormatting.GREEN + EnumChatFormatting.ITALIC + 1000 / interval + " attack/s";
		else rtrn[1] = " Speed: " + EnumChatFormatting.GREEN + 1000 / interval + " attack/s";
		return rtrn;
	}
	
	@Override
	public void executeInterval() {
		if (Minecraft.getSystemTime() - lastAttack < delay) return;
		EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
		PlayerControllerMP pc = Minecraft.getMinecraft().playerController;
		MovingObjectPosition moprt = Minecraft.getMinecraft().objectMouseOver;
		if (moprt == null) return;
		if (moprt.entityHit != null) pc.attackEntity(p, moprt.entityHit);
		else if (Minecraft.getMinecraft().playerController.isInCreativeMode() && moprt != null && moprt.hitVec != null && moprt.sideHit != null)
			pc.func_180511_b(new BlockPos(moprt.hitVec), moprt.sideHit);
//		} else KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode(), true);
		p.swingItem();
		delay = r.nextInt(200) + 50;
		lastAttack = Minecraft.getSystemTime();
	}
}
