package net.mms_projects.copy_it.clipboard_services;

import net.mms_projects.copy_it.ServiceInterface;

public interface CopyServiceInterface extends ServiceInterface {

	public void activateCopy();

	public void deactivateCopy();

	public boolean isCopyActivated();

    @Deprecated
	public void requestSet(String content);

    /**
     * This method changes the clipboard content.
     * @param content The content to set
     */
    public void setContent(String content);

}
