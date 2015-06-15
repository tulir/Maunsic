package net.maunium.Maunsic.Gui.Alts;

import java.util.Random;

import org.lwjgl.input.Keyboard;

import com.mcf.davidee.guilib.basic.BasicScreen;
import com.mcf.davidee.guilib.basic.Label;
import com.mcf.davidee.guilib.core.Button;
import com.mcf.davidee.guilib.core.Container;
import com.mcf.davidee.guilib.vanilla.ButtonVanilla;
import com.mcf.davidee.guilib.vanilla.TextFieldVanilla;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Util.I18n;
import net.maunium.Maunsic.Util.LoginSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

/**
 * Gui to change cracked username or login as a premium user.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class GuiChangeUsername extends BasicScreen {
	private Maunsic host;
	protected TextFieldVanilla username, password;
	private Label title, unLabel, pwLabel, info;
	private ButtonVanilla finish, cancel, alts, randomName;
	private Container c;
	
	public GuiChangeUsername(Maunsic host) {
		super(Minecraft.getMinecraft().currentScreen);
		this.host = host;
	}
	
	public void trySave() {
		if (password.getText().isEmpty()) {
			if (LoginSystem.setUsername(username.getText())) {
				Maunsic.printChat("message.alt.username", Minecraft.getMinecraft().getSession().getUsername());
				close();
			} else {
				info.setText(I18n.translate("message.alt.failusername"));
				c.addWidgets(info);
				onRevalidate();
			}
		} else {
			Session s;
			try {
				s = LoginSystem.login(username.getText(), password.getText());
			} catch (LoginSystem.LoginException e) {
				info.setText(I18n.translate("message.alt.faillogin", e.getMessage()));
				c.addWidgets(info);
				onRevalidate();
				return;
			}
			if (LoginSystem.setSession(s)) {
				Maunsic.printChat("message.alt.login", s.getUsername());
				close();
			} else {
				info.setText(I18n.translate("message.alt.failsession"));
				c.addWidgets(info);
				onRevalidate();
			}
		}
	}
	
	@Override
	public void onRevalidate() {
		int xpl = width / 2 - 210 / 2;
		int xpt = xpl + 60;
		
		Maunsic.getLogger().trace("Setting positions", this);
		title.setPosition(width / 2, 15);
		
		username.setPosition(xpt, 50);
		password.setPosition(xpt, 75);
		
		info.setPosition(width / 2, 100);
		
		unLabel.setPosition(xpl, 55);
		pwLabel.setPosition(xpl, 80);
		
		alts.setPosition(width / 2 - 162, 125);
		randomName.setPosition(width / 2 + 12, 125);
		
		cancel.setPosition(width / 2 + 12, 150);
		finish.setPosition(width / 2 - 162, 150);
		
		Maunsic.getLogger().trace("Revalidating container", this);
		c.revalidate(0, 0, width, height);
	}
	
	@Override
	public void onInit() {
		Maunsic.getLogger().trace("Creating labels", this);
		title = new Label(I18n.translate("conf.username.title"));
		unLabel = new Label(I18n.translate("conf.username.unlabel"));
		pwLabel = new Label(I18n.translate("conf.username.pwlabel"));
		info = new Label("");
		
		Maunsic.getLogger().trace("Creating text fields", this);
		username = new TextFieldVanilla(150, 20, new TextFieldVanilla.VanillaFilter());
		username.setMaxLength(64);
		username.setText(Minecraft.getMinecraft().getSession().getUsername());
		password = new TextFieldVanilla(150, 20, new TextFieldVanilla.CommonFilter());
		password.setMaxLength(64);
		
		Maunsic.getLogger().trace("Creating buttons", this);
		finish = new ButtonVanilla(150, 20, I18n.translate("conf.username.finish"), this);
		cancel = new ButtonVanilla(150, 20, I18n.translate("conf.username.cancel"), this);
		alts = new ButtonVanilla(150, 20, I18n.translate("conf.alts.enter"), this);
		randomName = new ButtonVanilla(150, 20, I18n.translate("conf.alts.random"), this);
		
		Maunsic.getLogger().trace("Creating container and adding widgets", this);
		c = new Container();
		c.addWidgets(username, password, finish, cancel, unLabel, pwLabel, title, alts, randomName);
		Maunsic.getLogger().trace("Adding container", this);
		containers.add(c);
	}
	
	@Override
	public boolean onClose() {
		return false;
	}
	
	@Override
	public void onKeyTyped(char c, int kc) {
		if (kc == Keyboard.KEY_RETURN) trySave();
		else super.onKeyTyped(c, kc);
	}
	
	@Override
	public void onButtonClicked(Button button, int code) {
		if (code != 0) return;
		if (button.equals(finish)) trySave();
		else if (button.equals(cancel)) close();
		else if (button.equals(alts)) Minecraft.getMinecraft().displayGuiScreen(new GuiAlts(this, host));
		else if (button.equals(randomName)) {
			StringBuffer sb = new StringBuffer();
			char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789_".toCharArray();
			int len = chars.length;
			Random r = new Random(System.nanoTime());
			for (int i = 0; i < 16; i++)
				sb.append(chars[r.nextInt(len)]);
			username.setText(sb.toString());
		}
	}
}
