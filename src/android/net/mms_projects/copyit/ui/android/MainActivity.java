package net.mms_projects.copyit.ui.android;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import net.mms_projects.copyit.ClipboardUtils;
import net.mms_projects.copyit.FileStreamBuilder;
import net.mms_projects.copyit.R;
import net.mms_projects.copyit.api.ServerApi;
import net.mms_projects.copyit.api.endpoints.ClipboardContentEndpoint;
import net.mms_projects.copyit.app.CopyItAndroid;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

		// Get intent, action and MIME type
		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();

		if (Intent.ACTION_SEND.equals(action) && type != null) {
			if ("text/plain".equals(type)) {
				handleSendText(intent); // Handle text being sent
			}
		}
	}

	private void handleSendText(Intent intent) {
		String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
		if (sharedText != null) {
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(this);

			Map<String, ?> settings = preferences.getAll();
			for (String key : settings.keySet()) {
				System.out.println(key + ": " + settings.get(key));
			}

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

			ClipboardUtils clipboard = new ClipboardUtils(MainActivity.this);

			CopyItTask task = new HandleShareTask(api);
			task.execute(clipboard.getText());
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

		ClipboardUtils clipboard = new ClipboardUtils(MainActivity.this);

		CopyItTask task = new CopyItTask(api);
		task.execute(clipboard.getText());
	}

	public void pasteIt(View view) {
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

		PasteItTask task = new PasteItTask(api);
		task.execute();
	}

	public void sendToApp(View view) {
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

		SendToAppTask task = new SendToAppTask(api);
		task.execute();
	}

	public void doLogin(View view) {
		Intent intent = new Intent(this, LoginActivity.class);
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

	private class CopyItTask extends ServerApiTask<String, Void, Boolean> {
		public CopyItTask(ServerApi api) {
			super(api);
		}

		private String content;

		@Override
		protected Boolean doInBackground(String... params) {
			this.content = params[0];

			try {
				return new ClipboardContentEndpoint(api).update(this.content);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			System.out.println(result);
			if (result) {
				Toast.makeText(
						MainActivity.this,
						MainActivity.this.getResources().getString(
								R.string.text_content_pushed, this.content),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	private class PasteItTask extends ServerApiTask<Void, Void, String> {
		public PasteItTask(ServerApi api) {
			super(api);
		}

		@Override
		protected String doInBackground(Void... params) {
			try {
				return new ClipboardContentEndpoint(api).get();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String content) {
			ClipboardUtils clipboard = new ClipboardUtils(MainActivity.this);

			Toast.makeText(
					MainActivity.this,
					MainActivity.this.getResources().getString(
							R.string.text_content_pulled, content),
					Toast.LENGTH_LONG).show();
			clipboard.setText(content);
		}
	}

	private class SendToAppTask extends ServerApiTask<Void, Void, String> {
		public SendToAppTask(ServerApi api) {
			super(api);
		}

		@Override
		protected String doInBackground(Void... params) {
			try {
				return new ClipboardContentEndpoint(api).get();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String content) {
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, content);
			sendIntent.setType("text/plain");
			startActivity(Intent.createChooser(
					sendIntent,
					MainActivity.this.getResources().getString(
							R.string.dialog_title_select_send_app)));
		}
	}

	private class HandleShareTask extends CopyItTask {
		public HandleShareTask(ServerApi api) {
			super(api);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			MainActivity.this.finish();
		}
	}
}