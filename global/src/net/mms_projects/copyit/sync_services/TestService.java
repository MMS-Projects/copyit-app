package net.mms_projects.copyit.sync_services;

import java.util.Date;

import net.mms_projects.copyit.SyncListener;

public class TestService implements PushServiceInterface, PullingServiceInterface {

	public static String SERVICE_NAME = "test";
	
	protected SyncListener listener;
	private boolean pushEnabled;
	private boolean pullingEnabled;

	public TestService(SyncListener listener) {
		this.listener = listener;
	}

	@Override
	public String getServiceName() {
		return SERVICE_NAME;
	}
	
	@Override
	public void activatePulling() {
		this.pullingEnabled = true;
		System.out.println("Activated! Faking pulls");
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					listener.onPulled("Random content 1", new Date());
					Thread.sleep(500);
					listener.onPulled("Random content 2", new Date());
					Thread.sleep(100);
					listener.onPulled("Random content 3", new Date());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}).start();
	}

	@Override
	public void deactivatePulling() {
		this.pullingEnabled = false;
		System.out.println("Deactivated!");
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
	public void doPush(final String content, final Date date) {
		new Thread(new Runnable() {

			@Override
			public void run() {
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
		}).start();
	}

	@Override
	public boolean isPullingActivated() {
		return this.pullingEnabled;
	}

	@Override
	public boolean isPushActivated() {
		return this.pushEnabled;
	}

}
