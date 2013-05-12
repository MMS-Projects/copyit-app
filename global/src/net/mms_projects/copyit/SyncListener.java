package net.mms_projects.copyit;

import java.util.Date;

public interface SyncListener {

	public void onPushed(String content, Date date);
	public void onPulled(String content, Date date);
	
}
