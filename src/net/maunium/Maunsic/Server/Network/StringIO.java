package net.maunium.Maunsic.Server.Network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Basic UTF-8 String input/output to streams.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class StringIO {
	/**
	 * Read an UTF-8 string with a maximum length of 255 from the given input stream.
	 * 
	 * @param in The input stream to read from.
	 * @return The string.
	 * @throws IOException Errors from input stream reading.
	 */
	public static String read(InputStream in) throws IOException {
		int length = in.read();
		byte[] br = new byte[length];
		in.read(br);
		return new String(br, StandardCharsets.UTF_8);
	}
	
	/**
	 * Write an UTF-8 string with a maximum length of 255 to the given output stream.
	 * 
	 * @param out The output stream to write to.
	 * @param s The string.
	 * @throws IOException Errors from output stream writing.
	 */
	public static void write(OutputStream out, String s) throws IOException {
		int length = s.length();
		byte[] br = s.getBytes(StandardCharsets.UTF_8);
		out.write(length);
		out.write(br);
	}
}
