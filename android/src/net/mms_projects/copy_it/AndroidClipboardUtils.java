package net.mms_projects.copy_it;

import net.mms_projects.copy_it.ClipboardUtils;
import net.mms_projects.copy_it.R;
import net.mms_projects.copy_it.clipboard_backends.HoneycombBackend;
import net.mms_projects.copy_it.clipboard_backends.PreHoneycombBackend;
import android.content.Context;

public class AndroidClipboardUtils extends ClipboardUtils {

	private Context context;

	public AndroidClipboardUtils(Context context) {
		this.context = context;

		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
			this.setClipboardBackend(new PreHoneycombBackend(this.context));
		} else {
			this.setClipboardBackend(new HoneycombBackend(this.context));
		}
	}

}
