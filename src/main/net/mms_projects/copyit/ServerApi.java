package net.mms_projects.copyit;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
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
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ServerApi {

	public UUID deviceId;
	public String devicePassword;

	String apiUrl = "http://copyit.dev.mms-projects.net";
	URL apiUrlObject;

	public ServerApi() {
		try {
			this.apiUrlObject = new URL(this.apiUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public boolean set(String data) throws Exception {
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("data", data));

		JSONObject json = this.doRequest("clipboard-data", "1", "PUT",
				nameValuePairs);

		if (!((String) json.get("status")).equalsIgnoreCase("OK")) {
			throw new Exception("The server returned an error code");
		}

		return true;
	}

	public String get() throws Exception {
		JSONObject json = this.doRequest("clipboard-data", "1", "GET");
		System.out.println(json.toJSONString());
		if (!((String) json.get("status")).equalsIgnoreCase("OK")) {
			throw new Exception("The server returned an error code");
		}
		return (String) json.get("data");
	}

	protected JSONObject doRequest(String endpoint, String id, String method)
			throws Exception {
		return this.doRequest(endpoint, id, method,
				new ArrayList<NameValuePair>());
	}

	protected JSONObject doRequest(String endpoint, String id, String method,
			List<NameValuePair> parameters) throws Exception {
		String url = this.apiUrl + "/api/" + endpoint;
		if (!id.isEmpty()) {
			url += "/" + id;
		}
		url += ".json?";

		URIBuilder uriBuilder = null;
		try {
			uriBuilder = new URIBuilder(url);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		uriBuilder.addParameter("device_id", this.deviceId.toString());
		uriBuilder.addParameter("device_password", this.devicePassword);
		URI uri = null;
		try {
			uri = uriBuilder.build();
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JSONParser parser = new JSONParser();
		HttpResponse response = null;
		String responseText = null;

		HttpClient httpclient = new DefaultHttpClient();
		if (method == "GET") {
			HttpGet request = new HttpGet(uri);
			response = httpclient.execute(request);
		} else if (method == "POST") {
			HttpPost request = new HttpPost(uri);
			request.setEntity(new UrlEncodedFormEntity(parameters));
			response = httpclient.execute(request);
		} else if (method == "PUT") {
			HttpPut request = new HttpPut(uri);
			request.setEntity(new UrlEncodedFormEntity(parameters));
			response = httpclient.execute(request);
		}

		HttpEntity entity = response.getEntity();

		responseText = IOUtils.toString(entity.getContent(), "UTF-8");
		return (JSONObject) parser.parse(responseText);
	}

}
