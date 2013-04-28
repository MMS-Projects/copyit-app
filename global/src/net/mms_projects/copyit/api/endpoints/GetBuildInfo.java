package net.mms_projects.copyit.api.endpoints;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import net.mms_projects.copyit.api.ApiEndpoint;
import net.mms_projects.copyit.api.ServerApi;
import net.mms_projects.copyit.api.responses.ApiResponse;
import net.mms_projects.copyit.api.responses.JenkinsBuildResponse;

public class GetBuildInfo extends ApiEndpoint {

	public GetBuildInfo(ServerApi api) {
		super(api);
		
		this.setGetResponseType(JenkinsBuildResponse.class);
	}

	public JenkinsBuildResponse getLatestStableBuild() throws Exception {
		return this.get("lastStableBuild");
	}

	@Override
	public JenkinsBuildResponse get(String id) throws Exception {
		ApiResponse response = this.api.doRawRequest(this.getGetResponseType(),
				this.api.apiUrl + "/" + id + "/api/json",
				"GET", new ArrayList<NameValuePair>());
		return (JenkinsBuildResponse) response;
	}

}
