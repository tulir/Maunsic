package net.maunium.Maunsic.Server.Network;

import java.util.HashMap;
import java.util.Map;

/**
 * Allows registering packets that can be easily read/written from/to the server.
 * 
 * @author Tulir293
 * @since 0.1
 * @from MaunsicServer
 */
public class PacketRegistry {
	private static Map<Integer, Class<? extends MPacket>> packets = new HashMap<Integer, Class<? extends MPacket>>();
	
	/**
	 * Get the packet with the given ID.
	 * 
	 * @param id The ID of the packet.
	 * @return The packet, or null if not found.
	 */
	public static MPacket getPacket(int id) {
		try {
			return packets.get(id).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Register a packet. The packet must have a public static integer packetId to define the ID of the packet.
	 * 
	 * @param mp The packet class to register.
	 */
	public static void registerPacket(Class<? extends MPacket> mp) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		int id = mp.getField("packetId").getInt(null);
		if (packets.containsKey(id)) return;
		packets.put(id, mp);
	}
}
