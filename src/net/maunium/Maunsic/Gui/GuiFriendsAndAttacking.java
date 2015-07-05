package net.maunium.Maunsic.Gui;

import org.lwjgl.input.Keyboard;

import com.mcf.davidee.guilib.basic.BasicScreen;
import com.mcf.davidee.guilib.basic.Label;
import com.mcf.davidee.guilib.core.Button;
import com.mcf.davidee.guilib.core.Container;
import com.mcf.davidee.guilib.core.FocusableWidget;
import com.mcf.davidee.guilib.vanilla.ButtonVanilla;
import com.mcf.davidee.guilib.vanilla.ScrollbarVanilla;
import com.mcf.davidee.guilib.vanilla.extended.ExtendedIntSlider;
import com.mcf.davidee.guilib.vanilla.focusable.FocusableLabel;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Settings.Attacking;
import net.maunium.Maunsic.Util.EntityClassFinder;
import net.maunium.Maunsic.Util.I18n;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatAllowedCharacters;

/**
 * Gui to handle friends, targets and other attacking settings.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class GuiFriendsAndAttacking extends BasicScreen {
	private Maunsic host;
	private Container c, fc, tc;
	private ScrollbarVanilla fsb, tsb;
	
	private Label title, fct, tct;
	private ButtonVanilla addFriend, addTarget, removeFriend, removeTarget, back;
	private ExtendedIntSlider range;
	
	public GuiFriendsAndAttacking(GuiMaunsic parent, Maunsic host) {
		super(parent);
		this.host = host;
	}
	
	@Override
	public void onInit() {
		Maunsic.getLogger().trace("Creating containers", this);
		c = new Container();
		fsb = new ScrollbarVanilla(10);
		fc = new Container(fsb, 14, 4);
		tsb = new ScrollbarVanilla(10);
		tc = new Container(tsb, 14, 4);
		
		Maunsic.getLogger().trace("Creating title", this);
		title = new Label(I18n.translate("conf.fanda.title"));
		fct = new Label(I18n.translate("conf.fanda.fctitle"));
		tct = new Label(I18n.translate("conf.fanda.tctitle"));
		
		Maunsic.getLogger().trace("Creating Int Sliders", this);
		range = new ExtendedIntSlider(I18n.translate("conf.fanda.range"), (int) Attacking.range, 1, 8);
		
		Maunsic.getLogger().trace("Creating buttons", this);
		addFriend = new ButtonVanilla(150, 20, I18n.translate("conf.fanda.addfriend"), this);
		addTarget = new ButtonVanilla(150, 20, I18n.translate("conf.fanda.addtarget"), this);
		removeFriend = new ButtonVanilla(150, 20, I18n.translate("conf.fanda.removefriend"), this);
		removeTarget = new ButtonVanilla(150, 20, I18n.translate("conf.fanda.removetarget"), this);
		back = new ButtonVanilla(150, 20, I18n.translate("conf.back"), this);
		
		Maunsic.getLogger().trace("Creating friends list", this);
		for (String s : Attacking.getFriends())
			fc.addWidgets(new FocusableLabel(s, false));
		for (Class<? extends EntityLivingBase> s : Attacking.getTargets())
			tc.addWidgets(new FocusableLabel(s.getSimpleName().substring(6), false));
		
		Maunsic.getLogger().trace("Adding widgets", this);
		c.addWidgets(range, tct, fct, addFriend, addTarget, removeFriend, removeTarget, back, title);
		Maunsic.getLogger().trace("Adding containers", this);
		containers.add(fc);
		containers.add(tc);
		containers.add(c);
	}
	
	@Override
	public void onRevalidate() {
		title.setPosition(width / 2, 15);
		int xp1 = width / 2 - 150 - 12;
		int xp2 = width / 2 + 12;
		
		int y = 5;
		for (FocusableWidget fl : fc.getFocusableWidgets()) {
			fl.setPosition(xp2 + 5, 50 + y);
			y += 10;
		}
		y = 5;
		for (FocusableWidget fl : tc.getFocusableWidgets()) {
			fl.setPosition(xp1 + 5, 50 + y);
			y += 10;
		}
		
		addFriend.setPosition(xp2, 150);
		addTarget.setPosition(xp1, 150);
		removeFriend.setPosition(xp2, 175);
		removeTarget.setPosition(xp1, 175);
		
		back.setPosition(xp1, 200);
		range.setPosition(xp2, 200);
		
		fct.setPosition(xp2 + 75, 35);
		tct.setPosition(xp1 + 75, 35);
		
		c.revalidate(0, 0, width, width);
		fc.revalidate(xp2, 50, 150, 95);
		tc.revalidate(xp1, 50, 150, 95);
	}
	
	@Override
	public void save() {
		Maunsic.getLogger().trace("Saving data", this);
		Attacking.range = range.getIntValue();
		Attacking.clearFriends();
		for (FocusableWidget fw : fc.getFocusableWidgets()) {
			if (fw instanceof FocusableLabel) {
				FocusableLabel fl = (FocusableLabel) fw;
				Attacking.addFriend(fl.getText());
			}
		}
		Attacking.clearTargets();
		for (FocusableWidget fw : tc.getFocusableWidgets()) {
			if (fw instanceof FocusableLabel) {
				FocusableLabel fl = (FocusableLabel) fw;
				Class<? extends EntityLivingBase> c = EntityClassFinder.getEntityClass(fl.getText());
				if (c != null) Attacking.addTarget(c);
				else Maunsic.printChatError("message.fanda.failtarget", fl.getText());
			}
		}
		Attacking.save(host.getConfig());
	}
	
	@Override
	public void onKeyTyped(char c, int kc) {
		if (kc == Keyboard.KEY_DELETE) {
			if (fc.hasFocusedWidget()) fc.deleteFocused();
			else if (tc.hasFocusedWidget()) tc.deleteFocused();
		} else if (kc == Keyboard.KEY_RETURN) {
			if (fc.hasFocusedWidget()) fc.getFocusedWidget().focusLost();
			if (tc.hasFocusedWidget()) tc.getFocusedWidget().focusLost();
			fc.setFocused(null);
			tc.setFocused(null);
		} else if (fc.hasFocusedWidget()) {
			if (tc.hasFocusedWidget()) tc.getFocusedWidget().focusLost();
			tc.setFocused(null);
			if (fc.getFocusedWidget() instanceof FocusableLabel) {
				FocusableLabel fl = (FocusableLabel) fc.getFocusedWidget();
				if (kc == Keyboard.KEY_RETURN) fc.setFocused(null);
				else if (kc == Keyboard.KEY_BACK) {
					if (fl.getText().length() < 2) fl.setText("_");
					else fl.setText(fl.getText().substring(0, fl.getText().length() - 1));
				} else if (ChatAllowedCharacters.isAllowedCharacter(c) && fl.getText().length() <= 16)
					if (fl.getText().equals("_")) fl.setText(Character.toString(c));
					else fl.setText(fl.getText() + c);
			}
		} else if (tc.hasFocusedWidget()) {
			if (fc.hasFocusedWidget()) fc.getFocusedWidget().focusLost();
			fc.setFocused(null);
			if (tc.getFocusedWidget() instanceof FocusableLabel) {
				FocusableLabel fl = (FocusableLabel) tc.getFocusedWidget();
				if (kc == Keyboard.KEY_RETURN) tc.setFocused(null);
				else if (kc == Keyboard.KEY_BACK) {
					if (fl.getText().length() < 2) fl.setText("_");
					else fl.setText(fl.getText().substring(0, fl.getText().length() - 1));
				} else if (ChatAllowedCharacters.isAllowedCharacter(c) && fl.getText().length() <= 16)
					if (fl.getText().equals("_")) fl.setText(Character.toString(c));
					else fl.setText(fl.getText() + c);
			}
		} else super.onKeyTyped(c, kc);
	}
	
	@Override
	public void drawBackground() {
		super.drawBackground();
		Gui.drawRect(fc.left(), fc.top(), fc.right(), fc.bottom(), 0x44444444);
		Gui.drawRect(tc.left(), tc.top(), tc.right(), tc.bottom(), 0x44444444);
	}
	
	@Override
	public void onButtonClicked(Button button, int code) {
		if (button.equals(back)) close();
		else if (button.equals(addFriend)) {
			FocusableLabel newFriend = new FocusableLabel("_", false);
			fc.addWidgets(newFriend);
			onRevalidate();
			tc.setFocused(null);
			newFriend.focusGained();
			fc.setFocused(newFriend);
		} else if (button.equals(addTarget)) {
			FocusableLabel newTarget = new FocusableLabel("_", false);
			tc.addWidgets(newTarget);
			onRevalidate();
			fc.setFocused(null);
			newTarget.focusGained();
			tc.setFocused(newTarget);
		} else if (button.equals(removeFriend) && fc.hasFocusedWidget()) fc.deleteFocused();
		else if (button.equals(removeTarget) && tc.hasFocusedWidget()) tc.deleteFocused();
	}
}