package net.mms_projects.copyit;

import net.mms_projects.copyit.dialog_response.LoginResponse;
import net.mms_projects.copyit.forms.LoginDialog;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ActionProvider {

	protected Clipboard clipboard;
	protected ServerApi api;

	public ActionProvider(Display display) {
		this.clipboard = new Clipboard(display);
		this.api = new ServerApi();
	}

	public void doLogin(Shell dialogParent) {
		LoginDialog dialog = new LoginDialog(dialogParent);
		LoginResponse response = dialog.open();

		this.api.deviceId = response.deviceId;
		this.api.devicePassword = response.devicePassword;
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
