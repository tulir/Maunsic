package net.maunium.Maunsic.Gui;

import java.util.ArrayList;
import java.util.List;

import com.mcf.davidee.guilib.basic.BasicScreen;
import com.mcf.davidee.guilib.basic.Label;
import com.mcf.davidee.guilib.core.Button;
import com.mcf.davidee.guilib.core.Container;
import com.mcf.davidee.guilib.core.Widget;
import com.mcf.davidee.guilib.vanilla.ButtonVanilla;
import com.mcf.davidee.guilib.vanilla.extended.ExtendedIntSlider;
import com.mcf.davidee.guilib.vanilla.extended.StateButton;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Actions.ActionBlink;
import net.maunium.Maunsic.Actions.ActionFly;
import net.maunium.Maunsic.Gui.Alts.GuiChangeUsername;
import net.maunium.Maunsic.Gui.KeyMaucros.GuiKeyMaucros;
import net.maunium.Maunsic.Gui.KeyMaucros.GuiLuaThreads;
import net.maunium.Maunsic.Gui.XRay.GuiXrayBlocks;
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
	
	private static int page = 0;
	private int pageCount = 1;
	private Container c;
	private List<Widget> widgets = new ArrayList<Widget>();
	
	private Label title;
	private ButtonVanilla keys, alts, keymaucros, luathreads, xray, fanda, prevPage, nextPage, close;
	private StateButton autophase, autoblink, autophasesprint;
	private ExtendedIntSlider speed, jump, blinksafety;
	
	public GuiMaunsic(Maunsic host) {
		super(null);
		this.host = host;
	}
	
	@Override
	protected void onInit() {
		c = new Container();
		containers.add(c);
		title = new Label("");
		
		prevPage = new ButtonVanilla(I18n.translate("conf.prevpage"), this);
		nextPage = new ButtonVanilla(I18n.translate("conf.nextpage"), this);
		close = new ButtonVanilla(I18n.translate("conf.backtogame"), this);
		
		alts = new ButtonVanilla(I18n.translate("conf.alts"), this);
		keymaucros = new ButtonVanilla(I18n.translate("conf.km"), this);
		luathreads = new ButtonVanilla(I18n.translate("conf.lua.threads"), this);
		keys = new ButtonVanilla(I18n.translate("conf.keys"), this);
		xray = new ButtonVanilla(I18n.translate("conf.xray"), this);
		fanda = new ButtonVanilla(I18n.translate("conf.fanda"), this);
		add(widgets, alts, keymaucros, luathreads, keys, xray, fanda);
		
		speed = new ExtendedIntSlider(I18n.translate("conf.amv.speed"), host.actionFly.getSpeed(), ActionFly.MIN_SPEED, ActionFly.MAX_SPEED);
		jump = new ExtendedIntSlider(amount -> amount == 0 ? I18n.translate("conf.amv.jump.default") : I18n.translate("conf.amv.jump", amount),
				host.actionFly.getJump(), 0, 100);
		add(widgets, speed, jump);
		
		autoblink = new StateButton(150, 20, I18n.translate("conf.blink.automated"), this, ActionBlink.automated ? 1 : 0);
		blinksafety = new ExtendedIntSlider(amount -> amount == 0 ? I18n.translate("conf.blink.safety") + ": " + I18n.translate("off")
				: I18n.translate("conf.blink.safety") + ": " + amount + " " + I18n.translate("conf.blink.safety.level"), ActionBlink.safetyLevel, 0, 10);
		autophase = new StateButton(150, 20, I18n.translate("conf.phase.automated"), this, host.actionPhase.automated ? 1 : 0);
		autophasesprint = new StateButton(150, 20, I18n.translate("conf.phase.autoforward"), this, host.actionPhase.autoforward ? 1 : 0);
		add(widgets, autoblink, blinksafety, autophase, autophasesprint);
	}
	
	public void add(List<Widget> l, Widget... w) {
		for (Widget ww : w)
			l.add(ww);
	}
	
	@Override
	protected void onRevalidate() {
		c.getWidgets().clear();
		
		title.setText(I18n.translate("conf.title", Maunsic.longVersion, page + 1));
		title.setPosition(width / 2, 15);
		c.addWidgets(title);
		
		int x1 = width / 2 - 150 - 12, x2 = width / 2 + 12;
		int pagesize = 2 * ((height - 90) / 25), y = 2, start = page * pagesize, end = start + pagesize;
		pageCount = (int) Math.ceil((double) widgets.size() / (double) pagesize);
		if (end > widgets.size()) end = widgets.size();
		boolean side = true;
		
		for (int i = start; i < end; i++) {
			Widget w = widgets.get(i);
			w.setPosition(side ? x1 : x2, y * 25);
			side = !side;
			if (side) y++;
			c.addWidgets(w);
		}
		
		prevPage.setPosition(x1, height - 40);
		nextPage.setPosition(x2, height - 40);
		if (page < 1) {
			close.setPosition(x1, height - 40);
			c.addWidgets(close);
		} else if (page > pageCount - 2) {
			close.setPosition(x2, height - 40);
			c.addWidgets(close);
		}
		
		if (page < pageCount - 1) c.addWidgets(nextPage);
		if (page > 0) c.addWidgets(prevPage);
		
		Maunsic.getLogger().trace("Revalidating container for page " + page, this);
		c.revalidate(0, 0, width, height);
	}
	
	@Override
	protected void onButtonClicked(Button b, int code) {
		if (code != 0) return;
		// Yes, they are supposed to be the same instance, so using == is just fine.
		if (b == close) close();
		else if (b == nextPage) {
			page++;
			onRevalidate();
		} else if (b == prevPage) {
			page--;
			onRevalidate();
		} else if (b == keys) Minecraft.getMinecraft().displayGuiScreen(new GuiKeybinds(this, host));
		else if (b == alts) Minecraft.getMinecraft().displayGuiScreen(new GuiChangeUsername(host));
		else if (b == keymaucros) Minecraft.getMinecraft().displayGuiScreen(new GuiKeyMaucros(this, host));
		else if (b == luathreads) Minecraft.getMinecraft().displayGuiScreen(new GuiLuaThreads(this));
		else if (b == xray) Minecraft.getMinecraft().displayGuiScreen(new GuiXrayBlocks(host));
		else if (b == fanda) Minecraft.getMinecraft().displayGuiScreen(new GuiFriendsAndAttacking(this, host));
	}
	
	@Override
	protected void save() {
		host.actionFly.setSpeed(speed.getIntValue());
		host.actionFly.setJump(jump.getIntValue());
		
		ActionBlink.automated = autoblink.getState() == 1;
		ActionBlink.safetyLevel = blinksafety.getIntValue();
		host.actionPhase.automated = autophase.getState() == 1;
		host.actionPhase.autoforward = autophasesprint.getState() == 1;
		// Save all the action configs to RAM
		host.getActionHandler().saveAll();
		// Save config to disk.
		host.saveConfig();
	}
}
