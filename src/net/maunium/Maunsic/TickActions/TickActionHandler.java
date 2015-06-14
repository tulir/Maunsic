package net.maunium.Maunsic.TickActions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * System to run specific tasks each tick if enabled.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class TickActionHandler {
//	private Maunsic host;
	private Set<TickAction> startActions = new HashSet<TickAction>(), endActions = new HashSet<TickAction>();
	
//	public TickActionHandler(Maunsic host) {
//		this.host = host;
//	}
	
	/**
	 * Register an action.
	 * 
	 * @param ta The action to register.
	 * @param phase The tick event phase to execute this action at.
	 */
	public void registerAction(TickAction ta, TickEvent.Phase phase) {
		switch (phase) {
			case START:
				startActions.add(ta);
			case END:
				endActions.add(ta);
		}
	}
	
	/**
	 * Unregister an action.
	 * 
	 * @param ta The action to unregister.
	 * @param phase The value used when registering the action.
	 */
	public void unregisterAction(TickAction ta, TickEvent.Phase phase) {
		switch (phase) {
			case START:
				startActions.remove(ta);
			case END:
				endActions.remove(ta);
		}
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.PlayerTickEvent evt) {
		if (evt.phase.equals(TickEvent.Phase.START)) {
			for (TickAction ta : startActions)
				if (ta.isActive()) ta.execute();
		} else if (evt.phase.equals(TickEvent.Phase.END)) {
			for (TickAction ta : endActions)
				if (ta.isActive()) ta.execute();
		}
	}
	
	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent.Text evt) {
		for (TickAction ta : startActions)
			if (ta.isActive()) add(evt.left, ta.getStatusText());
		for (TickAction ta : endActions)
			if (ta.isActive()) add(evt.left, ta.getStatusText());
	}
	
	/**
	 * Add the contents of {@code objects} to {@code list}
	 */
	private void add(List<String> list, String[] objects) {
		if (objects == null) return;
		for (String s : objects)
			list.add(s);
	}
}
