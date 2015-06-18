package net.maunium.Maunsic.KeyMaucros;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.maunium.Maunsic.KeyMaucros.Commands.MaucroCommand;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

/**
 * A class extending Key Maucro for Key Maucros that execute Command Chains.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class CCKeyMaucro extends KeyMaucro {
	static final long serialVersionUID = 293110001L;
	
	private static Set<MaucroCommand> commands = new HashSet<MaucroCommand>();
	
	/**
	 * Register the given CommandChain key maucro command.
	 * 
	 * @return As defined in {@link java.util.HashSet#add(Object)}
	 */
	public static boolean registerMaucroCommand(MaucroCommand command) {
		return commands.add(command);
	}
	
	private String[] messages;
	
	public CCKeyMaucro(String name, String message, int keyCode, ExecPhase phase, int... shiftKeys) {
		super(name, keyCode, phase, shiftKeys);
		this.setMessages(message);
	}
	
	public CCKeyMaucro(String name, String[] messages, int keyCode, ExecPhase phase, int... shiftKeys) {
		super(name, keyCode, phase, shiftKeys);
		this.setMessages(messages);
	}
	
	@Override
	public Type getType() {
		return Type.COMMANDCHAIN;
	}
	
	public String[] getMessages() {
		return messages;
	}
	
	public String getMessage(int i) {
		return messages[i];
	}
	
	@Override
	public String getData() {
		String rtrn = "";
		for (String s : messages)
			if (rtrn.equals("")) rtrn = s;
			else rtrn = rtrn + "|" + s;
		return rtrn;
	}
	
	public JsonArray getMessagesAsJson() {
		JsonArray ja = new JsonArray();
		for (String s : messages)
			ja.add(new JsonPrimitive(s));
		return ja;
	}
	
	public void setMessages(String unparsed) {
		messages = parse(unparsed);
	}
	
	public void setMessages(String[] messages) {
		this.messages = messages;
	}
	
	private String[] parse(String s) {
		if (s.contains("|")) return s.split(Pattern.quote("|"));
		else return new String[] { s };
	}
	
	public void printMessages() {
		for (String s : messages)
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(" - " + s));
	}
	
	@Override
	public JsonObject toJson() {
		JsonObject jo = new JsonObject();
		jo.addProperty("type", getType().toGuiState());
		jo.addProperty("name", getName());
		jo.addProperty("keycode", getKeyCode());
		jo.add("messages", getMessagesAsJson());
		jo.addProperty("phase", phase.toInt());
		jo.add("shiftkeys", getShiftKeysAsJson());
		return jo;
	}
	
	public static CCKeyMaucro fromJson(JsonObject jo) {
		try {
			String name = jo.get("name").getAsString();
			int keycode = jo.get("keycode").getAsInt();
			
			JsonArray ma = jo.get("messages").getAsJsonArray();
			String[] messages = new String[ma.size()];
			for (int i = 0; i < ma.size(); i++)
				messages[i] = ma.get(i).getAsString();
			
			ExecPhase ep = ExecPhase.fromInt(jo.get("phase").getAsInt());
			
			JsonArray ska = jo.get("shiftkeys").getAsJsonArray();
			int[] shiftkeys = new int[ska.size()];
			for (int i = 0; i < ska.size(); i++)
				shiftkeys[i] = ska.get(i).getAsInt();
			return new CCKeyMaucro(name, messages, keycode, ep, shiftkeys);
		} catch (Throwable t) {
			return null;
		}
	}
	
	@Override
	public void executeMacro() {
		if (Minecraft.getMinecraft() == null) return;
		if (Minecraft.getMinecraft().thePlayer == null) return;
		for (String s : messages)
			if (s.startsWith("#") && s.contains(";")) {
				String cmd = s.substring(1, s.indexOf(';'));
				for (MaucroCommand mc : commands)
					if (mc.getName().equalsIgnoreCase(cmd) || (mc.getOwner() + ":" + mc.getName()).equalsIgnoreCase(cmd)) {
						String arg = s.substring(s.indexOf(';') + 1);
						String[] args = arg.split(Pattern.quote(","));
						mc.execute(cmd, args);
						break;
					}
			} else Minecraft.getMinecraft().thePlayer.sendChatMessage(s);
	}
}
