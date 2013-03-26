package net.mms_projects.copyit;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {

	private Properties defaults = new Properties();
	private Properties properties;

	public Settings() {
		this.properties = new Properties(defaults);
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
			stream = new BufferedInputStream(new FileInputStream(
					"options.properties"));
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
		BufferedOutputStream stream;
		try {
			File file = new File("options.properties");
			if (!file.exists()) {
				file.createNewFile();
			}
			this.properties.store(new BufferedOutputStream(
					new FileOutputStream(file)), "");
		} catch (FileNotFoundException e) {
			// we checked this first so this shouldn't occurs
		} catch (IOException e) {
			// something went wrong with the stream
			e.printStackTrace();
		}
	}

}
