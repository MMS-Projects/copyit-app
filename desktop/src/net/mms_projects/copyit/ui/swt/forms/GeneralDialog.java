package net.mms_projects.copyit.ui.swt.forms;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Shell;

public abstract class GeneralDialog extends Dialog {

	public GeneralDialog(Shell parent, int style) {
		super(parent, style);
	}

	/**
	 * Open the dialog.
	 */
	abstract public void open();

	/**
	 * Create contents of the dialog.
	 */
	abstract protected void createContents();

	abstract protected void updateForm();

}
