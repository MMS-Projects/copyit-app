package net.mms_projects.copy_it.clipboard_services;

import net.mms_projects.copy_it.ServiceInterface;

public interface PasteServiceInterface extends ServiceInterface {

	public void activatePaste();

	public void deactivatePaste();

	public boolean isPasteActivated();

	@Deprecated
	public void requestGet();

    /**
     * This method gets the content from the clipboard.
     *
     * If it takes a while then this method will block. Use
     * {@link #requestGet()} if you want to return immediately
     * and don't care about listening for a event.
     * @return Returns the clipboard content
     */
    public String getContent();

}
