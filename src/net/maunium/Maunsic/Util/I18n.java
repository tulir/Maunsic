package net.maunium.Maunsic.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Server.ServerHandler;

/**
 * An I18n class for Maunsic to have better control over things.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class I18n {
	private static Properties lang = new Properties();
	
	/**
	 * Initializes Maunsic I18n using the given InputStream
	 * 
	 * @throws IOException As defined in properties.load(InputStream r)
	 */
	public static void init(InputStream is) throws IOException {
		lang.load(is);
	}
	
	/**
	 * Initializes Maunsic I18n using the given Reader
	 * 
	 * @throws IOException As defined in properties.load(Reader r)
	 */
	public static void init(Reader r) throws IOException {
		lang.load(r);
	}
	
	/**
	 * Initializes Maunsic I18n using the given language file.
	 * 
	 * @param langDir The directory to look for the language file in.
	 * @param language The language file name to use (without .lang ending)
	 * @throws FileNotFoundException If the given file and the default language file don't exist.
	 * @throws IOException As defined in I18n.init(Reader r)
	 */
	public static void init(File langDir, String language) throws IOException {
		File f = new File(langDir, language + ".lang");
		if (!f.exists()) {
			Maunsic.getLogger().warning("Language not found: " + language + ". Trying en_US.");
			f = new File(langDir, "en_US.lang");
			if (!f.exists()) {
				Maunsic.getLogger().error("en_US.lang not found. Using in-jar language.");
				init(Maunsic.class.getResourceAsStream("/lang/en_US.lang"));
			}
		}
		init(new InputStreamReader(new FileInputStream(f), "UTF-8"));
	}
	
	/**
	 * Formats an I18n node with possible arguments.
	 * 
	 * @param node The node to format.
	 * @param replace The arguments to replace {i}'s with.
	 * @return The formatted String.
	 */
	public static String format(String node, Object... replace) {
		if (!ServerHandler.canUse()) return "";
		if (lang.containsKey(node)) {
			String rtrn = lang.getProperty(node);
			int i = 0;
			for (Object o : replace) {
				rtrn = rtrn.replace("{" + i + "}", o + "");
				i++;
			}
			return rtrn;
		} else return node;
	}
}