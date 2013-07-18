package net.mms_projects.copy_it;

import java.util.ArrayList;
import java.util.List;

import net.mms_projects.copy_it.ui.UserInterfaceImplementation;

abstract public class EnvironmentIntegration {

	private NotificationManager notificationManager;
	private List<EnvironmentIntegration> integrationProviders = new ArrayList<EnvironmentIntegration>();
	private UserInterfaceImplementation userInterfaceImplementation;

	/**
	 * This will set the current notification manager to the one provided
	 * 
	 * @param notificationManager
	 *            The notification manager to set
	 */
	public void setNotificationManager(NotificationManager notificationManager) {
		this.notificationManager = notificationManager;
	}

	/**
	 * This returns the current notification manager
	 * 
	 * @return The current notification manager
	 */
	public NotificationManager getNotificationManager() {
		return this.notificationManager;
	}

	/**
	 * Adds some sub integration that will get setup after the parent has been
	 * setup.
	 * 
	 * @param integrationProvider
	 *            The actual integration provider to add
	 */
	public void addIntegration(EnvironmentIntegration integrationProvider) {
		this.integrationProviders.add(integrationProvider);
	}

	/**
	 * This method initializes the setup of the integration.
	 */
	final public void setup() {
		if (this.getUserInterfaceImplementation() == null) {
			throw new IllegalStateException("No UserInterfaceImplementation set");
		}
		this.standaloneSetup();

		for (EnvironmentIntegration integrationProvider : this.integrationProviders) {
			integrationProvider.setUserInterfaceImplementation(this.getUserInterfaceImplementation());
			integrationProvider.setup();
		}
	}

	/**
	 * This method handles the internal setup of the integration. Do not execute
	 * this directly use {@link EnvironmentIntegration#setup
	 * EnvironmentIntegration.setup} instead!
	 */
	abstract public void standaloneSetup();
	
	public void setUserInterfaceImplementation(UserInterfaceImplementation userInterfaceImplementation) {
		this.userInterfaceImplementation = userInterfaceImplementation;
	}
	
	protected UserInterfaceImplementation getUserInterfaceImplementation() {
		return this.userInterfaceImplementation;
	}

	/**
	 * A interface that describes a basic notification manager with one method
	 * to send a notification.
	 */
	static public interface NotificationManager {
		enum NotificationUrgency {
			LOW, NORMAL, CRITICAL
		}

		/**
		 * This sends a notification to the user
		 * 
		 * @param id
		 *            The optional notification ID that this notification
		 *            replaces. The server must atomically (ie with no flicker
		 *            or other visual cues) replace the given notification with
		 *            this one. This allows clients to effectively modify the
		 *            notification while it's active. A value of value of 0
		 *            means that this notification won't replace any existing
		 *            notifications.
		 * @param urgency
		 *            The urgency the notification should have.
		 * @param icon
		 *            The optional program icon of the calling application. See
		 *            Icons and Images. Can be an empty string, indicating no
		 *            icon.
		 * @param summary
		 *            The summary text briefly describing the notification.
		 * @param body
		 *            The optional detailed body text. Can be empty.
		 */
		public void notify(int id, NotificationUrgency urgency, String icon,
				String summary, String body);

	}

}
