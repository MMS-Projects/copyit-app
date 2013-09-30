package net.mms_projects.copy_it.ui.android;

import java.util.UUID;

import net.mms_projects.copy_it.LoginResponse;
import net.mms_projects.copy_it.R;
import net.mms_projects.copy_it.activities.BrowserLoginActivity;
import net.mms_projects.copy_it.android.tasks.SetupDeviceTask;
import net.mms_projects.copy_it.api.ServerApi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.google.analytics.tracking.android.EasyTracker;

public class LoginActivity extends SherlockActivity {

	private static final int ACTIVITY_LOGIN = 1;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	private UiLifecycleHelper uiHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.uiHelper = new UiLifecycleHelper(this, callback);
		this.uiHelper.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void onSessionStateChange(final Session session,
			final SessionState state, final Exception exception) {
		if (state.isOpened()) {
			this.log.info("Logged in...");

			Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(this.getString(
					R.string.login_dialog_text_automatic_service_login,
					"Facebook"));
			builder.setCancelable(true);
			builder.setPositiveButton(R.string.dialog_button_yes,
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							/*
							 * Close the dialog because it is no longer needed
							 */
							dialog.dismiss();

							/*
							 * Start the browser login with Facebook as login
							 * service and the Facebook accesstoken
							 */
							openBrowserLogin("facebook",
									session.getAccessToken());
						}
					});
			builder.setNegativeButton(R.string.dialog_button_no,
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							/*
							 * Close the dialog because it is no longer needed
							 */
							dialog.dismiss();
						}
					});
			AlertDialog dialog = builder.create();
			dialog.show();
		} else if (state.isClosed()) {
			this.log.info("Logged out...");
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

	@Override
	public void onResume() {
		super.onResume();

		// For scenarios where the main activity is launched and user
		// session is not null, the session state change notification
		// may not be triggered. Trigger it if it's open/closed.
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}

		this.uiHelper.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();

		this.uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		this.uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		this.uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void openBrowserLogin(View view) {
		Intent intent = new Intent(this, BrowserLoginActivity.class);
		startActivityForResult(intent, LoginActivity.ACTIVITY_LOGIN);
	}

	public void openBrowserLogin(String loginProvider, String accesstoken) {
		log.info("Openening browser login using provider and accesstoken");

		Intent intent = new Intent(this, BrowserLoginActivity.class);
		intent.putExtra(BrowserLoginActivity.EXTRA_PROVIDER, loginProvider);
		intent.putExtra(BrowserLoginActivity.EXTRA_ACCESS_TOKEN, accesstoken);
		startActivityForResult(intent, LoginActivity.ACTIVITY_LOGIN);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case LoginActivity.ACTIVITY_LOGIN:
			if (resultCode == RESULT_OK) {
				LoginResponse response = new LoginResponse();
				response.deviceId = UUID.fromString(data
						.getStringExtra("device_id"));
				response.devicePassword = data
						.getStringExtra("device_password");

				SharedPreferences preferences = PreferenceManager
						.getDefaultSharedPreferences(this);

				try {
					Editor preferenceEditor = preferences.edit();
					preferenceEditor.putString("device.id",
							response.deviceId.toString());
					preferenceEditor.putString("device.password",
							response.devicePassword);
					preferenceEditor.commit();
				} catch (Exception event) {
					// TODO Auto-generated catch block
					event.printStackTrace();
				}

				ServerApi api = new ServerApi();
				api.deviceId = response.deviceId;
				api.devicePassword = response.devicePassword;
				api.apiUrl = preferences.getString("server.baseurl", this
						.getResources().getString(R.string.default_baseurl));

				LoginTask task = new LoginTask(this, api);
				task.execute();
				break;
			}
		}

		this.uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	private class LoginTask extends SetupDeviceTask {
		public LoginTask(Context context, ServerApi api) {
			super(context, api);

			this.setProgressDialigMessage(context.getResources().getString(
					R.string.text_logging_in));
		}

		@Override
		protected void onPostExecute(Boolean result) {
			try {
				this.doExceptionCheck();

				if (result) {
					Toast.makeText(
							this.context,
							this.context.getResources().getString(
									R.string.text_login_successful),
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception exception) {
				// TODO Auto-generated catch block
				exception.printStackTrace();

				AlertDialog alertDialog = new AlertDialog.Builder(this.context)
						.create();
				alertDialog.setTitle(this.context.getResources().getString(
						R.string.dialog_title_error));
				alertDialog.setMessage(this.context.getResources().getString(
						R.string.error_device_setup_failed,
						exception.getMessage()));
				alertDialog.setButton(
						DialogInterface.BUTTON_POSITIVE,
						this.context.getResources().getString(
								R.string.dialog_button_okay),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});
				alertDialog.setIcon(R.drawable.ic_launcher);
				alertDialog.show();
				return;
			}
			
			super.onPostExecute(result);

			LoginActivity.this.finish();
		}
	}

}
