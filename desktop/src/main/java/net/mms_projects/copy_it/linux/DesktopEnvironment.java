package net.mms_projects.copy_it.linux;

/**
 * This class allows easy checking on which desktop environment the app is
 * running
 */
public class DesktopEnvironment {

	public enum Environment {
		Unity, Gnome, Kde
	}

	public static Environment getDesktopEnvironment() {
		if (isUnity()) {
			return Environment.Unity;
		} else if (isGnome()) {
			return Environment.Gnome;
		}
		return null;
	}

	public static boolean isUnity() {
		String desktopEnvironment = XDG.getCurrentDesktop();
		if ((desktopEnvironment != null)
				&& (desktopEnvironment.equalsIgnoreCase("Unity"))) {
			return true;
		}
		return false;
	}

	public static boolean isGnome() {
		String desktopEnvironment = XDG.getCurrentDesktop();
		if ((desktopEnvironment != null)
				&& (desktopEnvironment.equalsIgnoreCase("GNOME"))) {
			return true;
		}
		return false;
	}

	public static boolean isKde() {
		String desktopEnvironment = XDG.getCurrentDesktop();
		if ((desktopEnvironment != null)
				&& (desktopEnvironment.equalsIgnoreCase("GNOME"))) {
			return true;
		}
		return false;
	}

}
