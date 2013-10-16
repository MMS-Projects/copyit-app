package net.mms_projects.copy_it.activities;

import net.mms_projects.copy_it.R;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.analytics.tracking.android.EasyTracker;

public class LoggedOutActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTheme(R.style.AppTheme);
		setContentView(R.layout.activity_logged_out);

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);

		try {
			Editor preferenceEditor = preferences.edit();
			preferenceEditor.remove("device.id");
			preferenceEditor.remove("device.password");
			preferenceEditor.commit();
		} catch (Exception event) {
			// TODO Auto-generated catch block
			event.printStackTrace();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		EasyTracker.getInstance().activityStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();

		EasyTracker.getInstance().activityStop(this);
	}

}
