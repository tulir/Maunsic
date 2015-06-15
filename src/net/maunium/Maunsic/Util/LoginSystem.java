package net.maunium.Maunsic.Util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import net.maunium.Maunsic.Maunsic;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class LoginSystem {
	/**
	 * Logs in with the given username and password and then creates an instance of the Session class and returns it.
	 * 
	 * @param username The username (or email) to log in with.
	 * @param password The password to log in with.
	 * @return The created instance of Session.
	 * @throws LoginException If logging in fails
	 */
	public static Session login(String username, String password) throws LoginException {
		// The debug and trace logger calls explain this fairly well.
		Maunsic.getLogger().debug("Attempting to create a session with username " + username + ".");
		Maunsic.getLogger().trace("Creating JSON request payload");
		JsonObject agent = new JsonObject();
		agent.addProperty("name", "Minecraft");
		agent.addProperty("version", 1);
		JsonObject payload = new JsonObject();
		payload.add("agent", agent);
		payload.addProperty("username", username);
		payload.addProperty("password", password);
		
		JsonObject response;
		try {
			Maunsic.getLogger().trace("Posting the request to Mojang Auth server.");
			response = LoginSystem.post("https://authserver.mojang.com/authenticate", payload);
		} catch (IOException e) {
			if (e.getMessage().equals("Server returned HTTP response code: 403 for URL: https://authserver.mojang.com/authenticate")) throw new LoginException(
					"Invalid username or password (HTTP 403 Forbidden)");
			else throw new LoginException(e.getMessage());
		}
		if (response == null) throw new LoginException("Invalid response from auth server.");
		Maunsic.getLogger().trace("Response received. Reading data...");
		
		if (response.has("accessToken") && response.has("availableProfiles")) {
			Maunsic.getLogger().trace("Response OK. Creating Session and returning it");
			JsonObject profile = response.getAsJsonArray("availableProfiles").get(0).getAsJsonObject();
			Session s = new Session(profile.get("name").getAsString(), profile.get("id").getAsString(), response.get("accessToken").getAsString(),
					Session.Type.MOJANG.toString());
			return s;
		} else if (response.has("errorMessage")) throw new LoginException(response.get("errorMessage").getAsString());
		else throw new LoginException("Invalid response from auth server.");
	}
	
	/**
	 * Set the current Minecraft session to the given one.<br>
	 * Uses reflection.
	 * 
	 * @param s The session to use.
	 * @return If setting the session succeeded
	 */
	public static boolean setSession(Session s) {
		try {
			ReflectionHelper.setPrivateValue(Minecraft.class, Minecraft.getMinecraft(), s, "field_71449_j");
			return true;
		} catch (Exception e1) {
			try {
				ReflectionHelper.setPrivateValue(Minecraft.class, Minecraft.getMinecraft(), s, "session");
				return true;
			} catch (Exception e2) {
				return false;
			}
		}
	}
	
	/**
	 * Sets the username of the current session to the given value.
	 * 
	 * @return If setting the username succeeded.
	 */
	public static boolean setUsername(String s) {
		try {
			ReflectionHelper.setPrivateValue(Session.class, Minecraft.getMinecraft().getSession(), s, "field_74286_b");
			return true;
		} catch (Exception e1) {
			try {
				ReflectionHelper.setPrivateValue(Session.class, Minecraft.getMinecraft().getSession(), s, "username");
				return true;
			} catch (Exception e2) {
				return false;
			}
		}
	}
	
	public static class LoginException extends Exception {
		private static final long serialVersionUID = -8825220800285796475L;
		
		public LoginException(String reason) {
			super(reason);
		}
	}
	
	private static JsonObject post(String targetURL, JsonObject request) throws IOException {
		Maunsic.getLogger().trace("Posting a JSON request to " + targetURL + ".");
		String requestValue = request.toString();
		HttpsURLConnection connection = null;
		try {
			Maunsic.getLogger().trace("Creating connection.");
			URL url = new URL(targetURL);
			connection = (HttpsURLConnection) url.openConnection();
			Maunsic.getLogger().trace("Setting properties");
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			
			connection.setRequestProperty("Content-Length", Integer.toString(requestValue.length()));
			connection.setRequestProperty("Content-Language", "en-US");
			
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setReadTimeout(3000);
			connection.setConnectTimeout(3000);
			
			Maunsic.getLogger().trace("Connecting...");
			connection.connect();
			
			Maunsic.getLogger().trace("Writing request to server.");
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			out.writeBytes(requestValue);
			out.flush();
			
			Maunsic.getLogger().trace("Reading response from server.");
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer response = new StringBuffer();
			String line;
			while ((line = reader.readLine()) != null) {
				if (response.length() > 0) response.append('\n');
				response.append(line);
			}
			if (response.toString().trim().isEmpty()) return null;
			try {
				Maunsic.getLogger().trace("Parsing response");
				JsonParser parser = new JsonParser();
				Object responseObject = parser.parse(response.toString());
				if (!(responseObject instanceof JsonObject)) throw new IOException("Response not type of JSONObject: " + response);
				reader.close();
				Maunsic.getLogger().trace("All done!");
				return (JsonObject) responseObject;
			} catch (JsonParseException exception) {
				reader.close();
				throw new IOException("Response not valid JSON: " + response, exception);
			}
		} catch (IOException exception) {
			throw exception;
		} catch (Exception exception) {
			throw new IOException("Error connecting", exception);
		} finally {
			if (connection != null) connection.disconnect();
		}
	}
}
