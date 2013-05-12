package net.mms_projects.copyit.sync_services;

public interface PullingServiceInterface extends ServiceInterface {

	public void activatePulling();

	public void deactivatePulling();
	
	public boolean isPullingActivated();

}
