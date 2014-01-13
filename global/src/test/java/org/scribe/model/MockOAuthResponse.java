package org.scribe.model;

import net.mms_projects.copy_it.global.tests.MockHttpURLConnection;

import java.io.IOException;
import java.net.URL;

public class MockOAuthResponse extends Response {

    public MockOAuthResponse(URL url, int code, String body) throws IOException {
        super(MockHttpURLConnection.createMockHttpURLConnection(url, code, body));
    }
}
