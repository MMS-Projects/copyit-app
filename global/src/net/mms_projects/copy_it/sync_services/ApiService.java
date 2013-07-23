package net.mms_projects.copy_it.sync_services;

import java.util.Date;
import java.util.concurrent.Executor;

import net.mms_projects.copy_it.SyncListener;
import net.mms_projects.copy_it.api.endpoints.ClipboardContentEndpoint;

public class ApiService implements PullServiceInterface, PushServiceInterface {

	protected SyncListener listener;
	protected ClipboardContentEndpoint endpoint;
	private Executor executor;

	public ApiService(SyncListener listener, ClipboardContentEndpoint endpoint) {
		this.listener = listener;
		this.endpoint = endpoint;
	}

	public void setEndpoint(ClipboardContentEndpoint endpoint) {
		this.endpoint = endpoint;
	}

	@Override
	public String getServiceName() {
		return "api";
	}

	@Override
	public void setExecutor(Executor executor) {
		this.executor = executor;
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
	public void updateRemoteContentAsync(final String content, final Date date) {
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
	public void setRemoteContent(String content, Date date) {
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
