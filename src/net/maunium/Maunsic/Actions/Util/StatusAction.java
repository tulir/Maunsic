package net.maunium.Maunsic.Actions.Util;

import net.maunium.Maunsic.Util.MaunsiConfig;

/**
 * The base class for all Maunsic actions.
 * 
 * @author Tulir293
 * @since 0.1
 */
public interface StatusAction {
	/**
	 * @return The text to render on the left side of the screen when this action is active. An empty string will render an empty line and a null string will be
	 *         ignored. To render no text, return null.
	 */
	public String[] getStatusText();
	
	/**
	 * @return True if this action is active and should be executed on the next tick. False otherwise.
	 */
	public boolean isActive();
	
	/**
	 * Activate or deactivate this action.
	 * 
	 * @param active True if the action should be activated, false if it should be deactivated.
	 */
	public void setActive(boolean active);
	
	public void saveData(MaunsiConfig conf);
	
	public void loadData(MaunsiConfig conf);
}
