package net.mms_projects.copyit;

import java.util.Date;

import net.mms_projects.copyit.api.endpoints.ClipboardContentEndpoint;

import org.eclipse.swt.widgets.Display;

public class SyncingThread extends Thread implements PollingServiceInterface {

	public static String SERVICE_NAME = "polling";

	private boolean enabled;
	private int delay = 5;
	private String currentContent;
	private ClipboardContentEndpoint endpoint;
	private SyncListener listener;

	public SyncingThread(SyncListener listener,
			ClipboardContentEndpoint endpoint) {
		this.listener = listener;
		this.endpoint = endpoint;

		this.setDaemon(true);
		this.start();
	}

	public void setEndpoint(ClipboardContentEndpoint endpoint) {
		this.endpoint = endpoint;
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			try {
				Thread.sleep(this.delay * 1000);

				if (!this.enabled) {
					return;
				}

				Display.getDefault().asyncExec(
						new RefreshClipboard(this.endpoint));
			} catch (InterruptedException e) {
				this.interrupt();
			}
		}
	}

	private class RefreshClipboard implements Runnable {
		private ClipboardContentEndpoint endpoint;

		public RefreshClipboard(ClipboardContentEndpoint endpoint) {
			this.endpoint = endpoint;
		}

		@Override
		public void run() {
			String newData = "";
			try {
				newData = this.endpoint.get();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if ((SyncingThread.this.currentContent != null)
					&& (SyncingThread.this.currentContent.equals(newData))) {
				return;
			}
			
			SyncingThread.this.currentContent = newData;
			
			if (newData.length() == 0) {
				return;
			}

			listener.onPulled(newData, new Date());
		}
	}

	@Override
	public String getServiceName() {
		return SERVICE_NAME;
	}

	@Override
	public void activatePolling() {
		this.enabled = true;
	}

	@Override
	public void deactivatePolling() {
		this.enabled = false;
	}

	@Override
	public boolean isPollingActivated() {
		return this.enabled;
	}

}
