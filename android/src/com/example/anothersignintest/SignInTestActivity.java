package com.example.anothersignintest;

import java.io.IOException;

import net.mms_projects.copy_it.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusClient.OnAccessRevokedListener;

/**
 * @see <a href="https://gist.github.com/ianbarber/5170508">https://gist.github.com/ianbarber/5170508</a>
 */
public class SignInTestActivity extends Activity implements
		ConnectionCallbacks, OnConnectionFailedListener, OnClickListener,
		OnAccessRevokedListener {

	private static final String TAG = "SignInTestActivity";

	// A magic number we will use to know that our sign-in error
	// resolution activity has completed.
	private static final int OUR_REQUEST_CODE = 49404;

	// The core Google+ client.
	private PlusClient mPlusClient;

	// A flag to stop multiple dialogues appearing for the user.
	private boolean mResolveOnFail;

	// We can store the connection result from a failed connect()
	// attempt in order to make the application feel a bit more
	// responsive for the user.
	private ConnectionResult mConnectionResult;

	// A progress dialog to display when the user is connecting in
	// case there is a delay in any of the dialogs being ready.
	private ProgressDialog mConnectionProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in_test);
		// We pass through this for all three arguments, specifying the:
		// 1. Context
		// 2. Object to call onConnected and onDisconnected on
		// 3. Object to call onConnectionFailed on
		mPlusClient = new PlusClient.Builder(this, this, this)
				.setVisibleActivities("http://schemas.google.com/BuyActivity")
				.build();

		// We use mResolveOnFail as a flag to say whether we should trigger
		// the resolution of a connectionFailed ConnectionResult.
		mResolveOnFail = false;

		// Connect our sign in, sign out and disconnect buttons.
		findViewById(R.id.sign_in_button).setOnClickListener(this);
		findViewById(R.id.sign_out_button).setOnClickListener(this);
		findViewById(R.id.revoke_access_button).setOnClickListener(this);
		findViewById(R.id.sign_out_button).setVisibility(View.INVISIBLE);
		findViewById(R.id.revoke_access_button).setVisibility(View.INVISIBLE);

		// Configure the ProgressDialog that will be shown if there is a
		// delay in presenting the user with the next sign in step.
		mConnectionProgressDialog = new ProgressDialog(this);
		mConnectionProgressDialog.setMessage("Signing in...");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.v(TAG, "Start");
		// Every time we start we want to try to connect. If it
		// succeeds we'll get an onConnected() callback. If it
		// fails we'll get onConnectionFailed(), with a result!
		mPlusClient.connect();
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.v(TAG, "Stop");
		// It can be a little costly to keep the connection open
		// to Google Play Services, so each time our activity is
		// stopped we should disconnect.
		mPlusClient.disconnect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.v(TAG, "ConnectionFailed - User resolvable");
		// Most of the time, the connection will fail with a
		// user resolvable result. We can store that in our
		// mConnectionResult property ready for to be used
		// when the user clicks the sign-in button.
		if (result.hasResolution()) {
			mConnectionResult = result;
			if (mResolveOnFail) {
				// This is a local helper function that starts
				// the resolution of the problem, which may be
				// showing the user an account chooser or similar.
				startResolution();
			}
		}
	}

	@Override
	public void onConnected(Bundle bundle) {
		// Yay! We can get the oAuth 2.0 access token we are using.
		Log.v(TAG, "Connected. Yay!");

		// Turn off the flag, so if the user signs out they'll have to
		// tap to sign in again.
		mResolveOnFail = false;

		// Hide the progress dialog if its showing.
		mConnectionProgressDialog.dismiss();

		// Hide the sign in button, show the sign out buttons.
		findViewById(R.id.sign_in_button).setVisibility(View.INVISIBLE);
		findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
		findViewById(R.id.revoke_access_button).setVisibility(View.VISIBLE);

		// Retrieve the oAuth 2.0 access token.
		final Context context = this.getApplicationContext();
		AsyncTask task = new AsyncTask() {
			@Override
			protected Object doInBackground(Object... params) {
				String scope = "oauth2:" + Scopes.PLUS_LOGIN
						+ " https://www.googleapis.com/auth/userinfo.email";
				try {
					// We can retrieve the token to check via
					// tokeninfo or to pass to a service-side
					// application.
					String token = GoogleAuthUtil.getToken(context,
							mPlusClient.getAccountName(), scope);
				} catch (UserRecoverableAuthException e) {
					// This error is recoverable, so we could fix this
					// by displaying the intent to the user.
					// e.printStackTrace();
					startActivityForResult(e.getIntent(), OUR_REQUEST_CODE);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (GoogleAuthException e) {
					e.printStackTrace();
				}
				return null;
			}
		};
		task.execute((Void) null);
	}

	@Override
	public void onDisconnected() {
		// Bye!
		Log.v(TAG, "Disconnected. Bye!");
	}

	protected void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		Log.v(TAG, "ActivityResult: " + requestCode);
		if (requestCode == OUR_REQUEST_CODE && responseCode == RESULT_OK) {
			// If we have a successful result, we will want to be able to
			// resolve any further errors, so turn on resolution with our
			// flag.
			mResolveOnFail = true;
			// If we have a successful result, lets call connect() again. If
			// there are any more errors to resolve we'll get our
			// onConnectionFailed, but if not, we'll get onConnected.
			if (!mPlusClient.isConnected()) {
				Log.v(TAG, "Connecting because we're not connected");
				mPlusClient.connect();
			}
		} else if (requestCode == OUR_REQUEST_CODE && responseCode != RESULT_OK) {
			// If we've got an error we can't resolve, we're no
			// longer in the midst of signing in, so we can stop
			// the progress spinner.
			mConnectionProgressDialog.dismiss();
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.sign_in_button:
			Log.v(TAG, "Tapped sign in");
			if (!mPlusClient.isConnected()) {
				// Show the dialog as we are now signing in.
				mConnectionProgressDialog.show();
				// Make sure that we will start the resolution (e.g. fire the
				// intent and pop up a dialog for the user) for any errors
				// that come in.
				mResolveOnFail = true;
				// We should always have a connection result ready to resolve,
				// so we can start that process.
				if (mConnectionResult != null) {
					startResolution();
				} else {
					// If we don't have one though, we can start connect in
					// order to retrieve one.
					mPlusClient.connect();
				}
			}
			break;
		case R.id.sign_out_button:
			Log.v(TAG, "Tapped sign out");
			// We only want to sign out if we're connected.
			if (mPlusClient.isConnected()) {
				// Clear the default account in order to allow the user
				// to potentially choose a different account from the
				// account chooser.
				mPlusClient.clearDefaultAccount();

				// Disconnect from Google Play Services, then reconnect in
				// order to restart the process from scratch.
				mPlusClient.disconnect();
				mPlusClient.connect();

				// Hide the sign out buttons, show the sign in button.
				findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
				findViewById(R.id.sign_out_button)
						.setVisibility(View.INVISIBLE);
				findViewById(R.id.revoke_access_button).setVisibility(
						View.INVISIBLE);
			}
			break;
		case R.id.revoke_access_button:
			Log.v(TAG, "Tapped disconnect");
			if (mPlusClient.isConnected()) {
				// Clear the default account as in the Sign Out.
				mPlusClient.clearDefaultAccount();

				// Go away and revoke access to this entire application.
				// This will call back to onAccessRevoked when it is
				// complete as it needs to go away to the Google
				// authentication servers to revoke all token.
				mPlusClient.revokeAccessAndDisconnect(this);
			}
			break;
		default:
			// Unknown id.
		}
	}

	@Override
	public void onAccessRevoked(ConnectionResult status) {
		// mPlusClient is now disconnected and access has been revoked.
		// We should now delete any data we need to comply with the
		// developer properties. To reset ourselves to the original state,
		// we should now connect again. We don't have to disconnect as that
		// happens as part of the call.
		mPlusClient.connect();

		// Hide the sign out buttons, show the sign in button.
		findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
		findViewById(R.id.sign_out_button).setVisibility(View.INVISIBLE);
		findViewById(R.id.revoke_access_button).setVisibility(View.INVISIBLE);
	}

	/**
	 * A helper method to flip the mResolveOnFail flag and start the resolution
	 * of the ConnenctionResult from the failed connect() call.
	 */
	private void startResolution() {
		try {
			// Don't start another resolution now until we have a
			// result from the activity we're about to start.
			mResolveOnFail = false;
			// If we can resolve the error, then call start resolution
			// and pass it an integer tag we can use to track. This means
			// that when we get the onActivityResult callback we'll know
			// its from being started here.
			mConnectionResult.startResolutionForResult(this, OUR_REQUEST_CODE);
		} catch (SendIntentException e) {
			// Any problems, just try to connect() again so we get a new
			// ConnectionResult.
			mPlusClient.connect();
		}
	}
}