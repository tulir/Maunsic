package net.maunium.Maunsic.KeyMaucros;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.lwjgl.input.Keyboard;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.maunium.Maunsic.Listeners.KeyHandling.MauKeybind;
import net.maunium.Maunsic.Util.MaunsiConfig;

/**
 * An abstract Key Maucro. Contains the basic parts, but execution is up to subclasses.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public abstract class KeyMaucro implements Serializable, Comparable<KeyMaucro> {
	static final long serialVersionUID = 29300001L;
	
	private static List<KeyMaucro> keymaucros = new ArrayList<KeyMaucro>();
	
	public static void sort() {
		Collections.sort(keymaucros);
	}
	
	/**
	 * Add the given key maucro to the key maucro registry.
	 */
	public static void addKeyMaucro(KeyMaucro km) {
		if (keymaucros.size() > 0) {
			for (int i = 0; i < keymaucros.size(); i++) {
				if (keymaucros.get(i).getName().equals(km.getName())) modifyKeyMaucro(i, km);
				else continue;
				return;
			}
		}
		keymaucros.add(km);
		sort();
	}
	
	/**
	 * Set the key maucro in the given key index to the given new key maucro.
	 * 
	 * @return True if the key maucro was updated, false otherwise.
	 */
	public static boolean modifyKeyMaucro(int index, KeyMaucro newkm) {
		if (keymaucros.size() > index && !keymaucros.get(index).equals(newkm)) {
			keymaucros.set(index, newkm);
			sort();
			return true;
		}
		return false;
	}
	
	/**
	 * Remove the given key maucro from the registry.
	 */
	public static boolean removeKeyMaucro(KeyMaucro km) {
		return keymaucros.remove(km);
	}
	
	/**
	 * Remove the key maucro in the given index from the registry.
	 * 
	 * @return True if there was a key maucro in the given index and it was removed. False otherwise.
	 */
	public static boolean removeKeyMaucro(int index) {
		if (keymaucros.size() > index) {
			keymaucros.remove(index);
			return true;
		} else return false;
	}
	
	/**
	 * Get all the key maucros.
	 */
	public static List<KeyMaucro> getKeyMaucros() {
		return keymaucros;
	}
	
	/**
	 * Save the key maucros to the given MaunsiConfig.
	 */
	public static void save(MaunsiConfig conf) {
		sort();
		JsonArray ja = new JsonArray();
		for (KeyMaucro km : getKeyMaucros())
			ja.add(km.toJson());
		conf.set("keymaucros", ja);
	}
	
	/**
	 * Load all key maucros from the given MaunsiConfig.
	 */
	public static void load(MaunsiConfig conf) {
		JsonElement jae = conf.get("keymaucros");
		if (!jae.isJsonArray()) return;
		JsonArray ja = jae.getAsJsonArray();
		for (JsonElement je : ja) {
			if (!je.isJsonObject()) continue;
			KeyMaucro km = fromJson(je.getAsJsonObject());
			if (km != null) keymaucros.add(km);
		}
		sort();
	}
	
	protected String name;
	protected int keyCode;
	protected int[] shiftKeys;
	protected ExecPhase phase;
	
	public KeyMaucro(String name, int keyCode, ExecPhase phase, int... shiftKeys) {
		this.name = name;
		this.keyCode = keyCode;
		this.shiftKeys = shiftKeys;
		this.phase = phase;
	}
	
	@Override
	public int compareTo(KeyMaucro to) {
		int x = shiftKeys.length;
		int y = to.shiftKeys.length;
		return x < y ? 1 : x == y ? 0 : -1;
	}
	
	public static int compare(KeyMaucro km1, KeyMaucro km2) {
		return km1.compareTo(km2);
	}
	
	/**
	 * Execute this Key Maucro
	 */
	public abstract void executeMacro();
	
	/**
	 * Get the type of this Key Maucro
	 */
	public abstract Type getType();
	
	/**
	 * Get the data of this Key Maucro.<br>
	 * Usually something such as the Execution data.<br>
	 * Should never return null!
	 */
	public abstract String getData();
	
	/**
	 * Get the name of this Key Maucro
	 */
	public final String getName() {
		return name;
	}
	
	/**
	 * Set the name of this Key Maucro.
	 */
	public final void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get the key code of this Key Maucro
	 */
	public final int getKeyCode() {
		return keyCode;
	}
	
	/**
	 * Set the key code of this Key Maucro
	 */
	public final void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}
	
	/**
	 * Get the Execution Phase of this Key Maucro
	 */
	public final ExecPhase getExecutionPhase() {
		return phase;
	}
	
	/**
	 * Set the Execution Phase of this Key Maucro
	 */
	public final void setExecutionPhase(ExecPhase phase) {
		this.phase = phase;
	}
	
	/**
	 * Get the shift keys of this Key Maucro
	 */
	public final int[] getShiftKeys() {
		return shiftKeys;
	}
	
	/**
	 * Set the shift keys of this Key Maucro
	 */
	public final void setShiftKeys(int... shiftKeys) {
		this.shiftKeys = shiftKeys;
	}
	
	public String getShiftKeysAsString() {
		String s = "";
		for (int i : shiftKeys)
			if (s.equals("")) s = toNaturalCase(Keyboard.getKeyName(i));
			else s = s + " + " + toNaturalCase(Keyboard.getKeyName(i));
		return s;
	}
	
	/**
	 * Get all the shift keys in a Json Array.
	 */
	public JsonArray getShiftKeysAsJson() {
		JsonArray ja = new JsonArray();
		for (int i : shiftKeys)
			ja.add(new JsonPrimitive(i));
		return ja;
	}
	
	/**
	 * First letter upper case, rest lower case.
	 */
	private String toNaturalCase(String s) {
		return s.substring(0, 1).toUpperCase(Locale.ENGLISH) + s.substring(1).toLowerCase(Locale.ENGLISH);
	}
	
	/**
	 * Save this key maucro to a json object.
	 */
	public abstract JsonObject toJson();
	
	/**
	 * Parse a key maucro from the given json object.
	 */
	public static KeyMaucro fromJson(JsonObject jo) {
		Type t = Type.fromGuiState(jo.get("type").getAsInt());
		switch (t) {
			case LUA:
				return LuaKeyMaucro.fromJson(jo);
			case COMMANDCHAIN:
				return CCKeyMaucro.fromJson(jo);
			default:
				return null;
		}
	}
	
	/**
	 * An Exception that is thrown when parsing a Key Maucro from a String fails.
	 * 
	 * @author Tulir293
	 * @since 0.1
	 * @from Maucros
	 */
	public static class KeyMaucroFormatException extends IllegalArgumentException {
		private static final long serialVersionUID = 734506366998594210L;
		
		/**
		 * Constructs a KeyMaucroFormatException with the given reason.
		 * 
		 * @param r The reason
		 */
		public KeyMaucroFormatException(String r) {
			super(r);
		}
	}
	
	/**
	 * Returns true if the main key and all the shift keys are pressed.
	 */
	public boolean isMacroDown() {
		for (int i : shiftKeys)
			if (!MauKeybind.isDown(i)) return false;
		return MauKeybind.isDown(keyCode);
	}
	
	/**
	 * Checks if the keys are down and executes the Maucro if they are.
	 * 
	 * @return If the Maucro was executed or not.
	 */
	public final boolean checkAndExecute() {
		if (isMacroDown()) {
			executeMacro();
			return true;
		} else return false;
	}
	
	/**
	 * Check if this Key Maucro is equal to the given one.
	 */
	public boolean equals(KeyMaucro km) {
		if (!name.equals(km.name)) return false;
		if (!getType().equals(km.getType())) return false;
		if (keyCode != km.keyCode) return false;
		if (!shiftKeys.equals(km.shiftKeys)) return false;
		if (!phase.equals(km.phase)) return false;
		if (!getData().equals(km.getData())) return false;
		return true;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof KeyMaucro) return equals((KeyMaucro) o);
		else return false;
	}
	
	/**
	 * The Key Maucro type.
	 * 
	 * @author Tulir293
	 * @since 0.1
	 * @from Maucros
	 */
	public static enum Type {
		COMMANDCHAIN, LUA;
		public int toGuiState() {
			switch (this) {
				case COMMANDCHAIN:
					return 0;
				case LUA:
					return 1;
				default:
					return -1;
			}
		}
		
		public static Type fromString(String s) {
			if (s.equalsIgnoreCase("commandchain")) return COMMANDCHAIN;
			else if (s.equalsIgnoreCase("lua")) return LUA;
			else return null;
		}
		
		public static Type fromGuiState(int state) {
			switch (state) {
				case 0:
					return COMMANDCHAIN;
				case 1:
					return LUA;
				default:
					return null;
			}
		}
	}
	
	/**
	 * The execution phase of a Key Maucro
	 * 
	 * @author Tulir293
	 * @since 0.1
	 */
	public static enum ExecPhase {
		PRECHECKS_DOWN, PREKEYS_DOWN, POSTKEYS_DOWN, PRECHECKS_UP, PREKEYS_UP, POSTKEYS_UP;
		
		public int toInt() {
			switch (this) {
				case PRECHECKS_UP:
					return 0;
				case PREKEYS_UP:
					return 1;
				case POSTKEYS_UP:
					return 2;
				case PRECHECKS_DOWN:
					return 3;
				case PREKEYS_DOWN:
					return 4;
				case POSTKEYS_DOWN:
					return 5;
				default:
					return -1;
			}
		}
		
		public static ExecPhase fromInt(int i) {
			switch (i) {
				case 0:
					return PRECHECKS_DOWN;
				case 1:
					return PREKEYS_DOWN;
				case 2:
					return POSTKEYS_DOWN;
				case 3:
					return PRECHECKS_UP;
				case 4:
					return PREKEYS_UP;
				case 5:
					return POSTKEYS_UP;
				default:
					return null;
			}
		}
	}
}
