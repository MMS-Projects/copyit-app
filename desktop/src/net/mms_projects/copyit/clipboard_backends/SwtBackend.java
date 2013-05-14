package net.mms_projects.copyit.clipboard_backends;

import net.mms_projects.copyit.ClipboardListener;
import net.mms_projects.copyit.PollingServiceInterface;
import net.mms_projects.copyit.clipboard_services.CopyServiceInterface;
import net.mms_projects.copyit.clipboard_services.PasteServiceInterface;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;

public class SwtBackend implements CopyServiceInterface, PasteServiceInterface,
		PollingServiceInterface {

	public static String SERVICE_NAME = "swt";
	
	private Clipboard clipboard;
	private ClipboardListener listener;
	private boolean pollingEnabled;
	private String currentContent;

	public SwtBackend(final ClipboardListener listener) {
		this.listener = listener;
		this.clipboard = new Clipboard(Display.getDefault());

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (!pollingEnabled) {
						continue;
					}
					
					getContent(false);
				}
			}
		}).start();
	}

	@Override
	public void setContent(final String text) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				TextTransfer textTransfer = TextTransfer.getInstance();
				clipboard.setContents(new Object[] { text },
						new Transfer[] { textTransfer });
				currentContent = text;
				listener.onContentSet(text);
			}
		});
	}

	@Override
	public void getContent() {
		this.getContent(true);
	}
	
	private void getContent(final boolean allowNull) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				TextTransfer transfer = TextTransfer.getInstance();
				String data = (String) clipboard.getContents(transfer);
				if ((data == null) && (!allowNull)) {
					return;
				}
				if (!data.equals(currentContent)) {
					currentContent = data;
					listener.onContentGet(data);
				}
			}

		});
	}

	@Override
	public String getServiceName() {
		return SERVICE_NAME;
	}

	@Override
	public void activatePaste() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deactivatePaste() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isPasteActivated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void activateCopy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deactivateCopy() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isCopyActivated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void activatePolling() {
		this.pollingEnabled = true;
	}

	@Override
	public void deactivatePolling() {
		this.pollingEnabled = false;
	}

	@Override
	public boolean isPollingActivated() {
		return this.pollingEnabled;
	}
}
