package net.mms_projects.copy_it;

import java.util.Date;
import java.util.concurrent.Executor;

import net.mms_projects.copy_it.api.endpoints.ClipboardContentEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SyncingThread extends Thread implements PollingServiceInterface {

	public static String SERVICE_NAME = "polling";

	private boolean enabled;
	private int delay = 5;
	private String currentContent;
	private ClipboardContentEndpoint endpoint;
	private SyncListener listener;
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private Executor executor;

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

				this.executor.execute(new RefreshClipboard(this.endpoint));
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
	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

	@Override
	public Executor getExecutor() {
		return this.executor;
	}

	@Override
	public void activatePolling() {
		this.enabled = true;
		log.debug("The service has been enabled");
	}

	@Override
	public void deactivatePolling() {
		this.enabled = false;
		log.debug("The service has been disabled");
	}

	@Override
	public boolean isPollingActivated() {
		return this.enabled;
	}

}
