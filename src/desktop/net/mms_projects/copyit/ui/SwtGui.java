package net.mms_projects.copyit.ui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.mms_projects.copyit.OpenBrowser;
import net.mms_projects.copyit.Settings;
import net.mms_projects.copyit.api.ServerApi;
import net.mms_projects.copyit.api.endpoints.GetBuildInfo;
import net.mms_projects.copyit.api.responses.JenkinsBuildResponse;
import net.mms_projects.copyit.app.CopyItDesktop;
import net.mms_projects.copyit.ui.swt.TrayEntry;
import net.mms_projects.copyit.ui.swt.forms.PreferencesDialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;

public class SwtGui extends AbstractUi {

	protected Display display;
	protected Tray tray;

	// The system tray icon
	protected TrayEntry trayEntry;

	protected Shell activityShell;

	public SwtGui(Settings settings) {
		super(settings);

		try {
			this.display = Display.getDefault();
		} catch (UnsatisfiedLinkError error) {
			error.printStackTrace();

			String message = "It looks like there was an error while loading the SWT libraries.\n"
					+ "The following error was thrown:\n\n"
					+ error.getMessage();
			JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		this.tray = display.getSystemTray();
		this.activityShell = new Shell(this.display);

		this.trayEntry = new TrayEntry(this.tray, this.settings,
				this.activityShell);
	}

	@Override
	public void open() {
		if (this.settings.get("run.firsttime") == null) {
			MessageBox firstTimer = new MessageBox(this.activityShell);
			firstTimer
					.setMessage("It appairs this is the first time you started the app. "
							+ "The preferences will open to setup your login.");
			firstTimer.open();

			new PreferencesDialog(this.activityShell, this.settings).open();
			this.settings.set("run.firsttime", "nope");
		}

		this.checkVersion();

		while (!this.activityShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public void checkVersion() {
		if (CopyItDesktop.getBuildNumber() != 0) {
			this.display.asyncExec(new Runnable() {
				@Override
				public void run() {
					int currentBuildNumber = CopyItDesktop.getBuildNumber();
					int latestBuildNumber = 0;

					ServerApi api = new ServerApi();
					api.apiUrl = "http://jenkins.marlinc.nl";

					JenkinsBuildResponse response = null;
					try {
						response = new GetBuildInfo(api).getLatestStableBuild();
						latestBuildNumber = response.number;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (latestBuildNumber > currentBuildNumber) {
						System.out.println("Latest version: "
								+ latestBuildNumber);
						MessageBox box = new MessageBox(activityShell,
								SWT.ICON_INFORMATION | SWT.YES | SWT.NO);
						box.setMessage("There is a new version available! Click yes to download the latest version.");
						int boxResponse = box.open();
						if (boxResponse == SWT.YES) {
							OpenBrowser.openUrl(response.url);
						}
					}
				}
			});
		}
	}
}
