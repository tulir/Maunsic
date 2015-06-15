package net.maunium.Maunsic.Gui;

import com.mcf.davidee.guilib.basic.BasicScreen;
import com.mcf.davidee.guilib.basic.Label;
import com.mcf.davidee.guilib.core.Button;
import com.mcf.davidee.guilib.core.Container;
import com.mcf.davidee.guilib.vanilla.ButtonVanilla;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Util.I18n;

import net.minecraft.client.Minecraft;

/**
 * The main config Gui for Maunsic.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class GuiMaunsic extends BasicScreen {
	private Maunsic host;
	
	private Container[] pages;
	private static int page = 0;
	
	private Label title;
	private ButtonVanilla keys;
	
	public GuiMaunsic(Maunsic host) {
		super(null);
		this.host = host;
	}
	
	@Override
	protected void onInit() {
		pages = new Container[] { new Container() };
		
		title = new Label("");
		keys = new ButtonVanilla(I18n.translate("conf.keys"), this);
		
		pages[0].addWidgets(keys);
	}
	
	@Override
	protected void onRevalidate() {
		containers.clear();
		containers.add(pages[page]);
		
		title.setText(I18n.translate("conf.title", Maunsic.longVersion, page + 1));
		title.setPosition(width / 2, 15);
		pages[page].addWidgets(title);
		
		keys.setPosition(x(1), y(1));
		
		Maunsic.getLogger().trace("Revalidating container for page " + page, this);
		pages[page].revalidate(0, 0, width, height);
	}
	
	@Override
	protected void onButtonClicked(Button b, int code) {
		if (code != 0) return;
		// Yes, they are supposed to be the same instance, so using == is just fine.
		if (b == keys) Minecraft.getMinecraft().displayGuiScreen(new GuiKeybinds(this, host));
	}
	
	@Override
	protected void save() {
		host.saveConfig();
	}
	
	private int y(int y) {
		return 25 + y * 25;
	}
	
	private int x(int x) {
		if (x == 1) return width / 2 - 150 - 12;
		else if (x == 2) return width / 2 + 12;
		else return x;
	}
}
