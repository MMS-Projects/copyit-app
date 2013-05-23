package net.mms_projects.copy_it.tests;

import net.mms_projects.copyit.ui.android.MainActivity;
import android.content.Intent;
import android.widget.Button;

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

	public void testLayout() {
		int buttonSettings = net.mms_projects.copy_it.R.id.button_settings;
		assertNotNull(activity.findViewById(buttonSettings));
		Button buttonViewSettings = (Button) activity
				.findViewById(buttonSettings);
		assertEquals(
				"Incorrect label of the button",
				activity.getString(net.mms_projects.copy_it.R.string.dashboard_button_settings),
				buttonViewSettings.getText());
	}

}
