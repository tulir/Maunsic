package net.maunium.Maunsic.Core;

import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import net.maunium.Maunsic.Server.ServerHandler;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;

@MCVersion(value = "1.8")
@Name(value = "MaunsiCore")
public class MaunsiCoreLoader implements IFMLLoadingPlugin {
	private static final Logger log = FMLLog.getLogger();
	
	public MaunsiCoreLoader() {
//		Jasypt restriction remove
//		Restrictions.remove();
		ServerHandler.magic();
	}
	
	@Override
	public String[] getASMTransformerClass() {
		return new String[] { /* ClassNameHere.class.getName() */};
	}
	
	@Override
	public String getModContainerClass() {
		return null;
	}
	
	@Override
	public String getSetupClass() {
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data) {}
	
	@Override
	public String getAccessTransformerClass() {
		return MaunsicAT.class.getName();
	}
	
	public static void log(Level l, String msg) {
		log.log(l, "[MaunsiCore] " + msg);
	}
	
	public static void catching(Level l, Throwable t) {
		log.catching(l, t);
	}
}
