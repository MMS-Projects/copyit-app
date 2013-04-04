package net.mms_projects.copyit.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import net.mms_projects.copyit.FileStreamBuilder;
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
		this.settings.setFileStreamBuilder(new StreamBuilder());
		this.settings.loadProperties();

		AbstractUi ui = new SwtGui(this.settings);
		ui.open();

		this.settings.saveProperties();
	}

	class StreamBuilder extends FileStreamBuilder {

		@Override
		public FileInputStream getInputStream() throws IOException {
			File file = new File("options.properties");
			if (!file.exists()) {
				System.out.println("No settings file. Creating it.");
				file.createNewFile();
			}
			return new FileInputStream(file);
		}

		@Override
		public FileOutputStream getOutputStream() throws IOException {
			File file = new File("options.properties");
			if (!file.exists()) {
				System.out.println("No settings file. Creating it.");
				file.createNewFile();
			}
			return new FileOutputStream(file);
		}

	}

}
