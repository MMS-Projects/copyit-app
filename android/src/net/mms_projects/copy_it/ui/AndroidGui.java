package net.mms_projects.copy_it.ui;

import net.mms_projects.copy_it.R;
import net.mms_projects.copy_it.Settings;
import net.mms_projects.copy_it.ui.AbstractUi;
import net.mms_projects.copy_it.ui.android.MainActivity;

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
