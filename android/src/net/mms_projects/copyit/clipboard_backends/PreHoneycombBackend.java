package net.mms_projects.copyit.clipboard_backends;

import android.content.Context;
import net.mms_projects.copy_it.R;

@SuppressWarnings("deprecation")
public class PreHoneycombBackend extends AbstractAndroidClipboardBackend {

	public PreHoneycombBackend(Context context) {
		super(context);
	}

	@Override
	public String getText() {
		android.text.ClipboardManager clipboard = (android.text.ClipboardManager) this.context.getSystemService(Context.CLIPBOARD_SERVICE);
		return clipboard.getText().toString();
	}

	@Override
	public void setText(String text) {
		android.text.ClipboardManager clipboard = (android.text.ClipboardManager) this.context.getSystemService(Context.CLIPBOARD_SERVICE);
		clipboard.setText(text);
	}

}
