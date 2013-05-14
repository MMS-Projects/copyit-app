package net.mms_projects.copyit.clipboard_services;

import net.mms_projects.copyit.ClipboardListener;

public class TestService implements CopyServiceInterface, PasteServiceInterface {

	public static String SERVICE_NAME = "test";

	protected ClipboardListener listener;

	private String testContent;

	public TestService(ClipboardListener listener) {
		this.listener = listener;
	}

	@Override
	public void setContent(String content) {
		this.testContent = content;
		this.listener.onContentSet(this.testContent);
	}

	@Override
	public void getContent() {
		this.listener.onContentGet(this.testContent);
	}

	@Override
	public String getServiceName() {
		return SERVICE_NAME;
	}

	@Override
	public void activateCopy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deactivateCopy() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isCopyActivated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void activatePaste() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deactivatePaste() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isPasteActivated() {
		// TODO Auto-generated method stub
		return false;
	}

}
