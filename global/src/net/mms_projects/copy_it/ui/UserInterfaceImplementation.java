package net.mms_projects.copy_it.ui;

public interface UserInterfaceImplementation {

	public void setSettingsUserInterface(SettingsUserInterface settingsUserInterface);

	public SettingsUserInterface getSettingsUserInterface();

	public static interface UserInterface {

		public void show();

		public void hide();

	}

	public static interface SettingsUserInterface extends UserInterface {

	}

}
