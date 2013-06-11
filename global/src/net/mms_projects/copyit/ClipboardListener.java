package net.mms_projects.copyit;

public interface ClipboardListener {

	public void onContentSet(String content);

	public void onContentGet(String content);

}
