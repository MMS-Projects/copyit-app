package net.mms_projects.copy_it.global.tests;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MockHttpURLConnection extends HttpURLConnection {

    int code;
    String body;
    InputStream inputStream;
    InputStream errorStream;

    static public HttpURLConnection createMockHttpURLConnection(URL url, int code, String message) {
        MockHttpURLConnection httpURLConnection = new MockHttpURLConnection(url);
        httpURLConnection.setResponseCode(code);
        httpURLConnection.setResponseMessage(message);

        return httpURLConnection;
    }

    public MockHttpURLConnection(URL url) {
        super(url);
    }

    @Override
    public int getResponseCode() throws IOException {
        return this.responseCode;
    }

    @Override
    public InputStream getErrorStream() {
        return this.errorStream;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return this.inputStream;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public String getResponseMessage() throws IOException {
        return this.responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        InputStream stream = new ByteArrayInputStream(responseMessage.getBytes());

        this.inputStream = stream;
        this.errorStream = stream;

        this.responseMessage = responseMessage;
    }

    @Override
    public void disconnect() {

    }

    @Override
    public boolean usingProxy() {
        return false;
    }

    @Override
    public void connect() throws IOException {
    }
}
