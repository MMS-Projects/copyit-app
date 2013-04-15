package net.mms_projects.copyit.android.tasks;

import net.mms_projects.copyit.ClipboardUtils;
import net.mms_projects.copyit.R;
import net.mms_projects.copyit.api.ServerApi;
import net.mms_projects.copyit.api.endpoints.ClipboardContentEndpoint;
import android.content.Context;
import android.widget.Toast;

public class PasteItTask extends ServerApiUiTask<Void, Void, String> {
	public PasteItTask(Context context, ServerApi api) {
		super(context, api);
	}

	@Override
	protected String doInBackground(Void... params) {
		try {
			return new ClipboardContentEndpoint(api).get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String content) {
		ClipboardUtils clipboard = new ClipboardUtils(this.context);

		Toast.makeText(
				this.context,
				this.context.getResources().getString(
						R.string.text_content_pulled, content),
				Toast.LENGTH_LONG).show();
		clipboard.setText(content);
		
		super.onPostExecute(content);
	}
}