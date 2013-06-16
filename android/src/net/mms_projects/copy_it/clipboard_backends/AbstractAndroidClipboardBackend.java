package net.mms_projects.copy_it.clipboard_backends;

import android.content.Context;
import net.mms_projects.copy_it.R;
import net.mms_projects.copy_it.clipboard_backends.AbstractClipboardBackend;

abstract public class AbstractAndroidClipboardBackend extends AbstractClipboardBackend {

	protected Context context;
	
	public AbstractAndroidClipboardBackend(Context context) {
		this.context = context;
	}

}
