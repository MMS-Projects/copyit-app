package net.mms_projects.copyit.ui.swt;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import net.mms_projects.copyit.ServerApi;
import net.mms_projects.copyit.Settings;
import net.mms_projects.copyit.ui.swt.dialog_response.LoginResponse;
import net.mms_projects.copyit.ui.swt.forms.AutoLoginDialog;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

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

	public void doLogin(Shell dialogParent) {
		AutoLoginDialog dialog = new AutoLoginDialog(dialogParent, this.settings);
		LoginResponse response = dialog.open();

		if (response.deviceId == null) {
			return;
		}
		
		try {
			this.settings.set("device.id", response.deviceId.toString());
			this.settings.set("device.password", response.devicePassword);
			this.settings.saveProperties();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.api.deviceId = response.deviceId;
		this.api.devicePassword = response.devicePassword;
		
		try {
			InetAddress addr = InetAddress.getLocalHost();
			String hostname = addr.getHostName();

			this.api.initDevice(hostname);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void doDataSet() {
		TextTransfer transfer = TextTransfer.getInstance();
		String data = (String) this.clipboard.getContents(transfer);
		if (data != null) {
			try {
				this.api.set(data);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void doDataGet() {
		String data;
		try {
			data = this.api.get();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		TextTransfer textTransfer = TextTransfer.getInstance();
		this.clipboard.setContents(new Object[] { data },
				new Transfer[] { textTransfer });
	}

}