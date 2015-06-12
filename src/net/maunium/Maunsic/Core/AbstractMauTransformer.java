package net.maunium.Maunsic.Core;

import org.apache.logging.log4j.Level;

import net.maunium.Maunsic.Server.ServerHandler;

import net.minecraft.launchwrapper.IClassTransformer;

/**
 * Base for transformers that abstracts whether or not the environment is obfuscated.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public abstract class AbstractMauTransformer implements IClassTransformer {
	private final String obfName, deobfName;
	
	public AbstractMauTransformer(String deobfName, String obfName) {
		this.obfName = obfName;
		this.deobfName = deobfName;
	}
	
	/**
	 * This method is called only to transform the class given in the constructor.
	 * 
	 * @param bytes The original class bytes.
	 * @param obf If the class is obfuscated or not.
	 * @return The transformed class bytes
	 */
	public abstract byte[] transform(byte[] bytes, boolean obf);
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		// I aint transformin' anything if I've been killswitched or don't have a valid licence.
		if (!ServerHandler.canUse()) return basicClass;
		try {
			// If the name equals the deobfuscated name, call transform with obf false
			if (name.equals(deobfName)) return transform(basicClass, false);
			// If the name equals the obfuscated name, call transform with obf true
			else if (name.equals(obfName)) return transform(basicClass, true);
		} catch (Throwable t) {
			MaunsiCoreLoader.log.fatal("Fatal error while attempting to transform " + name);
			MaunsiCoreLoader.log.catching(Level.FATAL, t);
		}
		
		// Class name didn't match, return default.
		return basicClass;
	}
	
	/**
	 * Get the name of the class.
	 * 
	 * @param obf Return the obfuscated or deobfuscated name.
	 * @return The class name.
	 */
	public final String getName(boolean obf) {
		if (obf) return obfName;
		else return deobfName;
	}
}
