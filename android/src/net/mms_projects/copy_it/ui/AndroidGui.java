package net.mms_projects.copy_it.ui;

import net.mms_projects.copy_it.R;
import net.mms_projects.copy_it.Config;
import net.mms_projects.copy_it.ui.android.MainActivity;
import net.mms_projects.copy_it.ui_old.AbstractUi;

public class AndroidGui extends AbstractUi {

	protected MainActivity activity;

	public AndroidGui(Config settings, MainActivity activity) {
		super(settings);

		this.activity = activity;
	}

	@Override
	public void open() {
	}

}
