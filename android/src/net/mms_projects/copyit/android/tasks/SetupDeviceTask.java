package net.mms_projects.copyit.android.tasks;

import java.net.InetAddress;

import net.mms_projects.copy_it.R;
import net.mms_projects.copyit.api.ServerApi;
import net.mms_projects.copyit.api.endpoints.DeviceEndpoint;
import android.content.Context;

public class SetupDeviceTask extends ServerApiUiTask<Void, Void, Boolean> {

	public SetupDeviceTask(Context context, ServerApi api) {
		super(context, api);

		this.setUseProgressDialog(true);
	}

	@Override
	protected Boolean doInBackgroundWithException(Void... params)
			throws Exception {
		InetAddress addr = InetAddress.getLocalHost();
		String hostname = addr.getHostName();

		return new DeviceEndpoint(api).create(hostname);
	}

}