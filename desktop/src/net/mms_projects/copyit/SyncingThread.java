package net.mms_projects.copyit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.mms_projects.copyit.api.ServerApi;
import net.mms_projects.copyit.api.endpoints.ClipboardContentEndpoint;

import org.eclipse.swt.widgets.Display;

public class SyncingThread extends Thread implements SettingsListener {

	private boolean enabled;
	private int delay = 5;
	private Settings settings;
	private List<SyncingListener> listeners = new ArrayList<SyncingListener>();
	private String currentContent;

	public SyncingThread(Settings settings) {
		this.settings = settings;

		this.addListener(new SyncingListener() {
			@Override
			public void onClipboardChange(String data, Date date) {
				System.out.println("New clipboard content: " + data);
				System.out.println("Date: " + date.toString());
			}

			@Override
			public void onPreSync() {
			}

			@Override
			public void onPostSync() {
			}
		});
	}

	public void setEnabled(boolean state) {
		this.enabled = state;
	}

	public void addListener(SyncingListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			try {
				Thread.sleep(this.delay * 1000);

				if (!this.enabled) {
					return;
				}

				for (SyncingListener listener : listeners) {
					listener.onPreSync();
				}

				ServerApi api = new ServerApi();
				api.deviceId = UUID.fromString(this.settings.get("device.id"));
				api.devicePassword = this.settings.get("device.password");
				api.apiUrl = this.settings.get("server.baseurl");

				Display.getDefault().asyncExec(new RefreshClipboard(api));

				for (SyncingListener listener : listeners) {
					listener.onPostSync();
				}
			} catch (InterruptedException e) {
				this.interrupt();
			}
		}
	}

	private class RefreshClipboard implements Runnable {
		private ServerApi api;

		public RefreshClipboard(ServerApi api) {
			this.api = api;
		}

		@Override
		public void run() {
			String newData = "";
			try {
				newData = new ClipboardContentEndpoint(api).get();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if ((SyncingThread.this.currentContent != null)
					&& (SyncingThread.this.currentContent.equals(newData))) {
				return;
			}

			SyncingThread.this.currentContent = newData;

			for (SyncingListener listener : listeners) {
				listener.onClipboardChange(newData, new Date());
			}
		}
	}

	@Override
	public void onChange(String key, String value) {
		if ("sync.polling.enabled".equals(key)) {
			this.setEnabled(Boolean.parseBoolean(value));
		}
	}
}
