package net.mms_projects.copyit.ui.android;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import net.mms_projects.copy_it.R;
import net.mms_projects.copyit.LoginResponse;
import net.mms_projects.copyit.PasswordGenerator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class BrowserLoginActivity extends SherlockActivity {

	protected LoginResponse response = new LoginResponse();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PasswordGenerator generator = new PasswordGenerator();
		response.devicePassword = generator.generatePassword();

		setContentView(R.layout.activity_browser_login);

		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);

		String baseUrl = preferences.getString("server.baseurl", this
				.getResources().getString(R.string.default_baseurl));

		WebView webview = (WebView) findViewById(R.id.webview);
		webview.loadUrl(baseUrl + "/app-setup/setup?device_password="
				+ response.devicePassword);
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				URL location = null;
				try {
					location = new URL(url);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}

				System.out.println(url);

				if (location.getPath().startsWith("/app-setup/done/")) {
					response.deviceId = UUID.fromString(location.getPath()
							.substring(16));
					Intent returnIntent = new Intent();
					returnIntent.putExtra("device_id",
							response.deviceId.toString());
					returnIntent.putExtra("device_password",
							response.devicePassword);
					setResult(RESULT_OK, returnIntent);
					finish();
				} else if (location.getPath().startsWith("/app-setup/fail/")) {
					Intent returnIntent = new Intent();
					setResult(RESULT_CANCELED, returnIntent);
					finish();
				} else {
					view.loadUrl(url);
				}
				return true;
			}
		});
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

}
