package net.mms_projects.copy_it;

import java.util.HashMap;
import java.util.Map;

import net.mms_projects.copy_it.listeners.EnabledListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FunctionalityManager<F extends Activatable> {

	private Map<String, F> functionality = new HashMap<String, F>();

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public void addFunctionality(String key, F functionality) {
		this.functionality.put(key, functionality);
	}

	public void enable(String key) {
		this.functionality.get(key).enable();
		log.debug("Enabled " + key);
	}

	public void disable(String key) {
		this.functionality.get(key).disable();
		log.debug("Disabled " + key);
	}

	public void setEnabled(String key, boolean enabled) {
		this.functionality.get(key).setEnabled(enabled);
		log.debug("Set " + key + " enabled to " + enabled);
	}

	public boolean isEnabled(String key) {
		return this.functionality.get(key).isEnabled();
	}
	
	public void addEnabledListener(String key, EnabledListener listener) {
		this.functionality.get(key).addEnabledListener(listener);
	}

}
