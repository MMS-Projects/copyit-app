package net.mms_projects.copyit.app;

import net.mms_projects.copyit.Settings;
import net.mms_projects.copyit.api.ServerApi;
import android.app.Application;

public class AndroidApplication extends Application {

	private Settings settings;
	private ServerApi api;
	private static AndroidApplication instance;

	public Settings getSettings() {
		return this.settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}
	
	public ServerApi getApi() {
		return this.api;
	}
	
	public void setApi(ServerApi api) {
		this.api = api;
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
