package net.maunium.Maunsic.Server.Network.Packets;

import java.io.IOException;
import java.net.Socket;

import net.maunium.Maunsic.Server.ServerHandler;
import net.maunium.Maunsic.Server.Network.MPacket;

public class PacketKillswitchResponse implements MPacket {
	public static final int packetId = 12, NOT_KILLSWITCHED = 1, KILLSWITCHED_BUT_KEY_BYPASS = 2, KILLSWITCHED_NO_KEY = 3, KILLSWITCHED_INVALID_KEY = 4;
	private int data;
	
	public PacketKillswitchResponse() {}
	
	public PacketKillswitchResponse(int data) {
		this.data = data;
	}
	
	@Override
	public void read(Socket s) throws IOException {
		data = s.getInputStream().read();
	}
	
	@Override
	public void write(Socket s) throws IOException {
		s.getOutputStream().write(packetId);
		s.getOutputStream().write(data);
	}
	
	@Override
	public void handle(Socket s) {
		if (data == NOT_KILLSWITCHED || data == KILLSWITCHED_BUT_KEY_BYPASS) ServerHandler.killswitched = false;
		else ServerHandler.killswitched = true;
	}
}
