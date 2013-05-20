package net.mms_projects.copyit.ui.swt.forms.login_dialogs;

import net.mms_projects.copyit.LoginResponse;
import net.mms_projects.copyit.PasswordGenerator;
import net.mms_projects.copyit.ui.swt.forms.GeneralDialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

public abstract class AbstractLoginDialog extends GeneralDialog {

	protected Shell shell;

	private LoginResponse response;
	private String password;

	public AbstractLoginDialog(Shell parent) {
		super(parent, SWT.DIALOG_TRIM | SWT.MIN | SWT.PRIMARY_MODAL);
	}

	/**
	 * @return the result
	 */
	final public LoginResponse getResponse() {
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

}
