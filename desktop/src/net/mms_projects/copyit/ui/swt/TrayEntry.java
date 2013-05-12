package net.mms_projects.copyit.ui.swt;

import java.util.Date;

import net.mms_projects.copyit.Settings;
import net.mms_projects.copyit.SyncManager;
import net.mms_projects.copyit.SyncingListener;

import org.eclipse.swt.widgets.Shell;

public class TrayEntry implements SyncingListener {

	protected Settings settings;
	protected Shell activityShell;
	protected SyncManager syncManager;

	public TrayEntry(Settings settings, Shell activityShell, SyncManager syncManager) {
		this.settings = settings;
		this.activityShell = activityShell;
		this.syncManager = syncManager;
	}

	@Override
	public void onPreSync() {
	}

	@Override
	public void onClipboardChange(String data, Date date) {
	}

	@Override
	public void onPostSync() {
	}

}
