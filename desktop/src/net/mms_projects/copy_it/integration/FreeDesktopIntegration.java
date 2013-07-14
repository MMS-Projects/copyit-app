package net.mms_projects.copy_it.integration;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import net.mms_projects.copy_it.EnvironmentIntegration;
import net.mms_projects.copy_it.Messages;
import net.mms_projects.copy_it.PathBuilder;
import net.mms_projects.copy_it.app.CopyItDesktop;
import net.mms_projects.copy_it.integration.notifications.FreedesktopNotificationManager;

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

	@Override
	public void standaloneSetup() {
		try {
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

	private List<String> generateDesktopContents() {
		List<String> content = new ArrayList<String>();
		content.add("[Desktop Entry]");
		content.add("Version=1.0");
		content.add("Type=Application");
		content.add("Name=" + Messages.getString("app_name"));
		content.add("Icon=copyit");
		content.add("Exec=" + this.generateJavaCommandLine() + " %f");
		content.add("Terminal=false");
		content.add("StartupNotify=true");
		return content;
	}

	/**
	 * This method generates a java command line that can be used to run the
	 * application the way it was started in this runtime
	 * 
	 * @return A string with the Java command line
	 */
	private String generateJavaCommandLine() {
		List<String> commandline = new ArrayList<String>();

		commandline.add("java");
		/*
		 * This adds some the command line arguments passed to the current JVM
		 */
		commandline.addAll(ManagementFactory.getRuntimeMXBean()
				.getInputArguments());
		/*
		 * This adds the current class path
		 */
		commandline.add("-classpath \"" + getClasspath() + "\"");
		/*
		 * This adds the main class to the command line
		 */
		commandline.add(CopyItDesktop.class.getName());

		/*
		 * Converts the array of command line arguments to a long string with
		 * all the command line arguments. Delimited by a string.
		 */
		String rawCommandline = "";
		for (String part : commandline) {
			rawCommandline += part + " ";
		}

		return rawCommandline;
	}

	/**
	 * This methods returns the class path used by the current runtime
	 * 
	 * @return Returns the class path used by the current runtime
	 */
	private String getClasspath() {
		String classpath = "";
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();

		URL[] urls = ((URLClassLoader) classLoader).getURLs();

		for (URL url : urls) {
			classpath += url.getFile();
			classpath += System.getProperty("path.separator");
		}
		return classpath;
	}

	/**
	 * The FreeDesktop auto start manager that writes follows the FreeDesktop
	 * specifications
	 */
	class FreeDesktopAutostartManager implements
			EnvironmentIntegration.AutostartManager {

		/**
		 * This creates a .desktop file in the auto start directory to make it
		 * auto start
		 */
		@Override
		public void setupAutostartup() {
			/*
			 * Write a .desktop file to the auto start directory
			 */
			FreeDesktopIntegration.this.writeDesktopFile(PathBuilder
					.getAutostartDirectory());
		}

	}

}
