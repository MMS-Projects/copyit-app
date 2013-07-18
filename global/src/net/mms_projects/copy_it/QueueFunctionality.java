package net.mms_projects.copy_it;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.mms_projects.copy_it.listeners.EnabledListener;
import net.mms_projects.copy_it.ui.UserInterfaceImplementation;

public class QueueFunctionality implements Activatable, SyncListener {

	private boolean enabled;
	private List<EnabledListener> enabledListeners = new ArrayList<EnabledListener>();
	private UserInterfaceImplementation userInterfaceImplementation;

	public QueueFunctionality(
			UserInterfaceImplementation userInterfaceImplementation) {
		this.userInterfaceImplementation = userInterfaceImplementation;
	}

	@Override
	public void enable() {
		this.enabled = true;

		for (EnabledListener listener : this.enabledListeners) {
			listener.onEnabled();
		}
	}

	@Override
	public void disable() {
		this.enabled = false;

		this.userInterfaceImplementation.getQueueUserInterface().hide();

		for (EnabledListener listener : this.enabledListeners) {
			listener.onDisabled();
		}
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (enabled) {
			this.enable();
		} else {
			this.disable();
		}
	}

	@Override
	public void addEnabledListener(EnabledListener listener) {
		this.enabledListeners.add(listener);
	}

	@Override
	public void onPushed(String content, Date date) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPulled(String content, Date date) {
		if (this.isEnabled()) {
			this.userInterfaceImplementation.getQueueUserInterface()
					.addContent(content, date);
		}
	}

}
