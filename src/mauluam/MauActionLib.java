package mauluam;

import java.lang.reflect.Field;
import java.util.Locale;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Actions.Util.StatusAction;

public class MauActionLib extends TwoArgFunction {
	private Maunsic host;
	
	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaValue lib = LuaValue.tableOf();
		lib.set("setsetting", new setsetting());
		lib.set("getsetting", new getsetting());
		lib.set("activate", new activate());
		lib.set("deactivate", new deactivate());
		lib.set("toggle", new toggle());
		lib.set("isactive", new isactive());
		env.set("actions", lib);
		return lib;
	}
	
	public class getsetting extends TwoArgFunction {
		@Override
		public LuaValue call(LuaValue clazz, LuaValue field) {
//			Field f = null;
//			boolean b1 = false;
//			for (Class<?> c : Settings.class.getDeclaredClasses())
//				if (c.getSimpleName().equalsIgnoreCase(clazz.tojstring())) {
//					b1 = true;
//					for (Field ff : c.getDeclaredFields())
//						if (ff.getName().equalsIgnoreCase(field.tojstring())) {
//							f = ff;
//							break;
//						}
//				}
//			if (!b1) return LuaValue.valueOf("Class not found: \"" + clazz.tojstring() + "\"");
//			if (f == null) return LuaValue.valueOf("Field not found: \"" + field.tojstring() + "\"");
//			try {
//				if (!f.isAccessible()) f.setAccessible(true);
//				if (f.getType().equals(int.class)) return LuaValue.valueOf(f.getInt(null));
//				else if (f.getType().equals(double.class)) return LuaValue.valueOf(f.getDouble(null));
//				else if (f.getType().equals(boolean.class)) return LuaValue.valueOf(f.getBoolean(null));
//				else return LuaValue.valueOf(f.get(null).toString());
//			} catch (Exception e) {
//				Maunsic.getLogger().catching(e);
//				return LuaValue.valueOf("Exception: " + e.getMessage());
//			}
			return LuaValue.valueOf("Not yet implemented");
		}
	}
	
	public class setsetting extends ThreeArgFunction {
		@Override
		public LuaValue call(LuaValue clazz, LuaValue field, LuaValue value) {
//			Field f = null;
//			boolean b1 = false;
//			for (Class<?> c : Settings.class.getDeclaredClasses())
//				if (c.getSimpleName().equalsIgnoreCase(clazz.tojstring())) {
//					b1 = true;
//					for (Field ff : c.getDeclaredFields())
//						if (ff.getName().equalsIgnoreCase(field.tojstring())) {
//							f = ff;
//							break;
//						}
//				}
//			if (!b1) return LuaValue.valueOf("Class not found: \"" + clazz.tojstring() + "\"");
//			if (f == null) return LuaValue.valueOf("Field not found: \"" + field.tojstring() + "\"");
//			try {
//				if (!f.isAccessible()) f.setAccessible(true);
//				if (f.getType().equals(int.class)) f.set(null, value.toint());
//				else if (f.getType().equals(double.class)) f.set(null, value.todouble());
//				else if (f.getType().equals(String.class)) f.set(null, value.tojstring());
//				else if (f.getType().equals(boolean.class)) f.set(null, value.toboolean());
//				else return LuaValue.valueOf("Type \"" + f.getType().getSimpleName() + "\" could not be identified.");
//				return LuaValue.valueOf("Success");
//			} catch (Exception e) {
//				Maunsic.getLogger().catching(e);
//				return LuaValue.valueOf("Exception: " + e.getMessage());
//			}
			return LuaValue.valueOf("Not yet implemented");
		}
	}
	
	public class activate extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue method) {
			StatusAction a = getAction(method.tojstring());
			if (a != null) a.activate();
			else return LuaValue.valueOf(1);
			return LuaValue.valueOf(0);
		}
	}
	
	public class deactivate extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue method) {
			StatusAction a = getAction(method.tojstring());
			if (a != null) a.deactivate();
			else return LuaValue.valueOf(1);
			return LuaValue.valueOf(0);
		}
	}
	
	public class toggle extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue method) {
			StatusAction a = getAction(method.tojstring());
			if (a != null) a.toggle();
			else return LuaValue.valueOf(1);
			return LuaValue.valueOf(0);
		}
	}
	
	public class isactive extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue method) {
			StatusAction a = getAction(method.tojstring());
			if (a != null) return LuaValue.valueOf(a.isActive());
			else return LuaValue.FALSE;
		}
	}
	
	public StatusAction getAction(String name) {
		switch (name.toLowerCase(Locale.ENGLISH)) {
		// @mauformat=off
			case "fly": return host.actionFly;
			case "nofall": return host.actionNofall;
			case "blink": return host.actionBlink;
			case "spammer": return host.actionSpammer;
			case "attackaura": return host.actionAttackaura;
			case "autosoup": return host.actionAutosoup;
			case "aimbot": return host.actionAimbot;
			case "xray": return host.actionXray;
			case "tracer": return host.actionTracer;
			case "playeresp": return host.actionEsp;
			case "autouse": return host.actionAutouse;
			case "regen": return host.actionRegen;
			case "freecam": return host.actionFreecam;
			case "trajectories": return host.actionTrajectories;
			case "phase": return host.actionPhase;
			case "triggerbot": return host.actionTriggerbot;
			case "fullbright": return host.actionFullbright;
			case "fastbow": return host.actionFastbow;
		// @mauformat=on
			default:
				try {
					Field f = Maunsic.class.getDeclaredField("action" + name);
					Object o = f.get(host);
					if (o instanceof StatusAction) ((StatusAction) o).deactivate();
				} catch (Exception e) {
					Maunsic.getLogger().catching(e);
				}
				return null;
		}
	}
}
