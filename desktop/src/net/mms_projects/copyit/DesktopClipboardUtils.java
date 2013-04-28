package net.mms_projects.copyit;

import net.mms_projects.copyit.clipboard_backends.SwtBackend;

public class DesktopClipboardUtils extends ClipboardUtils {

	public DesktopClipboardUtils() {
		this.setClipboardBackend(new SwtBackend());
	}

}
