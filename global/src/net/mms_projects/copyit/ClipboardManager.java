package net.mms_projects.copyit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mms_projects.copyit.clipboard_services.CopyServiceInterface;
import net.mms_projects.copyit.clipboard_services.PasteServiceInterface;

public class ClipboardManager implements CopyServiceInterface,
		PasteServiceInterface, ClipboardListener {

	private Map<String, CopyServiceInterface> copyServices = new HashMap<String, CopyServiceInterface>();
	private Map<String, PasteServiceInterface> pasteServices = new HashMap<String, PasteServiceInterface>();
	private String copyService;
	private String pasteService;
	private List<ClipboardListener> listeners = new ArrayList<ClipboardListener>();

	public void addCopyService(CopyServiceInterface service) {
		this.copyServices.put(service.getServiceName(), service);
		if (this.copyService == null) {
			this.copyService = service.getServiceName();
			this.copyServices.get(this.copyService).activateCopy();
		}
	}

	public void addPasteService(PasteServiceInterface service) {
		this.pasteServices.put(service.getServiceName(), service);
		if (this.pasteService == null) {
			this.pasteService = service.getServiceName();
			this.pasteServices.get(this.pasteService).activatePaste();
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
		this.activateCopy();
	}

	public void setPullService(String service) {
		if (!this.pasteServices.containsKey(service)) {
			return;
		}
		this.deactivatePaste();
		this.pasteService = service;
		this.activatePaste();
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
		if (!this.isCopyActivated()) {
			this.copyServices.get(this.copyService).activateCopy();
		}
	}

	@Override
	public void deactivateCopy() {
		if (this.isCopyActivated()) {
			this.copyServices.get(this.pasteService).deactivateCopy();
		}
	}

	@Override
	public boolean isCopyActivated() {
		return this.copyServices.get(this.copyService).isCopyActivated();
	}

	@Override
	public void activatePaste() {
		if (!this.isPasteActivated()) {
			this.pasteServices.get(this.pasteService).activatePaste();
		}
	}

	@Override
	public void deactivatePaste() {
		if (!this.isPasteActivated()) {
			this.pasteServices.get(this.pasteService).deactivatePaste();
		}
	}

	@Override
	public boolean isPasteActivated() {
		return this.pasteServices.get(this.pasteService).isPasteActivated();
	}

}
