package net.maunium.Maunsic.Gui;

import com.mcf.davidee.guilib.basic.BasicScreen;
import com.mcf.davidee.guilib.basic.Label;
import com.mcf.davidee.guilib.core.Button;
import com.mcf.davidee.guilib.core.Container;
import com.mcf.davidee.guilib.vanilla.ButtonVanilla;
import com.mcf.davidee.guilib.vanilla.extended.ExtendedIntSlider;
import com.mcf.davidee.guilib.vanilla.extended.StateButton;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Util.I18n;

public class GuiAutosoup extends BasicScreen {
	private Maunsic host;
	private Container c;
	private Label title;
	private StateButton fullRefill, randomRefill, randomSoup;
	private ExtendedIntSlider interval;
	private ButtonVanilla back;
	
	public GuiAutosoup(GuiMaunsic parent, Maunsic host) {
		super(parent);
		this.host = host;
	}
	
	@Override
	protected void onInit() {
		c = new Container();
		title = new Label(I18n.translate("conf.autosoup.title"));
		
		fullRefill = new StateButton(200, 20, I18n.translate("conf.autosoup.fullrefill"), host.actionAutosoup.fullRefill ? 1 : 0);
		randomRefill = new StateButton(200, 20, I18n.translate("conf.autosoup.randomrefill"), host.actionAutosoup.randomizeRefill ? 1 : 0);
		randomSoup = new StateButton(200, 20, I18n.translate("conf.autosoup.randomsoup"), host.actionAutosoup.randomizeSoup ? 1 : 0);
		
		interval = new ExtendedIntSlider(200, 20, I18n.translate("conf.autosoup.interval"), host.actionAutosoup.getInterval(), 1, 200);
		
		back = new ButtonVanilla(200, 20, I18n.translate("conf.back"), this);
		
		c.addWidgets(title, fullRefill, randomRefill, randomSoup, interval, back);
		containers.add(c);
	}
	
	@Override
	protected void onRevalidate() {
		title.setPosition(width / 2, 15);
		
		fullRefill.setPosition(width / 2 - fullRefill.getWidth() / 2, 40);
		randomRefill.setPosition(width / 2 - randomRefill.getWidth() / 2, 65);
		randomSoup.setPosition(width / 2 - randomSoup.getWidth() / 2, 90);
		interval.setPosition(width / 2 - interval.getWidth() / 2, 115);
		
		back.setPosition(width / 2 - back.getWidth() / 2, height - 40);
		
		c.revalidate(0, 0, width, height);
	}
	
	@Override
	public void save() {
		host.actionAutosoup.fullRefill = fullRefill.getState() == 1;
		host.actionAutosoup.randomizeRefill = randomRefill.getState() == 1;
		host.actionAutosoup.randomizeSoup = randomSoup.getState() == 1;
		host.actionAutosoup.setInterval(interval.getIntValue());
	}
	
	@Override
	public void onButtonClicked(Button b, int code) {
		close();
	}
}
