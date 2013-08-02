package net.mms_projects.copy_it.ui.exceptions;

import org.eclipse.swt.SWTException;

public class NoBrowserEngineException extends RuntimeException {

	private static final long serialVersionUID = 1517746796713704274L;

	public NoBrowserEngineException(SWTException cause) {
		super("No engine available for the SWT browser", cause);
	}

}
