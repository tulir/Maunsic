package net.maunium.Maunsic.Server.Network;

public class PacketRegistry {
	private static Class<?>[] packets = new Class<?>[20];
	
	public static MPacket getPacket(int id) {
		try {
			return (MPacket) packets[id].newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void registerPacket(Class<? extends MPacket> mp) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		int id = mp.getField("packetId").getInt(null);
		if (packets[id] != null) return;
		packets[id] = mp;
	}
}
