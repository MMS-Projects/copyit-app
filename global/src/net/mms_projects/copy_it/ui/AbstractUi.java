package net.mms_projects.copy_it.ui;

import net.mms_projects.copy_it.Settings;

abstract public class AbstractUi {

	protected Settings settings;
	
	public AbstractUi(Settings settings) {
		this.settings = settings;
	}
	
	abstract public void open();
	
}
