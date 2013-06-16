package net.mms_projects.copy_it.clipboard_services;

import net.mms_projects.copy_it.ServiceInterface;

public interface PasteServiceInterface extends ServiceInterface {

	public void activatePaste();

	public void deactivatePaste();

	public boolean isPasteActivated();

	public void getContent();

}
