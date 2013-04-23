package net.mms_projects.copyit.app;

import android.app.Application;

public class AndroidApplication extends Application {

	private static AndroidApplication instance;

	public static AndroidApplication getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		AndroidApplication.instance = this;
	}

}
