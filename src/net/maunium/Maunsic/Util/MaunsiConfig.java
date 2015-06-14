package net.maunium.Maunsic.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonWriter;

/**
 * The Maunsic Configuration System. Based on Gson.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class MaunsiConfig {
	private JsonObject json;
	
	/**
	 * Create a new MaunsiConfig
	 */
	public MaunsiConfig() {
		json = new JsonObject();
	}
	
	/**
	 * Load the data from the given file to this MaunsiConfig.
	 * 
	 * @param f The file to load the data from.
	 * @throws JsonSyntaxException As defined in {@link JsonParser#parse(java.io.Reader)}
	 * @throws JsonIOException As defined in {@link JsonParser#parse(java.io.Reader)}
	 */
	public void load(File f) throws JsonIOException, JsonSyntaxException {
		if (!f.exists()) return;
		JsonParser parser = new JsonParser();
		try {
			json = parser.parse(new FileReader(f)).getAsJsonObject();
		} catch (FileNotFoundException e) { /* This should never happen, as f.exists() has been checked. */}
	}
	
	/**
	 * Save this MaunsiConfig to the given file.
	 * 
	 * @param f The file to save this config to.
	 * @throws IOException If writing fails.
	 */
	public void save(File f) throws IOException {
		JsonWriter jw = new JsonWriter(new FileWriter(f));
		jw.setIndent("  ");
		Gson gson = new Gson();
		gson.toJson(json, jw);
		jw.close();
	}
	
	/**
	 * Get the value in the path as an integer. If the value is not an integer, return 0.
	 * 
	 * @param path The path to search.
	 * @return The integer in the given path, or 0 if there was no valid integer in the given path.
	 */
	public int getInt(String path) {
		return getInt(path, 0);
	}
	
	/**
	 * Get the value in the path as an integer. If the value is not an integer, return the given value.
	 * 
	 * @param path The path to search.
	 * @param def The default value to return if there is no valid integer in the given path.
	 * @return The integer in the given path, or the given default value if there was no valid integer in the given path.
	 */
	public int getInt(String path, int def) {
		JsonElement e = get(json, path);
		if (e == null) return def;
		if (!e.isJsonPrimitive()) return def;
		try {
			return e.getAsInt();
		} catch (ClassCastException | IllegalStateException ex) {
			return def;
		}
	}
	
	/**
	 * Get the value in the path as a double. If the value is not a double, return NaN.
	 * 
	 * @param path The path to search.
	 * @return The double in the given path, or NaN if there was no valid double in the given path.
	 */
	public double getDouble(String path) {
		return getDouble(path, Double.NaN);
	}
	
	/**
	 * Get the value in the path as a double. If the value is not a double, return the given value.
	 * 
	 * @param path The path to search.
	 * @param def The default value to return if there is no valid double in the given path.
	 * @return The double in the given path, or the given default value if there was no valid double in the given path.
	 */
	public double getDouble(String path, double def) {
		JsonElement e = get(json, path);
		if (e == null) return def;
		if (!e.isJsonPrimitive()) return def;
		try {
			return e.getAsDouble();
		} catch (ClassCastException | IllegalStateException ex) {
			return def;
		}
	}
	
	/**
	 * Get the value in the path as a float. If the value is not a float, return NaN.
	 * 
	 * @param path The path to search.
	 * @return The float in the given path, or NaN if there was no valid float in the given path.
	 */
	public float getFloat(String path) {
		return getFloat(path, Float.NaN);
	}
	
	/**
	 * Get the value in the path as a float. If the value is not a float, return the given value.
	 * 
	 * @param path The path to search.
	 * @param def The default value to return if there is no valid float in the given path.
	 * @return The float in the given path, or the given default value if there was no valid float in the given path.
	 */
	public float getFloat(String path, float def) {
		JsonElement e = get(json, path);
		if (e == null) return def;
		if (!e.isJsonPrimitive()) return def;
		try {
			return e.getAsFloat();
		} catch (ClassCastException | IllegalStateException ex) {
			return def;
		}
	}
	
	/**
	 * Get the value in the path as a long. If the value is not a long, return 0.
	 * 
	 * @param path The path to search.
	 * @return The long in the given path, or 0 if there was no valid long in the given path.
	 */
	public long getLong(String path) {
		return getLong(path, 0);
	}
	
	/**
	 * Get the value in the path as a long. If the value is not a long, return the given value.
	 * 
	 * @param path The path to search.
	 * @param def The default value to return if there is no valid long in the given path.
	 * @return The long in the given path, or the given default value if there was no valid long in the given path.
	 */
	public long getLong(String path, long def) {
		JsonElement e = get(json, path);
		if (e == null) return def;
		if (!e.isJsonPrimitive()) return def;
		try {
			return e.getAsLong();
		} catch (ClassCastException | IllegalStateException ex) {
			return def;
		}
	}
	
	/**
	 * Get the value in the path as a String. If the value is not a String, return null.
	 * 
	 * @param path The path to search.
	 * @return The String in the given path, or null if there was no valid String in the given path.
	 */
	public String getString(String path) {
		return getString(path, null);
	}
	
	/**
	 * Get the value in the path as a String. If the value is not a String, return the given value.
	 * 
	 * @param path The path to String.
	 * @param def The default value to return if there is no valid String in the given path.
	 * @return The String in the given path, or the given default value if there was no valid String in the given path.
	 */
	public String getString(String path, String def) {
		JsonElement e = get(json, path);
		if (e == null) return def;
		if (!e.isJsonPrimitive()) return def;
		try {
			return e.getAsString();
		} catch (ClassCastException | IllegalStateException ex) {
			return def;
		}
	}
	
	/**
	 * Get the value in the path as a JsonElement. If the value is not a JsonElement, return null.
	 * 
	 * @param path The path to search.
	 * @return The JsonElement in the given path, or null if there was no valid JsonElement in the given path.
	 */
	public JsonElement get(String path) {
		return get(path, null);
	}
	
	/**
	 * Get the value in the path as a JsonElement. If the value is not a JsonElement, return the given value.
	 * 
	 * @param path The path to search.
	 * @param def The default value to return if there is no valid JsonElement in the given path.
	 * @return The JsonElement in the given path, or the given default value if there was no valid JsonElement in the given path.
	 */
	public JsonElement get(String path, JsonElement def) {
		JsonElement e = get(json, path);
		if (e == null) return def;
		else return e;
	}
	
	/**
	 * Hey! Don't you touch this! It's mine! But seriously, don't use this. It's only supposed to be used by the public methods for recursive abilities.
	 */
	private JsonElement get(JsonObject jo, String path) {
		if (path.indexOf('.') == -1) return jo.get(path);
		else {
			String[] ss = path.split(Pattern.quote("."), 2);
			JsonObject jo2;
			JsonElement je = jo.get(ss[0]);
			if (je == null) return null;
			else jo2 = je.getAsJsonObject();
			return get(jo2, ss[1]);
		}
	}
	
	/**
	 * Set the value in the given path to the given String.
	 * 
	 * @param path The path.
	 * @param obj The String.
	 */
	public void set(String path, String obj) {
		if (path.indexOf('.') == -1) json.addProperty(path, obj);
		else set(json, path, obj);
	}
	
	/**
	 * Set the value in the given path to the given Number.
	 * 
	 * @param path The path.
	 * @param obj The Number.
	 */
	public void set(String path, Number obj) {
		set(json, path, obj);
	}
	
	/**
	 * Set the value in the given path to the given Boolean.
	 * 
	 * @param path The path.
	 * @param obj The Boolean.
	 */
	public void set(String path, Boolean obj) {
		set(json, path, obj);
	}
	
	/**
	 * Set the value in the given path to the given Character.
	 * 
	 * @param path The path.
	 * @param obj The Character.
	 */
	public void set(String path, Character obj) {
		set(json, path, obj);
	}
	
	/**
	 * Set the value in the given path to the given JsonElement.
	 * 
	 * @param path The path.
	 * @param obj The JsonElement.
	 */
	public void set(String path, JsonElement obj) {
		set(json, path, obj);
	}
	
	/**
	 * Hey! Don't you touch this! It's mine! But seriously, don't use this. It's only supposed to be used by the public methods for recursive abilities.
	 */
	private void set(JsonObject jo, String path, String obj) {
		if (path.indexOf('.') == -1) jo.addProperty(path, obj);
		else {
			String[] ss = path.split(Pattern.quote("."), 2);
			JsonObject jo2;
			JsonElement je = jo.get(ss[0]);
			if (je == null) {
				jo2 = new JsonObject();
				jo.add(ss[0], jo2);
			} else jo2 = je.getAsJsonObject();
			set(jo2, ss[1], obj);
		}
	}
	
	/**
	 * Hey! Don't you touch this! It's mine! But seriously, don't use this. It's only supposed to be used by the public methods for recursive abilities.
	 */
	private void set(JsonObject jo, String path, Number obj) {
		if (path.indexOf('.') == -1) jo.addProperty(path, obj);
		else {
			String[] ss = path.split(Pattern.quote("."), 2);
			JsonObject jo2;
			JsonElement je = jo.get(ss[0]);
			if (je == null) {
				jo2 = new JsonObject();
				jo.add(ss[0], jo2);
			} else jo2 = je.getAsJsonObject();
			set(jo2, ss[1], obj);
		}
	}
	
	/**
	 * Hey! Don't you touch this! It's mine! But seriously, don't use this. It's only supposed to be used by the public methods for recursive abilities.
	 */
	private void set(JsonObject jo, String path, Boolean obj) {
		if (path.indexOf('.') == -1) jo.addProperty(path, obj);
		else {
			String[] ss = path.split(Pattern.quote("."), 2);
			JsonObject jo2;
			JsonElement je = jo.get(ss[0]);
			if (je == null) {
				jo2 = new JsonObject();
				jo.add(ss[0], jo2);
			} else jo2 = je.getAsJsonObject();
			set(jo2, ss[1], obj);
		}
	}
	
	/**
	 * Hey! Don't you touch this! It's mine! But seriously, don't use this. It's only supposed to be used by the public methods for recursive abilities.
	 */
	private void set(JsonObject jo, String path, Character obj) {
		if (path.indexOf('.') == -1) jo.addProperty(path, obj);
		else {
			String[] ss = path.split(Pattern.quote("."), 2);
			JsonObject jo2;
			JsonElement je = jo.get(ss[0]);
			if (je == null) {
				jo2 = new JsonObject();
				jo.add(ss[0], jo2);
			} else jo2 = je.getAsJsonObject();
			set(jo2, ss[1], obj);
		}
	}
	
	/**
	 * Hey! Don't you touch this! It's mine! But seriously, don't use this. It's only supposed to be used by the public methods for recursive abilities.
	 */
	private void set(JsonObject jo, String path, JsonElement obj) {
		if (path.indexOf('.') == -1) jo.add(path, obj);
		else {
			String[] ss = path.split(Pattern.quote("."), 2);
			JsonObject jo2;
			JsonElement je = jo.get(ss[0]);
			if (je == null) {
				jo2 = new JsonObject();
				jo.add(ss[0], jo2);
			} else jo2 = je.getAsJsonObject();
			set(jo2, ss[1], obj);
		}
	}
}
