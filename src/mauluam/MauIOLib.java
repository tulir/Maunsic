package mauluam;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import net.maunium.Maunsic.Maunsic;

public class MauIOLib extends TwoArgFunction {
	
	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaValue lib = LuaValue.tableOf();
		lib.set("chat", new chat());
		lib.set("print", new print());
		lib.set("printMau", new printMau());
		lib.set("error", new error());
		lib.set("errorMau", new errorMau());
		lib.set("read", new read());
		lib.set("readFile", new readFile());
		lib.set("writeFile", new writeFile());
		lib.set("log", new log());
		env.set("mauio", lib);
		return lib;
	}
	
	public static class writeFile extends VarArgFunction {
		@Override
		public LuaValue invoke(Varargs vardata) {
			if (!vardata.istable(1)) return LuaValue.valueOf(4);
			LuaTable data = vardata.checktable(1);
			StringBuffer sb = new StringBuffer();
			for (int i = 2; i < vardata.narg() + 1; i++)
				sb.append(vardata.tojstring(i));
			File f = new File(sb.toString());
			if (!f.exists()) return LuaValue.valueOf(1);
			try {
				MauFileUtils.clear(f);
			} catch (IOException e2) {
				Maunsic.getLogger().catching(e2);
				return LuaValue.valueOf(2);
			}
			BufferedWriter bw;
			try {
				bw = new BufferedWriter(new FileWriter(f));
			} catch (IOException e) {
				Maunsic.getLogger().catching(e);
				return LuaValue.valueOf(3);
			}
			for (int i = 1; i < data.length() + 1; i++)
				try {
					bw.write(data.get(i).tojstring());
				} catch (IOException e) {
					Maunsic.getLogger().catching(e);
					try {
						bw.close();
						return LuaValue.valueOf(4);
					} catch (IOException e1) {
						bw = null;
						return LuaValue.valueOf(4);
					}
				}
			try {
				bw.close();
			} catch (IOException e) {
				Maunsic.getLogger().catching(e);
				return LuaValue.valueOf(5);
			}
			return LuaValue.valueOf(0);
		}
	}
	
	public static class readFile extends VarArgFunction {
		@Override
		public LuaValue invoke(Varargs file) {
			StringBuffer sb = new StringBuffer();
			for (int i = 1; i < file.narg() + 1; i++)
				sb.append(file.tojstring(i));
			File f = new File(sb.toString());
			if (!f.exists()) return LuaValue.valueOf(1);
			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(f));
			} catch (FileNotFoundException e) {
				Maunsic.getLogger().catching(e);
				return LuaValue.valueOf(2);
			}
			String s;
			List<LuaValue> fileData = new ArrayList<LuaValue>();
			try {
				while ((s = br.readLine()) != null)
					fileData.add(LuaValue.valueOf(s));
				br.close();
			} catch (IOException e) {
				Maunsic.getLogger().catching(e);
				return LuaValue.valueOf(3);
			}
			LuaValue[] fileDataArray = fileData.toArray(new LuaValue[0]);
			return LuaValue.listOf(fileDataArray);
		}
	}
	
	public static class read extends ZeroArgFunction {
		@Override
		public LuaValue call() {
//			return LuaValue.valueOf(Maunsic.getInstance().getChatOutputListener().readString());
			return LuaValue.valueOf("Not yet implemented.");
		}
	}
	
	public static class chat extends VarArgFunction {
		@Override
		public LuaValue invoke(Varargs msg) {
			StringBuffer sb = new StringBuffer();
			for (int i = 1; i < msg.narg() + 1; i++)
				sb.append(msg.tojstring(i));
			Maunsic.sendChat(sb.toString());
			return LuaValue.NIL;
		}
	}
	
	public static class print extends VarArgFunction {
		@Override
		public LuaValue invoke(Varargs msg) {
			StringBuffer sb = new StringBuffer();
			for (int i = 1; i < msg.narg() + 1; i++)
				sb.append(msg.tojstring(i));
			Maunsic.printChatPlain("message.mauluam.out", sb.toString());
			return LuaValue.NIL;
		}
	}
	
	public static class printMau extends VarArgFunction {
		@Override
		public LuaValue invoke(Varargs msg) {
			StringBuffer sb = new StringBuffer();
			for (int i = 1; i < msg.narg() + 1; i++)
				sb.append(msg.tojstring(i));
			Maunsic.printChatPlain(sb.toString());
			return LuaValue.NIL;
		}
	}
	
	public static class error extends VarArgFunction {
		@Override
		public LuaValue invoke(Varargs msg) {
			StringBuffer sb = new StringBuffer();
			for (int i = 1; i < msg.narg() + 1; i++)
				sb.append(msg.tojstring(i));
			Maunsic.printChat("message.mauluam.err", sb.toString());
			return LuaValue.NIL;
		}
	}
	
	public static class errorMau extends VarArgFunction {
		@Override
		public LuaValue invoke(Varargs msg) {
			StringBuffer sb = new StringBuffer();
			for (int i = 1; i < msg.narg() + 1; i++)
				sb.append(msg.tojstring(i));
			Maunsic.printChatError(sb.toString());
			return LuaValue.NIL;
		}
	}
	
	static class log extends VarArgFunction {
		@Override
		public LuaValue invoke(Varargs msg) {
			if (msg.narg() > 1) {
				StringBuffer sb = new StringBuffer();
				for (int i = 2; i < msg.narg() + 1; i++)
					sb.append(msg.tojstring(i));
				Maunsic.getLogger().custom(sb.toString(), msg.tojstring(1), true);
			}
			return LuaValue.NIL;
		}
	}
}
