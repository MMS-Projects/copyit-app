package net.mms_projects.copyit.ui.swt.forms.login_dialogs;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import net.mms_projects.copyit.LoginResponse;
import net.mms_projects.copyit.Messages;
import net.mms_projects.copyit.Settings;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class AutoLoginDialog extends AbstractLoginDialog {

	protected Settings settings;

	private Browser browser;

	private Shell windowBuilderShell;

	public AutoLoginDialog(Shell parent, Settings settings) {
		super(parent);

		this.settings = settings;

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
		this.windowBuilderShell.setLayout(new FillLayout(SWT.HORIZONTAL));

		browser = new Browser(windowBuilderShell, SWT.NONE);
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
				System.out.println(location.getPath());
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

			}
		});
		this.shell = this.windowBuilderShell;
	}

	@Override
	protected void updateForm() {
		// TODO Auto-generated method stub

	}
}
