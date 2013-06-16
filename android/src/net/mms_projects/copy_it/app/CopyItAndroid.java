package net.mms_projects.copy_it.app;

import net.mms_projects.copy_it.R;
import net.mms_projects.copy_it.Settings;
import net.mms_projects.copy_it.app.CopyIt;
import net.mms_projects.copy_it.ui.AbstractUi;
import net.mms_projects.copy_it.ui.AndroidGui;
import net.mms_projects.copy_it.ui.android.MainActivity;
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
