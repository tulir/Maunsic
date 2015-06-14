package mauluam;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

public class MauReflectLib extends TwoArgFunction {
	
	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaValue lib = LuaValue.tableOf();
		lib.set("setsetting", new setsetting());
		lib.set("getsetting", new getsetting());
		lib.set("action", new action());
		env.set("reflection", lib);
		return lib;
	}
	
	public static class getsetting extends TwoArgFunction {
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
	
	public static class setsetting extends ThreeArgFunction {
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
	
	public static class action extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue method) {
//			try {
//				Action a = Actions.getAction(method.tojstring());
//				if (a != null) a.execute();
//				else return LuaValue.valueOf(1);
//				return LuaValue.valueOf(0);
//			} catch (Throwable e) {
//				Maunsic.getLogger().catching(e);
//				return LuaValue.valueOf(2);
//			}
			return LuaValue.valueOf("Not yet implemented");
		}
	}
}
