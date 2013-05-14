package net.mms_projects.copyit;


public interface PollingServiceInterface extends ServiceInterface {

	public void activatePolling();

	public void deactivatePolling();
	
	public boolean isPollingActivated();

}
