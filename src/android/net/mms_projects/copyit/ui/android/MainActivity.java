package net.mms_projects.copyit.ui.android;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import net.mms_projects.copyit.LoginResponse;
import net.mms_projects.copyit.R;
import net.mms_projects.copyit.ServerApi;
import net.mms_projects.copyit.Settings;
import net.mms_projects.copyit.app.AndroidApplication;
import net.mms_projects.copyit.app.CopyItAndroid;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	private CopyItAndroid app;
	private Settings settings;
	private ServerApi api;

	private static final int ACTIVITY_LOGIN = 1;

	public void setup(Settings settings, ServerApi api) {
		this.settings = settings;
		this.api = api;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.app = new CopyItAndroid();
		try {
			FileOutputStream output = openFileOutput("settings",
					Context.MODE_PRIVATE);
			this.app.run(this, openFileInput("settings"), output);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			System.out.println(this.api.get());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void copyIt(View view) {
		try {
			this.api.set(this.getClipboard());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void pasteIt(View view) {
		try {
			this.setClipboard(this.api.get());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void login(View view) {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivityForResult(intent, MainActivity.ACTIVITY_LOGIN);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case MainActivity.ACTIVITY_LOGIN:
			if (resultCode == RESULT_OK) {
				LoginResponse response = new LoginResponse();
				response.deviceId = UUID.fromString(data
						.getStringExtra("device_id"));
				response.devicePassword = data
						.getStringExtra("device_password");

				try {
					this.settings
							.set("device.id", response.deviceId.toString());
					this.settings.set("device.password",
							response.devicePassword);
					this.settings.saveProperties();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				this.api.deviceId = response.deviceId;
				this.api.devicePassword = response.devicePassword;

				try {
					InetAddress addr = InetAddress.getLocalHost();
					String hostname = addr.getHostName();

					this.api.initDevice(hostname);
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
	
	@SuppressWarnings("deprecation")
	protected void setClipboard(String text) {
		int sdk = android.os.Build.VERSION.SDK_INT;
		if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
		    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		    clipboard.setText(text);
		} else {
		    this.setClipboardHoneycomb(text);
		}
	}
	
	@SuppressWarnings("deprecation")
	protected String getClipboard() {
		int sdk = android.os.Build.VERSION.SDK_INT;
		if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
		    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		    return clipboard.getText().toString();
		} else {
		    return this.getClipboardHoneycomb();
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	protected void setClipboardHoneycomb(String text) {
		android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE); 
	    android.content.ClipData clip = android.content.ClipData.newPlainText("text label", text);
	    clipboard.setPrimaryClip(clip);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	protected String getClipboardHoneycomb() {
		android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE); 
		android.content.ClipData clip = clipboard.getPrimaryClip();
		return clip.getItemAt(0).getText().toString();
	}
}