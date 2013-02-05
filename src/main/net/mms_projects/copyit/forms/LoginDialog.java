package net.mms_projects.copyit.forms;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

public class LoginDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	protected LoginResponse response = new LoginResponse();;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public LoginDialog(Shell parent) {
		super(parent);
		setText("SWT Dialog");
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
		shell = new Shell(getParent(), getStyle());
		shell.setSize(800, 600);
		shell.setText(getText());

		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		Browser browser = new Browser(shell, SWT.NONE);
		browser.setUrl("https://www.facebook.com/dialog/oauth?client_id=560584000620528&redirect_uri=https://www.facebook.com/connect/login_success.html&response_type=token");
		browser.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		browser.addLocationListener(new LocationListener() {

			@Override
			public void changing(LocationEvent event) {
				System.out.println("Changing: " + event.location);
				if (event.location
						.startsWith("https://www.facebook.com/connect/login_success.html#access_token=")) {
					URL url;
					try {
						url = new URL(event.location);
					} catch (MalformedURLException e) {
						e.printStackTrace();
						return;
					}
					Map<String, String> arguments = LoginDialog.this.getQueryMap(url.getRef());
					if ((!arguments.containsKey("access_token")) || (!arguments.containsKey("expires_in"))) {
						return;
					}
					int expiresIn = Integer.parseInt(arguments.get("expires_in"));
					response.accessToken = arguments.get("access_token");
					response.expireDate = new Date(System.currentTimeMillis() + (expiresIn * 1000));
					response.error = 0;
					shell.close();
					shell.dispose();
				}
			}

			@Override
			public void changed(LocationEvent event) {
			}
		});

	}

	public Map<String, String> getQueryMap(String query)  
	{  
	    String[] params = query.split("&");  
	    Map<String, String> map = new HashMap<String, String>();  
	    for (String param : params)  
	    {  
	        String name = param.split("=")[0];  
	        String value = param.split("=")[1];  
	        map.put(name, value);  
	    }  
	    return map;  
	} 
	
}
