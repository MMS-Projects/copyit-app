package net.mms_projects.copyit.clipboard_backends;

import net.mms_projects.copyit.ClipboardListener;
import net.mms_projects.copyit.clipboard_services.CopyServiceInterface;
import net.mms_projects.copyit.clipboard_services.PasteServiceInterface;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;

public class SwtBackend implements CopyServiceInterface, PasteServiceInterface {

	private Clipboard clipboard;
	private ClipboardListener listener;

	public SwtBackend(ClipboardListener listener) {
		this.listener = listener;
		this.clipboard = new Clipboard(Display.getDefault());
	}

	@Override
	public void setContent(final String text) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				TextTransfer textTransfer = TextTransfer.getInstance();
				clipboard.setContents(new Object[] { text },
						new Transfer[] { textTransfer });
				listener.onContentSet(text);
			}
		});
	}

	@Override
	public void getContent() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				TextTransfer transfer = TextTransfer.getInstance();
				String data = (String) clipboard.getContents(transfer);
				listener.onContentGet(data);
			}

		});
	}

	@Override
	public String getServiceName() {
		// TODO Auto-generated method stub
		return null;
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
}
