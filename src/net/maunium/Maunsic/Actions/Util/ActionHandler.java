package net.maunium.Maunsic.Actions.Util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.maunium.Maunsic.Maunsic;

import net.minecraft.client.Minecraft;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * System to run specific tasks each tick if enabled.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class ActionHandler {
	private Maunsic host;
	private Set<TickAction> startActions = new HashSet<TickAction>(), endActions = new HashSet<TickAction>(), livingActions = new HashSet<TickAction>(),
			worldActions = new HashSet<TickAction>();
	private Set<StatusAction> allActions = new HashSet<StatusAction>();
	
	/**
	 * Initialize the action handler. This should only be used by the Maunsic main class.
	 */
	public ActionHandler(Maunsic host) {
		this.host = host;
	}
	
	/**
	 * Register an action.
	 * 
	 * @param ta The action to register.
	 * @param phase The tick event phase to execute this action at.
	 */
	public <T extends StatusAction> T registerAction(T ta, Phase phase) {
		switch (phase) {
			case TICKSTART:
				startActions.add((TickAction) ta);
				break;
			case TICKEND:
				endActions.add((TickAction) ta);
				break;
			case LIVING:
				livingActions.add((TickAction) ta);
				break;
			case WORLD:
				worldActions.add((TickAction) ta);
				break;
			default:
		}
		allActions.add(ta);
		return ta;
	}
	
	/**
	 * Unregister an action.
	 * 
	 * @param ta The action to unregister.
	 * @param phase The value used when registering the action.
	 */
	public <T extends StatusAction> void unregisterAction(T ta, Phase phase) {
		switch (phase) {
			case TICKSTART:
				startActions.remove(ta);
				break;
			case TICKEND:
				endActions.remove(ta);
				break;
			case LIVING:
				livingActions.remove(ta);
				break;
			case WORLD:
				worldActions.remove(ta);
				break;
			default:
		}
		allActions.remove(ta);
	}
	
	/**
	 * Save the configurations of all actions to RAM by calling the saveData method of each action.
	 */
	public void saveAll() {
		for (StatusAction a : allActions)
			a.saveData(host.getConfig());
	}
	
	/**
	 * Load the configurations of all actions to RAM by calling the saveData method of each action.
	 */
	public void loadAll() {
		for (StatusAction a : allActions)
			a.loadData(host.getConfig());
	}
	
	@SubscribeEvent
	public void onEntityUpdate(LivingEvent.LivingUpdateEvent evt) {
		if (evt.entity == Minecraft.getMinecraft().thePlayer) {
			for (TickAction ta : livingActions)
				if (ta.isActive()) ta.execute();
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
	public void onWorldRender(RenderWorldLastEvent evt) {
		for (TickAction ta : worldActions)
			if (ta.isActive()) ta.execute();
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
			if (s != null) list.add(s);
	}
	
	/**
	 * Contains the phase at which an action is to be executed.
	 * 
	 * @author Tulir293
	 * @since 0.1
	 */
	public static enum Phase {
		TICKSTART, TICKEND, LIVING, WORLD, STATUS;
	}
}
