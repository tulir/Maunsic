package mauluam;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * A WIP MauluaM library for managing the playerinventory.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class MauInvLib extends TwoArgFunction {
	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaValue lib = LuaValue.tableOf();
		lib.set("select", new select());
		env.set("inv", lib);
		return lib;
	}
	
	public static class select extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue slot) {
			InventoryPlayer ip = Minecraft.getMinecraft().thePlayer.inventory;
			ip.currentItem = slot.toint();
			return LuaValue.valueOf(ip.currentItem);
		}
	}
	
	// TODO: Inventory API
}