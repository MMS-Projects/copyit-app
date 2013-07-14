package net.mms_projects.copy_it.integration;

import net.mms_projects.copy_it.ClipboardManager;
import net.mms_projects.copy_it.Settings;
import net.mms_projects.copy_it.SyncManager;

import org.eclipse.swt.widgets.Shell;
import org.freedesktop.dbus.DBusConnection;

public class GnomeIntegration extends SwtIntegration {

	public GnomeIntegration(DBusConnection dbusConnection, Settings settings,
			Shell activityShell, SyncManager syncManager,
			ClipboardManager clipboardManager) {
		super(settings, activityShell, syncManager, clipboardManager);

		/*
		 * Add FreeDesktop integrations like notifications and writing .desktop
		 * files and the appropriate icons for the .desktop files
		 */
		this.addIntegration(new FreeDesktopIntegration(this, dbusConnection));
	}

	@Override
	public void standaloneSetup() {
		// TODO Auto-generated method stub

	}

}
