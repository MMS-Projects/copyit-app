package net.mms_projects.copyit.clipboard_services;

import net.mms_projects.copyit.ServiceInterface;

public interface PasteServiceInterface extends ServiceInterface {

	public void activatePaste();

	public void deactivatePaste();

	public boolean isPasteActivated();

	public void getContent();

}
