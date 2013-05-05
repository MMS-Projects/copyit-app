package net.mms_projects.copyit.ui.swt.forms;

import net.mms_projects.copyit.AndroidResourceLoader;
import net.mms_projects.copyit.Messages;
import net.mms_projects.copyit.app.CopyItDesktop;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class AboutDialog extends Dialog {

	protected Object result;
	protected Shell shell;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public AboutDialog(Shell parent, int style) {
		super(parent, style);
		setText(Messages.getString("title_activity_about"));
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM);
		shell.setSize(300, 250);
		shell.setText(getText());
		shell.setLayout(new FormLayout());

		Label appIcon = new Label(shell, SWT.NONE);
		appIcon.setImage(AndroidResourceLoader
				.getImage("drawable-xhdpi/ic_launcher.png"));
		Label appName = new Label(shell, SWT.NONE);
		appName.setAlignment(SWT.CENTER);
		Label appVersion = new Label(shell, SWT.NONE);
		appVersion.setAlignment(SWT.CENTER);
		Label appCopyright = new Label(shell, SWT.NONE);
		appCopyright.setAlignment(SWT.CENTER);
		Button btnClose = new Button(shell, SWT.NONE);
		
		FormData fd_appIcon = new FormData();
		fd_appIcon.top = new FormAttachment(0, 10);
		fd_appIcon.left = new FormAttachment(50, -50);
		fd_appIcon.right = new FormAttachment(50, 50);
		appIcon.setLayoutData(fd_appIcon);
		
		FormData fd_appName = new FormData();
		
		fd_appName.top = new FormAttachment(appIcon, 10);
		fd_appName.left = new FormAttachment(0, SWT.LEFT, 10);
		fd_appName.right = new FormAttachment(0, 283);
		appName.setLayoutData(fd_appName);
		appName.setText(Messages.getString("app_name"));
		
		FormData fd_appVersion = new FormData();
		fd_appVersion.left = new FormAttachment(appName, 0, SWT.LEFT);
		fd_appVersion.right = new FormAttachment(appName, 0, SWT.RIGHT);
		fd_appVersion.top = new FormAttachment(appName, 6);
		appVersion.setLayoutData(fd_appVersion);
		appVersion.setText(Messages.getString("about_version", CopyItDesktop.getVersion()));

		FormData fd_appCopyright = new FormData();
		fd_appCopyright.left = new FormAttachment(appName, 0, SWT.LEFT);
		fd_appCopyright.top = new FormAttachment(appVersion, 6);
		fd_appCopyright.right = new FormAttachment(appName, 0, SWT.RIGHT);
		appCopyright.setLayoutData(fd_appCopyright);
		appCopyright.setText(Messages.getString("about_copyright"));
		
		FormData fd_btnClose = new FormData();
		fd_btnClose.bottom = new FormAttachment(100, -10);
		fd_btnClose.right = new FormAttachment(appIcon, -7);
		fd_btnClose.left = new FormAttachment(appName, 0, SWT.LEFT);
		btnClose.setLayoutData(fd_btnClose);
		btnClose.setText("Close");
		btnClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				AboutDialog.this.shell.close();
			}
		});
	}
}
