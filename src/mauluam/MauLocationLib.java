package mauluam;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import net.maunium.Maunsic.Maunsic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class MauLocationLib extends TwoArgFunction {
	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaValue lib = LuaValue.tableOf();
		lib.set("getX", new getX());
		lib.set("getY", new getY());
		lib.set("getZ", new getZ());
		lib.set("getYaw", new getYaw());
		lib.set("getPitch", new getPitch());
		lib.set("setYaw", new setYaw());
		lib.set("setPitch", new setPitch());
		lib.set("setLook", new setLook());
		lib.set("setLookSmooth", new setLookSmooth());
		env.set("location", lib);
		return lib;
	}
	
	public static class setLookSmooth extends VarArgFunction {
		@Override
		public LuaValue invoke(Varargs args) {
			if (args.narg() < 3) return LuaValue.ONE;
			
			final float yaw = args.tofloat(1);
			final float pitch = args.tofloat(2);
			final float ms = args.tofloat(3);
			boolean vertmp;
			if (args.narg() > 4) vertmp = args.toboolean(5);
			else vertmp = false;
			final boolean verify = vertmp;
			
			if (args.narg() > 3 && args.toboolean(4)) {
				Thread t = new Thread() {
					@Override
					public void run() {
						EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
						double yawPerMs = yaw / ms;
						double pitchPerMs = pitch / ms;
						for (int i = 0; i < ms; i++) {
							p.rotationYaw += yawPerMs;
							p.rotationPitch += pitchPerMs;
							try {
								Thread.sleep(1);
							} catch (InterruptedException e) {
								Maunsic.getLogger().catching(e);
							}
						}
						if (verify) {
							p.rotationYaw = yaw;
							p.rotationPitch = pitch;
						}
					}
				};
				t.setName("MauLuaM|setLookSmooth");
				t.start();
				return LuaValue.ZERO;
			} else {
				EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
				double yawPerMs = yaw / ms;
				double pitchPerMs = pitch / ms;
				for (int i = 0; i < ms; i++) {
					p.rotationYaw += yawPerMs;
					p.rotationPitch += pitchPerMs;
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (verify) {
					p.rotationYaw = yaw;
					p.rotationPitch = pitch;
				}
				return LuaValue.ZERO;
			}
		}
	}
	
	public static class setLook extends TwoArgFunction {
		@Override
		public LuaValue call(LuaValue yaw, LuaValue pitch) {
			Minecraft.getMinecraft().thePlayer.rotationYaw = yaw.tofloat();
			Minecraft.getMinecraft().thePlayer.rotationPitch = pitch.tofloat();
			return LuaValue.NIL;
		}
	}
	
	public static class setPitch extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue pitch) {
			Minecraft.getMinecraft().thePlayer.rotationPitch = pitch.tofloat();
			return LuaValue.NIL;
		}
	}
	
	public static class setYaw extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue yaw) {
			Minecraft.getMinecraft().thePlayer.rotationYaw = yaw.tofloat();
			return LuaValue.NIL;
		}
	}
	
	public static class getPitch extends ZeroArgFunction {
		@Override
		public LuaValue call() {
			return LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.rotationPitch);
		}
	}
	
	public static class getYaw extends ZeroArgFunction {
		@Override
		public LuaValue call() {
			return LuaValue.valueOf(Math.abs(Minecraft.getMinecraft().thePlayer.rotationYaw) % 360);
		}
	}
	
	public static class getZ extends ZeroArgFunction {
		@Override
		public LuaValue call() {
			return LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.posZ);
		}
	}
	
	public static class getY extends ZeroArgFunction {
		@Override
		public LuaValue call() {
			return LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.posY);
		}
	}
	
	public static class getX extends ZeroArgFunction {
		@Override
		public LuaValue call() {
			return LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.posX);
		}
	}
}