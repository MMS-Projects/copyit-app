package net.mms_projects.copyit.app;


import java.io.FileInputStream;
import java.io.FileOutputStream;

import net.mms_projects.copyit.Settings;
import net.mms_projects.copyit.ui.AbstractUi;
import net.mms_projects.copyit.ui.AndroidGui;
import net.mms_projects.copyit.ui.android.MainActivity;

public class CopyItAndroid extends CopyIt {

	protected Settings settings;
	
	public Settings getSettings() {
		return this.settings;
	}
	
	public void run(MainActivity activity, FileInputStream inputStream, FileOutputStream outputStream) {
		this.settings = new Settings();
		this.settings.setFileStream(inputStream, outputStream);
		this.settings.loadProperties();
		
		AndroidApplication.getInstance().setSettings(this.settings);
		
		AbstractUi ui = new AndroidGui(this.settings, activity);
		ui.open();
		
		this.settings.saveProperties();
	}

}
