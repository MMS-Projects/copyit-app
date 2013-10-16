package net.mms_projects.copy_it.linux;

import java.util.HashMap;
import java.util.Map;

public class XDG {

	public static String getCurrentDesktop() {
		return getString("XDG_CURRENT_DESKTOP");
	}

	public static String getDataHome() {
		return getString("XDG_DATA_HOME");
	}

	public static String getConfigHome() {
		return getString("XDG_CONFIG_HOME");
	}

	public static String getCacheHome() {
		return getString("XDG_CACHE_HOME");
	}

	private static String getString(String variable) {
		String defaultValue = getDefault(variable);
		String value = (System.getenv(variable) != null) ? System
				.getenv(variable) : defaultValue;
		return value;
	}

	private static String getDefault(String variable) {
		Map<String, String> defaults = new HashMap<String, String>();
		defaults.put("XDG_DATA_HOME", parsePath("$HOME/.local/share"));
		defaults.put("XDG_CONFIG_HOME", parsePath("$HOME/.config"));
		defaults.put("XDG_CACHE_HOME", parsePath("$HOME/.cache"));
		return defaults.get(variable);
	}

	private static String parsePath(String path) {
		String home = System.getenv("HOME");
		path = path.replace("$HOME", home);
		return path;
	}

}
