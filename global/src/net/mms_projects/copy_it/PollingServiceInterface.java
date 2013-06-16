package net.mms_projects.copy_it;


public interface PollingServiceInterface extends ServiceInterface {

	public void activatePolling();

	public void deactivatePolling();
	
	public boolean isPollingActivated();

}
