package net.mms_projects.copyit.ui.swt.forms;

import net.mms_projects.copyit.LoginResponse;
import net.mms_projects.copyit.PasswordGenerator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public abstract class AbstractLoginDialog extends Dialog {

	protected Shell shell;

	private LoginResponse response;
	private String password;

	public AbstractLoginDialog(Shell parent) {
		super(parent, SWT.DIALOG_TRIM | SWT.MIN | SWT.PRIMARY_MODAL);
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	final public LoginResponse open() {
		Display display = this.getParent().getDisplay();
		
		this.createContents();
		this.shell.open();
		this.shell.layout();
		while (!this.shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		if (this.response != null) {
			if (this.response.deviceId == null) {
				new Exception(
						"Response initialized without device id. Could be wrong login?")
						.printStackTrace();
				return null;
			}
		}
		return this.response;
	}

	final protected String getPassword() {
		if (this.password == null) {
			PasswordGenerator generator = new PasswordGenerator();
			this.password = generator.generatePassword();
		}
		
		return this.password;
	}

	final protected void setResponse(LoginResponse response) {
		this.response = response;
		this.shell.close();
	}

	/**
	 * Create contents of the dialog.
	 */
	abstract protected void createContents();

}
