package net.mms_projects.copy_it.integration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.mms_projects.copy_it.Activatable;
import net.mms_projects.copy_it.ClipboardManager;
import net.mms_projects.copy_it.EnvironmentIntegration;
import net.mms_projects.copy_it.FunctionalityManager;
import net.mms_projects.copy_it.JavaCommandLine;
import net.mms_projects.copy_it.SyncManager;
import net.mms_projects.utils.StringUtils;

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
	}

	@Override
	public void standaloneSetup() {
		this.setAutostartManager(new WindowsAutoStartManager());
	}

	class WindowsAutoStartManager implements
			EnvironmentIntegration.AutostartManager {

		static public final String CHECK_COMMAND = "reg query HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run /v %1$s";
		static public final String ENABLE_COMMAND = "reg add HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run /v %1$s /t REG_SZ /d \"%2$s\"";
		static public final String DISABLE_COMMAND = "reg delete HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run /v %1$s /f";

		static public final String KEY_NAME = "copyit";

		@Override
		public boolean isEnabled() throws AutoStartSetupException {
			Process process = null;
			try {
				process = Runtime.getRuntime().exec(
						String.format(CHECK_COMMAND, KEY_NAME));
			} catch (IOException ioException) {
				throw new AutoStartSetupException(ioException);
			}

			BufferedReader inputStream = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			String line = null;
			try {
				while ((line = inputStream.readLine()) != null) {
					if (StringUtils.containsIgnoreCase(line, KEY_NAME)) {
						return true;
					}
				}
			} catch (IOException ioException) {
				throw new AutoStartSetupException(ioException);
			}

			return false;
		}

		@Override
		public void enableAutostart() throws AutoStartSetupException {
			try {
				Runtime.getRuntime().exec(
						String.format(ENABLE_COMMAND, KEY_NAME,
								JavaCommandLine.generateJavaCommandLine()));
			} catch (IOException e) {
				throw new AutoStartSetupException(e);
			}
		}

		@Override
		public void disableAutostart() throws AutoStartSetupException {
			try {
				Runtime.getRuntime().exec(
						String.format(DISABLE_COMMAND, KEY_NAME));
			} catch (IOException e) {
				throw new AutoStartSetupException(e);
			}
		}

	}

}
