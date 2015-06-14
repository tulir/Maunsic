package mauluam;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Simple file utilities for MauluaM
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class MauFileUtils {
	/**
	 * Try to write the contents to a file.
	 * 
	 * @param f The file to write the contents to.
	 * @param content The content to write.
	 * @return If the writing succeeded or not.
	 */
	public static boolean tryWrite(File f, Object... content) {
		// Null check
		if (f != null || content == null) return false;
		// Calls write(f, array) and catches possible exceptions.
		try {
			write(f, content);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	/**
	 * Try to write the contents to a file.
	 * 
	 * @param f The file to write the contents to.
	 * @param content The content to write.
	 * @return If the writing succeeded or not
	 */
	public static boolean tryWrite(File f, Collection<?> content) {
		// Null check
		if (f == null || content == null) return false;
		// Converts the content to an array and calls tryWrite(f, array) with it.
		return tryWrite(f, content.toArray());
	}
	
	/**
	 * Write the contents to a file.
	 * 
	 * @param f The file to write the contents to.
	 * @param content The content to write.
	 * @throws IOException If writing fails.
	 * @throws FileNotFoundException If the file can't be found.
	 */
	public static void write(File f, Object... content) throws IOException, FileNotFoundException {
		// Open a buffered writer
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		// Iterate through the content.
		for (Object o : content) {
			// Write entry
			bw.write(o.toString());
			// Make a new line
			bw.newLine();
		}
		// Close the buffered writer
		bw.close();
	}
	
	/**
	 * Write the content to a file.
	 * 
	 * @param f The file to write the content to.
	 * @param content The content to write.
	 * @throws IOException If writing fails.
	 * @throws FileNotFoundException If the file can't be found
	 */
	public static void write(File f, Collection<?> content) throws IOException, FileNotFoundException {
		// Converts the content to an array and calls write(f, array) with it.
		write(f, content.toArray());
	}
	
	/**
	 * Try to clear a file.
	 * 
	 * @param f The file to clear
	 * @return If clearing was successful or not
	 */
	public static boolean tryClear(File f) {
		// Null check
		if (f == null) return false;
		// Calls clear(f) and catches possible exceptions.
		try {
			clear(f);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	/**
	 * Clear a file.
	 * 
	 * @param f The file to clear
	 * @throws IOException If clearing the file failed.
	 */
	public static void clear(File f) throws IOException {
		// If the file exists, delete it.
		if (f.exists()) f.delete();
		// Create a new file.
		f.createNewFile();
	}
	
	/**
	 * Try to read the contents of a file.
	 * 
	 * @param f The file to read.
	 * @return The contents of the file, or null if reading failed.
	 */
	public static List<String> tryRead(File f) {
		// Null check
		if (f == null) return null;
		// Call read(f) and catch possible exceptions
		try {
			return read(f);
		} catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * Read the contents of a file.
	 * 
	 * @param f The file to read.
	 * @return The contents of the file
	 * @throws IOException If reading the file fails
	 * @throws FileNotFoundException If the file couldn't be found
	 */
	public static List<String> read(File f) throws IOException, FileNotFoundException {
		// Open a buffered reader.
		BufferedReader br = new BufferedReader(new FileReader(f));
		// Create the return list.
		List<String> rtrn = new ArrayList<String>();
		// Iterate through the contents of the file.
		String s;
		while ((s = br.readLine()) != null)
			// Add the line to the return list.
			rtrn.add(s);
		// Close the buffered reader.
		br.close();
		// Return the list.
		return rtrn;
	}
	
	/**
	 * Try to read the contents of an URL.
	 * 
	 * @param url The URL to read.
	 * @return The contents of the URL, or null if failed.
	 */
	public static List<String> tryRead(URL url) {
		// Null check
		if (url == null) return null;
		// Call read(url) and catch possible exceptions
		try {
			return read(url);
		} catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * Read the contents of an URL.
	 * 
	 * @param url The URL to read.
	 * @return The contents of the URL.
	 * @throws IOException
	 */
	public static List<String> read(URL url) throws IOException {
		// Open a buffered reader
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
		// Create the return list
		List<String> rtrn = new ArrayList<String>();
		// Iterate through the contents.
		String s;
		while ((s = br.readLine()) != null)
			// Add the line to the return list
			rtrn.add(s);
		// Close the buffered reader
		br.close();
		// Return the list
		return rtrn;
	}
}
