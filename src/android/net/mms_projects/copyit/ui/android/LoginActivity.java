package net.mms_projects.copyit.ui.android;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import net.mms_projects.copyit.LoginResponse;
import net.mms_projects.copyit.R;
import net.mms_projects.copyit.api.endpoints.DeviceEndpoint;
import net.mms_projects.copyit.app.AndroidApplication;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class LoginActivity extends Activity {

	private static final int ACTIVITY_LOGIN = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
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

				try {
					AndroidApplication.getInstance().getSettings()
							.set("device.id", response.deviceId.toString());
					AndroidApplication.getInstance().getSettings()
							.set("device.password", response.devicePassword);
					AndroidApplication.getInstance().getSettings()
							.saveProperties();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				AndroidApplication.getInstance().getApi().deviceId = response.deviceId;
				AndroidApplication.getInstance().getApi().devicePassword = response.devicePassword;

				try {
					InetAddress addr = InetAddress.getLocalHost();
					String hostname = addr.getHostName();

					new DeviceEndpoint(AndroidApplication.getInstance()
							.getApi()).create(hostname);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (Exception e) {
					AlertDialog alertDialog = new AlertDialog.Builder(this)
							.create();
					alertDialog.setTitle("Error");
					alertDialog.setMessage("Could not setup the device: "
							+ e.getMessage());
					alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
							"OK", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							});
					alertDialog.setIcon(R.drawable.ic_launcher);
					alertDialog.show();
				}
				break;
			}
		}
	}

}
