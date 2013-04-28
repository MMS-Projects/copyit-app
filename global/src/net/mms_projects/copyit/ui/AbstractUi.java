package net.mms_projects.copyit.ui;

import net.mms_projects.copyit.Settings;

abstract public class AbstractUi {

	protected Settings settings;
	
	public AbstractUi(Settings settings) {
		this.settings = settings;
	}
	
	abstract public void open();
	
}
