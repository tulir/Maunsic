package net.maunium.Maunsic.Logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.maunium.Maunsic.Maunsic;

import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;

/**
 * Used for logging Chat. Singleton instance only.
 * 
 * @author Tulir293
 * @since Unknown post-1.0 developement version.
 */
public class ChatLogger {
	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");;
	private BufferedWriter bw;
	private static ChatLogger instance = null;
	
	public static ChatLogger getChatLogger() {
		if (instance == null) throw new NullPointerException("Attempted to get the ChatLogger instance when it's null!");
		return instance;
	}
	
	/**
	 * Creates the singleton instance of ChatLogger.<br>
	 * Will throw a runtime exception if the instance variable is not null<br>
	 * To recreate the logger, you must use {@link #close()} first
	 * 
	 * @return The created instance.
	 */
	public static ChatLogger create() {
		if (instance != null) throw new RuntimeException("Attempted to create a new instance of ChatLogger without destroying the old one.");
		instance = new ChatLogger();
		return getChatLogger();
	}
	
	private ChatLogger() {
		File dir = new File(Minecraft.getMinecraft().mcDataDir + File.separator + "logs" + File.separator + "Chat");
		String s = new SimpleDateFormat("YYYY-MM-dd").format(new Date());
		if (!dir.exists()) dir.mkdir();
		int id = 1;
		for (File f2 : dir.listFiles())
			if (f2.getName().startsWith(s)) {
				int id2 = Integer.parseInt(f2.getName().substring(11, f2.getName().length() - 7));
				if (f2.length() == 0) f2.delete();
				else if (id2 >= id) id = id2 + 1;
			}
		logFile = new File(dir, s + "-" + id + ".maulog");
		if (getLogFile() == null) bw = null;
		else try {
			bw = new BufferedWriter(new FileWriter(getLogFile()));
		} catch (IOException e) {
			bw = null;
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes the buffered writer and nullifies the instance variable.<br>
	 * 
	 * @throws IOException If closing or flushing the buffered writer fails.
	 */
	public void close() throws IOException {
		instance = null;
		sdf = null;
		bw.flush();
		bw.close();
		bw = null;
	}
	
	/**
	 * Flushes the buffered writer.
	 * 
	 * @throws IOException If flushing fails.
	 */
	public void flush() throws IOException {
		bw.flush();
		lines = 0;
	}
	
	private File logFile = null;
	
	public File getLogFile() {
		if (Minecraft.getMinecraft() == null) return null;
		if (Minecraft.getMinecraft().mcDataDir == null) return null;
		return logFile;
	}
	
	public void in(Object o) {
		String msg = "[" + sdf.format(new Date()) + "] [Chat/IN]: " + o;
		write(msg, true);
	}
	
	public void out(Object o) {
		String msg = "[" + sdf.format(new Date()) + "] [Chat/OUT]: " + o;
		write(msg, false);
	}
	
	public void local(Object o) {
		String msg = "[" + sdf.format(new Date()) + "] [Chat/LOCAL]: " + o;
		write(msg, true);
	}
	
	public void custom(Object o, String s, boolean print) {
		String msg = "[" + sdf.format(new Date()) + "] [Chat/" + s + "]: " + o;
		write(msg, print);
	}
	
	int lines = 0;
	
	public static int flushLineCount = 15;
	
	private void write(String msg, boolean print) {
		if (print) Maunsic.sout.println(msg);
		if (bw == null) return;
		lines++;
		try {
			bw.write(msg);
			bw.newLine();
			if (ChatLogger.flushLineCount > 0 && lines > flushLineCount - 1) flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void printChatNoLog(IChatComponent msg) {
		if (Minecraft.getMinecraft() == null) return;
		if (Minecraft.getMinecraft().ingameGUI == null) return;
		if (Minecraft.getMinecraft().ingameGUI.getChatGUI() == null) return;
//		Minecraft.getMinecraft().ingameGUI.getChatGUI().setChatLine(msg, 0, Minecraft.getMinecraft().ingameGUI.getUpdateCounter(), false);
	}
	
	public static void printChat(IChatComponent msg) {
		printChatNoLog(msg);
		if (instance != null) instance.local(msg.getUnformattedText());
	}
}
