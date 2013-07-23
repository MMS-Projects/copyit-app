package net.mms_projects.copy_it.functionality;

import java.util.Date;

import net.mms_projects.copy_it.Activatable;
import net.mms_projects.copy_it.ClipboardListener;
import net.mms_projects.copy_it.ClipboardManager;
import net.mms_projects.copy_it.EnvironmentIntegration;
import net.mms_projects.copy_it.EnvironmentIntegration.NotificationManager;
import net.mms_projects.copy_it.EnvironmentIntegration.NotificationManager.NotificationUrgency;
import net.mms_projects.copy_it.Messages;
import net.mms_projects.copy_it.SyncListener;
import net.mms_projects.copy_it.SyncManager;
import net.mms_projects.copy_it.listeners.EnabledListener;

public class SyncClipboardBinding implements Activatable, SyncListener,
		ClipboardListener {

	protected SyncManager syncManager;
	protected ClipboardManager clipboardManager;

	private boolean enabled;
	private EnvironmentIntegration environmentIntegration;

	public SyncClipboardBinding(EnvironmentIntegration environmentIntegration,
			SyncManager syncManager, ClipboardManager clipboardManager) {
		this.environmentIntegration = environmentIntegration;
		this.syncManager = syncManager;
		this.clipboardManager = clipboardManager;
	}

	@Override
	public void onPushed(String content, Date date) {
		if (this.isEnabled()) {
			getNotificationManager().notify(10, NotificationUrgency.NORMAL, "",
					"CopyIt",
					Messages.getString("text_content_pushed", content));
		}
	}

	@Override
	public void onRemoteContentChange(final String content, Date date) {
		if (this.isEnabled()) {
			this.clipboardManager.requestSet(content);

			getNotificationManager().notify(10, NotificationUrgency.NORMAL, "",
					"CopyIt",
					Messages.getString("text_content_pulled", content));
		}
	}

	@Override
	public void onContentSet(String content) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onContentGet(String content) {
		if (this.isEnabled()) {
			this.syncManager.updateRemoteContentAsync(content, new Date());
		}
	}

	@Override
	public void enable() {
		this.enabled = true;
	}

	@Override
	public void disable() {
		this.enabled = false;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (enabled) {
			this.enable();
		} else {
			this.disable();
		}
	}

	@Override
	public void addEnabledListener(EnabledListener listener) {

	}

	private NotificationManager getNotificationManager() {
		return this.environmentIntegration.getNotificationManager();
	}

}
