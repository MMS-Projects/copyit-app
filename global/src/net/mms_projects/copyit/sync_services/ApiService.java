package net.mms_projects.copyit.sync_services;

import java.util.Date;

import net.mms_projects.copyit.SyncListener;
import net.mms_projects.copyit.api.endpoints.ClipboardContentEndpoint;

public class ApiService implements PullServiceInterface, PushServiceInterface {

	protected SyncListener listener;
	protected ClipboardContentEndpoint endpoint;

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
	public void doPush(final String content, final Date date) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					endpoint.update(content);
				} catch (Exception e) {
					e.printStackTrace();

					return;
				}

				listener.onPushed(content, date);
			}
		}).start();
	}

	@Override
	public void doPull() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					listener.onPulled(endpoint.get(), new Date());
				} catch (Exception e) {
					e.printStackTrace();

					return;
				}
			}
		}).start();
	}

	@Override
	public boolean isPushActivated() {
		return true;
	}

	@Override
	public boolean isPullActivated() {
		return true;
	}

}
