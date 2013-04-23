package net.mms_projects.copyit.clipboard_backends;

import android.content.Context;

abstract public class AbstractAndroidClipboardBackend extends AbstractClipboardBackend {

	protected Context context;
	
	public AbstractAndroidClipboardBackend(Context context) {
		this.context = context;
	}

}
