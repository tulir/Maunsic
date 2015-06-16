package net.maunium.Maunsic.Actions;

import org.lwjgl.input.Keyboard;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Actions.Util.StatusAction;
import net.maunium.Maunsic.Server.ServerHandler;
import net.maunium.Maunsic.Util.MaunsiConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

/**
 * Phasing through nonsolid blocks
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class ActionPhase implements StatusAction {
	private boolean active = false, automated = true, autoforward = true;
	
	public void phase() {
		// Is shift down? If yes, stop phasing
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) stopPhase();
		// Start automated phasing
		else if (automated) autophase();
		// Do a single manual phase
		else manualphase();
	}
	
	public void manualphase() {
		EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
		int look = MathHelper.floor_double(p.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		phase(p, look);
	}
	
	private PhaseThread pt = null;
	
	public boolean isPhasing() {
		return pt != null && pt.isAlive();
	}
	
	@SuppressWarnings("deprecation")
	public void autophase() {
		if (!ServerHandler.canUse()) return;
		if (pt != null && pt.isAlive()) {
			pt.stop();
			pt = null;
		}
		pt = new PhaseThread();
		pt.start();
	}
	
	@SuppressWarnings("deprecation")
	public void stopPhase() {
		if (pt != null && pt.isAlive()) {
			pt.stop();
			pt = null;
			KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), false);
		}
	}
	
	public class PhaseThread extends Thread {
		public PhaseThread() {
			super("Phase Thread");
		}
		
		private boolean forwardState = true;
		
		@Override
		public void run() {
			active = true;
			EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
			int look = MathHelper.floor_double(p.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
			double target;
			Maunsic.getLogger().debug("Attempting to phase through block. Direction: F" + look, this);
			if (look == 0) {
				target = p.posZ + 1;
				while (p.posZ < target) {
					phase(p, look);
					phase2();
				}
			} else if (look == 1) {
				target = p.posX - 1;
				while (p.posX > target) {
					phase(p, look);
					phase2();
				}
			} else if (look == 2) {
				target = p.posZ - 1;
				while (p.posZ > target) {
					phase(p, look);
					phase2();
				}
			} else if (look == 3) {
				target = p.posX + 1;
				while (p.posX < target) {
					phase(p, look);
					phase2();
				}
			}
			KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), false);
			Maunsic.getLogger().trace("Phasing complete", this);
			active = true;
		}
		
		private void phase2() {
			if (autoforward) {
				EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
				p.setSprinting(!p.isSprinting());
				KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), forwardState);
				if (forwardState) forwardState = false;
				else forwardState = true;
			}
			sleep();
		}
		
		private void sleep() {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {}
		}
	};
	
	private void phase(EntityPlayerSP p, int dir) {
		if (dir == 0) {
			for (int i = 0; i < 4; i++)
				p.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(p.posX, p.posY, p.posZ + 0.6, p.onGround));
			p.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(p.posX, p.posY - 0.5, p.posZ, p.onGround));
			p.posZ += 2.4;
			p.posY -= 0.5;
		} else if (dir == 1) {
			for (int i = 0; i < 4; i++)
				p.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(p.posX - 0.6, p.posY, p.posZ, p.onGround));
			p.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(p.posX, p.posY - 0.5, p.posZ, p.onGround));
			p.posX -= 2.4;
			p.posY -= 0.5;
		} else if (dir == 2) {
			for (int i = 0; i < 4; i++)
				p.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(p.posX, p.posY, p.posZ - 0.6, p.onGround));
			p.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(p.posX, p.posY - 0.5, p.posZ, p.onGround));
			p.posZ -= 2.4;
			p.posY -= 0.5;
		} else if (dir == 3) {
			for (int i = 0; i < 4; i++)
				p.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(p.posX + 0.6, p.posY, p.posZ, p.onGround));
			p.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(p.posX, p.posY - 0.5, p.posZ, p.onGround));
			p.posX += 2.4;
			p.posY -= 0.5;
		}
	}
	
	@Override
	public String[] getStatusText() {
		String[] rtrn = new String[2];
		rtrn[0] = "Automated Phase " + EnumChatFormatting.GREEN + "ON";
		if (autoforward) rtrn[1] = " Auto-forward: " + EnumChatFormatting.GREEN + "ON";
		else rtrn[1] = " Auto-forward: " + EnumChatFormatting.RED + "OFF";
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
		conf.set("actions.phase.automated", automated);
		conf.set("actions.phase.autoforward", autoforward);
	}
	
	@Override
	public void loadData(MaunsiConfig conf) {
		automated = conf.getBoolean("actions.phase.automated", automated);
		autoforward = conf.getBoolean("actions.phase.autoforward", autoforward);
	}
}
