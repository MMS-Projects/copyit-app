package net.mms_projects.copy_it.clipboard_backends;

abstract public class AbstractClipboardBackend {

	abstract public String getText();
	
	abstract public void setText(String text);
	
}
