package net.maunium.Maunsic.Server.Network.Packets;

import java.io.IOException;
import java.net.Socket;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Server.Network.MPacket;
import net.maunium.Maunsic.Server.Network.StringIO;

/**
 * Sent by the client to check if the version of the client has been killswitched.
 * 
 * @author Tulir293
 * @since 0.1
 * @from MaunsicServer
 */
public class PacketKillswitched implements MPacket {
	public static final int packetId = 11;
	private String version, key;
	
	public PacketKillswitched() {}
	
	public PacketKillswitched(String version) {
		this.version = version;
		key = null;
	}
	
	public PacketKillswitched(String version, String key) {
		this.version = version;
		this.key = key;
	}
	
	@Override
	public void read(Socket s) throws IOException {
		version = StringIO.read(s.getInputStream());
		if (s.getInputStream().read() == 1) key = StringIO.read(s.getInputStream());
		else key = null;
	}
	
	@Override
	public void write(Socket s) throws IOException {
		s.getOutputStream().write(packetId);
		StringIO.write(s.getOutputStream(), version);
		s.getOutputStream().write(key != null ? 1 : 0);
		if (key != null) StringIO.write(s.getOutputStream(), key);
	}
	
	@Override
	public void handle(Socket s) throws IOException {
		Maunsic.getLogger().warning("The server sent a PacketKillswitched, even though it is only supposed to be sent TO the server.");
	}
}
