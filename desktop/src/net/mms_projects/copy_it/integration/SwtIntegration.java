package net.mms_projects.copy_it.integration;

import net.mms_projects.copy_it.Activatable;
import net.mms_projects.copy_it.ClipboardManager;
import net.mms_projects.copy_it.Config;
import net.mms_projects.copy_it.EnvironmentIntegration;
import net.mms_projects.copy_it.FunctionalityManager;
import net.mms_projects.copy_it.SyncManager;

import org.eclipse.swt.widgets.Shell;

public class SwtIntegration extends EnvironmentIntegration {

	public SwtIntegration(Config settings,
			FunctionalityManager<Activatable> functionality,
			Shell activityShell, SyncManager syncManager,
			ClipboardManager clipboardManager) {
		/*
		 * Adds SWT integration like a tray icon
		 */
		BasicSwtIntegration swtIntegration = new BasicSwtIntegration(this,
				settings, functionality, activityShell, syncManager,
				clipboardManager);
		this.addIntegration(swtIntegration);

		/*
		 * Add some listeners to the SWT integration
		 */
		syncManager.addListener(swtIntegration);
		clipboardManager.addListener(swtIntegration);
	}

	@Override
	public void standaloneSetup() {
		// TODO Auto-generated method stub

	}

}
