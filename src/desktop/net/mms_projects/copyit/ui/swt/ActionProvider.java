package net.mms_projects.copyit.ui.swt;

import java.util.UUID;

import net.mms_projects.copyit.Settings;
import net.mms_projects.copyit.api.ServerApi;
import net.mms_projects.copyit.api.endpoints.ClipboardContentEndpoint;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;

public class ActionProvider {

	protected Clipboard clipboard;
	protected ServerApi api;
	protected Settings settings;

	public ActionProvider(Display display, Settings settings) {
		this.clipboard = new Clipboard(display);
		this.api = new ServerApi();
		this.settings = settings;

		if (settings.get("device.id") != null) {
			this.api.deviceId = UUID.fromString(settings.get("device.id"));
			this.api.devicePassword = settings.get("device.password");
		}
		this.api.apiUrl = settings.get("server.baseurl");
	}

	public void doDataSet() {
		TextTransfer transfer = TextTransfer.getInstance();
		String data = (String) this.clipboard.getContents(transfer);
		if (data != null) {
			try {
				new ClipboardContentEndpoint(this.api).update(data);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void doDataGet() {
		String data;
		try {
			data = new ClipboardContentEndpoint(this.api).get();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		TextTransfer textTransfer = TextTransfer.getInstance();
		this.clipboard.setContents(new Object[] { data },
				new Transfer[] { textTransfer });
	}

}