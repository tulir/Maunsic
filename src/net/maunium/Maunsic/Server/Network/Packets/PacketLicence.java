package net.maunium.Maunsic.Server.Network.Packets;

import java.io.IOException;
import java.net.Socket;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Server.Network.MPacket;
import net.maunium.Maunsic.Server.Network.StringIO;

/**
 * Sent by the client to validate its licence.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class PacketLicence implements MPacket {
	public static final int packetId = 1;
	/** The licence that is to be validated */
	private String licence;
	/** The MAC address of the user, used to validate the licence. */
	private String mac;
	
	public PacketLicence() {}
	
	public PacketLicence(String licence, String mac) {
		this.licence = licence;
		this.mac = mac;
	}
	
	/**
	 * Set the licence value.
	 */
	public void setLicense(String s) {
		licence = s;
	}
	
	/**
	 * Get the licence value.
	 */
	public String getLicense() {
		return licence;
	}
	
	/**
	 * Set the MAC value.
	 */
	public void setMAC(String s) {
		mac = s;
	}
	
	/**
	 * Get the MAC value.
	 */
	public String getMAC() {
		return mac;
	}
	
	@Override
	public void read(Socket s) throws IOException {
		licence = StringIO.read(s.getInputStream());
		mac = StringIO.read(s.getInputStream());
	}
	
	@Override
	public void write(Socket s) throws IOException {
		s.getOutputStream().write(packetId);
		StringIO.write(s.getOutputStream(), licence);
		StringIO.write(s.getOutputStream(), mac);
	}
	
	@Override
	public void handle(Socket s) throws IOException {
		Maunsic.getLogger().warning("The server sent a PacketKillswitched, even though it is only supposed to be sent TO the server.");
	}
}
