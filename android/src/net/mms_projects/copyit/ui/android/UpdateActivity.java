package net.mms_projects.copyit.ui.android;

import com.google.analytics.tracking.android.EasyTracker;

import net.mms_projects.copy_it.R;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class UpdateActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);

		Bundle extras = this.getIntent().getExtras();
		if (extras == null) {
			Intent returnIntent = new Intent();
			setResult(RESULT_CANCELED, returnIntent);
			finish();
		}

		NotificationManager notificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(1);

		TextView artifactName = (TextView) findViewById(R.id.label_artifact_name);
		artifactName.setText("copyit-android.apk");
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

	public void doDownload(View view) {
		Bundle extras = this.getIntent().getExtras();

		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(extras
				.getString("build_url")));
		startActivity(browserIntent);
	}

}
