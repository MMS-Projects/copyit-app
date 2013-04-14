package net.mms_projects.copyit.android.tasks;

import net.mms_projects.copyit.R;
import net.mms_projects.copyit.api.ServerApi;
import net.mms_projects.copyit.api.endpoints.ClipboardContentEndpoint;
import android.content.Context;
import android.widget.Toast;

public class CopyItTask extends ServerApiUiTask<String, Void, Boolean> {
	public CopyItTask(Context context, ServerApi api) {
		super(context, api);
	}

	private String content;

	@Override
	protected Boolean doInBackground(String... params) {
		this.content = params[0];

		try {
			return new ClipboardContentEndpoint(api).update(this.content);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		System.out.println(result);
		if (result) {
			Toast.makeText(
					this.context,
					this.context.getResources().getString(
							R.string.text_content_pushed, this.content),
					Toast.LENGTH_LONG).show();
		}
	}
}