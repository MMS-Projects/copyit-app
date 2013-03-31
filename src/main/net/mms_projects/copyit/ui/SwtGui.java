package net.mms_projects.copyit.ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tray;

import net.mms_projects.copyit.Settings;
import net.mms_projects.copyit.ui.swt.ActionProvider;
import net.mms_projects.copyit.ui.swt.TrayEntry;
import net.mms_projects.copyit.ui.swt.forms.MainWindow;

public class SwtGui extends AbstractUi {

	protected Display display = Display.getDefault();
	protected Tray tray       = display.getSystemTray();
	
	// The system tray icon
	protected TrayEntry trayEntry = null;
	
	public SwtGui(Settings settings) {
		super(settings);
		
		ActionProvider actionProvider = new ActionProvider(this.display, settings);
		this.trayEntry = new TrayEntry(this.tray, actionProvider);
	}
	
	@Override
	public void open() {
		MainWindow mainWindow = new MainWindow();
		mainWindow.open();
	}

}
