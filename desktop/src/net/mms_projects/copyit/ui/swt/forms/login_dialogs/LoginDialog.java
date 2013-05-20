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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class LoginDialog extends AbstractLoginDialog {

	protected Object result;
	protected Shell windowBuilderShell;
	private Text text;
	private Text text_1;
	private Button btnDone;

	public LoginDialog(Shell parent) {
		super(parent);

		setText(Messages.getString("title_activity_login"));
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
	@Override
	protected void createContents() {
		this.windowBuilderShell = new Shell(getParent());
		this.windowBuilderShell.setSize(800, 600);
		this.windowBuilderShell.setText(getText());
		this.windowBuilderShell.setLayout(new GridLayout(2, false));

		Label lblDeviceId = new Label(this.windowBuilderShell, SWT.NONE);
		lblDeviceId.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblDeviceId.setText("Device id");

		text = new Text(this.windowBuilderShell, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblGeneratedPassword = new Label(this.windowBuilderShell,
				SWT.NONE);
		lblGeneratedPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		lblGeneratedPassword.setText("Generated password");

		text_1 = new Text(this.windowBuilderShell, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));
		text_1.setText(this.getPassword());
		new Label(this.windowBuilderShell, SWT.NONE);
		new Label(this.windowBuilderShell, SWT.NONE);
		new Label(this.windowBuilderShell, SWT.NONE);

		btnDone = new Button(this.windowBuilderShell, SWT.NONE);
		btnDone.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				LoginResponse response = new LoginResponse();
				response.devicePassword = LoginDialog.this.getPassword();
				response.deviceId = UUID.fromString(text.getText());
				LoginDialog.this.setResponse(response);
			}
		});
		btnDone.setText("Done");
		this.shell = this.windowBuilderShell;
	}

	@Override
	protected void updateForm() {
		// TODO Auto-generated method stub

	}

}
