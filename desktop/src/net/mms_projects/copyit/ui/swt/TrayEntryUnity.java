package net.mms_projects.copyit.ui.swt;

import java.io.IOException;
import java.util.UUID;

import net.mms_projects.copyit.ClipboardUtils;
import net.mms_projects.copyit.DesktopClipboardUtils;
import net.mms_projects.copyit.DesktopIntegration;
import net.mms_projects.copyit.Settings;
import net.mms_projects.copyit.api.ServerApi;
import net.mms_projects.copyit.api.endpoints.ClipboardContentEndpoint;
import net.mms_projects.copyit.app.CopyItDesktop;
import net.mms_projects.copyit.ui.swt.forms.AboutDialog;
import net.mms_projects.copyit.ui.swt.forms.PreferencesDialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.freedesktop.dbus.DBusSigHandler;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.exceptions.DBusException;

public class TrayEntryUnity extends TrayEntry implements DBusSigHandler {

	public TrayEntryUnity(Settings settings, Shell activityShell) {
		super(settings, activityShell);

		try {
			CopyItDesktop.dbusConnection.addSigHandler(
					DesktopIntegration.action_pull.class, this);
			CopyItDesktop.dbusConnection.addSigHandler(
					DesktopIntegration.action_push.class, this);
			CopyItDesktop.dbusConnection.addSigHandler(
					DesktopIntegration.action_open_preferences.class, this);
			CopyItDesktop.dbusConnection.addSigHandler(
					DesktopIntegration.action_open_about.class, this);
			CopyItDesktop.dbusConnection.addSigHandler(
					DesktopIntegration.action_quit.class, this);
		} catch (DBusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String[] command = new String[2];
		command[0] = "python";
		command[1] = "scripts/desktop-integration.py";
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void handle(DBusSignal signal) {
		if (signal instanceof DesktopIntegration.action_push) {
			final ServerApi api = new ServerApi();
			api.deviceId = UUID.fromString(this.settings.get("device.id"));
			api.devicePassword = this.settings.get("device.password");
			api.apiUrl = this.settings.get("server.baseurl");

			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					ClipboardUtils clipboard = new DesktopClipboardUtils();
					String data = clipboard.getText();
					if (data != null) {
						try {
							new ClipboardContentEndpoint(api).update(data);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});

		} else if (signal instanceof DesktopIntegration.action_pull) {
			final ServerApi api = new ServerApi();
			api.deviceId = UUID.fromString(this.settings.get("device.id"));
			api.devicePassword = this.settings.get("device.password");
			api.apiUrl = this.settings.get("server.baseurl");

			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					ClipboardUtils clipboard = new DesktopClipboardUtils();
					String data;
					try {
						data = new ClipboardContentEndpoint(api).get();
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
					clipboard.setText(data);
				}
			});
		} else if (signal instanceof DesktopIntegration.action_open_preferences) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					new PreferencesDialog(TrayEntryUnity.this.activityShell,
							TrayEntryUnity.this.settings).open();
				}
			});
		} else if (signal instanceof DesktopIntegration.action_open_about) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					new AboutDialog(TrayEntryUnity.this.activityShell, SWT.NONE)
							.open();
				}
			});
		} else if (signal instanceof DesktopIntegration.action_quit) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					TrayEntryUnity.this.activityShell.close();
					System.exit(0);
				}
			});
		}
	}

}
