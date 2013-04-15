package net.mms_projects.copyit.android.tasks;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.mms_projects.copyit.R;
import net.mms_projects.copyit.api.ServerApi;
import net.mms_projects.copyit.api.endpoints.DeviceEndpoint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class SetupDeviceTask extends ServerApiUiTask<Void, Void, Boolean> {

	private Exception exception;

	public SetupDeviceTask(Context context, ServerApi api) {
		super(context, api);

		this.setUseProgressDialog(true);
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			String hostname = addr.getHostName();

			return new DeviceEndpoint(api).create(hostname);
		} catch (UnknownHostException event) {
			// TODO Auto-generated catch block
			event.printStackTrace();
		} catch (Exception event) {
			// TODO Auto-generated catch block
			event.printStackTrace();
		}

		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (!result) {
			AlertDialog alertDialog = new AlertDialog.Builder(this.context)
					.create();
			alertDialog.setTitle(this.context.getResources().getString(
					R.string.dialog_title_error));
			alertDialog.setMessage(this.context.getResources().getString(
					R.string.error_device_setup_failed,
					this.exception.getMessage()));
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, this.context
					.getResources().getString(R.string.dialog_button_okay),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			alertDialog.setIcon(R.drawable.ic_launcher);
			alertDialog.show();
			return;
		}
		Toast.makeText(
				this.context,
				this.context.getResources().getString(
						R.string.text_login_successful), Toast.LENGTH_SHORT)
				.show();

		super.onPostExecute(result);
	}
}