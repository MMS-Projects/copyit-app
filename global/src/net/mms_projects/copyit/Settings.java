package net.mms_projects.copyit;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Settings {

	private Properties defaults = new Properties();
	private Properties properties;
	private FileStreamBuilder fileStreamBuider;
	private Map<String, List<SettingsListener>> listeners = new HashMap<String, List<SettingsListener>>();

	public Settings() {
		this.defaults.setProperty("server.baseurl",
				"http://copyit.dev.mms-projects.net");
		this.properties = new Properties(defaults);
	}

	public void addListener(String key, SettingsListener listener) {
		if (!this.listeners.containsKey(key)) {
			this.listeners.put(key, new ArrayList<SettingsListener>());
		}
		this.listeners.get(key).add(listener);
	}

	public void setFileStreamBuilder(FileStreamBuilder fileStreamBuilder) {
		this.fileStreamBuider = fileStreamBuilder;
	}

	public void set(String key, String value) {
		this.properties.setProperty(key, value);
		saveProperties();
		this.notifyChangeListeners(key, value);
	}

	public void set(String key, boolean value) {
		this.set(key, Boolean.toString(value));
		
	}

	public String get(String key) {
		return this.properties.getProperty(key);
	}

	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(this.get(key));
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
	
	private void notifyChangeListeners(String key, String value) {
		if (!this.listeners.containsKey(key)) {
			return;
		}
		for (SettingsListener listener : this.listeners.get(key)) {
			listener.onChange(key, value);
		}
	}
}
