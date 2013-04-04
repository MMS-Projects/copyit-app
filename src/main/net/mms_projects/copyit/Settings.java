package net.mms_projects.copyit;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {

	private Properties defaults = new Properties();
	private Properties properties;
	private FileInputStream inputStream;
	private FileOutputStream outputStream;

	public Settings() {
		this.defaults.setProperty("server.baseurl",
				"http://copyit.dev.mms-projects.net");
		this.properties = new Properties(defaults);
	}

	public void setFileStream(FileInputStream inputStream,
			FileOutputStream outputStream) {
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}

	public void set(String key, String value) throws Exception {
		this.properties.setProperty(key, value);
		saveProperties();
	}

	public String get(String key) {
		return this.properties.getProperty(key);
	}

	public void loadProperties() {
		BufferedInputStream stream;
		try {
			stream = new BufferedInputStream(this.inputStream);
			this.properties.load(stream);
			stream.close();
		} catch (FileNotFoundException e) {
			// having no properties file is OK
		} catch (IOException e) {
			// something went wrong with the stream
			e.printStackTrace();
		}
	}

	public void saveProperties() {
		try {
			this.properties.store(new BufferedOutputStream(this.outputStream),
					"");
		} catch (FileNotFoundException e) {
			// we checked this first so this shouldn't occurs
		} catch (IOException e) {
			// something went wrong with the stream
			e.printStackTrace();
		}
	}

}
