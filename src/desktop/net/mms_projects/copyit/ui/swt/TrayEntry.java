package net.mms_projects.copyit.ui.swt;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

public class TrayEntry {

	protected Display display = Display.getDefault();
	protected Menu menu;
	protected TrayItem trayItem;

	protected Tray tray;
	protected ActionProvider actionProvider;
	
	private MenuItem menuItemCopyIt;
	private MenuItem menuItemPasteIt;

	public TrayEntry(Tray tray, final ActionProvider actionProvider) {
		this.tray = tray;
		this.actionProvider = actionProvider;

		this.trayItem = new TrayItem(tray, 0);

		this.trayItem.setImage(display.getSystemImage(SWT.ICON_WORKING));
		this.trayItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				System.out.println("selection");
			}
		});

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
		final Shell shell = new Shell(display);
		this.menu = new Menu(shell, SWT.POP_UP);

		this.menuItemCopyIt = new MenuItem(menu, SWT.PUSH);
		this.menuItemCopyIt.setText("Copy it");
		this.menuItemCopyIt.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				TrayEntry.this.actionProvider.doDataSet();
			}
		});

		this.menuItemPasteIt = new MenuItem(menu, SWT.PUSH);
		this.menuItemPasteIt.setText("Paste it");
		this.menuItemPasteIt.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				TrayEntry.this.actionProvider.doDataGet();
			}
		});

		MenuItem menuItemLogin = new MenuItem(menu, SWT.PUSH);
		menuItemLogin.setText("Login");
		menuItemLogin.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				TrayEntry.this.actionProvider.doLogin(shell);
			}
		});
		this.trayItem.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event event) {
				TrayEntry.this.menu.setVisible(true);
			}
		});
	}

}
