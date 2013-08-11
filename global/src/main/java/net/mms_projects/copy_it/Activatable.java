package net.mms_projects.copy_it;

import net.mms_projects.copy_it.listeners.EnabledListener;

/**
 * This interface adds methods to a class that can be enabled. This will give
 * classes a general usage for enabling and disabling.
 */
public interface Activatable {

	/**
	 * This enabled the class
	 */
	public void enable();

	/**
	 * This disables the class
	 */
	public void disable();

	/**
	 * Returns true if the class is enabled
	 * 
	 * @return This returns whenever the class is enabled
	 */
	public boolean isEnabled();

	/**
	 * Changes the enabled state of the class
	 * 
	 * @param enabled
	 *            Whenever the class needs to be enabled
	 */
	public void setEnabled(boolean enabled);

	/**
	 * Adds a listener that will get called when the class is enabled or disabled
	 * @param listener The listener to add
	 */
	public void addEnabledListener(EnabledListener listener);

}
