package net.maunium.Maunsic.TickActions.Util;

import net.minecraft.util.EnumChatFormatting;

/**
 * The base class for all Maunsic actions.
 * 
 * @author Tulir293
 * @since 0.1
 */
public interface StatusAction {
	/**
	 * @return The name of the action.
	 */
	public String getName();
	
	/**
	 * @return The text to render on the left side of the screen when this action is active. An empty string will render an empty line. To render no text,
	 *         return null.
	 */
	public default String[] getStatusText() {
		// Default implementation returns the name and a green "ON" text
		return new String[] { getName() + EnumChatFormatting.GREEN + " ON" };
	}
	
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
}
