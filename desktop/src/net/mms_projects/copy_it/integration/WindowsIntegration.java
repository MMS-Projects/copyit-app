package net.mms_projects.copy_it.integration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.mms_projects.copy_it.Activatable;
import net.mms_projects.copy_it.ClipboardManager;
import net.mms_projects.copy_it.EnvironmentIntegration;
import net.mms_projects.copy_it.FunctionalityManager;
import net.mms_projects.copy_it.JavaCommandLine;
import net.mms_projects.copy_it.PathBuilder;
import net.mms_projects.copy_it.SyncManager;

import org.apache.commons.io.FileUtils;

/**
 * This integration provider adds Windows specific integrations.
 */
public class WindowsIntegration extends EnvironmentIntegration {

	public WindowsIntegration(FunctionalityManager<Activatable> functionality,
			SyncManager syncManager, ClipboardManager clipboardManager) {
		/*
		 * Adds SWT integration like a tray icon
		 */
		BasicSwtIntegration swtIntegration = new BasicSwtIntegration(this,
				functionality, syncManager, clipboardManager);
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
		public boolean isEnabled() throws AutoStartSetupException {
			File file = new File(PathBuilder.getAutostartDirectory(),
					"copyit.bat");
			return file.exists();
		}

		@Override
		public void enableAutostart() throws AutoStartSetupException {
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

		@Override
		public void disableAutostart() throws AutoStartSetupException {
			File file = new File(PathBuilder.getAutostartDirectory(),
					"copyit.bat");
			file.delete();
		}

	}

}
