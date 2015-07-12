package mauluam;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import net.maunium.Maunsic.Maunsic;

/**
 * MauluaM library to interact with the Maunsic class.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class MaunsicUtilsLib extends TwoArgFunction {
	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaValue lib = LuaValue.tableOf();
		lib.set("version", new version());
		lib.set("longversion", new longversion());
		env.set("maunsicutils", lib);
		return lib;
	}
	
	public static class version extends ZeroArgFunction {
		@Override
		public LuaValue call() {
			return LuaValue.valueOf(Maunsic.version);
		}
	}
	
	public static class longversion extends ZeroArgFunction {
		@Override
		public LuaValue call() {
			return LuaValue.valueOf(Maunsic.longVersion);
		}
	}
}
