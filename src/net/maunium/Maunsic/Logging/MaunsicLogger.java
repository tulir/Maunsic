package net.maunium.Maunsic.Logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.maunium.Maunsic.Maunsic;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

/**
 * Used for logging Maunsic things. Singleton instance only.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class MaunsicLogger {
	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");;
	private BufferedWriter bw;
	private static MaunsicLogger instance = null;
	private int debugType = 0;
	private boolean print = true;
	
	public static MaunsicLogger getMaunsicLogger() {
		if (instance == null) throw new NullPointerException("Attempted to get the MaunsicLogger instance when it's null!");
		return instance;
	}
	
	/**
	 * Creates the singleton instance of MaunsicLogger.<br>
	 * Will throw a runtime exception if the instance variable is not null<br>
	 * To recreate the logger, you must use {@link #close()} first
	 * 
	 * @return The created instance.
	 */
	public static MaunsicLogger create() {
		if (instance != null) throw new RuntimeException("Attempted to create a new instance of MaunsicLogger without destroying the old one.");
		instance = new MaunsicLogger();
		return getMaunsicLogger();
	}
	
	private MaunsicLogger() {
		File dir = new File(Minecraft.getMinecraft().mcDataDir + File.separator + "logs" + File.separator + "Maunsic");
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
	
	public void custom(Object o, String cust) {
		custom(o, cust, true);
	}
	
	public void custom(Object o, String cust, boolean print) {
		String msg = "[" + sdf.format(new Date()) + "] [Maunsic/" + cust + "]: " + o;
		if (print) printout(msg, new ChatStyle().setColor(EnumChatFormatting.WHITE));
		else write(msg);
	}
	
	public void info(Object o) {
		String msg = "[" + sdf.format(new Date()) + "] [Maunsic/INFO]: " + o;
		printout(msg, new ChatStyle().setColor(EnumChatFormatting.WHITE));
	}
	
	public void warning(Object o) {
		String msg = "[" + sdf.format(new Date()) + "] [Maunsic/WARNING]: " + o;
		printout(msg, new ChatStyle().setColor(EnumChatFormatting.YELLOW));
	}
	
	public void error(Object o) {
		String msg = "[" + sdf.format(new Date()) + "] [Maunsic/ERROR]: " + o;
		printerr(msg, new ChatStyle().setColor(EnumChatFormatting.RED));
	}
	
	public void debug(Object o) {
		String msg = "[" + sdf.format(new Date()) + "] [Maunsic/DEBUG]: " + o;
		if (debugType < 1) {
			write(msg);
			return;
		}
		printout(msg, new ChatStyle().setColor(EnumChatFormatting.DARK_AQUA));
	}
	
	public void debug(Object o, Object c) {
		String msg = "[" + sdf.format(new Date()) + "] [Maunsic/" + c.getClass().getSimpleName() + "/DEBUG]: " + o;
		if (debugType < 1) {
			write(msg);
			return;
		}
		printout(msg, new ChatStyle().setColor(EnumChatFormatting.DARK_AQUA));
	}
	
	public void trace(Object o) {
		if (debugType < 2) return;
		String msg = "[" + sdf.format(new Date()) + "] [Maunsic/TRACE]: " + o;
		printout(msg, new ChatStyle().setColor(EnumChatFormatting.BLUE));
	}
	
	public void trace(Object o, Object c) {
		if (debugType < 2) return;
		String msg = "[" + sdf.format(new Date()) + "] [Maunsic/" + c.getClass().getSimpleName() + "/TRACE]: " + o;
		printout(msg, new ChatStyle().setColor(EnumChatFormatting.BLUE));
	}
	
	public void catching(Throwable e) {
		String msg = "[" + sdf.format(new Date()) + "] [Maunsic/EXCEPTION] " + e.getClass().getName() + ": " + e.getMessage();
		printerr(msg, new ChatStyle().setColor(EnumChatFormatting.DARK_RED));
		if (debugType >= 1) for (StackTraceElement ste : e.getStackTrace())
			printerr("	at " + ste.toString(), new ChatStyle().setColor(EnumChatFormatting.RED).setItalic(true));
		else for (StackTraceElement ste : e.getStackTrace())
			write("	at " + ste.toString());
	}
	
	private void printout(String msg, ChatStyle cs) {
		write(msg);
		if (print) Maunsic.sout.println(msg);
	}
	
	private void printerr(String msg, ChatStyle cs) {
		write(msg);
		if (print) Maunsic.serr.println(msg);
	}
	
	int lines = 0;
	
	public static int flushLineCount = 25;
	
	private void write(String msg) {
		if (bw == null) return;
		lines++;
		try {
			bw.write(msg);
			bw.newLine();
			if (flushLineCount > 0 && lines > flushLineCount - 1) flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean getPrint() {
		return print;
	}
	
	public void setPrint(boolean print) {
		this.print = print;
	}
	
	public int getDebugType() {
		return debugType;
	}
	
	public void setDebugType(int debugType) {
		this.debugType = debugType;
	}
}
