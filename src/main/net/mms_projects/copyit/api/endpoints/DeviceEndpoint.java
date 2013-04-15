package net.mms_projects.copyit.api.endpoints;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.mms_projects.copyit.api.ApiEndpoint;
import net.mms_projects.copyit.api.ServerApi;
import net.mms_projects.copyit.api.responses.DeviceCreateResponse;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

public class DeviceEndpoint extends ApiEndpoint {

	public DeviceEndpoint(ServerApi api) {
		super(api, "device");
		
		this.setCreateResponseType(DeviceCreateResponse.class);
	}
	
	public boolean create(String hostname) throws ClientProtocolException, IOException {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("device_name", hostname));

		DeviceCreateResponse response = (DeviceCreateResponse) super.create(this.api.deviceId.toString(), parameters);
		
		return response.messages.isEmpty();
	}

}
