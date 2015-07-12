package mauluam;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;

/**
 * MauluaM library to interact with the world.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class MauWorldLib extends TwoArgFunction {
	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaValue lib = LuaValue.tableOf();
		lib.set("move", new move());
		lib.set("getBlockType", new getBlockType());
		env.set("world", lib);
		return lib;
	}
	
	public static class getBlockType extends ThreeArgFunction {
		@Override
		public LuaValue call(LuaValue x, LuaValue y, LuaValue z) {
			return LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.worldObj.getBlockState(new BlockPos(x.toint(), y.toint(), z.toint())).getBlock()
					.getUnlocalizedName().substring(5));
		}
	}
	
	public static class move extends VarArgFunction {
		@Override
		public LuaValue invoke(Varargs args) {
			if (args.narg() < 2) return LuaValue.valueOf(1);
			int direction = args.toint(1);
			int blocks = args.toint(2);
			int failTime;
			if (args.narg() > 2) failTime = args.toint(3);
			else failTime = 1000;
			
			EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
			if (blocks <= 0) LuaValue.valueOf(2);
			
			long st = Minecraft.getSystemTime();
			KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), true);
			if (direction == 0) {
				double sZ = p.posZ;
				while (p.posZ < sZ + blocks)
					if (Minecraft.getSystemTime() - blocks * failTime > st) break;
			} else if (direction == 1) {
				double sX = p.posX;
				while (p.posX > sX - blocks)
					if (Minecraft.getSystemTime() - blocks * failTime > st) break;
			} else if (direction == 2) {
				double sZ = p.posZ;
				while (p.posZ > sZ - blocks)
					if (Minecraft.getSystemTime() - blocks * failTime > st) break;
			} else if (direction == 3) {
				double sX = p.posX;
				while (p.posX < sX + blocks)
					if (Minecraft.getSystemTime() - blocks * failTime > st) break;
			} else {
				KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), false);
				return LuaValue.valueOf(3);
			}
			KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), false);
			return LuaValue.valueOf(0);
		}
	}
}
