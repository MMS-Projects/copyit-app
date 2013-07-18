package net.mms_projects.copy_it.app;

import net.mms_projects.copy_it.R;
import net.mms_projects.copy_it.Config;
import net.mms_projects.copy_it.app.CopyIt;
import net.mms_projects.copy_it.ui.AndroidGui;
import net.mms_projects.copy_it.ui.android.MainActivity;
import net.mms_projects.copy_it.ui_old.AbstractUi;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class CopyItAndroid extends CopyIt {

	protected Config settings;

	public void run(MainActivity activity) {
		this.settings = new Config();

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
