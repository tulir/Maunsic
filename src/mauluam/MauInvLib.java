package mauluam;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

/**
 * A WIP MauluaM library for managing the player's inventory.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class MauInvLib extends TwoArgFunction {
	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaValue lib = LuaValue.tableOf();
		lib.set("select", new select());
		lib.set("move", new move());
		lib.set("getType", new getType());
		lib.set("drop", new drop());
		lib.set("dropAll", new dropAll());
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
	
	public static class move extends TwoArgFunction {
		@Override
		public LuaValue call(LuaValue arg1, LuaValue arg2) {
			
			return LuaValue.NIL;
		}
	}
	
	public static class getType extends VarArgFunction {
		@Override
		public LuaValue invoke(Varargs vardata) {
			EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
			
			ItemStack is;
			if (vardata.narg() == 0) is = p.getHeldItem();
			else is = p.getInventory()[vardata.arg1().toint()];
			if (is == null) return LuaValue.NIL;
			
			return LuaValue.valueOf(is.getItem().getUnlocalizedNameInefficiently(is));
			
		}
	}
	
	public static class drop extends VarArgFunction {
		@Override
		public LuaValue invoke(Varargs vardata) {
			Minecraft.getMinecraft().thePlayer.sendQueue
					.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
			return LuaValue.NIL;
		}
	}
	
	public static class dropAll extends VarArgFunction {
		@Override
		public LuaValue invoke(Varargs vardata) {
			Minecraft.getMinecraft().thePlayer.sendQueue
					.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ALL_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
			return LuaValue.NIL;
		}
	}
}