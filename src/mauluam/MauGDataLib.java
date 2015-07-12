package mauluam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import net.maunium.Maunsic.Maunsic;

/**
 * MauluaM library for globally storing and reading data from lua scripts.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class MauGDataLib extends TwoArgFunction {
	
	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaValue lib = LuaValue.tableOf();
		lib.set("set", new set());
		lib.set("get", new get());
		lib.set("load", new load());
		lib.set("save", new save());
		env.set("gdata", lib);
		return lib;
	}
	
	private static Map<String, LuaValue> data = new HashMap<String, LuaValue>();
	
	public static class load extends ZeroArgFunction {
		@Override
		public LuaValue call() {
			try {
				loadGlobalData();
				return LuaValue.TRUE;
			} catch (IOException e) {
				Maunsic.getLogger().catching(e);
				return LuaValue.FALSE;
			}
		}
	}
	
	public static class save extends ZeroArgFunction {
		@Override
		public LuaValue call() {
			try {
				saveGlobalData();
				return LuaValue.TRUE;
			} catch (IOException e) {
				Maunsic.getLogger().catching(e);
				return LuaValue.FALSE;
			}
		}
	}
	
	public static class set extends TwoArgFunction {
		@Override
		public LuaValue call(LuaValue key, LuaValue value) {
			synchronized (data) {
				if (data.containsKey(key.tojstring())) {
					data.put(key.tojstring(), value);
					return LuaValue.valueOf(true);
				} else {
					data.put(key.tojstring(), value);
					return LuaValue.valueOf(false);
				}
			}
		}
	}
	
	public static class get extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue key) {
			synchronized (data) {
				if (data.containsKey(key.tojstring())) return data.get(key.tojstring());
				else return LuaValue.NIL;
			}
		}
	}
	
	public static void saveGlobalData() throws IOException {
		synchronized (data) {
			File gdataFile = new File(Maunsic.getConfDir(), "luagdata.maudat");
			MauFileUtils.clear(gdataFile);
			Properties props = new Properties();
			for (Entry<String, LuaValue> e : data.entrySet())
				if (e.getValue().isboolean()) props.setProperty(e.getKey(), e.getValue().toboolean() + "");
				else if (e.getValue().isstring()) props.setProperty(e.getKey(), e.getValue().tostring() + "");
			props.store(new FileOutputStream(gdataFile), "Lua Key Maunsic - Global Data");
		}
	}
	
	public static void loadGlobalData() throws IOException {
		synchronized (data) {
			File gdataFile = new File(Maunsic.getConfDir(), "luagdata.maudat");
			Properties props = new Properties();
			props.load(new FileInputStream(gdataFile));
			for (Entry<Object, Object> e : props.entrySet()) {
				String key = e.getKey().toString();
				try {
					boolean value = Boolean.parseBoolean(e.getValue().toString());
					data.put(key, LuaValue.valueOf(value));
					continue;
				} catch (Exception ex) {}
				try {
					int value = Integer.parseInt(e.getValue().toString());
					data.put(key, LuaValue.valueOf(value));
					continue;
				} catch (Exception ex) {}
				try {
					double value = Double.parseDouble(e.getValue().toString());
					data.put(key, LuaValue.valueOf(value));
					continue;
				} catch (Exception ex) {}
				String value = e.getValue().toString();
				data.put(key, LuaValue.valueOf(value));
			}
		}
	}
}
