package net.mms_projects.copy_it.ui.swt;

import org.eclipse.swt.widgets.Display;

public class SwtUtils {

	public static boolean isUIThread() {
		return (Thread.currentThread().getId() == Display.getDefault()
				.getThread().getId());
	}

}
