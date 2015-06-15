package net.maunium.Maunsic.Settings;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.maunium.Maunsic.Util.MaunsiConfig;

/**
 * Stores alt accounts.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class AltAccounts {
	private Map<String, byte[]> alts = new HashMap<String, byte[]>();
	
	/**
	 * Save the alt accounts to the given MaunsiConfig instance.
	 */
	public void save(MaunsiConfig conf) {
		JsonArray ja = new JsonArray();
		for (Entry<String, byte[]> e : alts.entrySet())
			ja.add(new JsonPrimitive(e.getKey() + "||" + new String(Base64.encodeBase64(e.getValue()), StandardCharsets.UTF_8)));
		conf.set("alts", ja);
	}
	
	/**
	 * Load the alt accounts from the given MaunsiConfig instance.
	 */
	public void load(MaunsiConfig conf) {
		JsonArray ja;
		JsonElement je = conf.get("alts", new JsonArray());
		if (je.isJsonArray()) ja = je.getAsJsonArray();
		else ja = new JsonArray();
		for (JsonElement e : ja) {
			if (!e.isJsonPrimitive()) continue;
			String s = e.getAsString();
			if (!s.contains("||")) continue;
			String[] ss = s.split(Pattern.quote("||"), 2);
			byte[] passwd = Base64.decodeBase64(ss[1].getBytes(StandardCharsets.UTF_8));
			addAlt(ss[0], passwd);
		}
	}
	
	/**
	 * Add an alt account with the given username and password.
	 * 
	 * @return As defined in {@link #addAlt(String, byte[])}
	 */
	public int addAlt(String user, String password) {
		return addAlt(user, password.getBytes(StandardCharsets.UTF_8));
	}
	
	/**
	 * Add an alt account with the given username and password.
	 * 
	 * @return 2 if the exact alt was already added, 1 if the password was updated and 0 if the alt was new.
	 */
	public int addAlt(String user, byte[] password) {
		if (alts.containsKey(user)) {
			if (equals(alts.get(user), password)) return 2;
			else {
				alts.put(user, password);
				return 1;
			}
		} else {
			alts.put(user, password);
			return 0;
		}
	}
	
	/**
	 * Remove the password of the given username.
	 * 
	 * @return True if the alt used to be in the list and was removed. False if it wasn't in the list.
	 */
	public boolean delAlt(String user) {
		if (alts.containsKey(user)) {
			alts.remove(user);
			return true;
		}
		return false;
	}
	
	/**
	 * Get the password of the given user.
	 * 
	 * @return The password, or null if user not found.
	 */
	public String getPassword(String user) {
		if (!alts.containsKey(user)) return null;
		else return new String(alts.get(user), StandardCharsets.UTF_8);
	}
	
	/**
	 * Get a list of the alt usernames.
	 */
	public Set<String> getAlts() {
		return alts.keySet();
	}
	
	/**
	 * Nope.
	 */
	private boolean equals(byte[] a1, byte[] a2) {
		if (a1.length != a2.length) return false;
		for (int i = 0; i < a1.length; i++)
			if (a1[i] != a2[i]) return false;
		return true;
	}
}
