package net.mms_projects.copyit;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;

public class ServerApi {

	public UUID deviceId;
	public String devicePassword;
	public String apiUrl = "http://copyit.dev.mms-projects.net";

	URL apiUrlObject;

	public ServerApi() {
		try {
			this.apiUrlObject = new URL(this.apiUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public boolean initDevice(String deviceName) throws Exception {
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("device_name", deviceName));

		ApiResponse response = null;
		try {
			response = this.doRequest("device", this.deviceId.toString(), "POST",
					nameValuePairs);
		} catch (Exception e) {
			// TODO: weird crash without this try catch
			System.out.println(e.getMessage());
		}

		if (!(response.status.equalsIgnoreCase("OK"))) {
			throw new Exception("The server returned an error code: "
					+ response.messages.get(0));
		}

		return true;
	}

	public boolean set(String data) throws Exception {
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("data", data));

		ApiResponse response = this.doRequest("clipboard-data", "1", "PUT",
				nameValuePairs);

		if (!(response.status.equalsIgnoreCase("OK"))) {
			throw new Exception("The server returned an error code: "
					+ response.messages.get(0));
		}

		return true;
	}

	public String get() throws Exception {
		ApiResponse response = this.doRequest("clipboard-data", "1", "GET");
		if (!(response.status.equalsIgnoreCase("OK"))) {
			throw new Exception("The server returned an error code: "
					+ response.messages.get(0));
		}
		return response.data;
	}

	protected ApiResponse doRequest(String endpoint, String id, String method)
			throws Exception {
		return this.doRequest(endpoint, id, method,
				new ArrayList<NameValuePair>());
	}

	protected ApiResponse doRequest(String endpoint, String id, String method,
			List<NameValuePair> parameters) throws Exception {
		String url = this.apiUrl + "/api/" + endpoint;
		if (id.length() != 0) {
			url += "/" + id;
		}
		url += ".json?";
		url += "device_id=" + this.deviceId.toString() + "&";
		url += "device_password=" + this.devicePassword;
		
		HttpResponse response = null;
		String responseText = null;

		HttpClient httpclient = new DefaultHttpClient();
		if (method == "GET") {
			HttpGet request = new HttpGet(url);
			response = httpclient.execute(request);
		} else if (method == "POST") {
			HttpPost request = new HttpPost(url);
			request.setEntity(new UrlEncodedFormEntity(parameters));
			response = httpclient.execute(request);
		} else if (method == "PUT") {
			HttpPut request = new HttpPut(url);
			request.setEntity(new UrlEncodedFormEntity(parameters));
			response = httpclient.execute(request);
		}

		HttpEntity entity = response.getEntity();

		responseText = IOUtils.toString(entity.getContent(), "UTF-8");
		
		ApiResponse data = new Gson().fromJson(responseText, ApiResponse.class);
		return data;
	}

}
