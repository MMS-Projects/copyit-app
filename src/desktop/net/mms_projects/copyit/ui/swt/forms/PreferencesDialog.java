package net.mms_projects.copyit.ui.swt.forms;

import net.mms_projects.copyit.LoginResponse;
import net.mms_projects.copyit.Settings;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

public class PreferencesDialog extends Dialog {

	protected Shell shell;

	private Text textEncryptionPassphrase;
	private Settings settings;
	private Label lblDeviceIdHere;
	private Button btnLogin;

	private Button btnManualLogin;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param Settings the settings
	 */
	public PreferencesDialog(Shell parent, Settings settings) {
		super(parent, SWT.DIALOG_TRIM);

		this.settings = settings;

		setText("Preferences");
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

		this.settings.saveProperties();
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM);
		shell.setSize(558, 403);
		shell.setText(getText());

		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(10, 10, 538, 348);

		TabItem tbtmAccount = new TabItem(tabFolder, SWT.NONE);
		tbtmAccount.setText("Account");

		Composite compositeAccount = new Composite(tabFolder, SWT.NONE);
		tbtmAccount.setControl(compositeAccount);
		compositeAccount.setLayout(null);

		Label lblAccountName = new Label(compositeAccount, SWT.NONE);
		lblAccountName.setBounds(10, 10, 160, 17);
		lblAccountName.setText("Account name:");

		Label lblAccountNameHere = new Label(compositeAccount, SWT.NONE);
		lblAccountNameHere.setBounds(200, 10, 180, 17);
		lblAccountNameHere.setText("Account name here...");

		Label lblDeviceId = new Label(compositeAccount, SWT.NONE);
		lblDeviceId.setBounds(10, 33, 70, 17);
		lblDeviceId.setText("Device id:");

		lblDeviceIdHere = new Label(compositeAccount, SWT.NONE);
		lblDeviceIdHere.setBounds(200, 33, 180, 17);
		lblDeviceIdHere.setText("Device id here...");

		btnLogin = new Button(compositeAccount, SWT.NONE);
		btnLogin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				AutoLoginDialog dialog = new AutoLoginDialog(shell,
						PreferencesDialog.this.settings);
				LoginResponse response = dialog.open();

				if (response == null) {
					System.out.println("No login response returned.");
					return;
				}

				System.out.println(response.deviceId);

				if (response.deviceId != null) {
					System.out.println(response.deviceId.toString());
				}
				System.out.println(response.devicePassword);

				PreferencesDialog.this.settings.set("device.id",
						response.deviceId.toString());
				PreferencesDialog.this.settings.set("device.password",
						response.devicePassword);

				PreferencesDialog.this.updateForm();
			}
		});
		btnLogin.setBounds(200, 56, 158, 29);
		btnLogin.setText("Login");
		
		btnManualLogin = new Button(compositeAccount, SWT.NONE);
		btnManualLogin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				LoginDialog dialog = new LoginDialog(shell);
				LoginResponse response = dialog.open();

				if (response == null) {
					System.out.println("No login response returned.");
					return;
				}

				System.out.println(response.deviceId);

				if (response.deviceId != null) {
					System.out.println(response.deviceId.toString());
				}
				System.out.println(response.devicePassword);

				PreferencesDialog.this.settings.set("device.id",
						response.deviceId.toString());
				PreferencesDialog.this.settings.set("device.password",
						response.devicePassword);

				PreferencesDialog.this.updateForm();
			}
		});
		btnManualLogin.setBounds(364, 56, 160, 29);
		btnManualLogin.setText("Manual login ");

		TabItem tbtmSecurity = new TabItem(tabFolder, SWT.NONE);
		tbtmSecurity.setText("Security");

		Composite compositeSecurity = new Composite(tabFolder, SWT.NONE);
		tbtmSecurity.setControl(compositeSecurity);

		final Button btnEnableLocalEncryption = new Button(compositeSecurity,
				SWT.CHECK);
		btnEnableLocalEncryption.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				textEncryptionPassphrase.setEnabled(btnEnableLocalEncryption
						.getSelection());
			}
		});
		btnEnableLocalEncryption.setBounds(10, 10, 184, 24);
		btnEnableLocalEncryption.setText("Enable local encryption");

		Label lblEncryptionPassphrase = new Label(compositeSecurity, SWT.NONE);
		lblEncryptionPassphrase.setBounds(10, 44, 184, 17);
		lblEncryptionPassphrase.setText("Encryption passphrase:");

		textEncryptionPassphrase = new Text(compositeSecurity, SWT.BORDER);
		textEncryptionPassphrase.setBounds(200, 40, 200, 27);

		Button btnClose = new Button(shell, SWT.NONE);
		btnClose.setBounds(457, 364, 91, 29);
		btnClose.setText("Close");
	}

	public void updateForm() {
		if (settings.get("device.id") != null) {
			this.lblDeviceIdHere.setText(settings.get("device.id"));
		} else {
			this.lblDeviceIdHere.setText("None");
		}

		if (settings.get("device.id") != null) {
			btnLogin.setText("Relogin");
		}
		if (settings.get("device.id") != null) {
			btnManualLogin.setText("Relogin (manual)");
		}
	}
}
