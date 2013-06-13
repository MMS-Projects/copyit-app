package net.mms_projects.copy_it.ui.swt;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import net.mms_projects.copy_it.ClipboardManager;
import net.mms_projects.copy_it.DesktopIntegration;
import net.mms_projects.copy_it.Messages;
import net.mms_projects.copy_it.PathBuilder;
import net.mms_projects.copy_it.Settings;
import net.mms_projects.copy_it.SettingsListener;
import net.mms_projects.copy_it.SyncManager;
import net.mms_projects.copy_it.app.CopyItDesktop;
import net.mms_projects.copy_it.ui.swt.forms.AboutDialog;
import net.mms_projects.copy_it.ui.swt.forms.PreferencesDialog;

import org.apache.commons.io.FileUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.freedesktop.Notifications;
import org.freedesktop.dbus.DBusSigHandler;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.UInt32;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrayEntryUnity extends TrayEntry implements DBusSigHandler,
		SettingsListener {

	private DesktopIntegration integration;
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public TrayEntryUnity(Settings settings, Shell activityShell,
			SyncManager syncManager, ClipboardManager clipboardManager) {
		super(settings, activityShell, syncManager, clipboardManager);

		this.settings.addListener("sync.polling.enabled", this);

		try {
			CopyItDesktop.dbusConnection.addSigHandler(
					DesktopIntegration.ready.class, this);
			CopyItDesktop.dbusConnection.addSigHandler(
					DesktopIntegration.action_pull.class, this);
			CopyItDesktop.dbusConnection.addSigHandler(
					DesktopIntegration.action_push.class, this);
			CopyItDesktop.dbusConnection.addSigHandler(
					DesktopIntegration.action_open_preferences.class, this);
			CopyItDesktop.dbusConnection.addSigHandler(
					DesktopIntegration.action_open_about.class, this);
			CopyItDesktop.dbusConnection.addSigHandler(
					DesktopIntegration.action_quit.class, this);
			CopyItDesktop.dbusConnection.addSigHandler(
					DesktopIntegration.action_enable_sync.class, this);
			CopyItDesktop.dbusConnection.addSigHandler(
					DesktopIntegration.action_disable_sync.class, this);
		} catch (DBusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		URL inputUrl = getClass().getResource(
				"/drawable-xxhdpi/app_icon_small_mono.png");
		File dest = new File(PathBuilder.getCacheDirectory(), "tray_icon.png");
		try {
			FileUtils.copyURLToFile(inputUrl, dest);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		File script = CopyItDesktop.exportResource("desktop-integration.py");
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
				integration = CopyItDesktop.dbusConnection.getRemoteObject(
						"net.mms_projects.copyit.DesktopIntegration", "/",
						DesktopIntegration.class);
				integration.setup(icon, icon);
				integration.set_enabled(this.settings
						.getBoolean("sync.polling.enabled"));
			} catch (DBusException e) {
				System.out
						.println("Could not connect to desktop integration script although it reported as ready. Exiting...");
				System.exit(1);
			}

		} else if (signal instanceof DesktopIntegration.action_push) {
			clipboardManager.getContent();
		} else if (signal instanceof DesktopIntegration.action_pull) {
			syncManager.doPull();
		} else if (signal instanceof DesktopIntegration.action_open_preferences) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					new PreferencesDialog(TrayEntryUnity.this.activityShell,
							TrayEntryUnity.this.settings).open();
				}
			});
		} else if (signal instanceof DesktopIntegration.action_open_about) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					new AboutDialog(TrayEntryUnity.this.activityShell, SWT.NONE)
							.open();
				}
			});
		} else if (signal instanceof DesktopIntegration.action_quit) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					TrayEntryUnity.this.activityShell.close();
					System.exit(0);
				}
			});
		} else if (signal instanceof DesktopIntegration.action_enable_sync) {
			this.settings.set("sync.polling.enabled", true);
			this.integration.set_enabled(true);
		} else if (signal instanceof DesktopIntegration.action_disable_sync) {
			this.settings.set("sync.polling.enabled", false);
			this.integration.set_enabled(false);
		}
	}

	@Override
	public void onChange(String key, String value) {
		if ("sync.polling.enabled".equals(key)) {
			this.integration.set_enabled(Boolean.parseBoolean(value));
		}
	}

	@Override
	public void onPushed(String content, Date date) {
		try {
			Notifications notify = CopyItDesktop.dbusConnection
					.getRemoteObject("org.freedesktop.Notifications",
							"/org/freedesktop/Notifications",
							Notifications.class);
			Map<String, Variant<Byte>> hints = new HashMap<String, Variant<Byte>>();
			hints.put("urgency", new Variant<Byte>((byte) 2));
			notify.Notify("CopyIt", new UInt32(0), "", "CopyIt",
					Messages.getString("text_content_pushed", content),
					new LinkedList<String>(), hints, -1);
		} catch (DBusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onPulled(final String content, Date date) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				clipboardManager.setContent(content);
			}
		});
		try {
			Notifications notify = CopyItDesktop.dbusConnection
					.getRemoteObject("org.freedesktop.Notifications",
							"/org/freedesktop/Notifications",
							Notifications.class);
			Map<String, Variant<Byte>> hints = new HashMap<String, Variant<Byte>>();
			hints.put("urgency", new Variant<Byte>((byte) 2));
			notify.Notify("CopyIt", new UInt32(0), "", "CopyIt",
					Messages.getString("text_content_pulled", content),
					new LinkedList<String>(), hints, -1);
		} catch (DBusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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