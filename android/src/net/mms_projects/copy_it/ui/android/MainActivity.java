package net.mms_projects.copy_it.ui.android;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import net.mms_projects.copy_it.AndroidClipboardUtils;
import net.mms_projects.copy_it.ClipboardUtils;
import net.mms_projects.copy_it.FileStreamBuilder;
import net.mms_projects.copy_it.R;
import net.mms_projects.copy_it.activities.HistoryActivity;
import net.mms_projects.copy_it.android.tasks.CheckUpdateTask;
import net.mms_projects.copy_it.android.tasks.CopyItTask;
import net.mms_projects.copy_it.android.tasks.PasteItTask;
import net.mms_projects.copy_it.android.tasks.SendToAppTask;
import net.mms_projects.copy_it.api.ServerApi;
import net.mms_projects.copy_it.app.CopyItAndroid;
import net.mms_projects.copy_it.models.HistoryItem.Change;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;

public class MainActivity extends SherlockFragmentActivity {

	private CopyItAndroid app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);

		if (!preferences.contains("device.id")) {
			Intent intent = new Intent(this, WelcomeActivity.class);
			startActivity(intent);
			finish();
			return;
		}

		this.app = new CopyItAndroid();
		this.app.run(this);

		setTheme(R.style.AppTheme);
		setContentView(R.layout.activity_main);

		// Get intent, action and MIME type
		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();

		if (Intent.ACTION_SEND.equals(action) && type != null) {
			if ("text/plain".equals(type)) {
				handleSendText(intent); // Handle text being sent
			}
		}

		ServerApi api = new ServerApi();
		api.apiUrl = this.getResources().getString(R.string.jenkins_joburl);

		if (CopyItAndroid.getBuildNumber(this) != 0) {
			CheckUpdateTask task = new CheckUpdateTask(this, api);
			task.execute();
		} else {
			Log.i("update-check",
					"Build number is 0. Not running Jenkins build. Ignoring update check!");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		ClipboardUtils clipboard = new AndroidClipboardUtils(MainActivity.this);

		TextView clipboardContent = (TextView) this
				.findViewById(R.id.clipboard_content);
		clipboardContent.setText(clipboard.getText());
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

	private void handleSendText(Intent intent) {
		String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
		if (sharedText != null) {
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(this);

			if (preferences.getString("device.id", null) == null) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(
						this.getResources().getString(
								R.string.text_login_question))
						.setPositiveButton(
								this.getResources().getString(
										R.string.dialog_button_yes),
								new MainActivity.LoginYesNoDialog())
						.setNegativeButton(
								this.getResources().getString(
										R.string.dialog_button_no),
								new MainActivity.LoginYesNoDialog()).show();
				return;
			}

			ServerApi api = new ServerApi();
			api.deviceId = UUID.fromString(preferences.getString("device.id",
					null));
			api.devicePassword = preferences.getString("device.password", null);
			api.apiUrl = preferences.getString("server.baseurl", this
					.getResources().getString(R.string.default_baseurl));

			CopyItTask task = new HandleShareTask(this, api);
			task.execute(sharedText);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.action_settings:
			intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_feedback:
			intent = new Intent(this, DebugActivity.class);
			intent.setAction(Intent.ACTION_SEND);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void copyIt(View view) {
		EasyTracker.getTracker().sendEvent("ui_action", "button_press",
				"push_button", null);

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);

		if (preferences.getString("device.id", null) == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					this.getResources().getString(R.string.text_login_question))
					.setPositiveButton(
							this.getResources().getString(
									R.string.dialog_button_yes),
							new MainActivity.LoginYesNoDialog())
					.setNegativeButton(
							this.getResources().getString(
									R.string.dialog_button_no),
							new MainActivity.LoginYesNoDialog()).show();
			return;
		}

		ServerApi api = new ServerApi();
		api.deviceId = UUID
				.fromString(preferences.getString("device.id", null));
		api.devicePassword = preferences.getString("device.password", null);
		api.apiUrl = preferences.getString("server.baseurl", this
				.getResources().getString(R.string.default_baseurl));

		ClipboardUtils clipboard = new AndroidClipboardUtils(MainActivity.this);

		CopyItTask task = new CopyItTask(this, api);
		task.setUseProgressDialog(true);
		task.execute(clipboard.getText());
	}

	public void pasteIt(View view) {
		EasyTracker.getTracker().sendEvent("ui_action", "button_press",
				"pull_button", null);

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		if (preferences.getString("device.id", null) == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					this.getResources().getString(R.string.text_login_question))
					.setPositiveButton(
							this.getResources().getString(
									R.string.dialog_button_yes),
							new MainActivity.LoginYesNoDialog())
					.setNegativeButton(
							this.getResources().getString(
									R.string.dialog_button_no),
							new MainActivity.LoginYesNoDialog()).show();
			return;
		}

		ServerApi api = new ServerApi();
		api.deviceId = UUID
				.fromString(preferences.getString("device.id", null));
		api.devicePassword = preferences.getString("device.password", null);
		api.apiUrl = preferences.getString("server.baseurl", this
				.getResources().getString(R.string.default_baseurl));

		PasteItTask task = new PullContentTask(this, api);
		task.setUseProgressDialog(true);
		task.execute();
	}

	public void sendToApp(View view) {
		EasyTracker.getTracker().sendEvent("ui_action", "button_press",
				"send_to_app_button", null);

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		if (preferences.getString("device.id", null) == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					this.getResources().getString(R.string.text_login_question))
					.setPositiveButton(
							this.getResources().getString(
									R.string.dialog_button_yes),
							new MainActivity.LoginYesNoDialog())
					.setNegativeButton(
							this.getResources().getString(
									R.string.dialog_button_no),
							new MainActivity.LoginYesNoDialog()).show();
			return;
		}

		ServerApi api = new ServerApi();
		api.deviceId = UUID
				.fromString(preferences.getString("device.id", null));
		api.devicePassword = preferences.getString("device.password", null);
		api.apiUrl = preferences.getString("server.baseurl", this
				.getResources().getString(R.string.default_baseurl));

		SendToAppTask task = new SendToAppTask(this, api);
		task.execute();
	}

	public void gotoHistory(View view) {
		Intent intent = new Intent(this, HistoryActivity.class);
		startActivity(intent);
	}

	public void gotoSettings(View view) {
		EasyTracker.getTracker().sendEvent("ui_action", "button_press",
				"settings_button", null);

		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	public void gotoAbout(View view) {
		EasyTracker.getTracker().sendEvent("ui_action", "button_press",
				"about_button", null);

		Intent intent = new Intent(this, AboutActivity.class);
		startActivity(intent);
	}

	class StreamBuilder extends FileStreamBuilder {

		private Activity activity;

		public StreamBuilder(Activity activity) {
			this.activity = activity;
		}

		@Override
		public FileInputStream getInputStream() throws IOException {
			return this.activity.openFileInput("settings");
		}

		@Override
		public FileOutputStream getOutputStream() throws IOException {
			return this.activity.openFileOutput("settings",
					Context.MODE_PRIVATE);
		}

	}

	class LoginYesNoDialog implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				Intent intent = new Intent(MainActivity.this,
						LoginActivity.class);
				MainActivity.this.startActivity(intent);
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				MainActivity.this.finish();
				break;
			}
		}
	}

	private class HandleShareTask extends CopyItTask {
		public HandleShareTask(Context context, ServerApi api) {
			super(context, api);

			this.historyChangeType = Change.RECEIVED_FROM_APP;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			MainActivity.this.finish();
		}
	}

	private class PullContentTask extends PasteItTask {

		public PullContentTask(Context context, ServerApi api) {
			super(context, api);
		}

		@Override
		protected void onPostExecute(String content) {
			super.onPostExecute(content);

			TextView clipboardContent = (TextView) MainActivity.this
					.findViewById(R.id.clipboard_content);
			clipboardContent.setText(content);
		}
	}

}