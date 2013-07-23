package net.mms_projects.copy_it;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.mms_projects.copy_it.sync_services.PullServiceInterface;
import net.mms_projects.copy_it.sync_services.PushServiceInterface;

public class SyncManager implements PushServiceInterface, PullServiceInterface,
		PollingServiceInterface, SyncListener, ClipboardListener {

	private Map<String, PushServiceInterface> pushServices = new HashMap<String, PushServiceInterface>();
	private Map<String, PullServiceInterface> pullServices = new HashMap<String, PullServiceInterface>();
	private Map<String, PollingServiceInterface> pullingServices = new HashMap<String, PollingServiceInterface>();
	private String pushService;
	private String pullService;
	private String pullingService;
	private List<SyncListener> listeners = new ArrayList<SyncListener>();

	private ClipboardManager clipboardManager;
	private String currentContent;
	private Executor executor;
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public SyncManager(ClipboardManager clipboardManager) {
		this.clipboardManager = clipboardManager;
		this.clipboardManager.addListener(this);
	}

	public void addPushService(PushServiceInterface service) {
		if (this.executor == null) {
			log.error("Before adding a service please define a executor");
			return;
		}
		service.setExecutor(executor);
		
		this.pushServices.put(service.getServiceName(), service);
		if (this.pushService == null) {
			this.pushService = service.getServiceName();
			this.pushServices.get(this.pushService).activatePush();
		}
	}

	public void addPullService(PullServiceInterface service) {
		if (this.executor == null) {
			log.error("Before adding a service please define a executor");
			return;
		}
		service.setExecutor(executor);
		
		this.pullServices.put(service.getServiceName(), service);
		if (this.pullService == null) {
			this.pullService = service.getServiceName();
			this.pullServices.get(this.pullService).activatePull();
		}
	}

	public void addPullingService(PollingServiceInterface service) {
		if (this.executor == null) {
			log.error("Before adding a service please define a executor");
			return;
		}
		service.setExecutor(executor);
		
		this.pullingServices.put(service.getServiceName(), service);
		if (this.pullingService == null) {
			this.pullingService = service.getServiceName();
			this.pullingServices.get(this.pullingService).activatePolling();
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
		this.deactivatePolling();
		this.pullingService = service;
		this.activatePolling();
	}

	@Deprecated
	@Override
	public void updateRemoteContentAsync(String content, Date date) {
		if (this.pushServices.isEmpty()) {
			return;
		}
		if (!this.pushServices.containsKey(this.pushService)) {
			return;
		}
		this.pushServices.get(this.pushService).updateRemoteContentAsync(content, date);
	}

	@Override
	public void setRemoteContent(String content, Date date) {
		if (this.pushServices.isEmpty()) {
			return;
		}
		if (!this.pushServices.containsKey(this.pushService)) {
			return;
		}
		this.pushServices.get(this.pushService).setRemoteContent(content, date);
	}
	
	@Deprecated
	@Override
	public void requestRemoteContentAsync() {
		if (this.pullServices.isEmpty()) {
			return;
		}
		if (!this.pullServices.containsKey(this.pullService)) {
			return;
		}
		this.pullServices.get(this.pullService).requestRemoteContentAsync();
	}


	@Override
	public String getRemoteContent() {
		if (this.pullServices.isEmpty()) {
			return null;
		}
		if (!this.pullServices.containsKey(this.pullService)) {
			return null;
		}
		return this.pullServices.get(this.pullService).getRemoteContent();
	}

	@Override
	public void onRemoteContentChange(String content, Date date) {
		if (content.equals(this.currentContent)) {
			return;
		}
		for (SyncListener listener : this.listeners) {
			listener.onRemoteContentChange(content, date);
		}
	}

	@Override
	public String getServiceName() {
		return "manager";
	}

	@Override
	public void setExecutor(Executor executor) {
		this.executor = executor;
		
		for (ServiceInterface service : this.pushServices.values()) {
			service.setExecutor(executor);
		}
		for (ServiceInterface service : this.pullServices.values()) {
			service.setExecutor(executor);
		}
		for (ServiceInterface service : this.pullingServices.values()) {
			service.setExecutor(executor);
		}
	}

	@Override
	public Executor getExecutor() {
		return this.executor;
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
	public void activatePolling() {
		if (!isPollingActivated()) {
			this.pullingServices.get(this.pullingService).activatePolling();
		}
	}

	@Override
	public void deactivatePolling() {
		if (isPollingActivated()) {
			this.pullingServices.get(this.pullingService).deactivatePolling();
		}
	}

	@Override
	public boolean isPollingActivated() {
		return this.pullingServices.get(this.pullingService)
				.isPollingActivated();
	}

	@Override
	public void onContentSet(String content) {
		this.currentContent = content;
	}

	@Override
	public void onContentGet(String content) {
		this.currentContent = content;
	}

}
