package net.maunium.Maunsic.Server;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.maunium.Maunsic.Server.Network.Packets.PacketLicence;

import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * The licence handler
 * 
 * @author Tulir293
 * @since 0.1
 */
public class LicensingSystem {
	/** Location of the file to save all the licences to */
	private static final File f = new File(System.getProperty("user.dir") + File.separator + "config" + File.separator + "Maunsic" + File.separator
			+ "licences.maudat");
	/** Map containing all of the mac, licence entries */
	private static Map<String, String> licences = new HashMap<String, String>();
	
	/**
	 * Add the given licence bound to the given MAC address to the licence map.
	 */
	public static void addLicence(String mac, String licence) {
		licences.put(mac, licence);
	}
	
	/**
	 * Query if the saved licence for the given MAC is valid.
	 */
	public static boolean query(String mac) throws IOException {
		String licence = licences.get(mac);
		if (licence != null && !licence.isEmpty()) ServerHandler.sendPacket(new PacketLicence(licence, mac));
		else return false;
		return true;
	}
	
	/**
	 * Open a frame that asks for a licence from the user and tells them their local MAC address.
	 */
	public static void requestLicence(String lmac) {
		JFrame frame = new JFrame("Maunsic Licencer");
		frame.setLayout(null);
		frame.setSize(new Dimension(276, 153));
		frame.setLocationRelativeTo(null);
		frame.setAlwaysOnTop(true);
		
		JLabel label = new JLabel("Please enter your Maunsic licence");
		
		JTextField licence = new JTextField();
		
		JTextField mac = new JTextField("Local MAC: " + lmac);
		mac.setEditable(false);
		
		JButton finish = new JButton("Save and Quit");
		finish.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!licence.getText().isEmpty()) {
					try {
						addLicence(lmac, licence.getText());
						saveLicences();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					FMLCommonHandler.instance().exitJava(0, false);
				}
			}
		});
		
		label.setSize(250, 20);
		licence.setSize(250, 20);
		finish.setSize(250, 30);
		mac.setSize(250, 20);
		label.setLocation(5, 5);
		licence.setLocation(5, 55);
		finish.setLocation(5, 80);
		mac.setLocation(5, 30);
		
		frame.add(label);
		frame.add(mac);
		frame.add(licence);
		frame.add(finish);
		frame.setVisible(true);
	}
	
	/**
	 * Save licences to disk.
	 */
	public static void saveLicences() throws IOException {
		if (!f.getParentFile().exists()) f.getParentFile().mkdirs();
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		
		for (Entry<String, String> e : licences.entrySet()) {
			bw.write(e.getKey());
			bw.write('@');
			bw.write(e.getValue());
			bw.newLine();
		}
		
		bw.flush();
		bw.close();
	}
	
	/**
	 * Load licences from disk.
	 */
	public static void loadLicences() throws IOException {
		if (!f.exists()) return;
		BufferedReader br = new BufferedReader(new FileReader(f));
		
		String s;
		while ((s = br.readLine()) != null) {
			if (s.indexOf('@') == -1) continue;
			String[] ss = s.split("@");
			licences.put(ss[0], ss[1]);
		}
		
		br.close();
	}
}
