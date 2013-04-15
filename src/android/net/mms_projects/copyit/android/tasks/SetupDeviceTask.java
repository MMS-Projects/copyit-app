package net.mms_projects.copyit.android.tasks;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.mms_projects.copyit.api.ServerApi;
import net.mms_projects.copyit.api.endpoints.DeviceEndpoint;
import android.content.Context;

public class SetupDeviceTask extends ServerApiUiTask<Void, Void, Boolean> {

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
		} catch (UnknownHostException exception) {
			// TODO Auto-generated catch block
			
			this.exception = exception;
			exception.printStackTrace();
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			
			this.exception = exception;	
			exception.printStackTrace();
		}

		return false;
	}

}