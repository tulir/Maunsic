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
	
	/**
	 * Construct an interval action with the default interval (20ms)
	 */
	public IntervalAction() {
		this(20);
	}
	
	/**
	 * Construct an interval action with the given interval.
	 */
	public IntervalAction(int interval) {
		this.interval = interval;
	}
	
	/**
	 * Execute the action after checking that the specific interval has passed.
	 */
	public abstract void executeInterval();
	
	@Override
	public final void execute() {
		if (Minecraft.getSystemTime() - previousExec > interval) {
			executeInterval();
			previousExec = Minecraft.getSystemTime();
		}
	}
}
