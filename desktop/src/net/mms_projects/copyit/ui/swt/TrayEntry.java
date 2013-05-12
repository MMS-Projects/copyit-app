package net.mms_projects.copyit.ui.swt;

import java.util.Date;

import net.mms_projects.copyit.Settings;
import net.mms_projects.copyit.SyncListener;
import net.mms_projects.copyit.SyncManager;

import org.eclipse.swt.widgets.Shell;

public class TrayEntry implements SyncListener {

	protected Settings settings;
	protected Shell activityShell;
	protected SyncManager syncManager;

	public TrayEntry(Settings settings, Shell activityShell, SyncManager syncManager) {
		this.settings = settings;
		this.activityShell = activityShell;
		this.syncManager = syncManager;
	}

	@Override
	public void onPushed(String content, Date date) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPulled(String content, Date date) {
		// TODO Auto-generated method stub
		
	}

}
