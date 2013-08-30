package net.mms_projects.copy_it;

import java.io.File;

import net.mms_projects.copy_it.linux.XDG;
import net.mms_projects.utils.OSValidator;

public class PathBuilder {

	public static File getConfigDirectory() {
		File directory = new File("." + File.separator);
		if (OSValidator.isWindows()) {
			directory = new File(System.getenv("APPDATA") + File.separator
					+ "copyit");
		} else if (OSValidator.isUnix()) {
			directory = new File(XDG.getConfigHome() + File.separator
					+ "copyit");
		}

		if (!directory.exists()) {
			directory.mkdir();
		}

		return directory;
	}

	public static File getCacheDirectory() {
		File directory = new File("." + File.separator);
		if (OSValidator.isUnix()) {
			directory = new File(XDG.getCacheHome() + File.separator + "copyit");
		}

		if (!directory.exists()) {
			directory.mkdir();
		}

		return directory;
	}

	public static File getIconDirectory(int size) {
		File directory = new File("." + File.separator);
		if (OSValidator.isUnix()) {
			directory = new File(System.getenv("HOME")
					+ "/.local/share/icons/hicolor/" + size + "x" + size
					+ "/apps");
		}

		if (!directory.exists()) {
			directory.mkdir();
		}

		return directory;
	}

	public static File getLauncherShortcutDirectory() {
		File directory = new File("." + File.separator);
		if (OSValidator.isUnix()) {
			directory = new File(System.getenv("HOME")
					+ "/.local/share/applications");
		}

		if (!directory.exists()) {
			directory.mkdir();
		}

		return directory;
	}

}
