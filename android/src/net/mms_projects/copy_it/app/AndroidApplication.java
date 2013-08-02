package net.mms_projects.copy_it.app;

import android.app.Application;
import net.mms_projects.copy_it.R;

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
