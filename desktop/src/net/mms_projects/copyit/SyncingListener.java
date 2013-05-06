package net.mms_projects.copyit;

import java.util.Date;

public interface SyncingListener {

	public void onPreSync();

	public void onClipboardChange(String data, Date date);

	public void onPostSync();

}
