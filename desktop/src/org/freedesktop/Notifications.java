package org.freedesktop;

import java.util.List;
import java.util.Map;

import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.UInt32;
import org.freedesktop.dbus.Variant;

public interface Notifications extends DBusInterface {
	/**
	 * 
	 * @param app_name
	 *            The optional name of the application sending the notification.
	 *            Can be blank.
	 * @param id
	 *            The optional notification ID that this notification replaces.
	 *            The server must atomically (ie with no flicker or other visual
	 *            cues) replace the given notification with this one. This
	 *            allows clients to effectively modify the notification while
	 *            it's active. A value of value of 0 means that this
	 *            notification won't replace any existing notifications.
	 * @param icon
	 *            The optional program icon of the calling application. See
	 *            Icons and Images. Can be an empty string, indicating no icon.
	 * @param summary
	 *            The summary text briefly describing the notification.
	 * @param body
	 *            The optional detailed body text. Can be empty.
	 * @param actions
	 *            Actions are sent over as a list of pairs. Each even element in
	 *            the list (starting at index 0) represents the identifier for
	 *            the action. Each odd element in the list is the localized
	 *            string that will be displayed to the user.
	 * @param hints
	 *            Optional hints that can be passed to the server from the
	 *            client program. Although clients and servers should never
	 *            assume each other supports any specific hints, they can be
	 *            used to pass along information, such as the process PID or
	 *            window ID, that the server may be able to make use of. See
	 *            Hints. Can be empty.
	 * @param timeout
	 *            The timeout time in milliseconds since the display of the
	 *            notification at which the notification should automatically
	 *            close. If -1, the notification's expiration time is dependent
	 *            on the notification server's settings, and may vary for the
	 *            type of notification. If 0, never expire.
	 * @return
	 */
	public UInt32 Notify(String app_name, UInt32 id, String icon,
			String summary, String body, List<String> actions,
			Map<String, Variant<Byte>> hints, int timeout);
}