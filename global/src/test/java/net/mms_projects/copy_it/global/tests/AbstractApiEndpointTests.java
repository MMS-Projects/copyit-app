package net.mms_projects.copy_it.global.tests;

import net.mms_projects.copy_it.sdk.api.exceptions.ApiException;
import net.mms_projects.copy_it.sdk.api.exceptions.http.HttpException;
import net.mms_projects.copy_it.sdk.api.exceptions.http.client.ImATeapotException;
import net.mms_projects.copy_it.sdk.api.exceptions.http.client.MethodNotAllowedException;
import net.mms_projects.copy_it.sdk.api.exceptions.http.client.TooManyRequestsException;
import net.mms_projects.copy_it.sdk.api.exceptions.http.client.UnsupportedMediaTypeException;
import net.mms_projects.copy_it.sdk.api.exceptions.http.server.InternalServerErrorException;
import net.mms_projects.copy_it.sdk.api.exceptions.http.server.ServiceUnavailableException;
import net.mms_projects.copy_it.sdk.api.v1.ApiEndpoint;

import org.junit.Assert;
import org.junit.Test;
import org.scribe.model.MockOAuthResponse;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AbstractApiEndpointTests {

    class TestingEndpoint extends ApiEndpoint<Object> {
        @Override
        public HttpException generateErrorException(Response response) {
            return super.generateErrorException(response);
        }

        @Override
        public void signRequest(OAuthRequest request) {
            super.signRequest(request);
        }

        @Override
        public Object handleServerResponse(Response response) throws ApiException, HttpException {
            return null;
        }
    }

    @Test(
            timeout = 500
    )
    public void clientErrorThrowingChecks() throws IllegalAccessException, InstantiationException {
        Map<Class<? extends HttpException>, Integer> errors = new HashMap<Class<? extends HttpException>, Integer>();
        errors.put(MethodNotAllowedException.class, 405);
        errors.put(UnsupportedMediaTypeException.class, 415);
        errors.put(ImATeapotException.class, 418);
        errors.put(TooManyRequestsException.class, 429);

        TestingEndpoint endpoint = new TestingEndpoint();

        Response response = null;

        for (Class<? extends HttpException> genericHttpException : errors.keySet()) {
            HttpException httpException = genericHttpException.newInstance();
            int errorCode = errors.get(genericHttpException);

            try {
                response = new MockOAuthResponse(
                        new URL("http://example.com"), errorCode, "Nothing"
                );
            } catch (IOException exception) {
                throw new IllegalStateException(exception);
            }

            HttpException thrownHttpException = endpoint.generateErrorException(response);
            Assert.assertNotNull(
                    "No HttpException thrown. Should have been " + genericHttpException.getSimpleName(),
                    thrownHttpException
            );

            Assert.assertEquals(genericHttpException, thrownHttpException.getClass());
        }

    }

    @Test(
            timeout = 500
    )
    public void serverErrorThrowingChecks() throws IllegalAccessException, InstantiationException {
        Map<Class<? extends HttpException>, Integer> errors = new HashMap<Class<? extends HttpException>, Integer>();
        errors.put(InternalServerErrorException.class, 500);
        errors.put(ServiceUnavailableException.class, 503);

        TestingEndpoint endpoint = new TestingEndpoint();

        Response response = null;

        for (Class<? extends HttpException> genericHttpException : errors.keySet()) {
            HttpException httpException = genericHttpException.newInstance();
            int errorCode = errors.get(genericHttpException);

            try {
                response = new MockOAuthResponse(
                        new URL("http://example.com"), errorCode, "Nothing"
                );
            } catch (IOException exception) {
                throw new IllegalStateException(exception);
            }

            HttpException thrownHttpException = endpoint.generateErrorException(response);
            Assert.assertNotNull(
                    "No HttpException thrown. Should have been "
                            + genericHttpException.getSimpleName(),
                    thrownHttpException
            );

            Assert.assertEquals(genericHttpException, thrownHttpException.getClass());
        }

    }

}
