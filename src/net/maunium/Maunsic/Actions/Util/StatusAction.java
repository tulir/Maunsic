package net.maunium.Maunsic.Actions.Util;

import net.maunium.Maunsic.Util.MaunsiConfig;

/**
 * The base class for all Maunsic actions.
 * 
 * @author Tulir293
 * @since 0.1
 */
public abstract class StatusAction {
	protected boolean active = false;
	
	/**
	 * Deactivate this action.
	 */
	public void activate() {
		active = true;
	}
	
	/**
	 * Activate this action.
	 */
	public void deactivate() {
		active = false;
	}
	
	/**
	 * If this action is active, deactivate it. Otherwise activate it.
	 */
	public void toggle() {
		if (isActive()) deactivate();
		else activate();
	}
	
	/**
	 * @return True if this action is active and should be executed on the next tick. False otherwise.
	 */
	public boolean isActive() {
		return active;
	}
	
	/**
	 * Save the settings of this action to the given MaunsiConfig.
	 */
	public void saveData(MaunsiConfig conf) {}
	
	/**
	 * Load the settings of this action from the given MaunsiConfig.
	 */
	public void loadData(MaunsiConfig conf) {}
	
	/**
	 * @return The text to render on the left side of the screen when this action is active. An empty string will render an empty line and a null string will be
	 *         ignored. To render no text, return null.
	 */
	public abstract String[] getStatusText();
}
