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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class PreferencesDialog extends GeneralDialog {

	protected Shell shell;
	
	private Settings settings;

	private Text textEncryptionPassphrase;
	private Label lblDeviceIdHere;
	private Button btnLogin;
	private Button btnManualLogin;

	private Button btnEnablePolling;

	private Button btnEnableQueue;

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
	protected void createContents() {
		/*
		 * Definitions
		 */

		// Shell
		this.shell = new Shell(this.getParent(), SWT.DIALOG_TRIM);
		shell.setLayout(new FormLayout());
		// Elements
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		FormData fd_tabFolder = new FormData();
		fd_tabFolder.left = new FormAttachment(0, 10);
		fd_tabFolder.right = new FormAttachment(100, -6);
		fd_tabFolder.top = new FormAttachment(0, 10);
		fd_tabFolder.bottom = new FormAttachment(0, 358);
		tabFolder.setLayoutData(fd_tabFolder);
		Button btnClose = new Button(shell, SWT.NONE);
		FormData fd_btnClose = new FormData();
		fd_btnClose.top = new FormAttachment(tabFolder, 6);
		fd_btnClose.right = new FormAttachment(tabFolder, 0, SWT.RIGHT);
		fd_btnClose.left = new FormAttachment(0, 457);
		btnClose.setLayoutData(fd_btnClose);
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
		// Sync tab
		TabItem tbtmSync = new TabItem(tabFolder, SWT.NONE);
		Composite compositeSync = new Composite(tabFolder, SWT.NONE);
		btnEnablePolling = new Button(compositeSync, SWT.CHECK);
		btnEnableQueue = new Button(compositeSync, SWT.CHECK);
		
		/*
		 * Layout and settings
		 */

		// Shell
		this.shell.setSize(552, 434);
		this.shell.setText(getText());
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
		// Sync tab
		tbtmSync.setText("Sync");
		tbtmSync.setControl(compositeSync);
		btnEnablePolling.setBounds(10, 10, 168, 24);
		btnEnablePolling.setText(Messages.getString("PreferencesDialog.btnEnablePolling.text")); //$NON-NLS-1$
		btnEnableQueue.setBounds(10, 40, 115, 24);
		btnEnableQueue.setText(Messages.getString("PreferencesDialog.btnEnableQueue.text")); //$NON-NLS-1$

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
		// Sync tab
		btnEnablePolling.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				PreferencesDialog.this.settings.set("sync.polling.enabled", btnEnablePolling.getSelection());
				PreferencesDialog.this.updateForm();
			}
		});
		btnEnableQueue.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				PreferencesDialog.this.settings.set("sync.queue.enabled", btnEnableQueue.getSelection());
				PreferencesDialog.this.updateForm();
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
		btnEnablePolling.setSelection(this.settings.getBoolean("sync.polling.enabled"));
		btnEnableQueue.setSelection(this.settings.getBoolean("sync.queue.enabled"));
		btnEnableQueue.setEnabled(this.settings.getBoolean("sync.polling.enabled"));
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
