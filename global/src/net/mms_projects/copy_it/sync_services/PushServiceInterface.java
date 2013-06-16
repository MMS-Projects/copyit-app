package net.mms_projects.copy_it.sync_services;

import java.util.Date;

import net.mms_projects.copy_it.ServiceInterface;

public interface PushServiceInterface extends ServiceInterface {

	public void activatePush();

	public void deactivatePush();

	public boolean isPushActivated();
	
	public void doPush(String content, Date date);

}
