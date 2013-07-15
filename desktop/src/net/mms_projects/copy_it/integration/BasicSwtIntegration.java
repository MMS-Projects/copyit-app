package net.mms_projects.copy_it.integration;

import java.util.Date;

import net.mms_projects.copy_it.ClipboardListener;
import net.mms_projects.copy_it.ClipboardManager;
import net.mms_projects.copy_it.EnvironmentIntegration;
import net.mms_projects.copy_it.EnvironmentIntegration.NotificationManager.NotificationUrgency;
import net.mms_projects.copy_it.Messages;
import net.mms_projects.copy_it.Settings;
import net.mms_projects.copy_it.SyncListener;
import net.mms_projects.copy_it.SyncManager;
import net.mms_projects.copy_it.ui.swt.forms.AboutDialog;
import net.mms_projects.copy_it.ui.swt.forms.PreferencesDialog;
import net.mms_projects.utils.OSValidator;
import net.mms_projects.utils.StringUtils;

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
import org.eclipse.wb.swt.SWTResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicSwtIntegration extends EnvironmentIntegration implements
		SyncListener, ClipboardListener {

	protected Display display = Display.getDefault();
	protected Menu menu;
	protected TrayItem trayItem;
	protected Settings settings;
	protected Shell activityShell;
	protected SyncManager syncManager;
	protected ClipboardManager clipboardManager;

	protected Tray tray;

	private MenuItem menuItemCopyIt;
	private MenuItem menuItemPasteIt;
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private EnvironmentIntegration parentIntegration;

	public BasicSwtIntegration(EnvironmentIntegration parentIntegration,
			Settings settings, Shell activityShell, SyncManager syncManager,
			ClipboardManager clipboardManager) {
		this.parentIntegration = parentIntegration;
		this.tray = display.getSystemTray();
		this.trayItem = new TrayItem(tray, 0);
		this.settings = settings;
		this.activityShell = activityShell;
		this.syncManager = syncManager;
		this.clipboardManager = clipboardManager;
	}

	@Override
	public void standaloneSetup() {
		Image trayImage = SWTResourceManager.getImage(getClass(),
				"/images/icon-16.png");
		if (OSValidator.isUnix()) {
			String desktop = System.getenv("XDG_CURRENT_DESKTOP");
			if (desktop.equalsIgnoreCase("Unity")) {
				log.debug("Running on {} using the monochrome icon", desktop);
				trayImage = SWTResourceManager.getImage(getClass(),
						"/images/icon-16-mono.png");
			}
		}

		this.trayItem.setImage(trayImage);
		this.trayItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				log.debug("selection");
			}
		});

		this.createMenu();

		this.parentIntegration
				.setNotificationManager(new NotificationManagerSwt(
						this.display, this.trayItem));
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
				clipboardManager.requestGet();
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
				new PreferencesDialog(activityShell, settings, parentIntegration).open();
			}
		});

		MenuItem menuItemAbout = new MenuItem(menu, SWT.PUSH);
		menuItemAbout.setText("About");
		menuItemAbout.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				new AboutDialog(activityShell, SWT.NONE).open();
			}
		});

		MenuItem menuItemExit = new MenuItem(menu, SWT.PUSH);
		menuItemExit.setText("Exit");
		menuItemExit.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				activityShell.close();
			}
		});

		this.trayItem.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event event) {
				menu.setVisible(true);
			}
		});
	}

	@Override
	public void onPushed(final String content, Date date) {
		this.getNotificationManager().notify(10, NotificationUrgency.NORMAL,
				"", "Notification",
				Messages.getString("text_content_pushed", content));
	}

	@Override
	public void onPulled(final String content, Date date) {
		clipboardManager.requestSet(content);

		this.getNotificationManager().notify(10, NotificationUrgency.NORMAL,
				"", "Notification",
				Messages.getString("text_content_pulled", content));
	}

	private class NotificationManagerSwt implements NotificationManager {

		protected TrayItem trayItem;
		protected Display display;

		public NotificationManagerSwt(Display display, TrayItem trayItem) {
			this.display = display;
			this.trayItem = trayItem;
		}

		@Override
		public void notify(int id, NotificationUrgency urgency, String icon,
				final String summary, String body) {
			String content;
			if (body.length() > 100) {
				content = StringUtils.ellipsize(body, 100);
			} else {
				content = body;
			}
			final String finalContent = content;
			this.display.asyncExec(new Runnable() {
				@Override
				public void run() {
					final ToolTip tip = new ToolTip(activityShell, SWT.BALLOON
							| SWT.ICON_INFORMATION);
					tip.setText(summary);
					tip.setMessage(finalContent);
					trayItem.setToolTip(tip);
					tip.setVisible(true);
				}
			});
		}

	}

	@Override
	public void onContentSet(String content) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onContentGet(String content) {
		syncManager.doPush(content, new Date());
	}
}
