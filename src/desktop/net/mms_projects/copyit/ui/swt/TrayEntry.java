package net.mms_projects.copyit.ui.swt;

import java.util.UUID;

import net.mms_projects.copyit.AndroidResourceLoader;
import net.mms_projects.copyit.ClipboardUtils;
import net.mms_projects.copyit.DesktopClipboardUtils;
import net.mms_projects.copyit.Messages;
import net.mms_projects.copyit.Settings;
import net.mms_projects.copyit.api.ServerApi;
import net.mms_projects.copyit.api.endpoints.ClipboardContentEndpoint;
import net.mms_projects.copyit.ui.swt.forms.AboutDialog;
import net.mms_projects.copyit.ui.swt.forms.PreferencesDialog;
import net.mms_projects.utils.OSValidator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

public class TrayEntry {

	protected Display display = Display.getDefault();
	protected Menu menu;
	protected TrayItem trayItem;

	protected Tray tray;

	private MenuItem menuItemCopyIt;
	private MenuItem menuItemPasteIt;

	private Shell activityShell;
	private Settings settings;

	public TrayEntry(Tray tray, Settings settings, Shell activityShell) {
		this.tray = tray;
		this.settings = settings;
		this.trayItem = new TrayItem(tray, 0);

		Image trayImage = AndroidResourceLoader
				.getImage("drawable-xxhdpi/app_icon_small.png");
		if (OSValidator.isUnix()) {
			String desktop = System.getenv("XDG_CURRENT_DESKTOP");
			if (desktop.equalsIgnoreCase("Unity")) {
				System.out.println("Running on " + desktop
						+ " using the monochrome icon");
				trayImage = AndroidResourceLoader
						.getImage("drawable-xxhdpi/app_icon_small_mono.png");
			}
		}

		this.trayItem.setImage(trayImage);
		this.trayItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				System.out.println("selection");
			}
		});

		this.activityShell = activityShell;

		this.createMenu();
	}

	public void enableFeatures() {
		this.menuItemPasteIt.setEnabled(true);
		this.menuItemCopyIt.setEnabled(true);
	}

	public void disableFeatures() {
		this.menuItemPasteIt.setEnabled(false);
		this.menuItemCopyIt.setEnabled(false);
	}

	protected void createMenu() {
		this.menu = new Menu(this.activityShell, SWT.POP_UP);

		this.menuItemCopyIt = new MenuItem(menu, SWT.PUSH);
		this.menuItemCopyIt.setText("Copy it");
		this.menuItemCopyIt.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				ServerApi api = new ServerApi();
				api.deviceId = UUID.fromString(TrayEntry.this.settings
						.get("device.id"));
				api.devicePassword = TrayEntry.this.settings
						.get("device.password");
				api.apiUrl = TrayEntry.this.settings.get("server.baseurl");

				ClipboardUtils clipboard = new DesktopClipboardUtils();
				String data = clipboard.getText();
				if (data != null) {
					try {
						new ClipboardContentEndpoint(api).update(data);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				final ToolTip tip = new ToolTip(TrayEntry.this.activityShell,
						SWT.BALLOON | SWT.ICON_INFORMATION);
				tip.setText("Notification");
				tip.setMessage(Messages.getString("text_content_pushed", data));
				trayItem.setToolTip(tip);
				tip.setVisible(true);
			}
		});

		this.menuItemPasteIt = new MenuItem(menu, SWT.PUSH);
		this.menuItemPasteIt.setText("Paste it");
		this.menuItemPasteIt.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				ServerApi api = new ServerApi();
				api.deviceId = UUID.fromString(TrayEntry.this.settings
						.get("device.id"));
				api.devicePassword = TrayEntry.this.settings
						.get("device.password");
				api.apiUrl = TrayEntry.this.settings.get("server.baseurl");

				ClipboardUtils clipboard = new DesktopClipboardUtils();
				String data;
				try {
					data = new ClipboardContentEndpoint(api).get();
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				clipboard.setText(data);

				final ToolTip tip = new ToolTip(TrayEntry.this.activityShell,
						SWT.BALLOON | SWT.ICON_INFORMATION);
				tip.setText("Notification");
				tip.setMessage(Messages.getString("text_content_pulled", data));
				trayItem.setToolTip(tip);
				tip.setVisible(true);
			}
		});

		MenuItem menuItemPreferences = new MenuItem(menu, SWT.PUSH);
		menuItemPreferences.setText("Preferences");
		menuItemPreferences.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				new PreferencesDialog(TrayEntry.this.activityShell,
						TrayEntry.this.settings).open();
			}
		});

		MenuItem menuItemAbout = new MenuItem(menu, SWT.PUSH);
		menuItemAbout.setText("About");
		menuItemAbout.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				new AboutDialog(TrayEntry.this.activityShell, SWT.NONE).open();
			}
		});

		MenuItem menuItemExit = new MenuItem(menu, SWT.PUSH);
		menuItemExit.setText("Exit");
		menuItemExit.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				TrayEntry.this.activityShell.close();
			}
		});

		this.trayItem.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event event) {
				TrayEntry.this.menu.setVisible(true);
			}
		});
	}
}
