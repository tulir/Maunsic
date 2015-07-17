package net.maunium.Maunsic.Gui;

import java.io.File;
import java.util.regex.Pattern;

import com.mcf.davidee.guilib.basic.BasicScreen;
import com.mcf.davidee.guilib.basic.FocusedContainer;
import com.mcf.davidee.guilib.basic.Label;
import com.mcf.davidee.guilib.core.Button;
import com.mcf.davidee.guilib.core.Container;
import com.mcf.davidee.guilib.core.Scrollbar;
import com.mcf.davidee.guilib.vanilla.ButtonVanilla;
import com.mcf.davidee.guilib.vanilla.ScrollbarVanilla;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Util.I18n;

import net.minecraft.client.gui.Gui;

/**
 * Gui for language channging
 * 
 * @author Tulir293
 * @since 0.1
 */
public class GuiLanguage extends BasicScreen {
	private Maunsic host;
	private Container c, lc;
	private Scrollbar s;
	private Label title;
	private ButtonVanilla refresh, back;
	
	public GuiLanguage(GuiMaunsic parent, Maunsic host) {
		super(parent);
		this.host = host;
	}
	
	@Override
	protected void onInit() {
		s = new ScrollbarVanilla(10);
		title = new Label(I18n.translate("conf.language.title"));
		refresh = new ButtonVanilla(I18n.translate("conf.language.refresh"), this);
		back = new ButtonVanilla(I18n.translate("conf.back"), this);
		
		c = new Container();
		c.addWidgets(title, back, refresh);
	}
	
	@Override
	protected void onRevalidate() {
		containers.clear();
		title.setPosition(width / 2, 15);
		
		lc = new FocusedContainer(s, 14, 8);
		
		int i = 0;
		String ss = host.getConfig().getString("language", "en_US");
		for (File f : Maunsic.getConfDir("language").listFiles()) {
			ButtonVanilla sb = new ButtonVanilla(200, 20, f.getName().split(Pattern.quote("."), 2)[0], this);
			if (sb.getText().equals(ss)) sb.setText("[" + I18n.translate("conf.language.active") + "] " + sb.getText());
			sb.setPosition(width / 2 - sb.getWidth() / 2, 40 + i * 25);
			lc.addWidgets(sb);
			
			i++;
		}
		
		s.setPosition(width / 2 + 170, 0);
		back.setPosition(width / 2 - back.getWidth() - 12, height - 40);
		refresh.setPosition(width / 2 + 12, height - 40);
		
		containers.add(lc);
		containers.add(c);
		c.revalidate(0, 0, width, height);
		lc.revalidate(0, 35, width, height - 83);
	}
	
	@Override
	public void drawBackground() {
		super.drawBackground();
		Gui.drawRect(lc.left(), lc.top(), lc.right(), lc.bottom(), 0x44444444);
	}
	
	@Override
	public void onButtonClicked(Button b, int code) {
		System.out.println(b.getText());
		if (b == back) close();
		else if (b == refresh) onRevalidate();
		else {
			Maunsic.getLogger().info("Changing language to " + b.getText());
			host.getConfig().set("language", b.getText());
			I18n.reinitMaunsicI18n(host, true);
			onRevalidate();
		}
	}
}
