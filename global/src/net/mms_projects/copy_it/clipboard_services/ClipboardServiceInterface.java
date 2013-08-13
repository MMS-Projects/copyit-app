package net.mms_projects.copy_it.clipboard_services;

import net.mms_projects.copy_it.ServiceInterface;

public interface ClipboardServiceInterface extends ServiceInterface {

    public void activatePaste();

    public void deactivatePaste();

    public boolean isPasteActivated();

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
