package net.mms_projects.copy_it.sync_services;

import java.util.Date;
import java.util.concurrent.Executor;

import net.mms_projects.copy_it.PollingServiceInterface;
import net.mms_projects.copy_it.SyncListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestService implements PushServiceInterface,
		PollingServiceInterface {

	public static String SERVICE_NAME = "test";

	protected SyncListener listener;
	private boolean pushEnabled;
	private boolean pullingEnabled;
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private Executor executor;

	public TestService(SyncListener listener) {
		this.listener = listener;
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
		this.pullingEnabled = true;
		log.debug("Activated! Faking pulls");

		this.executor.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					listener.onRemoteContentChange("Random content 1", new Date());
					Thread.sleep(500);
					listener.onRemoteContentChange("Random content 2", new Date());
					Thread.sleep(100);
					listener.onRemoteContentChange("Random content 3", new Date());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
	}

	@Override
	public void deactivatePolling() {
		this.pullingEnabled = false;
		log.debug("Deactivated!");
	}

	@Override
	public void activatePush() {
		this.pushEnabled = true;
	}

	@Override
	public void deactivatePush() {
		this.pushEnabled = false;
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
	public boolean isPollingActivated() {
		return this.pullingEnabled;
	}

	@Override
	public boolean isPushActivated() {
		return this.pushEnabled;
	}

	@Override
	public void setRemoteContent(String content, Date date) {
		System.out
				.println("Contacting server blabla taking long time blabla....");

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		listener.onPushed(content, date);
	}

}
