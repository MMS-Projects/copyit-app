package net.mms_projects.copyit.forms;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore.PasswordProtection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.mms_projects.copyit.PasswordGenerator;
import net.mms_projects.copyit.dialog_response.LoginResponse;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class LoginDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	protected LoginResponse response = new LoginResponse();
	protected String loginUrl = "https://www.facebook.com/dialog/oauth?client_id=560584000620528&display=popup&redirect_uri=https://www.facebook.com/connect/login_success.html&response_type=token";
	private Text text;
	private Text text_1;
	private Button btnDone;

	public LoginDialog(Shell parent) {
		super(parent, SWT.DIALOG_TRIM | SWT.MIN | SWT.PRIMARY_MODAL);
		
		this.setText("Facebook login");
	}
	
	
	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public LoginResponse open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return this.response;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		PasswordGenerator generator = new PasswordGenerator();
		
		shell = new Shell(getParent());
		shell.setSize(800, 600);
		shell.setText(getText());
		shell.setLayout(new GridLayout(2, false));
		
		Label lblDeviceId = new Label(shell, SWT.NONE);
		lblDeviceId.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDeviceId.setText("Device id");
		
		text = new Text(shell, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblGeneratedPassword = new Label(shell, SWT.NONE);
		lblGeneratedPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblGeneratedPassword.setText("Generated password");
		
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text_1.setText(generator.generatePassword());
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		btnDone = new Button(shell, SWT.NONE);
		btnDone.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				response.deviceId = UUID.fromString(text.getText());
				response.devicePassword = text_1.getText();
				shell.close();
			}
		});
		btnDone.setText("Done");

		
	}
	
}
