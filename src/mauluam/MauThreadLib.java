package mauluam;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import net.maunium.Maunsic.Maunsic;

/**
 * MauluaM library to manage Lua threads.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class MauThreadLib extends TwoArgFunction {
	
	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaValue lib = LuaValue.tableOf();
		lib.set("start", new start());
		lib.set("stop", new stop());
		lib.set("setName", new setName());
		lib.set("getName", new getName());
		lib.set("sleep", new sleep());
		env.set("thread", lib);
		return lib;
	}
	
	public static class start extends VarArgFunction {
		@Override
		public LuaValue invoke(Varargs args) {
			String name = args.tojstring(1);
			StringBuffer sb = new StringBuffer();
			for (int i = 2; i < args.narg() + 1; i++)
				sb.append(args.tojstring(i));
			File file = new File(sb.toString());
			if (!file.exists()) return LuaValue.valueOf(1);
			LuaExecutionThread let = new LuaExecutionThread(name, file);
			startThread(let);
			return LuaValue.valueOf(0);
		}
	}
	
	public static class stop extends VarArgFunction {
		@Override
		public LuaValue invoke(Varargs args) {
			String s = null;
			if (args.narg() != 0) {
				StringBuffer sb = new StringBuffer();
				for (int i = 1; i < args.narg() + 1; i++)
					sb.append(args.tojstring(i));
				s = sb.toString();
			}
			
			if (s == null) s = Thread.currentThread().getName();
			
			if (threads.containsKey(s)) {
				stopThread(s);
				return LuaValue.TRUE;
			} else return LuaValue.FALSE;
		}
	}
	
	public static class sleep extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue time) {
			try {
				Thread.sleep(time.tolong());
			} catch (InterruptedException e) {
				Maunsic.getLogger().catching(e);
				return LuaValue.valueOf(1);
			}
			return LuaValue.valueOf(0);
		}
	}
	
	private static Map<String, LuaExecutionThread> threads = new HashMap<String, LuaExecutionThread>();
	
	public static class setName extends VarArgFunction {
		@Override
		public LuaValue invoke(Varargs args) {
			if (args.narg() == 0) return LuaValue.valueOf(1);
			StringBuffer sb = new StringBuffer();
			for (int i = 1; i < args.narg() + 1; i++)
				sb.append(args.tojstring(i));
			String s = sb.toString();
			Thread th = Thread.currentThread();
			if (th instanceof LuaExecutionThread) {
				LuaExecutionThread lext = (LuaExecutionThread) th;
				threads.remove(lext.getTName());
				lext.setTName(s);
				threads.put(s, lext);
				return LuaValue.valueOf(th.getName());
			}
			return LuaValue.NIL;
		}
	}
	
	public static class getName extends ZeroArgFunction {
		@Override
		public LuaValue call() {
			return LuaValue.valueOf(Thread.currentThread().getName());
		}
	}
	
	public static LuaExecutionThread removeThread(String name) {
		synchronized (threads) {
			return threads.remove(name);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static LuaExecutionThread stopThread(String name) {
		synchronized (threads) {
			Maunsic.getLogger().debug("Stopping " + name);
			if (threads.containsKey(name)) threads.get(name).stop();
			return threads.remove(name);
		}
	}
	
	public static void startThread(LuaExecutionThread let) {
		if (let == null) return;
		synchronized (threads) {
			Maunsic.getLogger().debug("Starting " + let.getTName() + " (File: " + let.getFile().getPath() + ")");
			String tname = let.getTName();
			int i = 1;
			while (threads.containsKey(let.getTName())) {
				let.setTName(tname + "-" + i);
				i++;
			}
			threads.put(let.getTName(), let);
			let.start();
		}
	}
	
	public static LuaExecutionThread getThread(String name) {
		synchronized (threads) {
			return threads.get(name);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void stopThreads() {
		synchronized (threads) {
			for (LuaExecutionThread let : threads.values()) {
				let.stop();
			}
			threads.clear();
		}
	}
	
	public static Set<String> getThreads() {
		Set<String> r = new HashSet<String>();
		r.addAll(threads.keySet());
		return r;
	}
}
