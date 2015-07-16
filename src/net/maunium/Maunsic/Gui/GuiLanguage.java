package net.maunium.Maunsic.Gui;

import com.mcf.davidee.guilib.basic.BasicScreen;
import com.mcf.davidee.guilib.basic.Label;
import com.mcf.davidee.guilib.core.Button;
import com.mcf.davidee.guilib.core.Container;
import com.mcf.davidee.guilib.core.Scrollbar;
import com.mcf.davidee.guilib.vanilla.ButtonVanilla;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Util.I18n;

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
		c = new Container();
		title = new Label(I18n.translate("conf.language.title"));
		
//		automated = new StateButton(I18n.translate("conf.phase.automated"), host.actionPhase.automated ? 1 : 0);
//		noclip = new StateButton(I18n.translate("conf.phase.noclip"), host.actionPhase.noclip ? 1 : 0);
//		ground = new StateButton(I18n.translate("conf.phase.ground"), host.actionPhase.ground ? 1 : 0);
//		fall = new StateButton(I18n.translate("conf.phase.fall"), host.actionPhase.fall ? 1 : 0);
//		
//		verticalPackets = new ExtendedIntSlider(I18n.translate("conf.phase.packets.vertical"), host.actionPhase.verticalPackets, 1, 10);
//		horizontalPackets = new ExtendedIntSlider(I18n.translate("conf.phase.packets.horizontal"), host.actionPhase.horizontalPackets, 1, 10);
//		movVertical = new ExtendedIntSlider(new ExtendedIntSlider.Format() {
//			@Override
//			public String format(int amount) {
//				return I18n.translate("conf.phase.move.vertical") + ": " + amount / 10.0;
//			}
//		}, (int) (host.actionPhase.movVertical * 10), 1, 50);
//		movHorizontal = new ExtendedIntSlider(new ExtendedIntSlider.Format() {
//			@Override
//			public String format(int amount) {
//				return I18n.translate("conf.phase.move.horizontal") + ": " + amount / 10.0;
//			}
//		}, (int) (host.actionPhase.movHorizontal * 10), 1, 50);
		
		back = new ButtonVanilla(200, 20, I18n.translate("conf.back"), this);
		
		c.addWidgets(title, back);
		containers.add(lc);
		containers.add(c);
	}
	
	@Override
	protected void onRevalidate() {
		title.setPosition(width / 2, 15);
		
//		automated.setPosition(width / 2 - automated.getWidth() - 12, 40);
//		noclip.setPosition(width / 2 - noclip.getWidth() - 12, 65);
//		ground.setPosition(width / 2 - ground.getWidth() - 12, 90);
//		fall.setPosition(width / 2 - fall.getWidth() - 12, 115);
//		
//		verticalPackets.setPosition(width / 2 + 12, 40);
//		horizontalPackets.setPosition(width / 2 + 12, 65);
//		movVertical.setPosition(width / 2 + 12, 90);
//		movHorizontal.setPosition(width / 2 + 12, 115);
		
		back.setPosition(width / 2 - back.getWidth() / 2, height - 40);
		
		c.revalidate(0, 0, width, height);
	}
	
	@Override
	public void save() {
	
	}
	
	@Override
	public void onButtonClicked(Button b, int code) {
		close();
	}
}
