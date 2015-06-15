package net.maunium.Maunsic.Gui.Alts;

import org.lwjgl.input.Keyboard;

import com.mcf.davidee.guilib.basic.BasicScreen;
import com.mcf.davidee.guilib.basic.Label;
import com.mcf.davidee.guilib.core.Button;
import com.mcf.davidee.guilib.core.Container;
import com.mcf.davidee.guilib.vanilla.ButtonVanilla;
import com.mcf.davidee.guilib.vanilla.TextFieldVanilla;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Util.I18n;

/**
 * Gui to add an alt account
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class GuiAddAlt extends BasicScreen {
	private Container c;
	private Maunsic host;
	private ButtonVanilla add, cancel;
	private TextFieldVanilla username, password;
	private Label title, unLabel, pwLabel;
	
	public GuiAddAlt(GuiAlts parent, Maunsic host) {
		super(parent);
		this.host = host;
	}
	
	@Override
	public void onButtonClicked(Button button) {
		if (username.getText().trim().isEmpty() || password.getText().trim().isEmpty()) add.setEnabled(false);
		else add.setEnabled(true);
		if (button.equals(add)) add();
		else if (button.equals(cancel)) close();
	}
	
	private void add() {
		if (!username.getText().trim().isEmpty() && !password.getText().trim().isEmpty()) {
			host.getAlts().addAlt(username.getText().trim(), password.getText().trim());
			close();
			if (getParent() instanceof GuiAlts) ((GuiAlts) getParent()).onRevalidate();
			else Maunsic.getLogger().debug("My parent isn't GuiAlts? Something may be wrong!", this);
		}
	}
	
	@Override
	public void onKeyTyped(char c, int kc) {
		if (kc == Keyboard.KEY_RETURN) add();
		else super.onKeyTyped(c, kc);
		if (username.getText().trim().isEmpty() || password.getText().trim().isEmpty()) add.setEnabled(false);
		else add.setEnabled(true);
	}
	
	@Override
	public void onRevalidate() {
		int xp1 = width / 2 - 150 - 12;
		int xp2 = width / 2 + 12;
		int xpl = width / 2 - 210 / 2;
		int xpt = xpl + 60;
		int mod = 25;
		
		Maunsic.getLogger().trace("Setting positions", this);
		title.setPosition(width / 2, 15);
		
		username.setPosition(xpt, mod * 2);
		password.setPosition(xpt, mod * 3);
		
		unLabel.setPosition(xpl, mod * 2);
		pwLabel.setPosition(xpl, mod * 3);
		
		add.setPosition(xp1, mod * 4);
		cancel.setPosition(xp2, mod * 4);
		
		if (username.getText().trim().isEmpty() || password.getText().trim().isEmpty()) add.setEnabled(false);
		else add.setEnabled(true);
		
		Maunsic.getLogger().trace("Revalidating container", this);
		c.revalidate(0, 0, width, height);
	}
	
	@Override
	public void onInit() {
		Maunsic.getLogger().trace("Creating text fields", this);
		username = new TextFieldVanilla(new TextFieldVanilla.VanillaFilter());
		password = new TextFieldVanilla(new TextFieldVanilla.CommonFilter());
		
		Maunsic.getLogger().trace("Creating labels", this);
		title = new Label(I18n.translate("conf.alts.add.title"));
		unLabel = new Label(I18n.translate("conf.alts.add.username"));
		pwLabel = new Label(I18n.translate("conf.alts.add.password"));
		
		Maunsic.getLogger().trace("Creating buttons", this);
		add = new ButtonVanilla(150, 20, I18n.translate("conf.alts.add.add"), this);
		cancel = new ButtonVanilla(150, 20, I18n.translate("conf.alts.add.cancel"), this);
		
		Maunsic.getLogger().trace("Creating container and adding widgets", this);
		c = new Container();
		c.addWidgets(add, cancel, username, password, unLabel, pwLabel);
		Maunsic.getLogger().trace("Adding container", this);
		containers.add(c);
	}
	
	@Override
	public void onReopen() {
		Maunsic.getLogger().debug("Reopened GUI", this);
		if (username.getText().trim().isEmpty() || password.getText().trim().isEmpty()) add.setEnabled(false);
		else add.setEnabled(true);
	}
}
