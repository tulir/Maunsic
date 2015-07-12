package mauluam;

import java.util.Random;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import com.github.darius.expr.Parser;
import com.github.darius.expr.SyntaxException;

import net.maunium.Maunsic.Maunsic;

import net.minecraft.client.Minecraft;

/**
 * MauluaM library with various mathematical functions.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class MauMathLib extends TwoArgFunction {
	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaValue lib = LuaValue.tableOf();
		lib.set("expr", new expr());
		lib.set("pi", new pi());
		lib.set("e", new e());
		lib.set("random", new random());
		lib.set("round", new round());
		lib.set("sqrt", new sqrt());
		lib.set("toRadians", new toRadians());
		lib.set("toDegrees", new toDegrees());
		lib.set("floor", new floor());
		lib.set("ceil", new ceil());
		lib.set("sin", new sin());
		lib.set("cos", new cos());
		lib.set("tan", new tan());
		lib.set("asin", new asin());
		lib.set("acos", new acos());
		lib.set("cosh", new cosh());
		lib.set("atan", new atan());
		lib.set("atan2", new atan2());
		lib.set("exp", new exp());
		lib.set("log", new log());
		lib.set("pow", new pow());
		env.set("inv", lib);
		return lib;
	}
	
	public static class expr extends VarArgFunction {
		@Override
		public LuaValue invoke(Varargs args) {
			StringBuffer sb = new StringBuffer();
			for (int i = 1; i < args.narg(); i++)
				sb.append(args.tojstring(i));
				
			try {
				return LuaValue.valueOf(Parser.parse(sb.toString()).value());
			} catch (SyntaxException ex) {
				Maunsic.getLogger().catching(ex);
				return LuaValue.ZERO;
			}
		}
	}
	
	public static class pi extends ZeroArgFunction {
		@Override
		public LuaValue call() {
			return LuaValue.valueOf(Math.PI);
		}
	}
	
	public static class e extends ZeroArgFunction {
		@Override
		public LuaValue call() {
			return LuaValue.valueOf(Math.E);
		}
	}
	
	public static class random extends VarArgFunction {
		private static Random r = new Random(Minecraft.getSystemTime());
		
		@Override
		public LuaValue invoke(Varargs args) {
			if (args.narg() > 0) return LuaValue.valueOf(r.nextInt(args.toint(1)));
			else return LuaValue.valueOf(r.nextInt());
		}
	}
	
	public static class round extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue arg) {
			return LuaValue.valueOf(Math.round(arg.todouble()));
		}
	}
	
	public static class sqrt extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue arg) {
			return LuaValue.valueOf(Math.sqrt(arg.todouble()));
		}
	}
	
	public static class toRadians extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue arg) {
			return LuaValue.valueOf(Math.toRadians(arg.todouble()));
		}
	}
	
	public static class toDegrees extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue arg) {
			return LuaValue.valueOf(Math.toDegrees(arg.todouble()));
		}
	}
	
	public static class floor extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue arg) {
			return LuaValue.valueOf(Math.floor(arg.todouble()));
		}
	}
	
	public static class ceil extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue arg) {
			return LuaValue.valueOf(Math.ceil(arg.todouble()));
		}
	}
	
	public static class sin extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue arg) {
			return LuaValue.valueOf(Math.sin(arg.todouble()));
		}
	}
	
	public static class cos extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue arg) {
			return LuaValue.valueOf(Math.cos(arg.todouble()));
		}
	}
	
	public static class tan extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue arg) {
			return LuaValue.valueOf(Math.tan(arg.todouble()));
		}
	}
	
	public static class asin extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue arg) {
			return LuaValue.valueOf(Math.asin(arg.todouble()));
		}
	}
	
	public static class acos extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue arg) {
			return LuaValue.valueOf(Math.acos(arg.todouble()));
		}
	}
	
	public static class atan extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue arg) {
			return LuaValue.valueOf(Math.atan(arg.todouble()));
		}
	}
	
	public static class atan2 extends TwoArgFunction {
		@Override
		public LuaValue call(LuaValue x, LuaValue y) {
			return LuaValue.valueOf(Math.atan2(y.todouble(), x.todouble()));
		}
	}
	
	public static class exp extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue d) {
			return LuaValue.valueOf(Math.exp(d.todouble()));
		}
	}
	
	public static class log extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue d) {
			return LuaValue.valueOf(Math.log(d.todouble()));
		}
	}
	
	public static class pow extends TwoArgFunction {
		@Override
		public LuaValue call(LuaValue x, LuaValue y) {
			return LuaValue.valueOf(Math.pow(x.todouble(), y.todouble()));
		}
	}
	
	public static class cosh extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue d) {
			return LuaValue.valueOf(Math.cosh(d.todouble()));
		}
	}
}
