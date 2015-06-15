package net.maunium.Maunsic.TickActions.Util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.client.Minecraft;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * System to run specific tasks each tick if enabled.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class ActionHandler {
//	private Maunsic host;
	private Set<TickAction> startActions = new HashSet<TickAction>(), endActions = new HashSet<TickAction>();
	private Set<StatusAction> allActions = new HashSet<StatusAction>();
	
//	public TickActionHandler(Maunsic host) {
//		this.host = host;
//	}
	
	/**
	 * Register a tick action.
	 * 
	 * @param ta The action to register.
	 * @param phase The tick event phase to execute this action at.
	 */
	public <T extends TickAction> T registerTickAction(T ta, TickEvent.Phase phase) {
		switch (phase) {
			case START:
				startActions.add(ta);
				break;
			case END:
				endActions.add(ta);
				break;
		}
		return registerAction(ta);
	}
	
	/**
	 * Unregister a tick action.
	 * 
	 * @param ta The action to unregister.
	 * @param phase The value used when registering the action.
	 */
	public <T extends TickAction> void unregisterTickAction(T ta, TickEvent.Phase phase) {
		switch (phase) {
			case START:
				startActions.remove(ta);
				break;
			case END:
				endActions.remove(ta);
				break;
		}
		unregisterAction(ta);
	}
	
	/**
	 * Register a tick action.
	 * 
	 * @param ta The action to register.
	 * @param phase The tick event phase to execute this action at.
	 */
	public <T extends StatusAction> T registerAction(T sa) {
		allActions.add(sa);
		return sa;
	}
	
	/**
	 * Unregister a tick action.
	 * 
	 * @param ta The action to unregister.
	 * @param phase The value used when registering the action.
	 */
	public <T extends StatusAction> void unregisterAction(T sa) {
		allActions.add(sa);
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
		if (Minecraft.getMinecraft().gameSettings.showDebugInfo) return;
		for (StatusAction sa : allActions)
			if (sa.isActive()) add(evt.left, sa.getStatusText());
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
