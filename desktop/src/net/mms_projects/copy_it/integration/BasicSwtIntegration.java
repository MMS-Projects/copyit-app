package net.mms_projects.copy_it.integration;

import java.util.Date;

import net.mms_projects.copy_it.Activatable;
import net.mms_projects.copy_it.ClipboardManager;
import net.mms_projects.copy_it.EnvironmentIntegration;
import net.mms_projects.copy_it.EnvironmentIntegration.NotificationManager.NotificationUrgency;
import net.mms_projects.copy_it.FunctionalityManager;
import net.mms_projects.copy_it.Messages;
import net.mms_projects.copy_it.SyncManager;
import net.mms_projects.copy_it.linux.DesktopEnvironment;
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

public class BasicSwtIntegration extends EnvironmentIntegration {

	protected Display display = Display.getDefault();
	protected Menu menu;
	protected TrayItem trayItem;
	protected SyncManager syncManager;
	protected ClipboardManager clipboardManager;

	protected Tray tray;

	private MenuItem menuItemCopyIt;
	private MenuItem menuItemPasteIt;
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private FunctionalityManager<Activatable> functionality;

	public BasicSwtIntegration(EnvironmentIntegration parentIntegration,
			FunctionalityManager<Activatable> functionality,
			SyncManager syncManager, ClipboardManager clipboardManager) {
		super(parentIntegration);
		
		this.tray = display.getSystemTray();
		this.trayItem = new TrayItem(tray, 0);
		this.functionality = functionality;
		this.syncManager = syncManager;
		this.clipboardManager = clipboardManager;
	}

	@Override
	public void standaloneSetup() {
		Image trayImage = SWTResourceManager.getImage(getClass(),
				"/images/icon-16.png");
		if (OSValidator.isUnix()) {
			if (DesktopEnvironment.isUnity()) {
				log.debug("Running on Unity using the monochrome icon");
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

		this.setNotificationManager(new NotificationManagerSwt(this.display,
				this.trayItem));
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
		this.menu = new Menu(new Shell(), SWT.POP_UP);

		this.menuItemCopyIt = new MenuItem(menu, SWT.PUSH);
		this.menuItemCopyIt.setText("Copy it ▲");
		this.menuItemCopyIt.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				String content = clipboardManager.getContent();

				syncManager.setRemoteContent(content, new Date());

				getNotificationManager().notify(10, NotificationUrgency.NORMAL,
						"", "CopyIt",
						Messages.getString("text_content_pushed", content));
			}
		});

		this.menuItemPasteIt = new MenuItem(menu, SWT.PUSH);
		this.menuItemPasteIt.setText("Paste it ▼");
		this.menuItemPasteIt.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				syncManager.requestRemoteContentAsync();
			}
		});

		MenuItem menuItemPreferences = new MenuItem(menu, SWT.PUSH);
		menuItemPreferences.setText("Preferences");
		menuItemPreferences.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				getUserInterfaceImplementation().getSettingsUserInterface()
						.open();
			}
		});

		MenuItem menuItemAbout = new MenuItem(menu, SWT.PUSH);
		menuItemAbout.setText("About");
		menuItemAbout.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				getUserInterfaceImplementation().getAboutUserInterface().open();
			}
		});

		MenuItem menuItemExit = new MenuItem(menu, SWT.PUSH);
		menuItemExit.setText("Exit");
		menuItemExit.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				getUserInterfaceImplementation().close();
			}
		});

		this.trayItem.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event event) {
				menu.setVisible(true);
			}
		});
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
					final ToolTip tip = new ToolTip(new Shell(), SWT.BALLOON
							| SWT.ICON_INFORMATION);
					tip.setText(summary);
					tip.setMessage(finalContent);
					trayItem.setToolTip(tip);
					tip.setVisible(true);
				}
			});
		}

	}

}
