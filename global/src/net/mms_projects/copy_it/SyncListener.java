package net.mms_projects.copy_it;

import java.util.Date;

public interface SyncListener {

	
	
	public void onRemoteContentChange(String content, Date date);
	
}
