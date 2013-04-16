package net.mms_projects.copyit.api.endpoints;

import java.util.ArrayList;
import java.util.List;

import net.mms_projects.copyit.api.ApiEndpoint;
import net.mms_projects.copyit.api.ServerApi;
import net.mms_projects.copyit.api.responses.ClipboardContentGetResponse;
import net.mms_projects.copyit.api.responses.ClipboardContentUpdateResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class ClipboardContentEndpoint extends ApiEndpoint {

	public ClipboardContentEndpoint(ServerApi api) {
		super(api, "clipboard-data");

		this.setGetResponseType(ClipboardContentGetResponse.class);
		this.setUpdateResponseType(ClipboardContentUpdateResponse.class);
	}

	public String get() throws Exception {
		ClipboardContentGetResponse response = (ClipboardContentGetResponse) super
				.get("1");

		return response.data;
	}

	public boolean update(String content) throws Exception {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("data", content));
		
		ClipboardContentUpdateResponse response = (ClipboardContentUpdateResponse) super.update("1", parameters);
	
		return response.messages.isEmpty();
	}
}
