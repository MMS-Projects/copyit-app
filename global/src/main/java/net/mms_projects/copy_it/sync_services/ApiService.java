package net.mms_projects.copy_it.sync_services;

import java.util.Date;
import java.util.concurrent.Executor;

import net.mms_projects.copy_it.SyncListener;
import net.mms_projects.copy_it.api.endpoints.ClipboardContentEndpoint;

/**
 * This sync service uses the CopyIt API to get and update the remote clipboard
 * content. It also polls the API to see if the clipboard content has changed.
 */
public final class ApiService implements PullServiceInterface,
        PushServiceInterface {

    /**
     * The sync listener to call when new remote content is available.
     */
    private SyncListener listener;

    /**
     * The API endpoint that gets called to get and update the remote clipboard
     * content.
     */
    private ClipboardContentEndpoint endpoint;
    /**
     * The executor to run background tasks in.
     */
    private Executor executor;

    /**
     * @param newListener The sync listener to use
     * @param newEndpoint The clipboard content API endpoint to use
     */
    public ApiService(final SyncListener newListener,
            final ClipboardContentEndpoint newEndpoint) {
        this.listener = newListener;
        this.endpoint = newEndpoint;
    }

    /**
     * This method changes the used clipboard content API endpoint.
     *
     * @param newEndpoint The new endpoint to use.
     */
    public void setEndpoint(final ClipboardContentEndpoint newEndpoint) {
        this.endpoint = newEndpoint;
    }

    @Override
    public String getServiceName() {
        return "api";
    }

    @Override
    public void setExecutor(final Executor newExecutor) {
        this.executor = newExecutor;
    }

    @Override
    public Executor getExecutor() {
        return this.executor;
    }

    @Override
    public void activatePush() {
    }

    @Override
    public void deactivatePush() {
    }

    @Override
    public void activatePull() {
    }

    @Override
    public void deactivatePull() {
    }

    @Override
    public void updateRemoteContentAsync(final String content,
            final Date date) {
        this.executor.execute(new Runnable() {
            @Override
            public void run() {
                setRemoteContent(content, date);
            }
        });
    }

    @Override
    public void requestRemoteContentAsync() {
        this.executor.execute(new Runnable() {
            @Override
            public void run() {
                listener.onRemoteContentChange(getRemoteContent(), new Date());
            }
        });
    }

    @Override
    public boolean isPushActivated() {
        return true;
    }

    @Override
    public boolean isPullActivated() {
        return true;
    }

    @Override
    public void setRemoteContent(final String content, final Date date) {
        try {
            this.endpoint.update(content);
        } catch (Exception e) {
            e.printStackTrace();

            return;
        }
    }

    @Override
    public String getRemoteContent() {
        try {
            return this.endpoint.get();
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

}
