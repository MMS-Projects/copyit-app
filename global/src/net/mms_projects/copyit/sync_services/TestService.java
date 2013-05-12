package net.mms_projects.copyit.sync_services;

import java.util.Date;

import net.mms_projects.copyit.SyncListener;

public class TestService implements PushServiceInterface, PullingServiceInterface {

	protected SyncListener listener;

	public TestService(SyncListener listener) {
		this.listener = listener;
	}

	@Override
	public String getServiceName() {
		return "test";
	}
	
	@Override
	public void activatePulling() {
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
		System.out.println("Deactivated!");
	}
	
	@Override
	public void activatePush() {
	}

	@Override
	public void deactivatePush() {
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

}
