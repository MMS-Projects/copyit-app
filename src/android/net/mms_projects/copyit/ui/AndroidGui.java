package net.mms_projects.copyit.ui;

import java.util.UUID;

import net.mms_projects.copyit.ServerApi;
import net.mms_projects.copyit.Settings;
import net.mms_projects.copyit.ui.android.MainActivity;

public class AndroidGui extends AbstractUi {

	protected MainActivity activity;
	protected ServerApi api;
	
	public AndroidGui(Settings settings, MainActivity activity) {
		super(settings);
		
		this.api = new ServerApi();
		if (settings.get("device.id") != null) {
			this.api.deviceId = UUID.fromString(settings.get("device.id"));
			this.api.devicePassword = settings.get("device.password");
		}
		this.api.apiUrl = settings.get("server.baseurl");
		
		this.activity = activity;
	}

	@Override
	public void open() {
		this.activity.setup(this.settings, this.api);
	}

}
