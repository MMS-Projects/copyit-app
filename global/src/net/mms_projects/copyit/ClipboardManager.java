package net.mms_projects.copyit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mms_projects.copyit.clipboard_services.CopyServiceInterface;
import net.mms_projects.copyit.clipboard_services.PasteServiceInterface;

public class ClipboardManager implements CopyServiceInterface,
		PasteServiceInterface, PollingServiceInterface, ClipboardListener {

	private Map<String, CopyServiceInterface> copyServices = new HashMap<String, CopyServiceInterface>();
	private Map<String, PasteServiceInterface> pasteServices = new HashMap<String, PasteServiceInterface>();
	private Map<String, PollingServiceInterface> pollingServices = new HashMap<String, PollingServiceInterface>();
	private String copyService;
	private String pasteService;
	private String pollingService;
	private boolean copyEnabled = true;
	private boolean pasteEnabled = true;
	private boolean pollingEnabled = true;
	private List<ClipboardListener> listeners = new ArrayList<ClipboardListener>();

	public void addCopyService(CopyServiceInterface service) {
		this.copyServices.put(service.getServiceName(), service);
		if (this.copyService == null) {
			this.copyService = service.getServiceName();
			if (this.isCopyActivated()) {
				this.copyServices.get(this.copyService).activateCopy();
			}
		}
	}

	public void addPasteService(PasteServiceInterface service) {
		this.pasteServices.put(service.getServiceName(), service);
		if (this.pasteService == null) {
			this.pasteService = service.getServiceName();
			if (this.isPasteActivated()) {
				this.pasteServices.get(this.pasteService).activatePaste();
			}
		}
	}

	public void addPollingService(PollingServiceInterface service) {
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

	public void setCopyService(String service) {
		if (!this.copyServices.containsKey(service)) {
			return;
		}
		this.deactivateCopy();
		this.copyService = service;
		if (this.isCopyActivated()) {
			this.activateCopy();
		}
	}

	public boolean isCopyServiceDefined() {
		if (this.copyServices.isEmpty()) {
			return false;
		}
		if (!this.copyServices.containsKey(this.copyService)) {
			return false;
		}
		return true;
	}

	public void setPasteService(String service) {
		if (!this.pasteServices.containsKey(service)) {
			return;
		}
		this.deactivatePaste();
		this.pasteService = service;
		if (this.isPasteActivated()) {
			this.activatePaste();
		}
	}

	public boolean isPasteServiceDefined() {
		if (this.pasteServices.isEmpty()) {
			return false;
		}
		if (!this.pasteServices.containsKey(this.pasteService)) {
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

	@Override
	public void setContent(String content) {
		if (this.copyServices.isEmpty()) {
			return;
		}
		if (!this.copyServices.containsKey(this.copyService)) {
			return;
		}
		this.copyServices.get(this.copyService).setContent(content);
	}

	@Override
	public void getContent() {
		if (this.pasteServices.isEmpty()) {
			return;
		}
		if (!this.pasteServices.containsKey(this.pasteService)) {
			return;
		}
		this.pasteServices.get(this.pasteService).getContent();
	}

	@Override
	public void onContentSet(String content) {
		for (ClipboardListener listener : this.listeners) {
			listener.onContentSet(content);
		}
	}

	@Override
	public void onContentGet(String content) {
		for (ClipboardListener listener : this.listeners) {
			listener.onContentGet(content);
		}
	}

	@Override
	public String getServiceName() {
		return "manager";
	}

	@Override
	public void activateCopy() {
		if (this.isCopyServiceDefined()
				&& !this.copyServices.get(this.copyService).isCopyActivated()) {
			this.copyServices.get(this.copyService).activateCopy();
		}
		this.copyEnabled = true;
	}

	@Override
	public void deactivateCopy() {
		if (this.isCopyServiceDefined()
				&& this.copyServices.get(this.copyService).isCopyActivated()) {
			this.copyServices.get(this.pasteService).deactivateCopy();
		}
		this.copyEnabled = false;
	}

	@Override
	public boolean isCopyActivated() {
		return this.copyEnabled;
	}

	@Override
	public void activatePaste() {
		if (this.isPasteServiceDefined()
				&& !this.pasteServices.get(this.pasteService)
						.isPasteActivated()) {
			this.pasteServices.get(this.pasteService).activatePaste();
		}
		this.pasteEnabled = true;
	}

	@Override
	public void deactivatePaste() {
		if (this.isPasteServiceDefined()
				&& this.pasteServices.get(this.pasteService).isPasteActivated()) {
			this.pasteServices.get(this.pasteService).deactivatePaste();
		}
		this.pasteEnabled = false;
	}

	@Override
	public boolean isPasteActivated() {
		return this.pasteEnabled;
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

}
