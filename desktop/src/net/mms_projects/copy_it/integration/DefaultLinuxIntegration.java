package net.mms_projects.copy_it.integration;

import net.mms_projects.copy_it.Activatable;
import net.mms_projects.copy_it.ClipboardManager;
import net.mms_projects.copy_it.EnvironmentIntegration;
import net.mms_projects.copy_it.Config;
import net.mms_projects.copy_it.FunctionalityManager;
import net.mms_projects.copy_it.SyncManager;

import org.eclipse.swt.widgets.Shell;
import org.freedesktop.dbus.DBusConnection;

public class DefaultLinuxIntegration extends EnvironmentIntegration {

	public DefaultLinuxIntegration(DBusConnection dbusConnection,
			Config settings, FunctionalityManager<Activatable> functionality, Shell activityShell, SyncManager syncManager,
			ClipboardManager clipboardManager) {
		/*
		 * Adds SWT integration like a tray icon
		 */
		BasicSwtIntegration swtIntegration = new BasicSwtIntegration(this,
				settings, functionality, activityShell, syncManager, clipboardManager);
		this.addIntegration(swtIntegration);

		/*
		 * Add some listeners to the SWT integration
		 */
		syncManager.addListener(swtIntegration);
		clipboardManager.addListener(swtIntegration);

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
