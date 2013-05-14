package net.mms_projects.copyit.sync_services;

import net.mms_projects.copyit.ServiceInterface;

public interface PullingServiceInterface extends ServiceInterface {

	public void activatePulling();

	public void deactivatePulling();
	
	public boolean isPullingActivated();

}
