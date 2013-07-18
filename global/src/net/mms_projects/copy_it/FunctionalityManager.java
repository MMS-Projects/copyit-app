package net.mms_projects.copy_it;

import java.util.HashMap;
import java.util.Map;

public class FunctionalityManager<F extends Activatable> {

	private Map<String, F> functionality = new HashMap<String, F>();

	public void addFunctionality(String key, F functionality) {
		this.functionality.put(key, functionality);
	}

	public void enable(String key) {
		this.functionality.get(key).enable();
	}

	public void disable(String key) {
		this.functionality.get(key).disable();
	}

	public void isEnabled(String key) {
		this.functionality.get(key).isEnabled();
	}

}
