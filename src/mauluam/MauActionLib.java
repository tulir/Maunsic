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
		public LuaValue call(LuaValue action, LuaValue field) {
			StatusAction sa = getAction(action.tojstring());
			if (sa == null) return LuaValue.valueOf(-1);
			Field f = null;
			Class<?> type = null;
			try {
				f = sa.getClass().getDeclaredField(field.tojstring());
				if (f == null) return LuaValue.valueOf(-3);
				type = f.getType();
				if (type == null) return LuaValue.valueOf(-3);
			} catch (Throwable e) {
				Maunsic.getLogger().catching(e);
				return LuaValue.valueOf(-2);
			}
			
			try {
				if (type.equals(String.class)) return LuaValue.valueOf((String) f.get(sa));
				else if (type.equals(int.class)) return LuaValue.valueOf(f.getInt(sa));
				else if (type.equals(double.class)) return LuaValue.valueOf(f.getDouble(sa));
				else if (type.equals(float.class)) return LuaValue.valueOf(f.getFloat(sa));
				else if (type.equals(boolean.class)) return LuaValue.valueOf(f.getBoolean(sa));
				else if (type.equals(byte[].class)) return LuaValue.valueOf((byte[]) f.get(sa));
			} catch (Throwable t) {
				return LuaValue.valueOf(-4);
			}
			return LuaValue.valueOf(-5);
		}
	}
	
	public class setsetting extends ThreeArgFunction {
		@Override
		public LuaValue call(LuaValue action, LuaValue field, LuaValue value) {
			StatusAction sa = getAction(action.tojstring());
			if (sa == null) return LuaValue.valueOf(-1);
			Field f;
			Class<?> type = null;
			try {
				f = sa.getClass().getDeclaredField(field.tojstring());
				if (f == null) return LuaValue.valueOf(-3);
				type = f.getType();
				if (type == null) return LuaValue.valueOf(-3);
			} catch (Throwable e) {
				Maunsic.getLogger().catching(e);
				return LuaValue.valueOf(-2);
			}
			try {
				switch (value.type()) {
					case LuaValue.TBOOLEAN:
						f.setBoolean(sa, value.toboolean());
						break;
					case LuaValue.TINT:
						f.setInt(sa, value.toint());
						break;
					case LuaValue.TNUMBER:
						f.setDouble(sa, value.todouble());
						break;
					case LuaValue.TSTRING:
						f.set(sa, value.tojstring());
						break;
					default:
						return LuaValue.valueOf(-5);
				}
			} catch (Throwable t) {
				return LuaValue.valueOf(-4);
			}
			return LuaValue.valueOf(0);
		}
	}
	
	public class activate extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue method) {
			if (!method.isstring()) return LuaValue.FALSE;
			StatusAction a = getAction(method.tojstring());
			if (a != null) a.activate();
			else return LuaValue.valueOf(1);
			return LuaValue.valueOf(0);
		}
	}
	
	public class deactivate extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue method) {
			if (!method.isstring()) return LuaValue.FALSE;
			StatusAction a = getAction(method.tojstring());
			if (a != null) a.deactivate();
			else return LuaValue.valueOf(1);
			return LuaValue.valueOf(0);
		}
	}
	
	public class toggle extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue method) {
			if (!method.isstring()) return LuaValue.FALSE;
			StatusAction a = getAction(method.tojstring());
			if (a != null) a.toggle();
			else return LuaValue.valueOf(1);
			return LuaValue.valueOf(0);
		}
	}
	
	public class isactive extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue method) {
			if (!method.isstring()) return LuaValue.FALSE;
			StatusAction a = getAction(method.tojstring());
			if (a != null) return LuaValue.valueOf(a.isActive());
			else return LuaValue.FALSE;
		}
	}
	
	public StatusAction getAction(String name) {
		Maunsic host = Maunsic.getInstance();
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
