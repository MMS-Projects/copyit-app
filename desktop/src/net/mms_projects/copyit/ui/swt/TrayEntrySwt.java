package net.mms_projects.copyit.ui.swt;

import java.util.Date;

import net.mms_projects.copyit.AndroidResourceLoader;
import net.mms_projects.copyit.ClipboardUtils;
import net.mms_projects.copyit.DesktopClipboardUtils;
import net.mms_projects.copyit.Messages;
import net.mms_projects.copyit.Settings;
import net.mms_projects.copyit.SyncManager;
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

public class TrayEntrySwt extends TrayEntry {

	protected Display display = Display.getDefault();
	protected Menu menu;
	protected TrayItem trayItem;

	protected Tray tray;

	private MenuItem menuItemCopyIt;
	private MenuItem menuItemPasteIt;

	public TrayEntrySwt(Settings settings, Shell activityShell, Tray tray,
			SyncManager syncManager) {
		super(settings, activityShell, syncManager);
		this.tray = tray;
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

		this.syncManager.addListener(this);
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
		this.menuItemCopyIt.setText("Copy it ▲");
		this.menuItemCopyIt.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				ClipboardUtils clipboard = new DesktopClipboardUtils();
				syncManager.doPush(clipboard.getText(), new Date());
			}
		});

		this.menuItemPasteIt = new MenuItem(menu, SWT.PUSH);
		this.menuItemPasteIt.setText("Paste it ▼");
		this.menuItemPasteIt.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				syncManager.doPull();
			}
		});

		MenuItem menuItemPreferences = new MenuItem(menu, SWT.PUSH);
		menuItemPreferences.setText("Preferences");
		menuItemPreferences.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				new PreferencesDialog(TrayEntrySwt.this.activityShell,
						TrayEntrySwt.this.settings).open();
			}
		});

		MenuItem menuItemAbout = new MenuItem(menu, SWT.PUSH);
		menuItemAbout.setText("About");
		menuItemAbout.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				new AboutDialog(TrayEntrySwt.this.activityShell, SWT.NONE)
						.open();
			}
		});

		MenuItem menuItemExit = new MenuItem(menu, SWT.PUSH);
		menuItemExit.setText("Exit");
		menuItemExit.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				TrayEntrySwt.this.activityShell.close();
			}
		});

		this.trayItem.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event event) {
				TrayEntrySwt.this.menu.setVisible(true);
			}
		});
	}

	@Override
	public void onPushed(final String content, Date date) {
		this.display.asyncExec(new Runnable() {
			@Override
			public void run() {
				final ToolTip tip = new ToolTip(
						TrayEntrySwt.this.activityShell, SWT.BALLOON
								| SWT.ICON_INFORMATION);
				tip.setText("Notification");
				tip.setMessage(Messages.getString("text_content_pushed",
						content));
				trayItem.setToolTip(tip);
				tip.setVisible(true);
			}
		});
	}

	@Override
	public void onPulled(final String content, Date date) {
		this.display.asyncExec(new Runnable() {
			@Override
			public void run() {
				ClipboardUtils clipboard = new DesktopClipboardUtils();
				clipboard.setText(content);
				
				final ToolTip tip = new ToolTip(
						TrayEntrySwt.this.activityShell, SWT.BALLOON
								| SWT.ICON_INFORMATION);
				tip.setText("Notification");
				tip.setMessage(Messages.getString("text_content_pulled",
						content));
				trayItem.setToolTip(tip);
				tip.setVisible(true);
			}
		});
	}
}
