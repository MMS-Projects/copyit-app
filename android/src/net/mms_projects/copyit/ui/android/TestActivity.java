package net.mms_projects.copyit.ui.android;

import java.util.List;

import net.mms_projects.copy_it.R;
import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.InstrumentationInfo;
import android.os.Bundle;
import android.view.View;

public class TestActivity extends Activity {

	protected List<InstrumentationInfo> list;
	protected final static String TARGET_PACKAGE = "net.mms_projects.copy_it";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		this.list = this.getPackageManager()
				.queryInstrumentation(TARGET_PACKAGE, 0);
	}

	public void startTesting(View view) {
		this.startInstrumentation(instrumentationForPosition(0), null, null);
	}

	public ComponentName instrumentationForPosition(int position) {
		if (this.list == null) {
			return null;
		}
		InstrumentationInfo ii = this.list.get(position);
		return new ComponentName(ii.packageName, ii.name);
	}

}
