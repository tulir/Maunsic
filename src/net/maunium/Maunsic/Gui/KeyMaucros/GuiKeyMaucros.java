package net.maunium.Maunsic.Gui.KeyMaucros;

import org.lwjgl.input.Keyboard;

import com.mcf.davidee.guilib.basic.BasicScreen;
import com.mcf.davidee.guilib.basic.FocusedContainer;
import com.mcf.davidee.guilib.basic.Label;
import com.mcf.davidee.guilib.core.Button;
import com.mcf.davidee.guilib.core.Container;
import com.mcf.davidee.guilib.vanilla.ButtonVanilla;
import com.mcf.davidee.guilib.vanilla.ScrollbarVanilla;
import com.mcf.davidee.guilib.vanilla.extended.ShiftableButton;
import com.mcf.davidee.guilib.vanilla.focusable.FocusableLabel;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.KeyMaucros.KeyMaucro;
import net.maunium.Maunsic.Util.I18n;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import net.minecraftforge.fml.client.config.GuiConfig;

/**
 * Gui for managing key maucros
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class GuiKeyMaucros extends BasicScreen {
	// kmlc means KeyMaucroListContainer
	private Container c, kmlc;
	private ScrollbarVanilla sb;
	private Label title;
	private ButtonVanilla newCC, newLua, back;
	private ShiftableButton[] buttons;
	private FocusableLabel[] labels;
	
	public GuiKeyMaucros(GuiConfig parent) {
		super(parent);
	}
	
	@Override
	public void onRevalidate() {
		Maunsic.getLogger().trace("Setting positions...", this);
		title.setPosition(width / 2, 15);
		back.setPosition(width / 2 - back.getWidth() / 2 - newCC.getWidth() - 10, height - 40);
		newCC.setPosition(back.getX() + back.getWidth() + 5, height - 40);
		newLua.setPosition(newCC.getX() + newCC.getWidth() + 5, height - 40);
		
		kmlc.getWidgets().clear();
		buttons = new ShiftableButton[KeyMaucro.getKeyMaucros().size()];
		labels = new FocusableLabel[KeyMaucro.getKeyMaucros().size()];
		
		Maunsic.getLogger().trace("Adding Key Maunsic to KMLContainer", this);
		for (int i = 0; i < KeyMaucro.getKeyMaucros().size(); i++) {
			KeyMaucro km = KeyMaucro.getKeyMaucros().get(i);
			
			labels[i] = new FocusableLabel(km.getName(), false);
			if (km.getShiftKeys().length <= 0) buttons[i] = new ShiftableButton(Keyboard.getKeyName(km.getKeyCode()), this);
			else buttons[i] = new ShiftableButton(km.getShiftKeysAsString() + " + " + Keyboard.getKeyName(km.getKeyCode()), this);
			buttons[i].setPosition(width / 2 - buttons[i].getWidth() / 2 + 22, 35 + 2 + i * 22);
			labels[i].setPosition(width / 2 - buttons[i].getWidth() / 2 + 16 - labels[i].getWidth() + 2, 35 + 6 + i * 22);
		}
		Maunsic.getLogger().trace("Adding key maucro widgets and setting scrollbar position", this);
		kmlc.addWidgets(buttons);
		kmlc.addWidgets(labels);
		
		sb.setPosition(width - 80, 35);
		Maunsic.getLogger().trace("Revalidating containers", this);
		c.revalidate(0, 0, width, height);
		kmlc.revalidate(80, 35, width - 160, height - 83);
	}
	
	@Override
	public void onInit() {
		c = new Container();
		Maunsic.getLogger().trace("Creating title label", this);
		title = new Label(I18n.translate("conf.km.title"));
		Maunsic.getLogger().trace("Creating buttons...", this);
		newCC = new ButtonVanilla(100, 20, I18n.translate("conf.km.editor.cc.enter"), this);
		newLua = new ButtonVanilla(100, 20, I18n.translate("conf.km.editor.lua.enter"), this);
		back = new ButtonVanilla(50, 20, I18n.translate("conf.back"), this);
		
		Maunsic.getLogger().trace("Creating KML container", this);
		sb = new ScrollbarVanilla(10);
		kmlc = new FocusedContainer(sb, 14, 4);
		Maunsic.getLogger().trace("Adding widgets to main container", this);
		c.addWidgets(title, newCC, newLua, back);
		
		Maunsic.getLogger().trace("Adding containers to list", this);
		containers.add(kmlc);
		containers.add(c);
		selectedContainer = c;
	}
	
	@Override
	public void drawBackground() {
		super.drawBackground();
		Gui.drawRect(kmlc.left(), kmlc.top(), kmlc.right() - 10, kmlc.bottom(), 0x44444444);
	}
	
	@Override
	public void save() {
		Maunsic.getLogger().trace("Saving Key Maunsic to file", this);
		KeyMaucro.sort();
		KeyMaucro.save();
	}
	
	@Override
	public void onKeyTyped(char c, int kc) {
		super.onKeyTyped(c, kc);
		if (kc == Keyboard.KEY_DELETE) for (int i = 0; i < labels.length; i++)
			if (labels[i].isFocused()) {
				KeyMaucro.removeKeyMaucro(i);
				onRevalidate();
			}
	}
	
	@Override
	public void onButtonClicked(Button button) {
		if (button.equals(newCC)) Minecraft.getMinecraft().displayGuiScreen(new GuiAddKeyMaucro(this, false));
		else if (button.equals(newLua)) Minecraft.getMinecraft().displayGuiScreen(new GuiAddKeyMaucro(this, true));
		else if (button.equals(back)) close();
		else for (int i = 0; i < KeyMaucro.getKeyMaucros().size(); i++)
			if (KeyMaucro.getKeyMaucros().get(i).getName().equals(labels[i].getText()) && buttons[i].equals(button)) {
				KeyMaucro km = KeyMaucro.getKeyMaucros().get(i);
				GuiAddKeyMaucro gakm = new GuiAddKeyMaucro(this, km.getType().equals(KeyMaucro.Type.LUA));
				Minecraft.getMinecraft().displayGuiScreen(gakm);
				gakm.addValues(km.getName(), km.getData(), km.getKeyCode(), km.getExecutionPhase(), i, km.getShiftKeys());
				break;
			}
	}
	
	// public void addKeyMaucro(String name, String chat, int keycode, int... shiftKeys) {
	// KeyMaucro.addKeyMaucro(new KeyMaucro(name.trim(), chat, keycode, shiftKeys));
	// }
	//
	// public void setKeyMaucro(String name, String chat, int keycode, int index, int... shiftKeys) {
	// KeyMaucro.modifyKeyMaucro(index, new KeyMaucro(name.trim(), chat, keycode, shiftKeys));
	// }
}
