package net.mms_projects.copyit.sync_services;

public interface PullServiceInterface extends ServiceInterface {

	public void activatePull();

	public void deactivatePull();

	public void doPull();

}
