package net.mms_projects.copyit.app;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

import net.mms_projects.copyit.ServerApi;
import net.mms_projects.copyit.forms.LoginDialog;
import net.mms_projects.copyit.forms.MainWindow;

public class CopyIt {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		Tray tray = display.getSystemTray();
		TrayItem item;
		if (tray != null) {
			item = new TrayItem(tray, SWT.NONE);
			item.setToolTipText("A");
			item.setImage(display.getSystemImage(SWT.ICON_ERROR));
			item.addListener(SWT.Show, new Listener() {
				public void handleEvent(Event event) {
					System.out.println("show");
				}
			});
			item.addListener(SWT.Hide, new Listener() {
				public void handleEvent(Event event) {
					System.out.println("hide");
				}
			});
			item.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					System.out.println("selection");
				}
			});
			item.addListener(SWT.DefaultSelection, new Listener() {
				public void handleEvent(Event event) {
					System.out.println("default selection");
				}
			});
			final Menu menu = new Menu(shell, SWT.POP_UP);
			for (int i = 0; i < 8; i++) {
				MenuItem mi = new MenuItem(menu, SWT.PUSH);
				mi.setText("Item" + i);
				mi.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {
						System.out.println("selection " + event.widget);
					}
				});
				if (i == 0)
					menu.setDefaultItem(mi);
			}
			item.addListener(SWT.MenuDetect, new Listener() {
				public void handleEvent(Event event) {
					menu.setVisible(true);
				}
			});
		}

		MainWindow.main(args);
	}

}
