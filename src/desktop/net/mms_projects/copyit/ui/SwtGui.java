package net.mms_projects.copyit.ui;

import net.mms_projects.copyit.Settings;
import net.mms_projects.copyit.ui.swt.ActionProvider;
import net.mms_projects.copyit.ui.swt.TrayEntry;
import net.mms_projects.copyit.ui.swt.forms.MainWindow;
import net.mms_projects.copyit.ui.swt.forms.PreferencesDialog;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;

public class SwtGui extends AbstractUi {

	protected Display display = Display.getDefault();
	protected Tray tray = display.getSystemTray();

	// The system tray icon
	protected TrayEntry trayEntry = null;

	public SwtGui(Settings settings) {
		super(settings);

		ActionProvider actionProvider = new ActionProvider(this.display,
				settings);
		this.trayEntry = new TrayEntry(this.tray, actionProvider);
	}

	@Override
	public void open() {
		Shell shell = new Shell();
		
		MainWindow mainWindow = new MainWindow();

		if (this.settings.get("device.id") == null) {
			MessageBox firstTimer = new MessageBox(shell);
			firstTimer
					.setMessage("It appairs this is the first time you started the app. "
							+ "The preferences will open to setup your login.");
			firstTimer.open();
			
			new PreferencesDialog(shell, this.settings).open();
		}

		mainWindow.open();
	}

}
