package net.mms_projects.copyit.app;

import java.util.Locale;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class AndroidApplication extends Application {

	private static AndroidApplication instance;

	public static AndroidApplication getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		updateLanguage(this);

		super.onCreate();

		AndroidApplication.instance = this;
	}

	public static void updateLanguage(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		String lang = prefs.getString("locale_override", "");
		updateLanguage(context, lang);
	}

	public static void updateLanguage(Context context, String lang) {
		Configuration cfg = new Configuration();
		if (!TextUtils.isEmpty(lang)) {
			cfg.locale = new Locale(lang);
		} else {
			cfg.locale = Locale.getDefault();
		}
		context.getResources().updateConfiguration(cfg, null);
	}

}
