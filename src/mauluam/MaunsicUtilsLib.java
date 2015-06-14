package mauluam;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import net.maunium.Maunsic.Maunsic;

public class MaunsicUtilsLib extends TwoArgFunction {
	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaValue lib = LuaValue.tableOf();
		lib.set("version", new version());
		env.set("maucrosutils", lib);
		return lib;
	}
	
	public static class version extends ZeroArgFunction {
		@Override
		public LuaValue call() {
			return LuaValue.valueOf(Maunsic.version);
		}
	}
}
