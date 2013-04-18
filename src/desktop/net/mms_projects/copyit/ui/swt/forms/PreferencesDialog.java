package net.mms_projects.copyit.ui.swt.forms;

import net.mms_projects.copyit.LoginResponse;
import net.mms_projects.copyit.Messages;
import net.mms_projects.copyit.Settings;
import net.mms_projects.copyit.api.ServerApi;
import net.mms_projects.copyit.api.endpoints.DeviceEndpoint;
import net.mms_projects.copyit.ui.swt.forms.login_dialogs.AbstractLoginDialog;
import net.mms_projects.copyit.ui.swt.forms.login_dialogs.AutoLoginDialog;
import net.mms_projects.copyit.ui.swt.forms.login_dialogs.LoginDialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

public class PreferencesDialog extends GeneralDialog {

	private Settings settings;

	private Text textEncryptionPassphrase;
	private Label lblDeviceIdHere;
	private Button btnLogin;
	private Button btnManualLogin;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param Settings
	 *            the settings
	 */
	public PreferencesDialog(Shell parent, Settings settings) {
		super(parent, SWT.DIALOG_TRIM);

		this.settings = settings;

		setText(Messages.getString("title_activity_settings"));
	}

	@Override
	public void open() {
		super.open();

		this.settings.saveProperties();
	}

	/**
	 * Create contents of the dialog.
	 */
	protected void createContents() {
		/*
		 * Definitions
		 */

		// Shell
		this.shell = new Shell(this.getParent(), SWT.DIALOG_TRIM);
		// Elements
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		Button btnClose = new Button(shell, SWT.NONE);
		// Account tab
		TabItem tbtmAccount = new TabItem(tabFolder, SWT.NONE);
		Composite compositeAccount = new Composite(tabFolder, SWT.NONE);
		Label lblAccountName = new Label(compositeAccount, SWT.NONE);
		Label lblAccountNameHere = new Label(compositeAccount, SWT.NONE);
		Label lblDeviceId = new Label(compositeAccount, SWT.NONE);
		this.lblDeviceIdHere = new Label(compositeAccount, SWT.NONE);
		this.btnLogin = new Button(compositeAccount, SWT.NONE);
		this.btnManualLogin = new Button(compositeAccount, SWT.NONE);
		// Security tab
		TabItem tbtmSecurity = new TabItem(tabFolder, SWT.NONE);
		Composite compositeSecurity = new Composite(tabFolder, SWT.NONE);
		final Button btnEnableLocalEncryption = new Button(compositeSecurity,
				SWT.CHECK);
		Label lblEncryptionPassphrase = new Label(compositeSecurity, SWT.NONE);
		this.textEncryptionPassphrase = new Text(compositeSecurity, SWT.BORDER);

		/*
		 * Layout and settings
		 */

		// Shell
		this.shell.setSize(558, 403);
		this.shell.setText(getText());
		// Elements
		tabFolder.setBounds(10, 10, 538, 348);
		btnClose.setBounds(457, 364, 91, 29);
		btnClose.setText("Close");
		// Account tab
		tbtmAccount.setText("Account");
		tbtmAccount.setControl(compositeAccount);
		compositeAccount.setLayout(null);
		lblAccountName.setBounds(10, 10, 160, 17);
		lblAccountName.setText("Account name:");
		lblAccountNameHere.setBounds(200, 10, 180, 17);
		lblAccountNameHere.setText("Account name here...");
		lblDeviceId.setBounds(10, 33, 70, 17);
		lblDeviceId.setText("Device id:");
		this.lblDeviceIdHere.setBounds(200, 33, 180, 17);
		this.lblDeviceIdHere.setText("Device id here...");
		this.btnLogin.setBounds(200, 56, 158, 29);
		this.btnLogin.setText(Messages.getString("button_login"));
		this.btnManualLogin.setBounds(364, 56, 160, 29);
		this.btnManualLogin.setText("Manual login ");
		// Security tab
		tbtmSecurity.setText("Security");
		tbtmSecurity.setControl(compositeSecurity);
		btnEnableLocalEncryption.setBounds(10, 10, 184, 24);
		btnEnableLocalEncryption.setText("Enable local encryption");
		lblEncryptionPassphrase.setBounds(10, 44, 184, 17);
		lblEncryptionPassphrase.setText("Encryption passphrase:");
		this.textEncryptionPassphrase.setBounds(200, 40, 200, 27);

		/*
		 * Listeners
		 */

		// Automatic login button
		this.btnLogin.addSelectionListener(new LoginSectionAdapter() {
			@Override
			public AbstractLoginDialog getLoginDialog() {
				return new AutoLoginDialog(shell,
						PreferencesDialog.this.settings);
			}
		});

		// Manual login
		this.btnManualLogin.addSelectionListener(new LoginSectionAdapter() {
			@Override
			public AbstractLoginDialog getLoginDialog() {
				return new LoginDialog(shell);
			}
		});
		// Encryption enable checkbox
		btnEnableLocalEncryption.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				textEncryptionPassphrase.setEnabled(btnEnableLocalEncryption
						.getSelection());
				PreferencesDialog.this.updateForm();
			}
		});
		// Close button
		btnClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				PreferencesDialog.this.shell.close();
			}
		});
	}

	protected void updateForm() {
		if (this.settings.get("device.id") != null) {
			this.lblDeviceIdHere.setText(this.settings.get("device.id"));
		} else {
			this.lblDeviceIdHere.setText("None");
		}

		if (this.settings.get("device.id") != null) {
			this.btnLogin.setText("Relogin");
		}
		if (this.settings.get("device.id") != null) {
			this.btnManualLogin.setText("Relogin (manual)");
		}
	}

	private abstract class LoginSectionAdapter extends SelectionAdapter {

		abstract public AbstractLoginDialog getLoginDialog();

		@Override
		final public void widgetSelected(SelectionEvent event) {
			AbstractLoginDialog dialog = this.getLoginDialog();
			dialog.open();
			LoginResponse response = dialog.getResponse();

			if (response == null) {
				System.out.println("No login response returned.");
				return;
			}

			ServerApi api = new ServerApi();
			api.deviceId = response.deviceId;
			api.devicePassword = response.devicePassword;

			try {
				new DeviceEndpoint(api).create("Interwebz Paste client");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			PreferencesDialog.this.settings.set("device.id",
					response.deviceId.toString());
			PreferencesDialog.this.settings.set("device.password",
					response.devicePassword);

			PreferencesDialog.this.updateForm();
		}

	}
}
