package net.mms_projects.copy_it.integration;

import net.mms_projects.copy_it.ClipboardManager;
import net.mms_projects.copy_it.Settings;
import net.mms_projects.copy_it.SyncManager;
import net.mms_projects.copy_it.integration.notifications.FreedesktopNotificationManager;

import org.eclipse.swt.widgets.Shell;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;

public class GnomeIntegration extends BasicSwtIntegration {

	public GnomeIntegration(DBusConnection dbusConnection, Settings settings,
			Shell activityShell, SyncManager syncManager,
			ClipboardManager clipboardManager) {
		super(settings, activityShell, syncManager, clipboardManager);

		try {
			this.setNotificationManager(new FreedesktopNotificationManager(
					dbusConnection));
		} catch (DBusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
