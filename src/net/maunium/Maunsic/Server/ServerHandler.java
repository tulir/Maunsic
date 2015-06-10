package net.maunium.Maunsic.Server;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.Collections;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.Level;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Core.MaunsiCoreLoader;
import net.maunium.Maunsic.Server.Network.MPacket;
import net.maunium.Maunsic.Server.Network.PacketRegistry;
import net.maunium.Maunsic.Server.Network.Packets.PacketKillswitchResponse;
import net.maunium.Maunsic.Server.Network.Packets.PacketKillswitched;
import net.maunium.Maunsic.Server.Network.Packets.PacketLicence;
import net.maunium.Maunsic.Server.Network.Packets.PacketLicenceResponse;

import net.minecraftforge.fml.common.FMLCommonHandler;

public class ServerHandler {
	public static boolean killswitched = true, licenced = false;
	private static String mac;
	private static Socket s;
	
	public static void magic() {
		try {
			NetworkInterface net = null;
			for (NetworkInterface ni : Collections.list(NetworkInterface.getNetworkInterfaces())) {
				if (!ni.isUp() || ni.isVirtual() || ni.isLoopback() || ni.isPointToPoint()) continue;
				net = ni;
				break;
			}
			if (net == null) throw new IOException("No valid network interfaces found!");
			byte[] macB = net.getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < macB.length; i++)
				sb.append(String.format("%02X%s", macB[i], i < macB.length - 1 ? "-" : ""));
			mac = sb.toString();
		} catch (IOException e) {
			mac = null;
			MaunsiCoreLoader.log(Level.ERROR, "Failed to get MAC address");
			MaunsiCoreLoader.catching(Level.ERROR, e);
			return;
		}
		
		MaunsiCoreLoader.log(Level.INFO, "Checking licence/killswitch status...");
		
		try {
			PacketRegistry.registerPacket(PacketLicence.class);
			PacketRegistry.registerPacket(PacketLicenceResponse.class);
			PacketRegistry.registerPacket(PacketKillswitched.class);
			PacketRegistry.registerPacket(PacketKillswitchResponse.class);
		} catch (Exception e) {
			MaunsiCoreLoader.catching(Level.ERROR, e);
		}
		try {
			s = new Socket("maunium.net", 29354);
		} catch (IOException e) {
			MaunsiCoreLoader.catching(Level.ERROR, e);
		}
		
		try {
			MaunsiCoreLoader.log(Level.DEBUG, "Sending killswitch query...");
			sendPacket(new PacketKillswitched(Maunsic.version));
			readResponse();
		} catch (IOException e) {
			MaunsiCoreLoader.log(Level.ERROR, "Failed to send killswitch query");
			MaunsiCoreLoader.catching(Level.ERROR, e);
		}
		
		if (ServerHandler.killswitched) {
			try {
				close();
			} catch (IOException e) {
				MaunsiCoreLoader.catching(Level.ERROR, e);
			}
			MaunsiCoreLoader.log(Level.ERROR, "Version killswitched; Informing the user.");
			JOptionPane.showMessageDialog(null, "The version of Maunsic that you\n" + "are using has been killswitched", "Killswitched",
					JOptionPane.ERROR_MESSAGE);
			FMLCommonHandler.instance().exitJava(0, false);
			return;
		}
		
		try {
			MaunsiCoreLoader.log(Level.DEBUG, "Loading licences from file...");
			LicensingSystem.loadLicences();
			MaunsiCoreLoader.log(Level.DEBUG, "Sending licence query...");
			if (LicensingSystem.query(mac)) readResponse();
		} catch (Exception e) {
			MaunsiCoreLoader.log(Level.ERROR, "Failed to send licence query");
			MaunsiCoreLoader.catching(Level.ERROR, e);
		}
		
		try {
			close();
		} catch (IOException e) {
			MaunsiCoreLoader.catching(Level.ERROR, e);
		}
		
		if (!ServerHandler.licenced) {
			MaunsiCoreLoader.log(Level.ERROR, "Invalid licence; Requesting new one from user.");
			LicensingSystem.requestLicence(mac);
		} else MaunsiCoreLoader.log(Level.INFO, "All checks passes; Granting access to Maunsic");
	}
	
	public static boolean canUse() {
		return !killswitched && licenced;
	}
	
	public static void sendPacket(MPacket p) throws IOException {
		p.write(s);
	}
	
	public static void readResponse() throws IOException {
		int pid = s.getInputStream().read();
		MPacket p = PacketRegistry.getPacket(pid);
		p.read(s);
		p.handle(s);
	}
	
	public static void close() throws IOException {
		s.getOutputStream().write(255);
		s.close();
	}
}