package mauluam;

import java.io.File;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseIoLib;
import org.luaj.vm2.lib.jse.JseOsLib;
import org.luaj.vm2.lib.jse.LuajavaLib;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Server.ServerHandler;

import net.minecraft.client.Minecraft;

/**
 * A class for executing lua files.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class LuaExecutionThread extends Thread {
	private static int lastUID = 0;
	
	private static int getNextUID() {
		lastUID++;
		return lastUID;
	}
	
	private int uid;
	private String name;
	private long startTime = -1;
	private File f;
	
	public void setTName(String name) {
		super.setName(name);
		this.name = name;
	}
	
	public String getTName() {
		return name;
	}
	
	public int getUID() {
		return uid;
	}
	
	public File getFile() {
		return f;
	}
	
	public LuaExecutionThread(String name, File f) {
		super();
		uid = getNextUID();
		this.name = name;
		this.f = f;
		setName(name);
	}
	
	@Override
	public void run() {
		if (!ServerHandler.canUse()) return;
		Globals globals = new Globals();
		globals.load(new JseBaseLib());
		globals.load(new PackageLib());
		globals.load(new Bit32Lib());
		globals.load(new TableLib());
		globals.load(new StringLib());
		globals.load(new JseIoLib());
		globals.load(new JseOsLib());
		globals.load(new LuajavaLib());
		globals.load(new MauGDataLib());
		globals.load(new MauInvLib());
		globals.load(new MauKeyboardLib());
		globals.load(new MauLocationLib());
		globals.load(new MauMathLib());
		globals.load(new MaunsicUtilsLib());
		globals.load(new MauIOLib());
		globals.load(new MauMcLib());
		globals.load(new MauActionLib());
		globals.load(new MauThreadLib());
		globals.load(new MauWorldLib());
		LoadState.install(globals);
		LuaC.install(globals);
		try {
			LuaValue chunk = globals.loadfile(f.getPath());
			startTime = Minecraft.getSystemTime();
			chunk.call();
		} catch (ThreadDeath td) {} catch (Throwable t) {
			Maunsic.printChatError("message.lua.execfail", f.getName(), t.getMessage());
			Maunsic.getLogger().catching(t);
		}
		MauThreadLib.removeThread(name);
	}
	
	public long getStartTime() {
		return startTime;
	}
	
	@Override
	public String toString() {
		return "Lua Execution Thread #" + uid + ": \"" + name + "\" executing file \"" + f.getName() + "\"";
	}
}
