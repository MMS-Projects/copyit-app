package net.mms_projects.copyit.ui.android;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import net.mms_projects.copyit.R;
import net.mms_projects.copyit.R.layout;
import net.mms_projects.copyit.R.menu;
import net.mms_projects.copyit.ServerApi;
import net.mms_projects.copyit.Settings;
import net.mms_projects.copyit.app.CopyItAndroid;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;

public class MainActivity extends Activity {

	private CopyItAndroid app;
	private Settings settings;
	private ServerApi api;
	
	public void setup(Settings settings, ServerApi api) {
		this.settings = settings;
		this.api = api;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.app = new CopyItAndroid();
		try {
			FileOutputStream output = openFileOutput("settings", Context.MODE_PRIVATE);
			this.app.run(
				this,
				openFileInput("settings"),
				output
			);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(this.settings.get("server.baseurl"));
		
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

}
