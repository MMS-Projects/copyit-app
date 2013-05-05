package net.mms_projects.copyit.ui.swt;

import org.eclipse.swt.widgets.Shell;

import net.mms_projects.copyit.Settings;

public class TrayEntry {

	protected Settings settings;
	protected Shell activityShell;

	public TrayEntry(Settings settings, Shell activityShell) {
		this.settings = settings;
		this.activityShell = activityShell;
	}

}
