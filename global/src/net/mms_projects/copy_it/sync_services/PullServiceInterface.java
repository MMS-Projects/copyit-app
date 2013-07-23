package net.mms_projects.copy_it.sync_services;

import net.mms_projects.copy_it.ServiceInterface;

public interface PullServiceInterface extends ServiceInterface {

	public void activatePull();

	public void deactivatePull();

	public boolean isPullActivated();

	public String getContent();
	
	@Deprecated
	public void doPull();

}
