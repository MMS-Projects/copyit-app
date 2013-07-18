package net.mms_projects.copy_it.ui;

public interface UserInterfaceImplementation {

	public void setSettingsUserInterface(
			SettingsUserInterface settingsUserInterface);

	public SettingsUserInterface getSettingsUserInterface();

	public void setAboutUserInterface(AboutUserInterface userInterface);

	public AboutUserInterface getAboutUserInterface();

	public static interface UserInterface {

		public void show();

		public void hide();

	}

	public static interface SettingsUserInterface extends UserInterface {

	}

	public static interface AboutUserInterface extends UserInterface {

	}

}
