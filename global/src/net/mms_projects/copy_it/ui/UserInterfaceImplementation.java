package net.mms_projects.copy_it.ui;

import java.util.Date;


public interface UserInterfaceImplementation {

	public void open();

	public void close();

	public void setSettingsUserInterface(
			SettingsUserInterface settingsUserInterface);

	public SettingsUserInterface getSettingsUserInterface();

	public void setAboutUserInterface(AboutUserInterface userInterface);

	public AboutUserInterface getAboutUserInterface();

	public void setQueueUserInterface(QueueUserInterface userInterface);

	public QueueUserInterface getQueueUserInterface();

	public static interface UserInterface {

		public void show();

		public void hide();

	}

	public static interface SettingsUserInterface extends UserInterface {

	}

	public static interface AboutUserInterface extends UserInterface {

	}

	public static interface QueueUserInterface extends UserInterface {

		public void addContent(String content, Date date);
		
	}

}
