package net.mms_projects.copy_it.global.tests.clipboard;

import net.mms_projects.copy_it.sdk.api.exceptions.ApiException;
import net.mms_projects.copy_it.sdk.api.exceptions.JsonParseException;
import net.mms_projects.copy_it.sdk.api.exceptions.http.success.NoContentException;
import net.mms_projects.copy_it.sdk.api.v1.Clipboard;

import org.junit.Assert;
import org.junit.Test;
import org.scribe.model.MockOAuthResponse;
import org.scribe.model.Response;

import java.io.IOException;
import java.net.URL;

public class ClipboardGetTests {

    @Test(
            expected = NoContentException.class,
            timeout = 500
    )
    public void getContentWithNothingSavedExceptionCheck() throws NoContentException {
        Clipboard.Get clipboardGet = new Clipboard.Get();

        Response response = null;
        try {
            response = new MockOAuthResponse(
                    new URL("http://api.copyit.mmsdev.org/1/clipboard/get"), 204, "No content"
            );
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }

        try {
            clipboardGet.handleServerResponse(response);
        } catch (ApiException exception) {
            Assert.fail("An unexpected exception was thrown: " + exception.toString());
        }
    }

    @Test(
            expected = JsonParseException.class,
            timeout = 500
    )
    public void getContentWithAInvalidJsonResponse() throws Throwable {
        Clipboard.Get clipboardGet = new Clipboard.Get();

        Response response = null;
        try {
            response = new MockOAuthResponse(
                    new URL("http://api.copyit.mmsdev.org/1/clipboard/get"), 200, "Invalid JSON yay!"
            );
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }

        try {
            clipboardGet.handleServerResponse(response);
        } catch (ApiException exception) {
            throw exception.getCause();
        } catch (NoContentException exception) {
            Assert.fail("An unexpected exception was thrown: " + exception.toString());
        }
    }

    @Test(
            expected = JsonParseException.class,
            timeout = 500
    )
    public void getContentResponseWithWrongDataTypes() throws Throwable {
        Clipboard.Get clipboardGet = new Clipboard.Get();

        Response response = null;
        try {
            response = new MockOAuthResponse(
                    new URL("http://api.copyit.mmsdev.org/1/clipboard/get"), 200, "{content: true, last_updated: 'Never!'}"
            );
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }

        try {
            clipboardGet.handleServerResponse(response);
        } catch (ApiException exception) {
            throw exception.getCause();
        } catch (NoContentException exception) {
            Assert.fail("An unexpected exception was thrown: " + exception.toString());
        }
    }

    @Test(timeout = 500)
    public void getContentReturnedDataCheck() throws Throwable {
        String content = "This is some clipboard content";
        int lastUpdated = (int) System.currentTimeMillis() / 1000;

        Clipboard.Get clipboardGet = new Clipboard.Get();

        Response response = null;
        try {
            response = new MockOAuthResponse(
                    new URL("http://api.copyit.mmsdev.org/1/clipboard/get"),
                    200,
                    "{content: '" + content + "', last_updated: " + lastUpdated + "}"
            );
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }

        Clipboard.Responses.Get parsedResponse = clipboardGet.handleServerResponse(response);

        Assert.assertEquals(content, parsedResponse.getContent());
        Assert.assertEquals(lastUpdated, parsedResponse.getLastUpdated());
    }

}
