package net.mms_projects.copyit;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.sun.net.httpserver.HttpsParameters;

public class ServerApi {

	String apiUrl = "http://interwebzpaste.com/api/";
	URL apiUrlObject;

	public ServerApi() {
		try {
			this.apiUrlObject = new URL(this.apiUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public boolean set(String accessToken, String data) {
		HttpResponse response;
		String responseText = null;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("action", "set"));
		nameValuePairs.add(new BasicNameValuePair("access_token", accessToken));
		nameValuePairs.add(new BasicNameValuePair("data", data));

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(this.apiUrl);
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException e1) {
			return false;
		}

		try {
			response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();

			responseText = IOUtils.toString(entity.getContent(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

}
