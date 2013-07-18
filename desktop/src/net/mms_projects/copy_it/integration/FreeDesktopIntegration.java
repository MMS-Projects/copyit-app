package net.mms_projects.copy_it.integration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.mms_projects.copy_it.EnvironmentIntegration;
import net.mms_projects.copy_it.JavaCommandLine;
import net.mms_projects.copy_it.Messages;
import net.mms_projects.copy_it.PathBuilder;
import net.mms_projects.copy_it.integration.notifications.FreedesktopNotificationManager;
import net.mms_projects.copy_it.listeners.EnabledListener;

import org.apache.commons.io.FileUtils;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;

public class FreeDesktopIntegration extends EnvironmentIntegration {

	private EnvironmentIntegration parentIntegration;
	private DBusConnection dbusConnection;

	public FreeDesktopIntegration(EnvironmentIntegration parentIntegration,
			DBusConnection dbusConnection) {
		this.parentIntegration = parentIntegration;
		this.dbusConnection = dbusConnection;
	}

	/**
	 * Sets up the FreeDesktop integration with FreeDesktop notifications and
	 * FreeDesktop auto start support using desktop files.
	 */
	@Override
	public void standaloneSetup() {
		try {
			/*
			 * Sets the FreeDesktop notification manager
			 */
			this.parentIntegration
					.setNotificationManager(new FreedesktopNotificationManager(
							this.dbusConnection));
		} catch (DBusException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		/*
		 * Sets the FreeDesktop autostart manager
		 */
		this.parentIntegration
				.setAutostartManager(new FreeDesktopAutostartManager());

		/*
		 * Write the 16x16 icon the the icon directory
		 */
		try {
			URL inputUrl = getClass().getResource("/images/icon-16-mono.png");
			File dest = new File(PathBuilder.getIconDirectory(16), "copyit.png");
			FileUtils.copyURLToFile(inputUrl, dest);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		/*
		 * Write the 512x512 icon the the icon directory
		 */
		try {
			URL inputUrl = getClass().getResource("/images/icon-512.png");
			File dest = new File(PathBuilder.getIconDirectory(512),
					"copyit.png");
			FileUtils.copyURLToFile(inputUrl, dest);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		/*
		 * This writes a .desktop file to the directory where launcher .desktop
		 * files should be.
		 */
		this.writeDesktopFile(PathBuilder.getLauncherShortcutDirectory());
	}

	/**
	 * This method writes a .desktop for the app to the specified path
	 * 
	 * @param path
	 *            The path the .desktop file should be created in
	 */
	private void writeDesktopFile(File path) {
		List<String> content = this.generateDesktopContents();

		File file = new File(path, "copyit.desktop");
		try {
			FileUtils.writeLines(file, content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		file.setExecutable(true);
	}

	/**
	 * This methods returns a list of all lines in the .desktop file
	 * 
	 * @return List of all lines for the .desktop file
	 */
	private List<String> generateDesktopContents() {
		List<String> content = new ArrayList<String>();
		content.add("[Desktop Entry]");
		content.add("Version=1.0");
		content.add("Type=Application");
		content.add("Name=" + Messages.getString("app_name"));
		content.add("Icon=copyit");
		content.add("Exec=" + JavaCommandLine.generateJavaCommandLine());
		content.add("Terminal=false");
		content.add("StartupNotify=true");
		return content;
	}

	/**
	 * The FreeDesktop auto start manager that writes follows the FreeDesktop
	 * specifications
	 */
	class FreeDesktopAutostartManager implements
			EnvironmentIntegration.AutostartManager {

		private List<EnabledListener> enabledListeners = new ArrayList<EnabledListener>();

		@Override
		public boolean isEnabled() {
			File file = new File(PathBuilder.getAutostartDirectory(),
					"copyit.desktop");
			return file.exists();
		}

		/**
		 * This creates a .desktop file in the auto start directory to make it
		 * auto start
		 */
		@Override
		public void enable() {
			/*
			 * Write a .desktop file to the auto start directory
			 */
			FreeDesktopIntegration.this.writeDesktopFile(PathBuilder
					.getAutostartDirectory());

			for (EnabledListener listener : this.enabledListeners) {
				listener.onEnabled();
			}
		}

		@Override
		public void disable() {
			File file = new File(PathBuilder.getAutostartDirectory(),
					"copyit.desktop");
			file.delete();

			for (EnabledListener listener : this.enabledListeners) {
				listener.onDisabled();
			}
		}

		@Override
		public void setEnabled(boolean enabled) {
			if (enabled) {
				this.enable();
			} else {
				this.disable();
			}
		}

		@Override
		public void addEnabledListener(EnabledListener listener) {
			this.enabledListeners.add(listener);
		}

	}

}
