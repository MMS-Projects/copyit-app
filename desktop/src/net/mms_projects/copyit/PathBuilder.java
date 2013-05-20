package net.mms_projects.copyit;

import java.io.File;

import net.mms_projects.utils.OSValidator;

public class PathBuilder {

	public static File getConfigDirectory() {
		File directory = new File("." + File.separator);
		if (OSValidator.isWindows()) {
			directory = new File(System.getenv("APPDATA") + File.separator
					+ "copyit");
			if (!directory.exists()) {
				directory.mkdir();
			}
		} else if (OSValidator.isUnix()) {
			directory = new File(System.getenv("HOME") + File.separator
					+ ".config" + File.separator + "copyit");
			if (!directory.exists()) {
				directory.mkdir();
			}
		}

		return directory;
	}
	
	public static File getCacheDirectory() {
		File directory = new File("." + File.separator);
		if (OSValidator.isUnix()) {
			directory = new File(System.getenv("HOME") + File.separator
					+ ".cache" + File.separator + "copyit");
			if (!directory.exists()) {
				directory.mkdir();
			}
		}

		return directory;
	}

}
