package net.mms_projects.copy_it;

abstract public class EnvironmentIntegration {

	private NotificationManager notificationManager;

	public void setNotificationManager(NotificationManager notificationManager) {
		this.notificationManager = notificationManager;
	}

	public NotificationManager getNotificationManager() {
		return this.notificationManager;
	}

	static public interface NotificationManager {
		enum NotificationUrgency {
			LOW, NORMAL, CRITICAL
		}
		/**
		 * 
		 * @param id
		 *            The optional notification ID that this notification
		 *            replaces. The server must atomically (ie with no flicker
		 *            or other visual cues) replace the given notification with
		 *            this one. This allows clients to effectively modify the
		 *            notification while it's active. A value of value of 0
		 *            means that this notification won't replace any existing
		 *            notifications.
		 * @param icon
		 *            The optional program icon of the calling application. See
		 *            Icons and Images. Can be an empty string, indicating no
		 *            icon.
		 * @param summary
		 *            The summary text briefly describing the notification.
		 * @param body
		 *            The optional detailed body text. Can be empty.
		 * @return
		 */
		public void notify(int id, NotificationUrgency urgency, String icon, String summary,
				String body);

	}

}
