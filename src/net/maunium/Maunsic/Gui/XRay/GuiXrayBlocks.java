package net.maunium.Maunsic.Gui.XRay;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mcf.davidee.guilib.basic.BasicScreen;
import com.mcf.davidee.guilib.basic.Label;
import com.mcf.davidee.guilib.core.Button;
import com.mcf.davidee.guilib.core.Container;
import com.mcf.davidee.guilib.core.Widget;
import com.mcf.davidee.guilib.vanilla.ButtonVanilla;
import com.mcf.davidee.guilib.vanilla.ScrollbarVanilla;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Actions.ActionXray;
import net.maunium.Maunsic.Util.I18n;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;

/**
 * Gui for managing the blocks that the X-Ray will hide.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class GuiXrayBlocks extends BasicScreen {
	private Maunsic host;
	private Container c, c2;
	private ScrollbarVanilla sb;
	private Label title;
	private ButtonVanilla back, selectAll, deselectAll;
	private List<BlockSelectButton> blocks;
	
	public GuiXrayBlocks(Maunsic host) {
		super(Minecraft.getMinecraft().currentScreen);
		this.host = host;
	}
	
	@Override
	public void onInit() {
		title = new Label(I18n.translate("conf.xray.title"));
		back = new ButtonVanilla(150, 20, I18n.translate("conf.back"), this);
		selectAll = new ButtonVanilla(100, 20, I18n.translate("conf.xray.selectall"), this);
		deselectAll = new ButtonVanilla(100, 20, I18n.translate("conf.xray.deselectall"), this);
		
		blocks = new ArrayList<BlockSelectButton>();
		@SuppressWarnings("unchecked")
		Iterator<Block> i = Block.blockRegistry.iterator();
		while (i.hasNext()) {
			Block b = i.next();
			if (b != null) {
				BlockSelectButton isb = new BlockSelectButton(b, this);
				isb.setSelected(ActionXray.isDisabled(b));
				blocks.add(isb);
			}
		}
		sb = new ScrollbarVanilla(10);
		c2 = new Container(sb, 14, 4);
		c = new Container();
		
		c2.addWidgets(blocks.toArray(new Widget[0]));
		c.addWidgets(title, back, selectAll, deselectAll);
		containers.add(c2);
		containers.add(c);
	}
	
	@Override
	public void onButtonClicked(Button b, int code) {
		if (b instanceof BlockSelectButton) {
			BlockSelectButton isb = (BlockSelectButton) b;
			if (isb.isSelected()) ActionXray.disableBlock(isb.getBase());
			else ActionXray.enableBlock(isb.getBase());
		} else if (b.equals(back)) close();
		else if (b.equals(deselectAll)) {
			for (BlockSelectButton bsb : blocks) {
				bsb.setSelected(false);
				ActionXray.enableBlock(bsb.getBase());
			}
		} else if (b.equals(selectAll)) {
			for (BlockSelectButton bsb : blocks) {
				bsb.setSelected(true);
				ActionXray.disableBlock(bsb.getBase());
			}
		}
	}
	
	@Override
	public void save() {
		if (ActionXray.enabled) Minecraft.getMinecraft().renderGlobal.loadRenderers();
		host.actionXray.saveData(host.getConfig());
	}
	
	@Override
	public void onRevalidate() {
		int y = 30;
		int x1 = width / 2 - BlockSelectButton.defWidth - 2;
		int x2 = width / 2 + 2;
		boolean side = true;
		for (BlockSelectButton isb : blocks) {
			isb.setPosition(side ? x1 : x2, y);
			if (side) side = false;
			else {
				side = true;
				y += 20;
			}
		}
		title.setPosition(width / 2, 15);
		back.setPosition(width / 2 - back.getWidth() / 2, 200);
		selectAll.setPosition(width / 2 - back.getWidth() / 2 - selectAll.getWidth() - 2, 200);
		deselectAll.setPosition(width / 2 + back.getWidth() / 2 + 2, 200);
		sb.setPosition(width / 2 + BlockSelectButton.defWidth + 2, 30);
		c.revalidate(0, 0, width, height);
		c2.revalidate(width / 2 - BlockSelectButton.defWidth - 3, 30, BlockSelectButton.defWidth * 2 + 4, back.getY() - 35);
	}
}
