package net.mms_projects.copyit;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mms_projects.copyit.sync_services.PullServiceInterface;
import net.mms_projects.copyit.sync_services.PullingServiceInterface;
import net.mms_projects.copyit.sync_services.PushServiceInterface;

public class SyncManager implements PushServiceInterface, PullServiceInterface,
		PullingServiceInterface, SyncListener {

	private Map<String, PushServiceInterface> pushServices = new HashMap<String, PushServiceInterface>();
	private Map<String, PullServiceInterface> pullServices = new HashMap<String, PullServiceInterface>();
	private Map<String, PullingServiceInterface> pullingServices = new HashMap<String, PullingServiceInterface>();
	private String pushService;
	private String pullService;
	private String pullingService;
	private List<SyncListener> listeners = new ArrayList<SyncListener>();

	public void addPushService(PushServiceInterface service) {
		this.pushServices.put(service.getServiceName(), service);
		if (this.pushService == null) {
			this.pushService = service.getServiceName();
			this.pushServices.get(this.pushService).activatePush();
		}
	}

	public void addPullService(PullServiceInterface service) {
		this.pullServices.put(service.getServiceName(), service);
		if (this.pullService == null) {
			this.pullService = service.getServiceName();
			this.pullServices.get(this.pullService).activatePull();
		}
	}

	public void addPullingService(PullingServiceInterface service) {
		this.pullingServices.put(service.getServiceName(), service);
		if (this.pullingService == null) {
			this.pullingService = service.getServiceName();
			this.pullingServices.get(this.pullingService).activatePulling();
		}
	}

	public void addListener(SyncListener listener) {
		this.listeners.add(listener);
	}

	public void setPushService(String service) {
		if (!this.pushServices.containsKey(service)) {
			return;
		}
		this.deactivatePush();
		this.pushService = service;
		this.activatePush();
	}

	public void setPullService(String service) {
		if (!this.pullServices.containsKey(service)) {
			return;
		}
		this.deactivatePull();
		this.pullService = service;
		this.activatePull();
	}

	public void setPullingService(String service) {
		if (!this.pullingServices.containsKey(service)) {
			return;
		}
		this.deactivatePulling();
		this.pullingService = service;
		this.activatePulling();
	}

	@Override
	public void doPush(String content, Date date) {
		if (this.pushServices.isEmpty()) {
			return;
		}
		if (!this.pushServices.containsKey(this.pushService)) {
			return;
		}
		this.pushServices.get(this.pushService).doPush(content, date);
	}

	@Override
	public void doPull() {
		if (this.pullServices.isEmpty()) {
			return;
		}
		if (!this.pullServices.containsKey(this.pullService)) {
			return;
		}
		this.pullServices.get(this.pullService).doPull();
	}

	@Override
	public void onPushed(String content, Date date) {
		for (SyncListener listener : this.listeners) {
			listener.onPushed(content, date);
		}
	}

	@Override
	public void onPulled(String content, Date date) {
		for (SyncListener listener : this.listeners) {
			listener.onPulled(content, date);
		}
	}

	@Override
	public String getServiceName() {
		return "manager";
	}

	@Override
	public void activatePush() {
		if (!this.isPushActivated()) {
			this.pushServices.get(this.pushService).activatePush();
		}
	}

	@Override
	public void deactivatePush() {
		if (this.isPushActivated()) {
			this.pushServices.get(this.pushService).deactivatePush();
		}
	}

	@Override
	public boolean isPushActivated() {
		return this.pushServices.get(this.pushService).isPushActivated();
	}

	@Override
	public void activatePull() {
		if (!isPullActivated()) {
			this.pullServices.get(this.pullService).activatePull();
		}
	}

	@Override
	public void deactivatePull() {
		if (isPullActivated()) {
			this.pullServices.get(this.pullService).deactivatePull();
		}
	}

	@Override
	public boolean isPullActivated() {
		return this.pullServices.get(this.pullService).isPullActivated();
	}

	@Override
	public void activatePulling() {
		if (!isPullingActivated()) {
			this.pullingServices.get(this.pullingService).activatePulling();
		}
	}

	@Override
	public void deactivatePulling() {
		if (isPullingActivated()) {
			this.pullingServices.get(this.pullingService).deactivatePulling();
		}
	}

	@Override
	public boolean isPullingActivated() {
		return this.pullingServices.get(this.pullingService)
				.isPullingActivated();
	}

}
