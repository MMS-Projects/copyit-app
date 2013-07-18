package net.mms_projects.copy_it.ui;

import net.mms_projects.copy_it.Activatable;
import net.mms_projects.copy_it.ClipboardManager;
import net.mms_projects.copy_it.Config;
import net.mms_projects.copy_it.EnvironmentIntegration;
import net.mms_projects.copy_it.FunctionalityManager;
import net.mms_projects.copy_it.SyncManager;
import net.mms_projects.copy_it.app.CopyItDesktop;
import net.mms_projects.copy_it.integration.DefaultLinuxIntegration;
import net.mms_projects.copy_it.integration.SwtIntegration;
import net.mms_projects.copy_it.integration.UnityIntegration;
import net.mms_projects.copy_it.integration.WindowsIntegration;
import net.mms_projects.copy_it.linux.DesktopEnvironment;
import net.mms_projects.copy_it.listeners.EnabledListener;
import net.mms_projects.copy_it.ui.swt.forms.DataQueue;
import net.mms_projects.copy_it.ui_old.AbstractUi;
import net.mms_projects.utils.OSValidator;

import org.eclipse.swt.SWT;

public class SwtGui extends AbstractUi {

	private UserInterfaceImplementation uiImplementation;
	private FunctionalityManager<Activatable> functionality;
	private EnvironmentIntegration environmentIntegration;

	public SwtGui(final Config settings,
			FunctionalityManager<Activatable> functionality,
			SyncManager syncManager, ClipboardManager clipboardManager) {
		super(settings);

		this.functionality = functionality;

		EnvironmentIntegration environmentIntegration = null;

		if (OSValidator.isUnix()) {
			switch (DesktopEnvironment.getDesktopEnvironment()) {
			case Unity:
				UnityIntegration environmentIntegrationUnity = new UnityIntegration(
						CopyItDesktop.dbusConnection, this.functionality,
						syncManager, clipboardManager);
				syncManager.addListener(environmentIntegrationUnity);
				clipboardManager.addListener(environmentIntegrationUnity);

				environmentIntegration = environmentIntegrationUnity;
				break;
			default:
				environmentIntegration = new DefaultLinuxIntegration(
						CopyItDesktop.dbusConnection, this.functionality,
						syncManager, clipboardManager);
				break;
			}
		} else if (OSValidator.isWindows()) {
			environmentIntegration = new WindowsIntegration(this.functionality,
					syncManager, clipboardManager);
		}
		if (environmentIntegration == null) {
			environmentIntegration = new SwtIntegration(this.functionality,
					syncManager, clipboardManager);
		}

		this.uiImplementation = new SwtInterface(settings, functionality,
				environmentIntegration, syncManager, clipboardManager);

		environmentIntegration
				.setUserInterfaceImplementation(this.uiImplementation);

		environmentIntegration.setup();

		this.environmentIntegration = environmentIntegration;
	}

	@Override
	public void open() {
		this.uiImplementation.open();
	}
}
