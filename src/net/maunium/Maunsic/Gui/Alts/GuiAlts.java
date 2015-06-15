package net.maunium.Maunsic.Gui.Alts;

import org.lwjgl.input.Keyboard;

import com.mcf.davidee.guilib.basic.BasicScreen;
import com.mcf.davidee.guilib.basic.FocusedContainer;
import com.mcf.davidee.guilib.basic.Label;
import com.mcf.davidee.guilib.core.Button;
import com.mcf.davidee.guilib.core.Container;
import com.mcf.davidee.guilib.vanilla.ButtonVanilla;
import com.mcf.davidee.guilib.vanilla.ScrollbarVanilla;
import com.mcf.davidee.guilib.vanilla.extended.ShiftableLabel;
import com.mcf.davidee.guilib.vanilla.focusable.FocusableLabel;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Util.I18n;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

/**
 * Gui to select an alt account.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class GuiAlts extends BasicScreen {
	private Maunsic host;
	private Container c;
	private FocusedContainer alts;
	private ScrollbarVanilla sb;
	private Label title;
	private FocusableLabel[] usernames;
	private ShiftableLabel[] passwords;
	private ButtonVanilla select, back, delete, add;
	private GuiChangeUsername parent;
	
	public GuiAlts(GuiChangeUsername parent, Maunsic host) {
		super(parent);
		this.parent = parent;
		this.host = host;
	}
	
	@Override
	public void onInit() {
		Maunsic.getLogger().trace("Creating title", this);
		title = new Label(I18n.translate("conf.alts.title"));
		Maunsic.getLogger().trace("Creating buttons", this);
		add = new ButtonVanilla(150, 20, I18n.translate("conf.alts.add"), this);
		delete = new ButtonVanilla(150, 20, I18n.translate("conf.alts.delete"), this);
		select = new ButtonVanilla(150, 20, I18n.translate("conf.alts.select"), this);
		back = new ButtonVanilla(150, 20, I18n.translate("conf.back"), this);
		
		Maunsic.getLogger().trace("Creating alt container", this);
		sb = new ScrollbarVanilla(10);
		alts = new FocusedContainer(sb, 14, 4);
		
		Maunsic.getLogger().trace("Creating main container and adding widgets", this);
		c = new Container();
		c.addWidgets(title, select, back, delete, add);
		Maunsic.getLogger().trace("Adding containers to list", this);
		containers.add(alts);
		containers.add(c);
	}
	
	@Override
	public void onRevalidate() {
		int xp1 = width / 2 - 150 - 12;
		int xp2 = width / 2 + 12;
		int mod = 25;
		
		Maunsic.getLogger().trace("Setting positions", this);
		title.setPosition(width / 2, 15);
		add.setPosition(xp1, mod * 6);
		delete.setPosition(xp2, mod * 6);
		select.setPosition(xp1, mod * 7);
		back.setPosition(xp2, mod * 7);
		
		Maunsic.getLogger().trace("Refreshing alt list", this);
		alts.getWidgets().clear();
		alts.getFocusableWidgets().clear();
		usernames = new FocusableLabel[host.getAlts().getAlts().size()];
		passwords = new ShiftableLabel[host.getAlts().getAlts().size()];
		int i = 0;
		for (String s : host.getAlts().getAlts()) {
			Maunsic.getLogger().trace("Adding entry for alt " + s, this);
			usernames[i] = new FocusableLabel(s, false);
			passwords[i] = new ShiftableLabel(host.getAlts().getPassword(s), false);
			usernames[i].setPosition(width / 2 - (xp2 + 150 - xp1) / 2 + 2, 32 + i * 10);
			passwords[i].setPosition(width / 2 + 2, 32 + i * 10);
			i++;
		}
		alts.addWidgets(usernames);
		alts.addWidgets(passwords);
		
		sb.setPosition(xp2 + 140, 30);
		Maunsic.getLogger().trace("Revalidating containers", this);
		c.revalidate(0, 0, width, height);
		alts.revalidate(xp1, 30, xp2 + 150 - xp1 - 10, mod * 4);
	}
	
	@Override
	public void drawBackground() {
		super.drawBackground();
		Gui.drawRect(alts.left(), alts.top(), alts.right(), alts.bottom(), 0x44444444);
	}
	
	@Override
	public void onKeyTyped(char c, int kc) {
		if (kc == Keyboard.KEY_RETURN) select();
		else if (kc == Keyboard.KEY_DELETE) delete();
		else if (kc == Keyboard.KEY_ADD) Minecraft.getMinecraft().displayGuiScreen(new GuiAddAlt(this, host));
		else super.onKeyTyped(c, kc);
		
	}
	
	@Override
	public void save() {
		host.getAlts().save(host.getConfig());
		host.saveConfig();
	}
	
	private void select() {
		for (int i = 0; i < usernames.length; i++)
			if (usernames[i].equals(alts.getFocusedWidget())) {
				parent.username.setText(usernames[i].getText());
				parent.password.setText(passwords[i].getText());
			}
		close();
	}
	
	private void delete() {
		if (alts.getFocusedWidget() instanceof FocusableLabel) {
			FocusableLabel l = (FocusableLabel) alts.getFocusedWidget();
			host.getAlts().delAlt(l.getText());
			onRevalidate();
		}
	}
	
	@Override
	public void onButtonClicked(Button button) {
		if (button.equals(select)) select();
		else if (button.equals(delete)) delete();
		else if (button.equals(back)) close();
		else if (button.equals(add)) Minecraft.getMinecraft().displayGuiScreen(new GuiAddAlt(this, host));
	}
}
