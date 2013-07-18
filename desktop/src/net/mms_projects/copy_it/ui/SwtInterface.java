package net.mms_projects.copy_it.ui;

import net.mms_projects.copy_it.Activatable;
import net.mms_projects.copy_it.Config;
import net.mms_projects.copy_it.EnvironmentIntegration;
import net.mms_projects.copy_it.FunctionalityManager;
import net.mms_projects.copy_it.ui.swt.forms.PreferencesDialog;

import org.eclipse.swt.widgets.Shell;

public class SwtInterface implements UserInterfaceImplementation {

	private SettingsUserInterface settingsUserInterface;

	public SwtInterface(Shell activeShell, Config settings,
			FunctionalityManager<Activatable> functionality,
			EnvironmentIntegration environmentIntegration) {
		this.setSettingsUserInterface(new PreferencesDialog(activeShell,
				settings, functionality, environmentIntegration));
	}

	@Override
	public void setSettingsUserInterface(
			SettingsUserInterface settingsUserInterface) {
		this.settingsUserInterface = settingsUserInterface;
	}

	@Override
	public SettingsUserInterface getSettingsUserInterface() {
		return this.settingsUserInterface;
	}

}
