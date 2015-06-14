package net.maunium.Maunsic.Server.Network;

import java.io.IOException;
import java.net.Socket;

/**
 * The base of a Maunsic Packet.<br>
 * All classes implementing this must have a public static final int packetId.<br>
 * 
 * @author Tulir293
 * @since 0.1
 * @from MaunsicServer
 */
public interface MPacket {
	/**
	 * Read this kind of a packet from the given socket. The socket input stream should only contain the data, not the ID.
	 */
	public void read(Socket s) throws IOException;
	
	/**
	 * Read this kind of a packet from the given socket. This only write both, the packet ID first and then the data.
	 */
	public void write(Socket s) throws IOException;
	
	/**
	 * Handle a received packet.
	 */
	public void handle(Socket s) throws IOException;
}
