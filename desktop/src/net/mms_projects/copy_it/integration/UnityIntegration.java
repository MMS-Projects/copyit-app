package net.mms_projects.copy_it.integration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import net.mms_projects.copy_it.Activatable;
import net.mms_projects.copy_it.ClipboardManager;
import net.mms_projects.copy_it.DesktopIntegration;
import net.mms_projects.copy_it.EnvironmentIntegration;
import net.mms_projects.copy_it.FunctionalityManager;
import net.mms_projects.copy_it.PathBuilder;
import net.mms_projects.copy_it.SyncManager;
import net.mms_projects.copy_it.app.CopyItDesktop;
import net.mms_projects.copy_it.integration.notifications.FreedesktopNotificationManager;
import net.mms_projects.copy_it.listeners.EnabledListener;
import net.mms_projects.copy_it.ui.UserInterfaceImplementation;

import org.apache.commons.io.FileUtils;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.DBusSigHandler;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.exceptions.DBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnityIntegration extends EnvironmentIntegration implements
		DBusSigHandler {

	protected SyncManager syncManager;
	protected ClipboardManager clipboardManager;
	protected DBusConnection dbusConnection;

	private DesktopIntegration integration;

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private FunctionalityManager<Activatable> functionality;
	private UserInterfaceImplementation uiImplementation;

	private EnabledListener pollingListener = new EnabledListener() {

		@Override
		public void onEnabled() {
			integration.set_enabled(true);
		}

		@Override
		public void onDisabled() {
			integration.set_enabled(false);
		}
	};

	public UnityIntegration(DBusConnection dbusConnection,
			FunctionalityManager<Activatable> functionality,
			SyncManager syncManager, ClipboardManager clipboardManager) {
		try {
			this.setNotificationManager(new FreedesktopNotificationManager(
					dbusConnection));
		} catch (DBusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.functionality = functionality;
		this.syncManager = syncManager;
		this.clipboardManager = clipboardManager;
		this.dbusConnection = dbusConnection;

		this.functionality.addEnabledListener("polling", this.pollingListener);

		/*
		 * Add FreeDesktop integrations like notifications and writing .desktop
		 * files and the appropriate icons for the .desktop files
		 */
		this.addIntegration(new FreeDesktopIntegration(this, dbusConnection));
	}

	@Override
	public void standaloneSetup() {
		try {
			dbusConnection.addSigHandler(DesktopIntegration.ready.class, this);
			dbusConnection.addSigHandler(DesktopIntegration.action_pull.class,
					this);
			dbusConnection.addSigHandler(DesktopIntegration.action_push.class,
					this);
			dbusConnection.addSigHandler(
					DesktopIntegration.action_open_preferences.class, this);
			dbusConnection.addSigHandler(
					DesktopIntegration.action_open_about.class, this);
			dbusConnection.addSigHandler(DesktopIntegration.action_quit.class,
					this);
			dbusConnection.addSigHandler(
					DesktopIntegration.action_enable_sync.class, this);
			dbusConnection.addSigHandler(
					DesktopIntegration.action_disable_sync.class, this);
		} catch (DBusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		URL inputUrl = getClass().getResource("/images/icon-16-mono.png");
		File dest = new File(PathBuilder.getCacheDirectory(), "tray_icon.png");
		try {
			FileUtils.copyURLToFile(inputUrl, dest);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		File script = CopyItDesktop
				.exportResource("scripts/desktop-integration.py");
		if (script == null) {
			script = new File("scripts/desktop-integration.py");
		}

		String[] command = new String[2];
		command[0] = "python";
		command[1] = script.getAbsolutePath();
		try {
			final Process process = Runtime.getRuntime().exec(command);
			log.info("Started desktop integration script");
			log.debug("Integration script path: " + script.getAbsolutePath());

			Thread ouputReadingthread = new ScriptOutputStreamReader(
					process.getInputStream());
			ouputReadingthread.setDaemon(true);
			ouputReadingthread.start();
			Thread errorReadingthread = new ScriptErrorStreamReader(
					process.getErrorStream());
			errorReadingthread.setDaemon(true);
			errorReadingthread.start();

			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					log.debug("Stopping desktop integration script");
					process.destroy();
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void handle(DBusSignal signal) {
		if (signal instanceof DesktopIntegration.ready) {
			String icon = new File(PathBuilder.getCacheDirectory(),
					"tray_icon.png").getAbsolutePath();
			try {
				integration = this.dbusConnection.getRemoteObject(
						"net.mms_projects.copyit.DesktopIntegration", "/",
						DesktopIntegration.class);
				integration.setup(icon, icon);
				integration
						.set_enabled(this.functionality.isEnabled("polling"));
			} catch (DBusException e) {
				log.error("Could not connect to desktop integration script although it reported as ready. Exiting...");
				System.exit(1);
			}

		} else if (signal instanceof DesktopIntegration.action_push) {
			clipboardManager.requestGet();
		} else if (signal instanceof DesktopIntegration.action_pull) {
			syncManager.doPull();
		} else if (signal instanceof DesktopIntegration.action_open_preferences) {
			this.getUserInterfaceImplementation().getSettingsUserInterface()
					.open();
		} else if (signal instanceof DesktopIntegration.action_open_about) {
			this.getUserInterfaceImplementation().getAboutUserInterface()
					.open();
		} else if (signal instanceof DesktopIntegration.action_quit) {
			this.getUserInterfaceImplementation().close();
		} else if (signal instanceof DesktopIntegration.action_enable_sync) {
			this.functionality.setEnabled("polling", true);
			this.integration.set_enabled(this.functionality
					.isEnabled("polling"));
		} else if (signal instanceof DesktopIntegration.action_disable_sync) {
			this.functionality.setEnabled("polling", false);
			this.integration.set_enabled(this.functionality
					.isEnabled("polling"));
		}
	}

	private class ScriptOutputStreamReader extends ScriptOutputReadingThread {

		public ScriptOutputStreamReader(InputStream stream) {
			super(stream);
		}

		@Override
		protected void log(String output) {
			log.debug("Integration script returned: {}", output);
		}

	}

	private class ScriptErrorStreamReader extends ScriptOutputReadingThread {

		public ScriptErrorStreamReader(InputStream stream) {
			super(stream);
		}

		@Override
		protected void log(String output) {
			log.warn("Integration script returned: {}", output);
		}

	}

	abstract private class ScriptOutputReadingThread extends Thread {

		protected final Logger log = LoggerFactory.getLogger(this.getClass());

		private InputStream stream;

		public ScriptOutputReadingThread(InputStream stream) {
			super();

			this.stream = stream;
		}

		@Override
		public void run() {
			super.run();

			BufferedReader inputStream = new BufferedReader(
					new InputStreamReader(this.stream));
			String line = null;
			try {
				while ((line = inputStream.readLine()) != null) {
					this.log(line);
				}
			} catch (IOException exception) {
				log.warn("Couldn't listen for integration script output",
						exception);
			}
		}

		abstract protected void log(String output);
	}

}
