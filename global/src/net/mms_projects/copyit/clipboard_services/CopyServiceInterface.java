package net.mms_projects.copyit.clipboard_services;

import net.mms_projects.copyit.ServiceInterface;

public interface CopyServiceInterface extends ServiceInterface {

	public void activateCopy();

	public void deactivateCopy();

	public boolean isCopyActivated();

	public void setContent(String content);

}
