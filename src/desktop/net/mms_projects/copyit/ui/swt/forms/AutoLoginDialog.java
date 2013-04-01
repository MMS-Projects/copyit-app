package net.mms_projects.copyit.ui.swt.forms;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import net.mms_projects.copyit.LoginResponse;
import net.mms_projects.copyit.PasswordGenerator;
import net.mms_projects.copyit.Settings;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class AutoLoginDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	protected LoginResponse response = new LoginResponse();
	private Browser browser;
	protected Settings settings;

	public AutoLoginDialog(Shell parent, Settings settings) {
		super(parent, SWT.DIALOG_TRIM | SWT.MIN | SWT.PRIMARY_MODAL);
		
		this.settings = settings;
		this.setText("Automatic setup");
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
		
		response.devicePassword = generator.generatePassword();
		
		shell = new Shell(getParent());
		shell.setSize(800, 600);
		shell.setText(getText());
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		browser = new Browser(shell, SWT.NONE);
		browser.setUrl(this.settings.get("server.baseurl") + "/app-setup/setup?device_password=" + response.devicePassword);
		browser.addLocationListener(new LocationListener() {
			@Override
			public void changing(LocationEvent event) {
				URL location = null;
				try {
					location = new URL(event.location);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				
				System.out.println(location.getPath());
				
				if (location.getPath().startsWith("/app-setup/done/")) {
					String deviceId = location.getPath().substring(16);
					response.deviceId = UUID.fromString(deviceId);
					shell.close();
				}
			}
			
			@Override
			public void changed(LocationEvent arg0) {
			}
		});
		
	}
	
}
