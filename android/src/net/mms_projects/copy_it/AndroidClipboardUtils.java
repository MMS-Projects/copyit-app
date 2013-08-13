package net.mms_projects.copy_it;

import java.util.concurrent.Executor;

import net.mms_projects.copy_it.clipboard_backends.AbstractClipboardBackend;
import net.mms_projects.copy_it.clipboard_backends.HoneycombBackend;
import net.mms_projects.copy_it.clipboard_backends.PreHoneycombBackend;
import net.mms_projects.copy_it.clipboard_services.ClipboardServiceInterface;
import android.content.Context;

public class AndroidClipboardUtils implements ClipboardServiceInterface {

	private Context context;

	private AbstractClipboardBackend backend;

	public AndroidClipboardUtils(Context context) {
		this.context = context;

		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
			this.setClipboardBackend(new PreHoneycombBackend(this.context));
		} else {
			this.setClipboardBackend(new HoneycombBackend(this.context));
		}
	}

	public void setClipboardBackend(AbstractClipboardBackend backend) {
		this.backend = backend;
	}

	@Override
	public void setExecutor(Executor executor) {
		// TODO Auto-generated method stub

	}

	@Override
	public Executor getExecutor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServiceName() {
		// TODO Auto-generated method stub
		return "android";
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
	public String getContent() {
		return this.backend.getText();
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
	public void requestSet(String content) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setContent(String content) {
		this.backend.setText(content);
	}

}
