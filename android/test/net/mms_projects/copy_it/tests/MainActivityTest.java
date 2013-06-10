package net.mms_projects.copy_it.tests;

import net.mms_projects.copyit.ui.android.MainActivity;
import android.content.Intent;

public class MainActivityTest extends
		android.test.ActivityUnitTestCase<MainActivity> {

	private MainActivity activity;

	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		Intent intent = new Intent(getInstrumentation().getTargetContext(),
				MainActivity.class);
		startActivity(intent, null, null);
		activity = getActivity();
	}

}
