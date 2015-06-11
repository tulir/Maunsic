package net.maunium.Maunsic.KeyMaucros;

import java.io.File;
import java.util.regex.Pattern;

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
	
	public String getScriptPath() {
		return file.getPath();
	}
	
	public String getAbsoluteScriptPath() {
		return file.getAbsolutePath();
	}
	
	@Override
	public String toString() {
		if (shiftKeys.length > 0) return getType() + "|" + getName() + "\\~~\\" + getKeyCode() + "\\~~\\" + file.getAbsolutePath() + "\\~~\\" + phase.toInt();
		else return getType() + "|" + getName() + "\\~~\\" + getKeyCode() + "\\~~\\" + file.getPath() + "\\~~\\" + phase.toInt() + "\\~~\\"
				+ getShiftKeysAsString();
	}
	
	@Override
	public void executeMacro() {
		if (!file.exists()) Maunsic.printChatError("message.lua.missingfile", file.getName(), getName());
		else Maunsic.printChatError_static("LuaJ not implemented");// if (Settings.Enabled.lua) {
//			mauluam.LuaExecutionThread t = new mauluam.LuaExecutionThread(getName(), file);
//			mauluam.MauThreadLib.startThread(t);
//		} else Maunsic.printChatError("message.missing.luaj");
	}
	
	public static KeyMaucro parseKeyMaucro(String s) {
		String[] temp = s.split(Pattern.quote("\\~~\\"));
		String name = temp[0];
		int kc = -1;
		try {
			kc = Integer.parseInt(temp[1]);
		} catch (NumberFormatException e) {
			Maunsic.getLogger().error("Error while parsing KeyMaucro: \"" + s + "\"");
			throw new KeyMaucroFormatException("Failed to parse main key code: " + temp[1] + " (" + e.getMessage() + ")");
		}
		if (kc < 0) {
			Maunsic.getLogger().error("Error while parsing KeyMaucro: \"" + s + "\"");
			throw new KeyMaucroFormatException("Key code can't be under 0: " + kc);
		}
		String file = temp[2];
		ExecPhase phase;
		try {
			phase = ExecPhase.fromInt(Integer.parseInt(temp[3]));
		} catch (NumberFormatException e) {
			Maunsic.getLogger().error("Error while parsing KeyMaucro: \"" + s + "\"");
			throw new KeyMaucroFormatException("Failed to parse main key code: " + temp[1] + " (" + e.getMessage() + ")");
		}
		
		int[] shiftKeys;
		if (temp.length == 5) {
			if (temp[4].contains("-~-")) {
				String[] temp2 = temp[3].split(Pattern.quote("-~-"));
				shiftKeys = new int[temp2.length];
				for (int i = 0; i < temp2.length; i++)
					try {
						shiftKeys[i] = Integer.parseInt(temp2[i]);
					} catch (NumberFormatException e) {
						Maunsic.getLogger().error("Error while parsing KeyMaucro: \"" + s + "\"");
						throw new KeyMaucroFormatException("Failed to parse shift key code: " + temp2[i] + " (" + e.getMessage() + ")");
					}
			} else try {
				shiftKeys = new int[] { Integer.parseInt(temp[4]) };
			} catch (NumberFormatException e) {
				Maunsic.getLogger().error("Error while parsing KeyMaucro: \"" + s + "\"");
				throw new KeyMaucroFormatException("Failed to parse shift key code: " + temp[3] + " (" + e.getMessage() + ")");
			}
		} else shiftKeys = new int[0];
		return new LuaKeyMaucro(name, new File(file), kc, phase, shiftKeys);
	}
	
	@Override
	public boolean equals(KeyMaucro km) {
		if (!(km instanceof LuaKeyMaucro)) return false;
		if (!super.equals(km)) return false;
		if (!getAbsoluteScriptPath().equals(((LuaKeyMaucro) km).getAbsoluteScriptPath())) return false;
		return true;
	}
	
	@Override
	public Type getType() {
		return Type.LUA;
	}
}
