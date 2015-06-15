package net.maunium.Maunsic.TickActions.Util;

/**
 * The base class for all Maunsic tick actions.
 * 
 * @author Tulir293
 * @since 0.1
 */
public interface TickAction extends StatusAction {
	/**
	 * Execute the action.
	 */
	public abstract void execute();
}
