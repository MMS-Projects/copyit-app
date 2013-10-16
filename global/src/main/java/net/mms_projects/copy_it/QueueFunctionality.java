package net.mms_projects.copy_it;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.mms_projects.copy_it.listeners.EnabledListener;
import net.mms_projects.copy_it.ui.UserInterfaceImplementation;

/**
 * This class provides the queue functionality. It gets notified when new
 * clipboard content is available on the server and then adds it to queue in the
 * user interface.
 */
public final class QueueFunctionality implements Activatable, SyncListener {

    /**
     * Whenever the functionality is enabled.
     */
    private boolean enabled;
    /**
     * A list of all the listeners that get notified when the functionality is
     * enabled or disabled.
     */
    private List<EnabledListener> enabledListeners
        = new ArrayList<EnabledListener>();
    /**
     * The user interface implementation used to update the queue.
     */
    private UserInterfaceImplementation userInterfaceImplementation;

    /**
     * @param newUserInterfaceImplementation The user interface implementation
     * to use
     */
    public QueueFunctionality(
            final UserInterfaceImplementation newUserInterfaceImplementation) {
        this.userInterfaceImplementation = newUserInterfaceImplementation;
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

        this.userInterfaceImplementation.getQueueUserInterface().close();

        for (EnabledListener listener : this.enabledListeners) {
            listener.onDisabled();
        }
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(final boolean newState) {
        if (newState) {
            this.enable();
        } else {
            this.disable();
        }
    }

    @Override
    public void addEnabledListener(final EnabledListener listener) {
        this.enabledListeners.add(listener);
    }

    @Override
    public void onRemoteContentChange(final String content, final Date date) {
        if (this.isEnabled()) {
            this.userInterfaceImplementation.getQueueUserInterface()
                    .addContent(content, date);
        }
    }

}
