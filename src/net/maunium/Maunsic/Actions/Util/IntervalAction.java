package net.maunium.Maunsic.Actions.Util;

import net.minecraft.client.Minecraft;

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
