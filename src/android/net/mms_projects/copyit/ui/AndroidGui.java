package net.mms_projects.copyit.ui;

import net.mms_projects.copyit.Settings;
import net.mms_projects.copyit.ui.android.MainActivity;

public class AndroidGui extends AbstractUi {

	protected MainActivity activity;

	public AndroidGui(Settings settings, MainActivity activity) {
		super(settings);

		this.activity = activity;
	}

	@Override
	public void open() {
	}

}
