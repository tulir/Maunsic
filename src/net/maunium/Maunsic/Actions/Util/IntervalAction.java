package net.maunium.Maunsic.Actions.Util;

import net.minecraft.client.Minecraft;

/**
 * Base class for actions that need to be executed at specific intervals.
 * 
 * @author Tulir293
 * @since 0.1
 */
public abstract class IntervalAction extends TickAction {
	protected int interval;
	protected long previousExec = 0;
	
	public IntervalAction() {
		this(20);
	}
	
	public IntervalAction(int interval) {
		this.interval = interval;
	}
	
	public abstract void executeInterval();
	
	@Override
	public final void execute() {
		if (Minecraft.getSystemTime() - previousExec > interval) {
			executeInterval();
			previousExec = Minecraft.getSystemTime();
		}
	}
}
