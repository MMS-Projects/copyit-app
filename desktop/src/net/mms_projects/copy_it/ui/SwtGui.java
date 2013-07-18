package net.mms_projects.copy_it.ui;

import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.mms_projects.copy_it.Activatable;
import net.mms_projects.copy_it.ClipboardManager;
import net.mms_projects.copy_it.Config;
import net.mms_projects.copy_it.EnvironmentIntegration;
import net.mms_projects.copy_it.FunctionalityManager;
import net.mms_projects.copy_it.Messages;
import net.mms_projects.copy_it.OpenBrowser;
import net.mms_projects.copy_it.SyncListener;
import net.mms_projects.copy_it.SyncManager;
import net.mms_projects.copy_it.api.ServerApi;
import net.mms_projects.copy_it.api.endpoints.GetBuildInfo;
import net.mms_projects.copy_it.api.responses.JenkinsBuildResponse;
import net.mms_projects.copy_it.app.CopyItDesktop;
import net.mms_projects.copy_it.clipboard_backends.SwtBackend;
import net.mms_projects.copy_it.integration.DefaultLinuxIntegration;
import net.mms_projects.copy_it.integration.SwtIntegration;
import net.mms_projects.copy_it.integration.UnityIntegration;
import net.mms_projects.copy_it.integration.WindowsIntegration;
import net.mms_projects.copy_it.linux.DesktopEnvironment;
import net.mms_projects.copy_it.ui.swt.forms.DataQueue;
import net.mms_projects.copy_it.ui.swt.forms.PreferencesDialog;
import net.mms_projects.utils.OSValidator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwtGui extends AbstractUi {

	protected DataQueue queueWindow;
	protected Display display;
	protected Shell activityShell;

	protected SyncManager syncManager;
	protected ClipboardManager clipboardManager;

	protected EnvironmentIntegration environmentIntegration;

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private FunctionalityManager<Activatable> functionality;

	public SwtGui(Config settings, SyncManager syncManager,
			ClipboardManager clipboardManager) {
		super(settings);

		this.syncManager = syncManager;
		this.clipboardManager = clipboardManager;

		this.functionality = new FunctionalityManager<Activatable>();

		try {
			this.display = Display.getDefault();
		} catch (UnsatisfiedLinkError error) {
			error.printStackTrace();

			String message = Messages.getString("text.error.swt_loading",
					error.getMessage());
			JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		SwtBackend swtClipboard = new SwtBackend(this.clipboardManager);

		this.clipboardManager.addCopyService(swtClipboard);
		this.clipboardManager.addPasteService(swtClipboard);
		this.clipboardManager.addPollingService(swtClipboard);

		this.activityShell = new Shell(this.display);

		EnvironmentIntegration environmentIntegration = null;

		if (OSValidator.isUnix()) {
			switch (DesktopEnvironment.getDesktopEnvironment()) {
			case Unity:
				UnityIntegration environmentIntegrationUnity = new UnityIntegration(
						CopyItDesktop.dbusConnection, this.settings,
						this.functionality, this.activityShell, syncManager,
						clipboardManager);
				syncManager.addListener(environmentIntegrationUnity);
				clipboardManager.addListener(environmentIntegrationUnity);

				environmentIntegration = environmentIntegrationUnity;
				break;
			default:
				environmentIntegration = new DefaultLinuxIntegration(
						CopyItDesktop.dbusConnection, this.settings,
						this.functionality, this.activityShell, syncManager,
						clipboardManager);
				break;
			}
		} else if (OSValidator.isWindows()) {
			environmentIntegration = new WindowsIntegration(settings,
					this.functionality, activityShell, syncManager,
					clipboardManager);
		}
		if (environmentIntegration == null) {
			environmentIntegration = new SwtIntegration(this.settings,
					this.functionality, this.activityShell, syncManager,
					clipboardManager);
		}

		environmentIntegration.setup();

		this.environmentIntegration = environmentIntegration;

		this.queueWindow = new DataQueue(this.activityShell, SWT.DIALOG_TRIM,
				this.clipboardManager);
	}

	@Override
	public void open() {
		if (this.settings.get("run.firsttime") == null) {
			MessageBox firstTimer = new MessageBox(this.activityShell);
			firstTimer.setMessage(Messages.getString("text.firstrun"));
			firstTimer.open();

			new PreferencesDialog(this.activityShell, this.settings,
					this.functionality, this.environmentIntegration).open();
			this.settings.set("run.firsttime", "nope");
		}

		// this.checkVersion();

		syncManager.addListener(new SyncListener() {
			@Override
			public void onPushed(String content, Date date) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPulled(final String content, Date date) {
				if (!SwtGui.this.settings.getBoolean("sync.queue.enabled")) {
					clipboardManager.requestSet(content);
				}
			}
		});
		syncManager.addListener(queueWindow);

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
						log.info("A new version is available: {} ",
								latestBuildNumber);
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
