package net.mms_projects.copy_it.ui.swt.forms.login_dialogs;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import net.mms_projects.copy_it.Config;
import net.mms_projects.copy_it.LoginResponse;
import net.mms_projects.copy_it.Messages;
import net.mms_projects.copy_it.ui.exceptions.NoBrowserEngineException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoLoginDialog extends AbstractLoginDialog {

	protected Config settings;

	private Browser browser;

	private Shell windowBuilderShell;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public AutoLoginDialog(Shell parent, Config settings) {
		super(parent);

		this.settings = settings;

		setText(Messages.getString("title_activity_login"));
	}

	/**
	 * Open the dialog.
	 */
	public void open() {
		try {
			this.createContents();
		} catch (NoBrowserEngineException exception) {
			this.log.error("No engine available to render browser pages",
					exception);

			MessageBox messageBox = new MessageBox(this.getParent());
			messageBox.setMessage(Messages.getString(
					"ui.browser.error.no-engine", exception.getMessage()));
			messageBox.open();
			return;
		}
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
		windowBuilderShell.setLayout(new FormLayout());

		try {
			browser = new Browser(windowBuilderShell, SWT.NONE);
		} catch (SWTException exception) {
			throw new NoBrowserEngineException(exception);
		}

		FormData fd_browser = new FormData();
		fd_browser.top = new FormAttachment(0, 10);
		fd_browser.left = new FormAttachment(0, 10);
		fd_browser.right = new FormAttachment(100, -10);
		browser.setLayoutData(fd_browser);
		browser.setUrl(this.settings.get("server.baseurl")
				+ "/app-setup/setup?device_password=" + this.getPassword());
		browser.addLocationListener(new LocationListener() {
			@Override
			public void changing(LocationEvent event) {
				URL location = null;
				try {
					location = new URL(event.location);
				} catch (MalformedURLException e) {
					e.printStackTrace();
					return;
				}
				System.out.println("Changing: " + location.getPath());
				if (location.getPath().startsWith("/app-setup/done/")) {
					String deviceId = location.getPath().substring(16);
					LoginResponse response = new LoginResponse();
					response.devicePassword = AutoLoginDialog.this
							.getPassword();
					response.deviceId = UUID.fromString(deviceId);
					AutoLoginDialog.this.setResponse(response);
				}
			}

			@Override
			public void changed(LocationEvent event) {
				URL location = null;
				try {
					location = new URL(event.location);
				} catch (MalformedURLException e) {
					e.printStackTrace();
					return;
				}
				System.out.println("Changed: " + location.getPath());
				if (location.getPath().startsWith("/app-setup/done/")) {
					String deviceId = location.getPath().substring(16);
					LoginResponse response = new LoginResponse();
					response.devicePassword = AutoLoginDialog.this
							.getPassword();
					response.deviceId = UUID.fromString(deviceId);
					AutoLoginDialog.this.setResponse(response);
				}
			}
		});

		this.shell = this.windowBuilderShell;

		final ProgressBar progressBar = new ProgressBar(windowBuilderShell,
				SWT.NONE);
		fd_browser.bottom = new FormAttachment(100, -30);
		FormData fd_progressBar = new FormData();
		fd_progressBar.bottom = new FormAttachment(browser, 20, SWT.BOTTOM);
		fd_progressBar.right = new FormAttachment(browser, 0, SWT.RIGHT);
		fd_progressBar.top = new FormAttachment(browser, 6);
		fd_progressBar.left = new FormAttachment(0, 10);
		progressBar.setLayoutData(fd_progressBar);

		browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
				if (event.total == 0)
					return;
				int ratio = event.current * 100 / event.total;
				progressBar.setSelection(ratio);
			}

			public void completed(ProgressEvent event) {
				progressBar.setSelection(0);
			}
		});
	}

	@Override
	protected void updateForm() {
		// TODO Auto-generated method stub

	}
}
