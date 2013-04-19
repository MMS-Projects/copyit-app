package net.mms_projects.copyit;

import net.mms_projects.copyit.clipboard_backends.AbstractClipboardBackend;

abstract public class ClipboardUtils {

	private AbstractClipboardBackend backend;
	
	public String getText() {
		return this.backend.getText();
	}
	
	public void setText(String text) {
		this.backend.setText(text);
	}
	
	public void setClipboardBackend(AbstractClipboardBackend backend) {
		this.backend = backend;
	}
	
}
