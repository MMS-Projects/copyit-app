package net.mms_projects.copyit.app;

import net.mms_projects.copyit.Settings;
import net.mms_projects.copyit.ui.AbstractUi;
import net.mms_projects.copyit.ui.SwtGui;
import net.mms_projects.copyit.ui.swt.forms.MainWindow;

public class CopyIt {

	protected Settings settings;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CopyIt app = new CopyIt();
		app.run();
	}

	public void run() {
		this.settings = new Settings();
		
		AbstractUi ui = new SwtGui(this.settings);
		ui.open();
	}

}
