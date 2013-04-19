package net.mms_projects.copyit.api;

import java.util.List;

import net.mms_projects.copyit.api.responses.ApiResponse;

import org.apache.http.NameValuePair;

public abstract class ApiEndpoint {

	protected ServerApi api;
	protected String endpoint;

	private Class<? extends ApiResponse> getResponseType;
	private Class<? extends ApiResponse> createResponseType;
	private Class<? extends ApiResponse> updateResponseType;
	private Class<? extends ApiResponse> deleteResponseType;

	public ApiEndpoint(ServerApi api, String endpoint) {
		this.api = api;
		this.endpoint = endpoint;
	}
	
	public ApiEndpoint(ServerApi api) {
		this.api = api;
	}

	final protected void setGetResponseType(Class<? extends ApiResponse> responseType) {
		this.getResponseType = responseType;
	}

	final protected void setCreateResponseType(Class<? extends ApiResponse>  responseType) {
		this.createResponseType = responseType;
	}

	final protected void setUpdateResponseType(Class<? extends ApiResponse>  responseType) {
		this.updateResponseType = responseType;
	}

	final protected void setDeleteResponseType(Class<? extends ApiResponse>  responseType) {
		this.deleteResponseType = responseType;
	}

	final protected Class<? extends ApiResponse> getGetResponseType() {
		return this.getResponseType;
	}
	
	final protected Class<? extends ApiResponse> getCreateResponseType() {
		return this.createResponseType;
	}
	
	final protected Class<? extends ApiResponse> getUpdateResponseType() {
		return this.updateResponseType;
	}
	
	final protected Class<? extends ApiResponse> getDeleteResponseType() {
		return this.deleteResponseType;
	}
	
	public ApiResponse get(String id) throws Exception{
		ApiResponse response = this.api.doRequest(this.getResponseType,
				this.endpoint, id, "GET");
		return response;
	}

	public ApiResponse create(String id, List<NameValuePair> parameters) throws Exception{
		ApiResponse response = this.api.doRequest(this.createResponseType,
				this.endpoint, id, "POST", parameters);
		return response;
	}

	public ApiResponse update(String id, List<NameValuePair> parameters) throws Exception {
		ApiResponse response = this.api.doRequest(this.updateResponseType,
				this.endpoint, id, "PUT", parameters);
		return response;
	}

	public ApiResponse delete(String id) throws Exception {
		ApiResponse response = this.api.doRequest(this.deleteResponseType,
				this.endpoint, id, "DELETE");
		return response;
	}

}
