package net.mms_projects.copyit;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {

	private Properties defaults = new Properties();
	private Properties properties;
	private FileStreamBuilder fileStreamBuider;

	public Settings() {
		this.defaults.setProperty("server.baseurl",
				"http://copyit.dev.mms-projects.net");
		this.properties = new Properties(defaults);
	}

	public void setFileStreamBuilder(FileStreamBuilder fileStreamBuilder) {
		this.fileStreamBuider = fileStreamBuilder;
	}

	public void set(String key, String value) {
		this.properties.setProperty(key, value);
		saveProperties();
	}

	public String get(String key) {
		return this.properties.getProperty(key);
	}

	public void loadProperties() {
		System.out.println("Loading settings...");
		BufferedInputStream stream;
		try {
			stream = new BufferedInputStream(
					this.fileStreamBuider.getInputStream());
			this.properties.load(stream);
			stream.close();
		} catch (IOException e) {
			// something went wrong with the stream
			e.printStackTrace();
		}
	}

	public void saveProperties() {
		System.out.println("Saving settings...");
		try {
			this.properties.store(new BufferedOutputStream(
					this.fileStreamBuider.getOutputStream()), "");
		} catch (IOException e) {
			// something went wrong with the stream
			e.printStackTrace();
		}
	}
}
