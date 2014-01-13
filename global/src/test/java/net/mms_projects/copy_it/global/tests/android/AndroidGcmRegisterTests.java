package net.mms_projects.copy_it.global.tests.android;

import junit.framework.Assert;

import net.mms_projects.copy_it.sdk.api.exceptions.ApiException;
import net.mms_projects.copy_it.sdk.api.exceptions.JsonParseException;
import net.mms_projects.copy_it.sdk.api.exceptions.http.success.NoContentException;
import net.mms_projects.copy_it.sdk.api.v1.Android;

import org.junit.Test;
import org.scribe.model.MockOAuthResponse;
import org.scribe.model.Response;

import java.io.IOException;
import java.net.URL;

public class AndroidGcmRegisterTests {

    @Test(
            expected = JsonParseException.class,
            timeout = 500
    )
    public void gcmRegisterWithAInvalidJsonResponse() throws Throwable {
        Android.GcmRegister endpoint = new Android.GcmRegister();

        Response response = null;
        try {
            response = new MockOAuthResponse(
                    new URL("http://api.copyit.mmsdev.org/1/android/register"), 200, "Invalid JSON yay!"
            );
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }

        try {
            endpoint.handleServerResponse(response);
        } catch (ApiException exception) {
            throw exception.getCause();
        } catch (NoContentException exception) {
            Assert.fail("An unexpected exception was thrown: " + exception.toString());
        }
    }

}
