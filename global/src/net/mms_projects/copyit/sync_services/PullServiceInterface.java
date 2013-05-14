package net.mms_projects.copyit.sync_services;

import net.mms_projects.copyit.ServiceInterface;

public interface PullServiceInterface extends ServiceInterface {

	public void activatePull();

	public void deactivatePull();

	public boolean isPullActivated();

	public void doPull();

}
