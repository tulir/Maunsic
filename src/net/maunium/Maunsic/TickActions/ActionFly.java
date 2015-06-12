package net.maunium.Maunsic.TickActions;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

/**
 * The TickAction for allowing flight.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class ActionFly implements TickAction {
	public static final int DEFAULT_SPEED = 20, MAX_SPEED = 980, MIN_SPEED = 0, TYPE_DISABLED = 0, TYPE_FLY = 1, TYPE_WALK = 2;
	private int speed = DEFAULT_SPEED, type = TYPE_DISABLED, jump = 0;
	
	@Override
	public String getName() {
		return "Fly";
	}
	
	@Override
	public boolean isActive() {
		return type > TYPE_DISABLED;
	}
	
	@Override
	public void setActive(boolean active) {
		if (active) type = TYPE_FLY;
		else type = TYPE_DISABLED;
	}
	
	public void setType(int type) {
		if (TYPE_DISABLED <= type && type >= TYPE_WALK) this.type = type;
	}
	
	public void setJump(int jump) {
		this.jump = jump;
	}
	
	public void setSpeed(int speed) {
		if (MIN_SPEED <= speed && speed >= MAX_SPEED) this.speed = speed;
	}
	
	@Override
	public void execute() {
		if (type == TYPE_DISABLED) return;
		GameSettings gs = Minecraft.getMinecraft().gameSettings;
		EntityPlayer p = Minecraft.getMinecraft().thePlayer;
		
		if (!Minecraft.getMinecraft().inGameHasFocus) {
			if (type == TYPE_FLY) {
				p.motionX = 0;
				p.motionY = 0;
				p.motionZ = 0;
			}
			return;
		}
		
		double extra = 0;
		if (type == TYPE_FLY) p.setSprinting(false);
		else extra = (p.isSprinting() ? speed / 100 / 5 : 0) - (p.isSneaking() ? speed / 100 / 5 : 0);
		
		double mX = p.motionX;
		double mY = p.motionY;
		double mZ = p.motionZ;
		float yaw = p.rotationYaw % 360;
		
		if (Keyboard.isKeyDown(gs.keyBindForward.getKeyCode())) {
			if (Keyboard.isKeyDown(gs.keyBindRight.getKeyCode())) {
				yaw += 135;
				if (yaw > 360) yaw -= 360;
			} else if (Keyboard.isKeyDown(gs.keyBindLeft.getKeyCode())) {
				yaw += 45;
				if (yaw > 360) yaw -= 360;
			} else {
				yaw += 90;
				if (yaw > 360) yaw -= 360;
			}
			mX = Math.cos(Math.toRadians(yaw)) * (speed / 100 + extra);
			mZ = Math.sin(Math.toRadians(yaw)) * (speed / 100 + extra);
		} else if (Keyboard.isKeyDown(gs.keyBindBack.getKeyCode())) {
			if (Keyboard.isKeyDown(gs.keyBindRight.getKeyCode())) {
				yaw -= 135;
				if (yaw > 360) yaw += 360;
			} else if (Keyboard.isKeyDown(gs.keyBindLeft.getKeyCode())) {
				yaw -= 45;
				if (yaw > 360) yaw += 360;
			} else {
				yaw -= 90;
				if (yaw > 360) yaw += 360;
			}
			mX = Math.cos(Math.toRadians(yaw)) * (speed / 100 + extra);
			mZ = Math.sin(Math.toRadians(yaw)) * (speed / 100 + extra);
		} else if (Keyboard.isKeyDown(gs.keyBindRight.getKeyCode())) {
			mX = -Math.cos(Math.toRadians(yaw)) * (speed / 100 + extra);
			mZ = -Math.sin(Math.toRadians(yaw)) * (speed / 100 + extra);
		} else if (Keyboard.isKeyDown(gs.keyBindLeft.getKeyCode())) {
			mX = Math.cos(Math.toRadians(yaw)) * (speed / 100 + extra);
			mZ = Math.sin(Math.toRadians(yaw)) * (speed / 100 + extra);
		} else if (type == TYPE_FLY) {
			mX = 0;
			mZ = 0;
		}
		
		if (type == TYPE_FLY) {
			if (Keyboard.isKeyDown(gs.keyBindJump.getKeyCode())) {
				if (Keyboard.isKeyDown(gs.keyBindSneak.getKeyCode())) mY = 0;
				else mY = speed / 100;
			} else if (Keyboard.isKeyDown(gs.keyBindSneak.getKeyCode())) mY = -(speed / 100);
			else mY = 0;
		} else if (Keyboard.isKeyDown(gs.keyBindJump.getKeyCode())) if (jump == 0) mY = speed / 100 * 2;
		else mY = jump;
		
		p.setVelocity(mX, mY, mZ);
	}
	
	@Override
	public String[] getStatusText() {
		String[] rtrn = new String[3];
		rtrn[0] = "Alt. Movement " + EnumChatFormatting.GREEN + "ON";
		rtrn[1] = " Mode " + EnumChatFormatting.GREEN + (type == TYPE_FLY ? "Fly" : "Walk");
		if (speed > MAX_SPEED / 2) rtrn[2] = " Speed " + EnumChatFormatting.RED + "" + EnumChatFormatting.ITALIC + speed;
		else if (speed > MAX_SPEED / 4) rtrn[2] = " Speed " + EnumChatFormatting.GREEN + "" + EnumChatFormatting.ITALIC + speed;
		else rtrn[2] = " Speed " + EnumChatFormatting.GREEN + "" + speed;
		return rtrn;
	}
}
