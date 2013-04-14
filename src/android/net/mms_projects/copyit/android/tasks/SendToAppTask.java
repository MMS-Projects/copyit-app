package net.mms_projects.copyit.android.tasks;

import net.mms_projects.copyit.R;
import net.mms_projects.copyit.api.ServerApi;
import net.mms_projects.copyit.api.endpoints.ClipboardContentEndpoint;
import android.content.Context;
import android.content.Intent;

public class SendToAppTask extends ServerApiUiTask<Void, Void, String> {
	public SendToAppTask(Context context, ServerApi api) {
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
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, content);
		sendIntent.setType("text/plain");
		this.context.startActivity(Intent.createChooser(
				sendIntent,
				this.context.getResources().getString(
						R.string.dialog_title_select_send_app)));
	}
}