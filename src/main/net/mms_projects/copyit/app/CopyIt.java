package net.mms_projects.copyit.app;

import net.mms_projects.copyit.ActionProvider;
import net.mms_projects.copyit.TrayEntry;
import net.mms_projects.copyit.forms.MainWindow;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;

public class CopyIt {

	protected Display display;
	protected Tray systemTray;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CopyIt app = new CopyIt();
		app.run();
	}

	public CopyIt() {
		this.display = Display.getDefault();
		this.systemTray = display.getSystemTray();
	}

	public void run() {
		ActionProvider actionProvider = new ActionProvider(display);

		new TrayEntry(this.systemTray, actionProvider);

		MainWindow mainWindow = new MainWindow();
		mainWindow.open();
	}

}
