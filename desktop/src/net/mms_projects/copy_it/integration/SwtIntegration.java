package net.mms_projects.copy_it.integration;

import net.mms_projects.copy_it.ClipboardManager;
import net.mms_projects.copy_it.EnvironmentIntegration;
import net.mms_projects.copy_it.Settings;
import net.mms_projects.copy_it.SyncManager;

import org.eclipse.swt.widgets.Shell;

public class SwtIntegration extends EnvironmentIntegration {

	private BasicSwtIntegration swtIntegration;

	public SwtIntegration(Settings settings, Shell activityShell,
			SyncManager syncManager, ClipboardManager clipboardManager) {
		/*
		 * Adds SWT integration like a tray icon
		 */
		this.setSwtIntegration(new BasicSwtIntegration(this, settings,
				activityShell, syncManager, clipboardManager));
		this.addIntegration(this.getSwtIntegration());
	}

	@Override
	public void standaloneSetup() {
		// TODO Auto-generated method stub

	}

	public void setSwtIntegration(BasicSwtIntegration swtIntegration) {
		this.swtIntegration = swtIntegration;
	}

	public BasicSwtIntegration getSwtIntegration() {
		return this.swtIntegration;
	}
}
