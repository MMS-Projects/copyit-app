package net.mms_projects.copyit.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import net.mms_projects.copyit.Settings;
import net.mms_projects.copyit.ui.AbstractUi;
import net.mms_projects.copyit.ui.SwtGui;

public class CopyItDesktop extends CopyIt {

	protected Settings settings;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CopyItDesktop app = new CopyItDesktop();
		app.run();
	}

	public void run() {
		this.settings = new Settings();
		try {
			File file = new File("options.properties");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileInputStream inputStream = new FileInputStream(
					"options.properties");
			FileOutputStream outputStream = new FileOutputStream(file);
			this.settings.setFileStream(inputStream, outputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		this.settings.loadProperties();
		
		AbstractUi ui = new SwtGui(this.settings);
		ui.open();
		
		this.settings.saveProperties();
	}

}
