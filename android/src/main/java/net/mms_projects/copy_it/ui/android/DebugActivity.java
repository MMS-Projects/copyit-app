package net.mms_projects.copy_it.ui.android;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;

import net.mms_projects.copy_it.R;
import net.mms_projects.copy_it.activities.debugging.*;
import net.mms_projects.copy_it.app.CopyItAndroid;
import net.mms_projects.utils.InlineSwitch;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@SuppressLint("InlinedApi")
public class DebugActivity extends SherlockActivity {

	public final static String ACTION_SEND = "send";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_debug);

		Map<String, String> info = new LinkedHashMap<String, String>();
        if (!preferences.getBoolean("api.use_dev_server", false)) {
            info.put(getString(R.string.debug_label_server_baseurl),
                    preferences.getString("server.baseurl", this.getResources()
                            .getString(R.string.default_baseurl)));
        } else {
            info.put(getString(R.string.debug_label_server_baseurl),
                    this.getResources()
                            .getString(R.string.copyit_server));
        }
		info.put(getString(R.string.debug_label_jenkins_baseurl), this
				.getResources().getString(R.string.jenkins_joburl));
		info.put(getString(R.string.debug_label_device_id), this.getResources()
				.getString(R.string.jenkins_joburl));
		try {
			UUID.fromString(preferences.getString("device.id", null));
			info.put(getString(R.string.debug_label_device_id),
					getString(R.string.debug_available));
		} catch (Exception e) {
			info.put(getString(R.string.debug_label_device_id),
					getString(R.string.debug_not_available));
		}
		try {
			UUID.fromString(preferences.getString("device.password", null));
			info.put(getString(R.string.debug_label_device_password),
					getString(R.string.debug_available));
		} catch (Exception e) {
			info.put(getString(R.string.debug_label_device_password),
					getString(R.string.debug_not_available));
		}
		info.put(this.getString(R.string.debug_label_build_number),
				Integer.toString(CopyItAndroid.getBuildNumber(this)));
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

		InlineSwitch<Integer, String> switcher = new InlineSwitch<Integer, String>();
		switcher.addClause(DisplayMetrics.DENSITY_LOW, "ldpi");
		switcher.addClause(DisplayMetrics.DENSITY_MEDIUM, "mdpi");
		switcher.addClause(DisplayMetrics.DENSITY_HIGH, "hdpi");
		if (android.os.Build.VERSION.SDK_INT >= 9) {
			switcher.addClause(DisplayMetrics.DENSITY_XHIGH, "xhdpi");
		}
		if (android.os.Build.VERSION.SDK_INT >= 16) {
			switcher.addClause(DisplayMetrics.DENSITY_XXHIGH, "xxhdpi");
		}
		switcher.setDefault(this.getResources().getString(
				R.string.debug_unknown));

		info.put(this.getString(R.string.debug_label_screen_density),
				switcher.runSwitch(Integer.valueOf(displayMetrics.densityDpi)));
		
		TableLayout table = (TableLayout) findViewById(R.id.debug_table);
		for (String key : info.keySet()) {
			TextView label = new TextView(this);
			label.setText(key);
			TextView value = new TextView(this);
			value.setText(info.get(key));
			
			TableRow row = new TableRow(this);
			row.addView(label);
			row.addView(value);
			
			table.addView(row);
		}

		if ((getIntent().getAction() != null)
				&& (getIntent().getAction().equals(Intent.ACTION_SEND))) {
			String text = "";
			for (String key : info.keySet()) {
				text += key + " - " + info.get(key) + "\n";
			}
			this.sendEmail(text);
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.debug, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.action_open_activity_test:
			intent = new Intent(this, TestActivity.class);
			this.startActivity(intent);
			return true;
        case R.id.action_open_activity_gcm:
            intent = new Intent(this, GcmActivity.class);
            this.startActivity(intent);
            return true;
            case R.id.action_open_settings:
                intent = new Intent(this, net.mms_projects.copy_it.activities.debugging.SettingsActivity.class);
                this.startActivity(intent);
                return true;
		default:
			return super.onOptionsItemSelected(item);
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

	protected void sendEmail(String text) {
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setType("message/rfc822");
		emailIntent.putExtra(Intent.EXTRA_EMAIL,
				new String[] { "bitbucket@mms-projects.net" });
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Copy It debug");
		emailIntent.putExtra(Intent.EXTRA_TEXT, text);
		try {
			startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(DebugActivity.this,
					"There are no email clients installed.", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public static class Launch extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent launchingIntent) {
			Intent intent = new Intent(context, DebugActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}

	}

}
