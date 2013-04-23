package net.mms_projects.copyit;

import java.io.IOException;

import net.mms_projects.utils.OSValidator;

public class OpenBrowser {

	public static void openUrl(String url) {
		if (OSValidator.isUnix()) {
			OpenBrowser.openUrlLinux(url);
		} else if (OSValidator.isWindows()) {
			OpenBrowser.openUrlWindows(url);
		}
	}

	protected static void openUrlWindows(String url) {
		String[] command = new String[2];
		command[0] = "start";
		command[1] = url;
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected static void openUrlLinux(String url) {
		String[] command = new String[2];
		command[0] = "xdg-open";
		command[1] = url;
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
