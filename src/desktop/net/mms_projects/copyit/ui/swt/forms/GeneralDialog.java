package net.mms_projects.copyit.ui.swt.forms;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public abstract class GeneralDialog extends Dialog {

	protected Shell shell;

	public GeneralDialog(Shell parent, int style) {
		super(parent, style);
	}

	/**
	 * Open the dialog.
	 */
	public void open() {
		this.createContents();
		this.updateForm();

		this.shell.open();
		this.shell.layout();
		Display display = getParent().getDisplay();
		while (!this.shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

	}

	/**
	 * Create contents of the dialog.
	 */
	abstract protected void createContents();

	abstract protected void updateForm();

}
