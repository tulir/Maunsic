package net.maunium.Maunsic.Server.Network.Packets;

import java.io.IOException;
import java.net.Socket;

import net.maunium.Maunsic.Server.ServerHandler;
import net.maunium.Maunsic.Server.Network.MPacket;

/**
 * Sent by the server to indicate whether or not the clients licence was accepted.
 * 
 * @author Tulir293
 * @since 0.1
 * @from MaunsicServer
 */
public class PacketLicenceResponse implements MPacket {
	public static final int packetId = 2;
	/** True if the licence was accepted, false otherwise. */
	public boolean accepted;
	
	public PacketLicenceResponse() {}
	
	public PacketLicenceResponse(boolean accepted) {
		this.accepted = accepted;
	}
	
	@Override
	public void read(Socket s) throws IOException {
		accepted = s.getInputStream().read() == 0;
	}
	
	@Override
	public void write(Socket s) throws IOException {
		s.getOutputStream().write(packetId);
		s.getOutputStream().write(accepted ? 0 : 1);
	}
	
	@Override
	public void handle(Socket s) {
		if (accepted) ServerHandler.licenced = true;
		else ServerHandler.licenced = false;
	}
}
