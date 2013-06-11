package net.mms_projects.copy_it.ui.swt;

import java.util.Date;

import net.mms_projects.copy_it.AndroidResourceLoader;
import net.mms_projects.copy_it.ClipboardManager;
import net.mms_projects.copy_it.Messages;
import net.mms_projects.copy_it.Settings;
import net.mms_projects.copy_it.SyncManager;
import net.mms_projects.copy_it.ui.swt.forms.AboutDialog;
import net.mms_projects.copy_it.ui.swt.forms.PreferencesDialog;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrayEntrySwt extends TrayEntry {

	protected Display display = Display.getDefault();
	protected Menu menu;
	protected TrayItem trayItem;

	protected Tray tray;

	private MenuItem menuItemCopyIt;
	private MenuItem menuItemPasteIt;
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public TrayEntrySwt(Settings settings, Shell activityShell, Tray tray,
			SyncManager syncManager, ClipboardManager clipboardManager) {
		super(settings, activityShell, syncManager, clipboardManager);
		this.tray = tray;
		this.trayItem = new TrayItem(tray, 0);

		Image trayImage = AndroidResourceLoader
				.getImage("drawable-xxhdpi/app_icon_small.png");
		if (OSValidator.isUnix()) {
			String desktop = System.getenv("XDG_CURRENT_DESKTOP");
			if (desktop.equalsIgnoreCase("Unity")) {
				log.debug("Running on {} using the monochrome icon", desktop);
				trayImage = AndroidResourceLoader
						.getImage("drawable-xxhdpi/app_icon_small_mono.png");
			}
		}

		this.trayItem.setImage(trayImage);
		this.trayItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				log.debug("selection");
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
				clipboardManager.getContent();
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
				clipboardManager.setContent(content);

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
