package mauluam;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import net.minecraft.client.Minecraft;

/**
 * MauluaM library to interact with the Minecraft class.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class MauMcLib extends TwoArgFunction {
	
	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaValue lib = LuaValue.tableOf();
		lib.set("stop", new stop());
		lib.set("getSystemTime", new getSystemTime());
		lib.set("isFocusedIngame", new isFocusedIngame());
		env.set("mc", lib);
		return lib;
	}
	
	public static class stop extends ZeroArgFunction {
		@Override
		public LuaValue call() {
			Minecraft.getMinecraft().shutdown();
			return LuaValue.NIL;
		}
	}
	
	public static class isFocusedIngame extends ZeroArgFunction {
		@Override
		public LuaValue call() {
			return LuaValue.valueOf(Minecraft.getMinecraft().inGameHasFocus);
		}
	}
	
	public static class getSystemTime extends ZeroArgFunction {
		@Override
		public LuaValue call() {
			return LuaValue.valueOf(Minecraft.getSystemTime());
		}
	}
}
