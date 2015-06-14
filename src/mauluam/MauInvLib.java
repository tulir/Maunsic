package mauluam;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;

public class MauInvLib extends TwoArgFunction {
	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaValue lib = LuaValue.tableOf();
		lib.set("setSelected", new setSelected());
		env.set("inv", lib);
		return lib;
	}
	
	public static class setSelected extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue slot) {
			InventoryPlayer ip = Minecraft.getMinecraft().thePlayer.inventory;
			ip.currentItem = slot.toint();
			return LuaValue.valueOf(ip.currentItem);
		}
	}
	
	// TODO: Inventory API
}