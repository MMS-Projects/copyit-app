package net.mms_projects.copy_it.ui.swt;

import java.util.Date;

import net.mms_projects.copy_it.ClipboardListener;
import net.mms_projects.copy_it.ClipboardManager;
import net.mms_projects.copy_it.Settings;
import net.mms_projects.copy_it.SyncListener;
import net.mms_projects.copy_it.SyncManager;

import org.eclipse.swt.widgets.Shell;

public class TrayEntry implements SyncListener, ClipboardListener {

	protected Settings settings;
	protected Shell activityShell;
	protected SyncManager syncManager;
	protected ClipboardManager clipboardManager;

	public TrayEntry(Settings settings, Shell activityShell, SyncManager syncManager, ClipboardManager clipboardManager) {
		this.settings = settings;
		this.activityShell = activityShell;
		this.syncManager = syncManager;
		this.clipboardManager = clipboardManager;
		
		this.clipboardManager.addListener(this);
	}

	@Override
	public void onPushed(String content, Date date) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPulled(String content, Date date) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onContentSet(String content) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onContentGet(String content) {
		syncManager.doPush(content, new Date());
	}

}
