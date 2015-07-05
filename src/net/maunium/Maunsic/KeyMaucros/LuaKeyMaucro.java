package net.maunium.Maunsic.KeyMaucros;

import java.io.File;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import mauluam.LuaExecutionThread;
import mauluam.MauThreadLib;

import net.maunium.Maunsic.Maunsic;

/**
 * A class extending Key Maucro for Key Maucros that execute Lua scripts.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class LuaKeyMaucro extends KeyMaucro {
	static final long serialVersionUID = 293120001L;
	
	public final File file;
	public static final File f = Maunsic.getConfDir("scripts");
	
	public LuaKeyMaucro(String name, String file, int keyCode, ExecPhase phase, int... shiftKeys) {
		this(name, new File(f, file), keyCode, phase, shiftKeys);
	}
	
	private LuaKeyMaucro(String name, File file, int keyCode, ExecPhase phase, int... shiftKeys) {
		super(name, keyCode, phase, shiftKeys);
		this.file = file;
	}
	
	@Override
	public String getData() {
		return file.getName();
	}
	
	@Override
	public JsonObject toJson() {
		JsonObject jo = new JsonObject();
		jo.addProperty("type", getType().toGuiState());
		jo.addProperty("name", getName());
		jo.addProperty("keycode", getKeyCode());
		jo.addProperty("file", file.getAbsolutePath());
		jo.addProperty("phase", phase.toInt());
		jo.add("shiftkeys", getShiftKeysAsJson());
		return jo;
	}
	
	public static LuaKeyMaucro fromJson(JsonObject jo) {
		try {
			String name = jo.get("name").getAsString();
			int keycode = jo.get("keycode").getAsInt();
			File file = new File(jo.get("file").getAsString());
			ExecPhase ep = ExecPhase.fromInt(jo.get("phase").getAsInt());
			JsonArray ska = jo.get("shiftkeys").getAsJsonArray();
			int[] shiftkeys = new int[ska.size()];
			for (int i = 0; i < ska.size(); i++)
				shiftkeys[i] = ska.get(i).getAsInt();
			return new LuaKeyMaucro(name, file, keycode, ep, shiftkeys);
		} catch (Throwable t) {
			return null;
		}
	}
	
	@Override
	public void executeMacro() {
		if (!file.exists()) Maunsic.printChatError("message.lua.missingfile", file.getName(), getName());
		else {
			LuaExecutionThread t = new LuaExecutionThread(getName(), file);
			MauThreadLib.startThread(t);
		}
	}
	
	@Override
	public Type getType() {
		return Type.LUA;
	}
}
