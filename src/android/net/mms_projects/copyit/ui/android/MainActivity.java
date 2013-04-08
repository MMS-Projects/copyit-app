package net.mms_projects.copyit.ui.android;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import net.mms_projects.copyit.FileStreamBuilder;
import net.mms_projects.copyit.R;
import net.mms_projects.copyit.api.ServerApi;
import net.mms_projects.copyit.api.endpoints.ClipboardContentEndpoint;
import net.mms_projects.copyit.app.CopyItAndroid;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	private CopyItAndroid app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.app = new CopyItAndroid();
		this.app.run(this, new StreamBuilder(this));

		setContentView(R.layout.activity_main);

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);

		if (!preferences.contains("device.id")) {
			Intent intent = new Intent(this, WelcomeActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void copyIt(View view) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);

		Map<String, ?> settings = preferences.getAll();
		for (String key : settings.keySet()) {
			System.out.println(key + ": " + settings.get(key));
		}

		if (preferences.getString("device.id", null) == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					"It looks like you're not logged in. Do you want to login in?")
					.setPositiveButton("Yes",
							new MainActivity.LoginYesNoDialog())
					.setNegativeButton("No",
							new MainActivity.LoginYesNoDialog()).show();
			return;
		}

		ServerApi api = new ServerApi();
		api.deviceId = UUID
				.fromString(preferences.getString("device.id", null));
		api.devicePassword = preferences.getString("device.password", null);
		api.apiUrl = preferences.getString("server.baseurl", this
				.getResources().getString(R.string.default_baseurl));

		try {
			String content = this.getClipboard();
			Toast.makeText(this, "Pushed the following content: " + content,
					Toast.LENGTH_LONG).show();
			new ClipboardContentEndpoint(api).update(content);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void pasteIt(View view) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		if (preferences.getString("device.id", null) == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					"It looks like you're not logged in. Do you want to login in?")
					.setPositiveButton("Yes",
							new MainActivity.LoginYesNoDialog())
					.setNegativeButton("No",
							new MainActivity.LoginYesNoDialog()).show();
			return;
		}

		ServerApi api = new ServerApi();
		api.deviceId = UUID
				.fromString(preferences.getString("device.id", null));
		api.devicePassword = preferences.getString("device.password", null);
		api.apiUrl = preferences.getString("server.baseurl", this
				.getResources().getString(R.string.default_baseurl));

		try {
			String content = new ClipboardContentEndpoint(api).get();
			Toast.makeText(this, "Pulled the following content: " + content,
					Toast.LENGTH_LONG).show();
			this.setClipboard(content);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void doLogin(View view) {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}

	@SuppressWarnings("deprecation")
	protected void setClipboard(String text) {
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
			android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			clipboard.setText(text);
		} else {
			this.setClipboardHoneycomb(text);
		}
	}

	@SuppressWarnings("deprecation")
	protected String getClipboard() {
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
			android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			return clipboard.getText().toString();
		} else {
			return this.getClipboardHoneycomb();
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	protected void setClipboardHoneycomb(String text) {
		android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		android.content.ClipData clip = android.content.ClipData.newPlainText(
				"text label", text);
		clipboard.setPrimaryClip(clip);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	protected String getClipboardHoneycomb() {
		android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		android.content.ClipData clip = clipboard.getPrimaryClip();
		return clip.getItemAt(0).getText().toString();
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
}