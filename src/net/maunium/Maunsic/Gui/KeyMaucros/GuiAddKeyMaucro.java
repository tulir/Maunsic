package net.maunium.Maunsic.Gui.KeyMaucros;

import com.mcf.davidee.guilib.basic.BasicScreen;
import com.mcf.davidee.guilib.basic.Label;
import com.mcf.davidee.guilib.core.Button;
import com.mcf.davidee.guilib.core.Container;
import com.mcf.davidee.guilib.vanilla.ButtonVanilla;
import com.mcf.davidee.guilib.vanilla.TextFieldVanilla;
import com.mcf.davidee.guilib.vanilla.extended.KeySelectButton;
import com.mcf.davidee.guilib.vanilla.extended.MultiKeySelectButton;
import com.mcf.davidee.guilib.vanilla.extended.StateButton;

import net.maunium.Maunsic.KeyMaucros.CCKeyMaucro;
import net.maunium.Maunsic.KeyMaucros.KeyMaucro;
import net.maunium.Maunsic.KeyMaucros.LuaKeyMaucro;
import net.maunium.Maunsic.Util.I18n;

/**
 * Gui for adding key maucros
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class GuiAddKeyMaucro extends BasicScreen {
	private Container c;
	private Label title, varLabel, keyLabel, shiftKeysLabel, nameLabel, phaseLabel, keystatusLabel;
	private TextFieldVanilla name, var;
	private ButtonVanilla cancel, finish;
	private StateButton phase, keystatus;
	private KeySelectButton key;
	private MultiKeySelectButton shiftKeys;
	private int index;
	private boolean lua;
	
	public GuiAddKeyMaucro(GuiKeyMaucros parent, boolean mode) {
		super(parent);
		lua = mode;
		index = -1;
	}
	
	public void addValues(String name, String var, int key, KeyMaucro.ExecPhase phase, int index, int[] shiftKeys) {
		this.name.setText(name);
		this.var.setText(var);
		this.key.setKeycode(key);
		this.shiftKeys.setKeycodes(shiftKeys);
		this.shiftKeys.updateText();
		this.phase.setState(phase.toInt() > 2 ? phase.toInt() - 3 : phase.toInt());
		keystatus.setState(phase.toInt() > 2 ? 1 : 0);
		this.index = index;
		finish.setEnabled(true);
	}
	
	@Override
	public void onInit() {
		title = new Label(I18n.translate("conf.km.editor." + (lua ? "lua" : "cc") + ".title"));
		
		nameLabel = new Label(I18n.translate("conf.km.editor.name"), false);
		varLabel = new Label(I18n.translate("conf.km.editor." + (lua ? "lua.file" : "cc.commands")), false);
		keyLabel = new Label(I18n.translate("conf.km.editor.key"), false);
		shiftKeysLabel = new Label(I18n.translate("conf.km.editor.shiftkeys"), false);
		phaseLabel = new Label(I18n.translate("conf.km.editor.phase"), false);
		keystatusLabel = new Label(I18n.translate("conf.km.editor.ks"), false);
		
		name = new TextFieldVanilla(200, 20, new TextFieldVanilla.VanillaFilter());
		var = new TextFieldVanilla(200, 20, new TextFieldVanilla.VanillaFilter());
		key = new KeySelectButton(200, 20, this);
		shiftKeys = new MultiKeySelectButton(200, 20, this);
		phase = new StateButton(200, 20, this, 1, new StateButton.BlankFormat(I18n.translate("conf.km.editor.phase.precheck"),
				I18n.translate("conf.km.editor.phase.prekeys"), I18n.translate("conf.km.editor.phase.postkeys")));
		keystatus = new StateButton(200, 20, this, 1,
				new StateButton.BlankFormat(I18n.translate("conf.km.editor.ks.up"), I18n.translate("conf.km.editor.ks.down")));
				
		finish = new ButtonVanilla(150, 20, I18n.translate("conf.km.editor.finish"), this);
		finish.setEnabled(false);
		cancel = new ButtonVanilla(150, 20, I18n.translate("conf.km.editor.cancel"), this);
		
		c = new Container();
		c.addWidgets(title, nameLabel, varLabel, keyLabel, shiftKeysLabel, phaseLabel, keystatusLabel, name, var, key, shiftKeys, phase, keystatus, finish,
				cancel);
		containers.add(c);
	}
	
	@Override
	public void onRevalidate() {
		title.setPosition(width / 2, 15);
		
		nameLabel.setPosition(width / 6, 53);
		varLabel.setPosition(width / 6, 78);
		keyLabel.setPosition(width / 6, 103);
		shiftKeysLabel.setPosition(width / 6, 128);
		phaseLabel.setPosition(width / 6, 153);
		keystatusLabel.setPosition(width / 6, 178);
		
		name.setPosition(width / 3, 50);
		var.setPosition(width / 3, 75);
		key.setPosition(width / 3, 100);
		shiftKeys.setPosition(width / 3, 125);
		phase.setPosition(width / 3, 150);
		keystatus.setPosition(width / 3, 175);
		
		finish.setPosition(width / 2 - 150 - 2, height - 40);
		cancel.setPosition(width / 2 + 2, height - 40);
		
		c.revalidate(0, 0, width, height);
	}
	
	@Override
	public void save() {
		if (lua) {
			KeyMaucro km = new LuaKeyMaucro(name.getText(), var.getText(), key.getKeycode(),
					KeyMaucro.ExecPhase.fromInt(phase.getState() + keystatus.getState() * 3), shiftKeys.getKeycodes());
			if (index == -1) KeyMaucro.addKeyMaucro(km);
			else KeyMaucro.modifyKeyMaucro(index, km);
		} else {
			KeyMaucro km = new CCKeyMaucro(name.getText(), var.getText(), key.getKeycode(),
					KeyMaucro.ExecPhase.fromInt(phase.getState() + keystatus.getState() * 3), shiftKeys.getKeycodes());
			if (index == -1) KeyMaucro.addKeyMaucro(km);
			else KeyMaucro.modifyKeyMaucro(index, km);
		}
	}
	
	@Override
	public boolean onClose() {
		return save;
	}
	
	private boolean save = false;
	
	public void close(boolean save) {
		this.save = save;
		close();
	}
	
	@Override
	public void onButtonClicked(Button button, int code) {
		if (code != 0) return;
		if (button.equals(cancel)) close(false);
		else if (button.equals(finish)) close(true);
	}
	
	@Override
	public void onKeyTyped(char c, int kc) {
		super.onKeyTyped(c, kc);
		if (this.c.hasFocusedWidget()) {
			if (!shiftKeys.isFocused() && this.c.getFocusedWidget().equals(shiftKeys)) this.c.setFocused(null);
			else if (!key.isFocused() && this.c.getFocusedWidget().equals(key)) this.c.setFocused(null);
		} else if (shiftKeys.isFocused()) shiftKeys.focusLost();
		else if (key.isFocused()) key.focusLost();
		
		if (key.getKeycode() != -1 && !name.getText().isEmpty() && !var.getText().isEmpty()) finish.setEnabled(true);
		else finish.setEnabled(false);
	}
}
