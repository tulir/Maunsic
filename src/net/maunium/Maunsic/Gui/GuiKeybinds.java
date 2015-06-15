package net.maunium.Maunsic.Gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.mcf.davidee.guilib.basic.BasicScreen;
import com.mcf.davidee.guilib.basic.FocusedContainer;
import com.mcf.davidee.guilib.basic.Label;
import com.mcf.davidee.guilib.core.Button;
import com.mcf.davidee.guilib.core.Button.ButtonHandler;
import com.mcf.davidee.guilib.core.Container;
import com.mcf.davidee.guilib.core.Scrollbar;
import com.mcf.davidee.guilib.vanilla.ButtonVanilla;
import com.mcf.davidee.guilib.vanilla.ScrollbarVanilla;
import com.mcf.davidee.guilib.vanilla.extended.KeySelectButton;
import com.mcf.davidee.guilib.vanilla.extended.KeySelectButton.KSBFormat;
import com.mcf.davidee.guilib.vanilla.extended.ShiftableButton;
import com.mcf.davidee.guilib.vanilla.extended.ShiftableLabel;
import com.mcf.davidee.guilib.vanilla.extended.StateButton;

import net.maunium.Maunsic.Listeners.KeyHandling.InputHandler;
import net.maunium.Maunsic.Listeners.KeyHandling.KeyRegistry;
import net.maunium.Maunsic.Listeners.KeyHandling.MauKeybind;
import net.maunium.Maunsic.Util.I18n;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

public class GuiKeybinds extends BasicScreen {
	private Label title;
	private Container c, kc;
	private Scrollbar s;
	private ButtonVanilla back;
	private StateButton disableAll;
	private ShiftableLabel[] keybindTitles;
	private KeySelectButtonWithDefault[] keybindKeys;
	private ShiftableButtonWithMeta[] keybindReset;
	
	public GuiKeybinds(GuiScreen parent) {
		super(parent);
	}
	
	@Override
	public void onInit() {
		c = new Container();
		s = new ScrollbarVanilla(10);
		kc = new FocusedContainer(s, 14, 8);
		
		title = new Label(I18n.translate("conf.keys.title"));
		back = new ButtonVanilla(I18n.translate("conf.back"), this);
		disableAll = new StateButton(I18n.translate("conf.keys.disableall"), this, InputHandler.isDisabled() ? 1 : 0);
		
		c.addWidgets(title, back, disableAll);
		
		containers.add(c);
		containers.add(kc);
		selectedContainer = kc;
	}
	
	@Override
	public void onRevalidate() {
		title.setPosition(width / 2, 15);
		
		back.setPosition(width / 2 + 12, height - 40);
		disableAll.setPosition(width / 2 - disableAll.getWidth() - 12, height - 40);
		
		kc.getWidgets().clear();
		kc.addWidgets(s);
		s.setPosition(width / 2 + 170, 0);
		
		List<MauKeybind> kbs = new ArrayList<MauKeybind>(KeyRegistry.getKeybinds());
		Collections.sort(kbs);
		
		keybindTitles = new ShiftableLabel[kbs.size()];
		keybindKeys = new KeySelectButtonWithDefault[kbs.size()];
		keybindReset = new ShiftableButtonWithMeta[kbs.size()];
		
		for (int i = 0; i < kbs.size(); i++) {
			MauKeybind kb = kbs.get(i);
			keybindTitles[i] = new ShiftableLabel(I18n.translate(kb.getName()), false);
			keybindKeys[i] = new KeySelectButtonWithDefault(100, 20, this, kb.getKeyCode(), new KSBF(), kb.getDefaultKeyCode());
			keybindReset[i] = new ShiftableButtonWithMeta(50, 20, I18n.translate("conf.keys.reset"), this, i);
			keybindTitles[i].setPosition(width / 2 - 150, 40 + i * 25);
			keybindKeys[i].setPosition(width / 2 - keybindKeys[i].getWidth() / 2 + 50, 40 + i * 25);
			keybindReset[i].setPosition(width / 2 + keybindKeys[i].getWidth() / 2 + 60, 40 + i * 25);
			kc.addWidgets(keybindTitles[i], keybindKeys[i], keybindReset[i]);
		}
		
		c.revalidate(0, 0, width, height);
		kc.revalidate(0, 35, width, height - 83);
	}
	
	@Override
	public void onButtonClicked(Button b) {
		if (b == disableAll) disableAll.setState(InputHandler.toggleDisable() ? 1 : 0);
		else if (b == back) close();
		else if (b instanceof ShiftableButtonWithMeta) {
			ShiftableButtonWithMeta sbwm = (ShiftableButtonWithMeta) b;
			KeySelectButtonWithDefault ksb = keybindKeys[sbwm.meta];
			ksb.setKeycode(ksb.default_);
		}
	}
	
	@Override
	public void drawBackground() {
		super.drawBackground();
		Gui.drawRect(kc.left(), kc.top(), kc.right(), kc.bottom(), 0x44444444);
	}
	
	public static class KSBF implements KSBFormat {
		@Override
		public String format(int keyCode, boolean pressed) {
			if (pressed) return "> " + getName(keyCode) + " <";
			else return getName(keyCode);
		}
		
		private String getName(int kc) {
			try {
				String s = Keyboard.getKeyName(kc);
				if (s != null && !s.isEmpty()) return s;
			} catch (Throwable t) {}
			try {
				String s = Mouse.getButtonName(kc);
				if (s != null && !s.isEmpty()) return s;
			} catch (Throwable t) {}
			return I18n.translate("conf.keys.notset");
		}
	}
	
	/**
	 * Shiftable button with metadata.
	 * 
	 * @author Tulir293
	 * @since 0.1
	 */
	public static class ShiftableButtonWithMeta extends ShiftableButton {
		public int meta;
		
		public ShiftableButtonWithMeta(int width, int height, String text, ButtonHandler h, int meta) {
			super(width, height, text, h);
			this.meta = meta;
		}
	}
	
	public static class KeySelectButtonWithDefault extends KeySelectButton {
		public int default_;
		
		public KeySelectButtonWithDefault(int width, int height, ButtonHandler h, int keycode, KeySelectButton.KSBFormat format, int default_) {
			super(width, height, h, keycode, format);
			this.default_ = default_;
		}
	}
}
