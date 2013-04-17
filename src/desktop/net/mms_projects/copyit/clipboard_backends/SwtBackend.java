package net.mms_projects.copyit.clipboard_backends;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;

public class SwtBackend extends AbstractClipboardBackend {

	private Clipboard clipboard;

	public SwtBackend() {
		this.clipboard = new Clipboard(Display.getDefault());
	}

	@Override
	public String getText() {
		TextTransfer transfer = TextTransfer.getInstance();
		String data = (String) this.clipboard.getContents(transfer);
		return data;
	}

	@Override
	public void setText(String text) {
		TextTransfer textTransfer = TextTransfer.getInstance();
		this.clipboard.setContents(new Object[] { text },
				new Transfer[] { textTransfer });
	}

}
