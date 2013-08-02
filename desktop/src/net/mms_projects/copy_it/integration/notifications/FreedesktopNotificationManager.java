package net.mms_projects.copy_it.integration.notifications;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import net.mms_projects.copy_it.EnvironmentIntegration.NotificationManager;

import org.freedesktop.Notifications;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.UInt32;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;

public class FreedesktopNotificationManager implements NotificationManager {

	private Notifications notifications;

	public FreedesktopNotificationManager(DBusConnection dbusConnection)
			throws DBusException {
		this.notifications = dbusConnection.getRemoteObject(
				"org.freedesktop.Notifications",
				"/org/freedesktop/Notifications", Notifications.class);

	}

	@Override
	public void notify(int id, NotificationManager.NotificationUrgency urgency,
			String icon, String summary, String body) {
		Map<String, Variant<Byte>> hints = new HashMap<String, Variant<Byte>>();
		switch (urgency) {
		case LOW:
			hints.put("urgency", new Variant<Byte>((byte) 0));
			break;
		case NORMAL:
			hints.put("urgency", new Variant<Byte>((byte) 1));
			break;
		case CRITICAL:
			hints.put("urgency", new Variant<Byte>((byte) 2));
			break;
		}
		this.notifications.Notify("CopyIt", new UInt32(id), icon, summary,
				body, new LinkedList<String>(), hints, -1);
	}

}