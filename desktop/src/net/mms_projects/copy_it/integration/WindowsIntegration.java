package net.mms_projects.copy_it.integration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.mms_projects.copy_it.ClipboardManager;
import net.mms_projects.copy_it.EnvironmentIntegration;
import net.mms_projects.copy_it.JavaCommandLine;
import net.mms_projects.copy_it.PathBuilder;
import net.mms_projects.copy_it.Settings;
import net.mms_projects.copy_it.SyncManager;

import org.apache.commons.io.FileUtils;
import org.eclipse.swt.widgets.Shell;

/**
 * This integration provider adds Windows specific integrations.
 */
public class WindowsIntegration extends EnvironmentIntegration {

	public WindowsIntegration(Settings settings, Shell activityShell,
			SyncManager syncManager, ClipboardManager clipboardManager) {
		/*
		 * Adds SWT integration like a tray icon
		 */
		BasicSwtIntegration swtIntegration = new BasicSwtIntegration(this,
				settings, activityShell, syncManager, clipboardManager);
		this.addIntegration(swtIntegration);

		/*
		 * Add some listeners to the SWT integration
		 */
		syncManager.addListener(swtIntegration);
		clipboardManager.addListener(swtIntegration);
	}

	@Override
	public void standaloneSetup() {
		this.setAutostartManager(new WindowsAutoStartManager());
	}

	class WindowsAutoStartManager implements
			EnvironmentIntegration.AutostartManager {

		@Override
		public void setupAutostartup() throws AutoStartSetupException {
			List<String> content = new ArrayList<String>();
			content.add("@echo off");
			content.add("start " + JavaCommandLine.generateJavaCommandLine());

			File file = new File(PathBuilder.getAutostartDirectory(),
					"copyit.bat");
			try {
				FileUtils.writeLines(file, content);
			} catch (IOException e) {
				throw new AutoStartSetupException(e);
			}
		}

	}

}
