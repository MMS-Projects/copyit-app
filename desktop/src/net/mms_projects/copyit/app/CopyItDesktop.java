package net.mms_projects.copyit.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import net.mms_projects.copyit.FileStreamBuilder;
import net.mms_projects.copyit.PathBuilder;
import net.mms_projects.copyit.Settings;
import net.mms_projects.copyit.ui.AbstractUi;
import net.mms_projects.copyit.ui.SwtGui;
import net.mms_projects.utils.OSValidator;

import org.freedesktop.Notifications;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.UInt32;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;

public class CopyItDesktop extends CopyIt {

	protected Settings settings;
	protected File lockFile;
	static public DBusConnection dbusConnection;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CopyItDesktop app = new CopyItDesktop();
		app.run();
	}

	public static String getVersion() {
		String version = "";
		if (CopyItDesktop.class.getPackage().getSpecificationVersion() != null) {
			version += CopyItDesktop.class.getPackage()
					.getSpecificationVersion();
		} else {
			version += "0.0.1";
		}

		if (CopyItDesktop.getBuildNumber() != 0) {
			version += "-" + CopyItDesktop.getBuildNumber();
		}
		return version;
	}

	public static int getBuildNumber() {
		try {
			return Integer.parseInt(CopyItDesktop.class.getPackage()
					.getImplementationVersion());
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public void run() {
		this.settings = new Settings();
		try {
			this.settings.setFileStreamBuilder(new StreamBuilder());
		} catch (IOException e) {
			e.printStackTrace();

			System.exit(1);
		}
		this.settings.loadProperties();

		if (OSValidator.isUnix()) {
			try {
				CopyItDesktop.dbusConnection = DBusConnection
						.getConnection(DBusConnection.SESSION);
			} catch (DBusException e1) {
				// TODO Auto-generated catch block
				System.out
						.println("Ahh could not connect to D-Bus. All kinds of explosions n'stuff. Fix it!");
				e1.printStackTrace();
				System.exit(1);
			}
		}

		this.lockFile = new File(PathBuilder.getConfigDirectory(), ".lock");
		if (this.lockFile.exists()) {
			String message = "An instance is already running. "
					+ "If not please remove the following lock file: "
					+ this.lockFile.getAbsolutePath();

			if (OSValidator.isUnix()) {
				try {
					Notifications notify = CopyItDesktop.dbusConnection
							.getRemoteObject("org.freedesktop.Notifications",
									"/org/freedesktop/Notifications",
									Notifications.class);
					Map<String, Variant<Byte>> hints = new HashMap<String, Variant<Byte>>();
					hints.put("urgency", new Variant<Byte>((byte) 2));
					notify.Notify("CopyIt", new UInt32(0), "", "CopyIt",
							message, new LinkedList<String>(), hints, -1);
				} catch (DBusException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			System.out.println(message);
			System.exit(0);
		} else {
			try {
				this.lockFile.createNewFile();

				this.lockFile.deleteOnExit();
			} catch (IOException e) {
				e.printStackTrace();

				System.exit(1);
			}
		}

		AbstractUi ui = new SwtGui(this.settings);
		ui.open();

		this.settings.saveProperties();
	}

	class StreamBuilder extends FileStreamBuilder {

		private File settingsFile;

		public StreamBuilder() throws IOException {
			this.settingsFile = new File(PathBuilder.getConfigDirectory(),
					"options.properties");
			if (!this.settingsFile.exists()) {
				System.out.println("No settings file. Creating it.");
				this.settingsFile.createNewFile();
			}
		}

		@Override
		public FileInputStream getInputStream() throws IOException {
			return new FileInputStream(this.settingsFile);
		}

		@Override
		public FileOutputStream getOutputStream() throws IOException {
			return new FileOutputStream(this.settingsFile);
		}

	}

}
