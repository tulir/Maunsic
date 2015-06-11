package net.maunium.Maunsic.KeyMaucros;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import net.maunium.Maunsic.Maunsic;
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
	
	public static boolean registerMaucroCommand(MaucroCommand command) {
		return commands.add(command);
	}
	
	public static class CommandAlreadyRegisteredException extends IllegalArgumentException {
		private static final long serialVersionUID = -3783607323630895752L;
		
		public CommandAlreadyRegisteredException(String msg) {
			super(msg);
		}
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
		return getMessagesAsString();
	}
	
	public String getMessagesAsString() {
		String rtrn = "";
		for (String s : messages)
			if (rtrn.equals("")) rtrn = s;
			else rtrn = rtrn + "|" + s;
		return rtrn;
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
	public String toString() {
		if (shiftKeys.length > 0) return getType() + "|" + getName() + "\\~~\\" + getKeyCode() + "\\~~\\" + getMessagesAsString() + "\\~~\\" + phase.toInt()
				+ "\\~~\\" + getShiftKeysToSave();
		else return getType() + "|" + getName() + "\\~~\\" + getKeyCode() + "\\~~\\" + getMessagesAsString() + "\\~~\\" + phase.toInt();
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
		String message = temp[2];
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
		return new CCKeyMaucro(name, message, kc, phase, shiftKeys);
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
	
	@Override
	public boolean equals(KeyMaucro km) {
		if (!(km instanceof CCKeyMaucro)) return false;
		if (!super.equals(km)) return false;
		if (!getMessagesAsString().equals(((CCKeyMaucro) km).getMessagesAsString())) return false;
		return true;
	}
}
