package net.mms_projects.copyit.app;

import net.mms_projects.copyit.Settings;
import net.mms_projects.copyit.ui.AbstractUi;
import net.mms_projects.copyit.ui.AndroidGui;
import net.mms_projects.copyit.ui.android.MainActivity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class CopyItAndroid extends CopyIt {

	protected Settings settings;

	public void run(MainActivity activity) {
		this.settings = new Settings();

		AbstractUi ui = new AndroidGui(this.settings, activity);
		ui.open();
	}
	
	public static int getBuildNumber(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
