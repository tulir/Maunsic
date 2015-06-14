package net.maunium.Maunsic.TickActions;

import org.lwjgl.input.Keyboard;

import net.maunium.Maunsic.Maunsic;

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
	private Maunsic host;
	public static final int DEFAULT_SPEED = 20, MAX_SPEED = 980, MIN_SPEED = 0, TYPE_DISABLED = 0, TYPE_FLY = 1, TYPE_WALK = 2;
	private int speed = DEFAULT_SPEED, type = TYPE_DISABLED, jump = 0;
	
	public ActionFly(Maunsic host) {
		this.host = host;
		setSpeed(host.getConfig().getInt("altmovement.speed", DEFAULT_SPEED));
		setJump(host.getConfig().getInt("altmovement.speed", 0));
	}
	
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
		if (TYPE_DISABLED <= type && type <= TYPE_WALK) this.type = type;
	}
	
	public void setJump(int jump) {
		this.jump = jump;
	}
	
	public void setSpeed(int speed) {
		if (MIN_SPEED <= getSpeed() && getSpeed() <= MAX_SPEED) this.speed = speed;
	}
	
	public void changeSpeed(boolean increase) {
		int amount;
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
				if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) amount = 100;
				else amount = 50;
			} else amount = 20;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) amount = 1;
			else amount = 5;
		} else amount = 10;
		
		int res = getSpeed() + (increase ? amount : -amount);
		if (amount > ActionFly.MAX_SPEED) {
			Maunsic.printChatError("message.speed.toohigh", ActionFly.MAX_SPEED);
			setSpeed(ActionFly.MAX_SPEED);
		} else if (amount < ActionFly.MIN_SPEED) {
			Maunsic.printChatError("message.speed.toolow", ActionFly.MIN_SPEED);
			setSpeed(ActionFly.MIN_SPEED);
		} else setSpeed(res);
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public double getMCSpeed() {
		return getSpeed() / 100;
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
		else extra = (p.isSprinting() ? getMCSpeed() / 5 : 0) - (p.isSneaking() ? getMCSpeed() / 5 : 0);
		
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
			mX = Math.cos(Math.toRadians(yaw)) * (getMCSpeed() + extra);
			mZ = Math.sin(Math.toRadians(yaw)) * (getMCSpeed() + extra);
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
			mX = Math.cos(Math.toRadians(yaw)) * (getMCSpeed() + extra);
			mZ = Math.sin(Math.toRadians(yaw)) * (getMCSpeed() + extra);
		} else if (Keyboard.isKeyDown(gs.keyBindRight.getKeyCode())) {
			mX = -Math.cos(Math.toRadians(yaw)) * (getMCSpeed() + extra);
			mZ = -Math.sin(Math.toRadians(yaw)) * (getMCSpeed() + extra);
		} else if (Keyboard.isKeyDown(gs.keyBindLeft.getKeyCode())) {
			mX = Math.cos(Math.toRadians(yaw)) * (getMCSpeed() + extra);
			mZ = Math.sin(Math.toRadians(yaw)) * (getMCSpeed() + extra);
		} else if (type == TYPE_FLY) {
			mX = 0;
			mZ = 0;
		}
		
		if (type == TYPE_FLY) {
			if (Keyboard.isKeyDown(gs.keyBindJump.getKeyCode())) {
				if (Keyboard.isKeyDown(gs.keyBindSneak.getKeyCode())) mY = 0;
				else mY = getMCSpeed();
			} else if (Keyboard.isKeyDown(gs.keyBindSneak.getKeyCode())) mY = -getMCSpeed();
			else mY = 0;
		} else if (Keyboard.isKeyDown(gs.keyBindJump.getKeyCode())) if (jump == 0) mY = getMCSpeed() * 2;
		else mY = jump;
		
		p.setVelocity(mX, mY, mZ);
	}
	
	private String amv = "Alt. Movement ", mode = " Mode ", fly = "Fly", walk = "Walk", speedt = " Speed ", on = "ON";
	
	public void setStatusI18n(String amv, String mode, String fly, String walk, String speed, String on) {
		this.amv = amv + " ";
		this.mode = " " + mode + " ";
		this.fly = fly;
		this.walk = walk;
		speedt = " " + speed + " ";
		this.on = on;
	}
	
	@Override
	public String[] getStatusText() {
		String[] rtrn = new String[3];
		rtrn[0] = amv + EnumChatFormatting.GREEN + on;
		rtrn[1] = mode + EnumChatFormatting.GREEN + (type == TYPE_FLY ? fly : walk);
		if (getSpeed() > MAX_SPEED / 2) rtrn[2] = speedt + EnumChatFormatting.RED + EnumChatFormatting.ITALIC + getSpeed();
		else if (getSpeed() > MAX_SPEED / 4) rtrn[2] = speedt + EnumChatFormatting.GREEN + EnumChatFormatting.ITALIC + getSpeed();
		else rtrn[2] = speedt + EnumChatFormatting.GREEN + getSpeed();
		return rtrn;
	}
}
