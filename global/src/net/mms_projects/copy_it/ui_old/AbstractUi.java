package net.mms_projects.copy_it.ui_old;

import net.mms_projects.copy_it.Config;

@Deprecated
abstract public class AbstractUi {

	protected Config settings;
	
	public AbstractUi(Config settings) {
		this.settings = settings;
	}
	
	abstract public void open();
	
}
