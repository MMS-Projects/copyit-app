package net.mms_projects.copyit.app;

import net.mms_projects.copyit.Settings;
import android.app.Application;

public class AndroidApplication extends Application {

	private Settings settings;
	private static AndroidApplication instance;

	public Settings getSettings() {
		return this.settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;

	}

	public static AndroidApplication getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		AndroidApplication.instance = this;
	}

}