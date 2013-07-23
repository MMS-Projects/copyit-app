package net.mms_projects.copy_it.clipboard_backends;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import net.mms_projects.copy_it.ClipboardListener;
import net.mms_projects.copy_it.PollingServiceInterface;
import net.mms_projects.copy_it.clipboard_services.CopyServiceInterface;
import net.mms_projects.copy_it.clipboard_services.PasteServiceInterface;

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
	private Executor executor;

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

					String newContent = getContent();
					if (newContent != null) {
						/*
						 * Check if the clipboard content is really new.
						 */
						if (!newContent.equals(currentContent)) {
							/*
							 * If so update the current content and broadcast to
							 * all the listeners
							 */
							currentContent = newContent;
							listener.onClipboardContentChange(newContent);
						}
					}
				}
			}
		}).start();
	}

	@Override
	public void requestSet(final String text) {
		this.getExecutor().execute(new Runnable() {
			@Override
			public void run() {
				setContent(text);
			}
		});
	}

	@Override
	public String getContent() {
		final CountDownLatch countDownLatch = new CountDownLatch(1);

		final String[] content = new String[1];

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				TextTransfer transfer = TextTransfer.getInstance();
				String data = (String) clipboard.getContents(transfer);
				content[0] = data;

				countDownLatch.countDown();
			}

		});

		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}

		return content[0];
	}

	@Override
	public void setContent(final String content) {
		if (content == null) {
			return;
		}

		final CountDownLatch countDownLatch = new CountDownLatch(1);

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				TextTransfer textTransfer = TextTransfer.getInstance();
				clipboard.setContents(new Object[] { content },
						new Transfer[] { textTransfer });
				currentContent = content;

				countDownLatch.countDown();
			}
		});

		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}
	}

	@Override
	public String getServiceName() {
		return SERVICE_NAME;
	}

	@Override
	public Executor getExecutor() {
		return this.executor;
	}

	@Override
	public void setExecutor(Executor executor) {
		this.executor = executor;
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
