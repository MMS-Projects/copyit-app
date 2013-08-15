package net.mms_projects.copy_it;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import net.mms_projects.copy_it.clipboard_services.ClipboardServiceInterface;
import net.mms_projects.copy_it.listeners.EnabledListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClipboardManager implements 
		ClipboardServiceInterface, PollingServiceInterface, ClipboardListener {

	private Map<String, ClipboardServiceInterface> clipboardServices = new HashMap<String, ClipboardServiceInterface>();
	private Map<String, PollingServiceInterface> pollingServices = new HashMap<String, PollingServiceInterface>();
	private String clipboardService;
	private String pollingService;
	private boolean enabled = true;
	private boolean pollingEnabled = true;
	private List<ClipboardListener> listeners = new ArrayList<ClipboardListener>();
	private Executor executor;
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public void addClipboardService(ClipboardServiceInterface service) {
		if (this.executor == null) {
			log.error("Before adding a service please define a executor");
			return;
		}
		service.setExecutor(executor);
		
		this.clipboardServices.put(service.getServiceName(), service);
		if (this.clipboardService == null) {
			this.clipboardService = service.getServiceName();
			if (this.isEnabled()) {
				this.clipboardServices.get(this.clipboardService).enable();
			}
		}
	}

	public void addPollingService(PollingServiceInterface service) {
		if (this.executor == null) {
			log.error("Before adding a service please define a executor");
			return;
		}
		service.setExecutor(executor);
		
		this.pollingServices.put(service.getServiceName(), service);
		if (this.pollingService == null) {
			this.pollingService = service.getServiceName();
			if (this.isPollingActivated()) {
				this.pollingServices.get(this.pollingService).activatePolling();
			}
		}
	}

	public void addListener(ClipboardListener listener) {
		this.listeners.add(listener);
	}

	public void setClipboardService(String service) {
		if (!this.clipboardServices.containsKey(service)) {
			return;
		}
		this.disable();
		this.clipboardService = service;
		if (this.isEnabled()) {
			this.enable();
		}
	}

	public boolean isClipboardServiceDefined() {
		if (this.clipboardServices.isEmpty()) {
			return false;
		}
		if (!this.clipboardServices.containsKey(this.clipboardService)) {
			return false;
		}
		return true;
	}

	public void setPollingService(String service) {
		if (!this.pollingServices.containsKey(service)) {
			return;
		}
		this.deactivatePolling();
		this.pollingService = service;
		if (this.isPollingActivated()) {
			this.activatePolling();
		}
	}

	public boolean isPollingServiceDefined() {
		if (this.pollingServices.isEmpty()) {
			return false;
		}
		if (!this.pollingServices.containsKey(this.pollingService)) {
			return false;
		}
		return true;
	}
	
	@Deprecated
	@Override
	public void requestSet(String content) {
		if (this.clipboardServices.isEmpty()) {
			return;
		}
		if (!this.clipboardServices.containsKey(this.clipboardService)) {
			return;
		}
		this.clipboardServices.get(this.clipboardService).requestSet(content);
	}

    @Override
    public void setContent(String content) {
        if (this.clipboardServices.isEmpty()) {
            return;
        }
        if (!this.clipboardServices.containsKey(this.clipboardService)) {
            return;
        }
        this.clipboardServices.get(this.clipboardService).setContent(content);
    }

    @Override
    public String getContent() {
        if (this.clipboardServices.isEmpty()) {
            return null;
        }
        if (!this.clipboardServices.containsKey(this.clipboardService)) {
            return null;
        }
        return this.clipboardServices.get(this.clipboardService).getContent();
    }

    @Override
	public void onClipboardContentChange(String content) {
		for (ClipboardListener listener : this.listeners) {
			listener.onClipboardContentChange(content);
		}
	}

	@Override
	public String getServiceName() {
		return "manager";
	}

	@Override
	public void setExecutor(Executor executor) {
		this.executor = executor;
		
		for (ServiceInterface service : this.clipboardServices.values()) {
			service.setExecutor(executor);
		}
		for (ServiceInterface service : this.pollingServices.values()) {
			service.setExecutor(executor);
		}
	}

	@Override
	public Executor getExecutor() {
		return this.executor;
	}

	@Override
	public void enable() {
		if (this.isClipboardServiceDefined()
				&& !this.clipboardServices.get(this.clipboardService).isEnabled()) {
			this.clipboardServices.get(this.clipboardService).enable();
		}
		this.enabled = true;
	}

	@Override
	public void disable() {
		if (this.isClipboardServiceDefined()
				&& this.clipboardServices.get(this.clipboardService).isEnabled()) {
			this.clipboardServices.get(this.clipboardService).disable();
		}
		this.enabled = false;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	public void activatePolling() {
		if (this.isPollingServiceDefined()
				&& !this.pollingServices.get(this.pollingService)
						.isPollingActivated()) {
			this.pollingServices.get(this.pollingService).activatePolling();
		}
		this.pollingEnabled = true;
	}

	@Override
	public void deactivatePolling() {
		if (this.isPollingServiceDefined()
				&& this.pollingServices.get(this.pollingService)
						.isPollingActivated()) {
			this.pollingServices.get(this.pollingService).deactivatePolling();
		}
		this.pollingEnabled = false;
	}

	@Override
	public boolean isPollingActivated() {
		return this.pollingEnabled;
	}

    @Override
    public void setEnabled(boolean enabled) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addEnabledListener(EnabledListener listener) {
        // TODO Auto-generated method stub
        
    }

}
