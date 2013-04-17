package net.mms_projects.copyit.ui.swt.forms.login_dialogs;

import java.util.UUID;

import net.mms_projects.copyit.LoginResponse;
import net.mms_projects.copyit.Messages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class LoginDialog extends AbstractLoginDialog {

	public LoginDialog(Shell parent) {
		super(parent);

		setText(Messages.getString("title_activity_login"));
	}

	@Override
	protected void createContents() {
		/*
		 * Definitions
		 */

		// Shell
		this.shell = new Shell(this.getParent());
		// Row 1
		Label lblDeviceId = new Label(this.shell, SWT.NONE);
		final Text textDeviceId = new Text(this.shell, SWT.BORDER);
		// Row 2
		Label lblGeneratedPassword = new Label(this.shell, SWT.NONE);
		Text textDevicePassword = new Text(this.shell, SWT.BORDER);
		// Row 3
		Button btnDone = new Button(this.shell, SWT.NONE);

		/*
		 * Layout and settings
		 */

		// Window
		this.shell.setLayout(new GridLayout(2, false));
		this.shell.setSize(800, 600);
		this.shell.setText(getText());
		// Row 1
		lblDeviceId.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblDeviceId.setText("Device id");
		textDeviceId.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		// Row 2
		lblGeneratedPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		lblGeneratedPassword.setText("Generated password");
		textDevicePassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		textDevicePassword.setText(this.getPassword());
		// Row 3
		btnDone.setText("Done");

		/*
		 * Listeners
		 */

		// Done button
		btnDone.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				LoginResponse response = new LoginResponse();
				response.deviceId = UUID.fromString(textDeviceId.getText());
				response.devicePassword = LoginDialog.this.getPassword();
				LoginDialog.this.setResponse(response);

			}
		});
	}

	@Override
	protected void updateForm() {
		// TODO Auto-generated method stub
		
	}

}
