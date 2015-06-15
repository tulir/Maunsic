package net.maunium.Maunsic.Actions.Util;

import net.minecraft.client.Minecraft;

public abstract class IntervalAction implements TickAction {
	protected int interval;
	protected long previousExec = 0;
	
	public IntervalAction() {
		this(10);
	}
	
	public IntervalAction(int interval) {
		this.interval = interval;
	}
	
	public abstract void executeInterval();
	
	@Override
	public void execute() {
		if (Minecraft.getSystemTime() - previousExec > interval) {
			executeInterval();
			previousExec = Minecraft.getSystemTime();
		}
	}
}
