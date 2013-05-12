package net.mms_projects.copyit.sync_services;

import java.util.Date;

public interface PushServiceInterface extends ServiceInterface {

	public void activatePush();

	public void deactivatePush();

	public void doPush(String content, Date date);

}
