package net.maunium.Maunsic.Gui.XRay;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.mcf.davidee.guilib.core.Widget;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEndPortal;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

/**
 * Used by BlockSelectButton that is used by GuiXrayBlocks
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class BlockTooltip extends Widget {
	
	public static final Map<Class<?>, String> NAME_MAP = new HashMap<Class<?>, String>();
	
	static {
		NAME_MAP.put(BlockPistonExtension.class, "Piston Extension");
		NAME_MAP.put(BlockPistonMoving.class, "Piston Moving");
		NAME_MAP.put(BlockEndPortal.class, "End Portal");
	}
	
	private static String getUnknownName(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof ItemBlock) {
			Class<? extends Block> blockClass = ((ItemBlock) item).block.getClass();
			return NAME_MAP.containsKey(blockClass) ? NAME_MAP.get(blockClass) : "Unknown";
		}
		return "Unknown";
	}
	
	private final List<String> tooltips;
	private final FontRenderer font;
	private final GuiScreen parent;
	
	/**
	 * See {@link net.minecraft.client.gui.inventory.GuiContainer#drawScreen} for more information.
	 */
	@SuppressWarnings("unchecked")
	public BlockTooltip(ItemStack stack, String localized, int baseId, GuiScreen parent) {
		super(0, 0);
		
		if (stack.getItem() != null) {
			tooltips = stack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);
			if (!tooltips.isEmpty()) {
				String name = tooltips.get(0);
				if (name.startsWith("tile.null.name")) name = name.replace("tile.null.name", getUnknownName(stack));
				tooltips.set(0, stack.getRarity().rarityColor.toString() + name);
				for (int i = 1; i < tooltips.size(); ++i)
					tooltips.set(i, EnumChatFormatting.GRAY.toString() + tooltips.get(i));
			}
			FontRenderer itemRenderer = stack.getItem().getFontRenderer(stack);
			font = itemRenderer == null ? mc.fontRendererObj : itemRenderer;
		} else {
			if (localized.startsWith("tile.null.name")) localized = localized.replace("tile.null.name", getUnknownName(stack));
			tooltips = Arrays.asList(localized + " " + idString(baseId));
			font = mc.fontRendererObj;
		}
		this.parent = parent;
		width = getMaxStringWidth();
		height = tooltips.size() > 1 ? tooltips.size() * 10 : 8;
	}
	
	private String idString(int id) {
		String s = id + "";
		if (s.length() == 1) s = "#000" + s;
		else if (s.length() == 2) s = "#00" + s;
		else if (s.length() == 3) s = "#0" + s;
		return "(" + s + ")";
	}
	
	@Override
	public void setPosition(int newX, int newY) {
		x = newX + 12;
		y = newY - 12;
		if (x + width + 6 > parent.width) x -= 28 + width;
		if (y + height + 6 > parent.height) y = parent.height - height - 6;
	}
	
	/**
	 * See {@link net.minecraft.client.gui.inventory.GuiContainer#drawHoveringText}
	 */
	@Override
	public void draw(int mx, int my) {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		if (!tooltips.isEmpty()) {
			final int outlineColor = 0xf0100010;
			drawRect(x - 3, y - 4, x + width + 3, y - 3, outlineColor);
			drawRect(x - 3, y + height + 3, x + width + 3, y + height + 4, outlineColor);
			drawRect(x - 3, y - 3, x + width + 3, y + height + 3, outlineColor);
			drawRect(x - 4, y - 3, x - 3, y + height + 3, outlineColor);
			drawRect(x + width + 3, y - 3, x + width + 4, y + height + 3, outlineColor);
			int gradient1 = 1347420415;
			int gradient2 = (gradient1 & 16711422) >> 1 | gradient1 & -16777216;
			drawGradientRect(x - 3, y - 3 + 1, x - 3 + 1, y + height + 3 - 1, gradient1, gradient2);
			drawGradientRect(x + width + 2, y - 3 + 1, x + width + 3, y + height + 3 - 1, gradient1, gradient2);
			drawGradientRect(x - 3, y - 3, x + width + 3, y - 3 + 1, gradient1, gradient1);
			drawGradientRect(x - 3, y + height + 2, x + width + 3, y + height + 3, gradient2, gradient2);
			for (int index = 0; index < tooltips.size(); ++index) {
				font.drawString(tooltips.get(index), x, y, -1);
				if (index == 0) y += 2;
				y += 10;
			}
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	@Override
	public boolean click(int mx, int my, int code) {
		return false;
	}
	
	private int getMaxStringWidth() {
		int max = 0;
		for (String s : tooltips) {
			int width = font.getStringWidth(s);
			if (width > max) max = width;
		}
		return max;
	}
	
}