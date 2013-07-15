package net.mms_projects.copy_it.ui.swt.forms;

import net.mms_projects.copy_it.EnvironmentIntegration;
import net.mms_projects.copy_it.EnvironmentIntegration.AutostartManager.AutoStartSetupException;
import net.mms_projects.copy_it.LoginResponse;
import net.mms_projects.copy_it.Messages;
import net.mms_projects.copy_it.Settings;
import net.mms_projects.copy_it.api.ServerApi;
import net.mms_projects.copy_it.api.endpoints.DeviceEndpoint;
import net.mms_projects.copy_it.ui.swt.forms.login_dialogs.AbstractLoginDialog;
import net.mms_projects.copy_it.ui.swt.forms.login_dialogs.AutoLoginDialog;
import net.mms_projects.copy_it.ui.swt.forms.login_dialogs.LoginDialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PreferencesDialog extends GeneralDialog {

	protected Shell shell;

	private Settings settings;
	private EnvironmentIntegration environmentIntegration;

	private Text textEncryptionPassphrase;
	private Label lblDeviceIdHere;
	private Button btnLogin;
	private Button btnManualLogin;
	private Button btnSetupAutoStart;

	private Button btnEnablePolling;

	private Button btnEnableQueue;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param Settings
	 *            the settings
	 */
	public PreferencesDialog(Shell parent, Settings settings,
			EnvironmentIntegration environmentIntegration) {
		super(parent, SWT.DIALOG_TRIM);

		this.settings = settings;
		this.environmentIntegration = environmentIntegration;

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
		compositeAccount.setLayout(new FormLayout());
		Label lblAccountName = new Label(compositeAccount, SWT.NONE);
		FormData fd_lblAccountName = new FormData();
		fd_lblAccountName.right = new FormAttachment(0, 170);
		fd_lblAccountName.top = new FormAttachment(0, 10);
		fd_lblAccountName.left = new FormAttachment(0, 10);
		lblAccountName.setLayoutData(fd_lblAccountName);
		Label lblAccountNameHere = new Label(compositeAccount, SWT.NONE);
		FormData fd_lblAccountNameHere = new FormData();
		fd_lblAccountNameHere.right = new FormAttachment(0, 380);
		fd_lblAccountNameHere.top = new FormAttachment(0, 10);
		fd_lblAccountNameHere.left = new FormAttachment(0, 200);
		lblAccountNameHere.setLayoutData(fd_lblAccountNameHere);
		Label lblDeviceId = new Label(compositeAccount, SWT.NONE);
		FormData fd_lblDeviceId = new FormData();
		fd_lblDeviceId.right = new FormAttachment(0, 80);
		fd_lblDeviceId.top = new FormAttachment(0, 33);
		fd_lblDeviceId.left = new FormAttachment(0, 10);
		lblDeviceId.setLayoutData(fd_lblDeviceId);
		this.lblDeviceIdHere = new Label(compositeAccount, SWT.NONE);
		FormData fd_lblDeviceIdHere = new FormData();
		fd_lblDeviceIdHere.right = new FormAttachment(0, 380);
		fd_lblDeviceIdHere.top = new FormAttachment(0, 33);
		fd_lblDeviceIdHere.left = new FormAttachment(0, 200);
		lblDeviceIdHere.setLayoutData(fd_lblDeviceIdHere);
		this.btnLogin = new Button(compositeAccount, SWT.NONE);
		FormData fd_btnLogin = new FormData();
		fd_btnLogin.left = new FormAttachment(0, 200);
		fd_btnLogin.top = new FormAttachment(lblDeviceIdHere, 6);
		fd_btnLogin.bottom = new FormAttachment(0, 85);
		btnLogin.setLayoutData(fd_btnLogin);
		this.btnManualLogin = new Button(compositeAccount, SWT.NONE);
		fd_btnLogin.right = new FormAttachment(btnManualLogin, -6);
		FormData fd_btnManualLogin = new FormData();
		fd_btnManualLogin.bottom = new FormAttachment(btnLogin, 0, SWT.BOTTOM);
		fd_btnManualLogin.left = new FormAttachment(0, 364);
		fd_btnManualLogin.right = new FormAttachment(100, -10);
		fd_btnManualLogin.top = new FormAttachment(lblDeviceIdHere, 6);
		btnManualLogin.setLayoutData(fd_btnManualLogin);

		this.btnSetupAutoStart = new Button(compositeAccount, SWT.NONE);
		/*
		 * If there is a auto start manager available enable the 'Setup auto
		 * start' button
		 */
		btnSetupAutoStart.setEnabled(this.environmentIntegration
				.hasAutostartManager());
		FormData fd_btnSetupAutoStart = new FormData();
		fd_btnSetupAutoStart.bottom = new FormAttachment(btnLogin, 35,
				SWT.BOTTOM);
		fd_btnSetupAutoStart.right = new FormAttachment(btnLogin, 0, SWT.RIGHT);
		fd_btnSetupAutoStart.top = new FormAttachment(btnLogin, 6);
		fd_btnSetupAutoStart.left = new FormAttachment(lblAccountNameHere, 0,
				SWT.LEFT);
		btnSetupAutoStart.setLayoutData(fd_btnSetupAutoStart);
		// Security tab
		TabItem tbtmSecurity = new TabItem(tabFolder, SWT.NONE);
		Composite compositeSecurity = new Composite(tabFolder, SWT.NONE);
		compositeSecurity.setLayout(new FormLayout());
		final Button btnEnableLocalEncryption = new Button(compositeSecurity,
				SWT.CHECK);
		FormData fd_btnEnableLocalEncryption = new FormData();
		fd_btnEnableLocalEncryption.right = new FormAttachment(0, 194);
		fd_btnEnableLocalEncryption.top = new FormAttachment(0, 10);
		fd_btnEnableLocalEncryption.left = new FormAttachment(0, 10);
		btnEnableLocalEncryption.setLayoutData(fd_btnEnableLocalEncryption);
		Label lblEncryptionPassphrase = new Label(compositeSecurity, SWT.NONE);
		FormData fd_lblEncryptionPassphrase = new FormData();
		fd_lblEncryptionPassphrase.right = new FormAttachment(0, 194);
		fd_lblEncryptionPassphrase.top = new FormAttachment(0, 44);
		fd_lblEncryptionPassphrase.left = new FormAttachment(0, 10);
		lblEncryptionPassphrase.setLayoutData(fd_lblEncryptionPassphrase);
		this.textEncryptionPassphrase = new Text(compositeSecurity, SWT.BORDER);
		FormData fd_textEncryptionPassphrase = new FormData();
		fd_textEncryptionPassphrase.right = new FormAttachment(0, 400);
		fd_textEncryptionPassphrase.top = new FormAttachment(0, 40);
		fd_textEncryptionPassphrase.left = new FormAttachment(0, 200);
		textEncryptionPassphrase.setLayoutData(fd_textEncryptionPassphrase);
		// Sync tab
		TabItem tbtmSync = new TabItem(tabFolder, SWT.NONE);
		Composite compositeSync = new Composite(tabFolder, SWT.NONE);
		compositeSync.setLayout(new FormLayout());
		btnEnablePolling = new Button(compositeSync, SWT.CHECK);
		FormData fd_btnEnablePolling = new FormData();
		fd_btnEnablePolling.top = new FormAttachment(0, 10);
		fd_btnEnablePolling.left = new FormAttachment(0, 10);
		btnEnablePolling.setLayoutData(fd_btnEnablePolling);
		btnEnableQueue = new Button(compositeSync, SWT.CHECK);
		FormData fd_btnEnableQueue = new FormData();
		fd_btnEnableQueue.top = new FormAttachment(0, 40);
		fd_btnEnableQueue.left = new FormAttachment(0, 10);
		btnEnableQueue.setLayoutData(fd_btnEnableQueue);

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
		lblAccountName.setText("Account name:");
		lblAccountNameHere.setText("Account name here...");
		lblDeviceId.setText("Device id:");
		this.lblDeviceIdHere.setText("Device id here...");
		this.btnLogin.setText(Messages.getString("button_login"));
		this.btnManualLogin.setText("Manual login ");
		if (this.environmentIntegration.hasAutostartManager()) {
			try {
				if (!this.environmentIntegration.getAutostartManager()
						.isEnabled()) {
					this.btnSetupAutoStart.setText(Messages
							.getString("autostart.button.enable"));
				} else {
					this.btnSetupAutoStart.setText(Messages
							.getString("autostart.button.disable"));
				}
			} catch (AutoStartSetupException e) {
				e.printStackTrace();
			}
		} else {
			this.btnSetupAutoStart.setText(Messages
					.getString("autostart.button.not_available"));
		}
		// Security tab
		tbtmSecurity.setText("Security");
		tbtmSecurity.setControl(compositeSecurity);
		btnEnableLocalEncryption.setText("Enable local encryption");
		lblEncryptionPassphrase.setText("Encryption passphrase:");
		// Sync tab
		tbtmSync.setText("Sync");
		tbtmSync.setControl(compositeSync);
		btnEnablePolling.setText(Messages
				.getString("PreferencesDialog.btnEnablePolling.text"));
		btnEnableQueue.setText(Messages
				.getString("PreferencesDialog.btnEnableQueue.text")); //$NON-NLS-1$

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

		/*
		 * This listener calls the environment integration, asks for the auto
		 * start manager and sets up the auto start functionality
		 */
		this.btnSetupAutoStart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				MessageBox messageBox = new MessageBox(
						PreferencesDialog.this.shell);

				try {
					if (!environmentIntegration.getAutostartManager()
							.isEnabled()) {
						/*
						 * Setup the auto start the way it should in the current
						 * environment
						 */
						environmentIntegration.getAutostartManager()
								.enableAutostart();

						/*
						 * Tell the user the setup is done
						 */
						messageBox.setMessage(Messages
								.getString("autostart.message.enabled"));
						messageBox.open();
					} else {
						/*
						 * Setup the auto start the way it should in the current
						 * environment
						 */
						environmentIntegration.getAutostartManager()
								.disableAutostart();

						/*
						 * Tell the user the setup is done
						 */
						messageBox.setMessage(Messages
								.getString("autostart.message.disabled"));
						messageBox.open();
					}
				} catch (AutoStartSetupException e) {
					/*
					 * Show the user a nice error dialog explaining the error
					 */
					messageBox.setMessage(Messages.getString(
							"autostart.error.exception_thrown",
							e.getLocalizedMessage()));
					messageBox.open();
				}
				updateForm();
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
				PreferencesDialog.this.settings.set("sync.polling.enabled",
						btnEnablePolling.getSelection());
				PreferencesDialog.this.updateForm();
			}
		});
		btnEnableQueue.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				PreferencesDialog.this.settings.set("sync.queue.enabled",
						btnEnableQueue.getSelection());
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

		if (this.environmentIntegration.hasAutostartManager()) {
			try {
				if (!this.environmentIntegration.getAutostartManager()
						.isEnabled()) {
					this.btnSetupAutoStart.setText(Messages
							.getString("autostart.button.enable"));
				} else {
					this.btnSetupAutoStart.setText(Messages
							.getString("autostart.button.disable"));
				}
			} catch (AutoStartSetupException e) {
				e.printStackTrace();
			}
		} else {
			this.btnSetupAutoStart.setText(Messages
					.getString("autostart.button.not_available"));
		}

		btnEnablePolling.setSelection(this.settings
				.getBoolean("sync.polling.enabled"));
		btnEnableQueue.setSelection(this.settings
				.getBoolean("sync.queue.enabled"));
		btnEnableQueue.setEnabled(this.settings
				.getBoolean("sync.polling.enabled"));
	}

	private abstract class LoginSectionAdapter extends SelectionAdapter {

		abstract public AbstractLoginDialog getLoginDialog();

		@Override
		final public void widgetSelected(SelectionEvent event) {
			AbstractLoginDialog dialog = this.getLoginDialog();
			dialog.open();
			LoginResponse response = dialog.getResponse();

			if (response == null) {
				log.debug("No login response returned.");
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
