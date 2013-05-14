package net.mms_projects.copyit.ui;

import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.mms_projects.copyit.ClipboardManager;
import net.mms_projects.copyit.OpenBrowser;
import net.mms_projects.copyit.Settings;
import net.mms_projects.copyit.SyncListener;
import net.mms_projects.copyit.SyncManager;
import net.mms_projects.copyit.api.ServerApi;
import net.mms_projects.copyit.api.endpoints.GetBuildInfo;
import net.mms_projects.copyit.api.responses.JenkinsBuildResponse;
import net.mms_projects.copyit.app.CopyItDesktop;
import net.mms_projects.copyit.clipboard_backends.SwtBackend;
import net.mms_projects.copyit.ui.swt.TrayEntry;
import net.mms_projects.copyit.ui.swt.TrayEntrySwt;
import net.mms_projects.copyit.ui.swt.TrayEntryUnity;
import net.mms_projects.copyit.ui.swt.forms.DataQueue;
import net.mms_projects.copyit.ui.swt.forms.PreferencesDialog;
import net.mms_projects.utils.OSValidator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;

public class SwtGui extends AbstractUi {

	protected DataQueue queueWindow;
	protected Display display;
	protected Tray tray;

	// The system tray icon
	protected TrayEntry trayEntry;

	protected Shell activityShell;

	protected SyncManager syncManager;
	protected ClipboardManager clipboardManager;

	public SwtGui(Settings settings, SyncManager syncManager,
			ClipboardManager clipboardManager) {
		super(settings);

		this.syncManager = syncManager;
		this.clipboardManager = clipboardManager;
		
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
		SwtBackend swtClipboard = new SwtBackend(this.clipboardManager);
		
		this.clipboardManager.addCopyService(swtClipboard);
		this.clipboardManager.addPasteService(swtClipboard);
		this.clipboardManager.addPollingService(swtClipboard);
		
		this.tray = display.getSystemTray();
		this.activityShell = new Shell(this.display);

		if (OSValidator.isUnix()) {
			String desktop = System.getenv("XDG_CURRENT_DESKTOP");
			if (desktop.equalsIgnoreCase("Unity")) {
				this.trayEntry = new TrayEntryUnity(this.settings,
						this.activityShell, syncManager, clipboardManager);
			}
		}
		if (this.trayEntry == null) {
			this.trayEntry = new TrayEntrySwt(this.settings,
					this.activityShell, this.tray, syncManager,
					clipboardManager);
		}
		this.queueWindow = new DataQueue(this.activityShell, SWT.DIALOG_TRIM,
				this.clipboardManager);
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

		syncManager.addListener(new SyncListener() {

			@Override
			public void onPushed(String content, Date date) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPulled(final String content, Date date) {
				if (!SwtGui.this.settings.getBoolean("sync.queue.enabled")) {
					clipboardManager.setContent(content);
				}
			}
		});
		syncManager.addListener(queueWindow);
		syncManager.addListener(this.trayEntry);

		this.queueWindow.setup();
		this.queueWindow.setEnabled(this.settings
				.getBoolean("sync.queue.enabled"));

		this.settings.addListener("sync.queue.enabled", this.queueWindow);

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
