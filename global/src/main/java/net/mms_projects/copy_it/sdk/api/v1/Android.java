package net.mms_projects.copy_it.sdk.api.v1;

import net.mms_projects.copy_it.sdk.api.exceptions.ApiException;
import net.mms_projects.copy_it.sdk.api.exceptions.JsonParseException;
import net.mms_projects.copy_it.sdk.api.exceptions.http.HttpException;
import net.mms_projects.copy_it.sdk.api.exceptions.http.success.NoContentException;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public class Android {

    private GcmRegister gcmRegisterEndpoint;
    private GcmUnregister gcmUnregisterEndpoint;

    //<editor-fold desc="Constructor">

    public Android(Token accessToken, OAuthService service, String endpointUrl) {
        this.gcmRegisterEndpoint = new GcmRegister(accessToken, service, endpointUrl);
        this.gcmUnregisterEndpoint = new GcmUnregister(accessToken, service, endpointUrl);
    }

    //</editor-fold>

    static public class GcmRegister extends ApiEndpoint<Responses.Register> {

        public GcmRegister() {
            super();
        }

        public GcmRegister(Token accessToken, OAuthService service, String endpointUrl) {
            super(accessToken, service, endpointUrl);
        }

        /**
         * Registers the GCM token with the CopyIt server
         *
         * @return An {@link net.mms_projects.copy_it.sdk.api.v1.Android.Responses.Register} containing the server response
         * @throws ApiException This gets thrown if something bad happened. The cause will be given.
         */
        public Responses.Register gcmRegister(String gcmToken) throws ApiException, NoContentException {
            if (gcmToken.length() > 4096) {
                throw new IllegalArgumentException("The max length of a GCM registration id is 4096 bytes");
            }
            // Create a request
            OAuthRequest request = new OAuthRequest(Verb.POST, this.getEndpointUrl() + "register");
            // Add the required parameters
            request.addBodyParameter("gcm_token", gcmToken);
            // Sign the request
            this.signRequest(request);
            // Send the request
            Response response = request.send();

            return this.handleServerResponse(response);
        }

        @Override
        public Responses.Register handleServerResponse(Response response) throws ApiException, NoContentException {
            if (response.getCode() == NoContentException.HTTP_CODE) {
                throw new NoContentException();
            }
            if (response.isSuccessful()) {
                try {
                    return this.parseJson(response.getBody(), Responses.Register.class);
                } catch (JsonParseException jsonException) {
                    throw new ApiException(jsonException);
                }
            }

            HttpException exception = this.generateErrorException(response);

            throw new ApiException(exception);
        }
    }

    static public class GcmUnregister extends ApiEndpoint<Responses.Unregister> {

        public GcmUnregister() {
            super();
        }

        public GcmUnregister(Token accessToken, OAuthService service, String endpointUrl) {
            super(accessToken, service, endpointUrl);
        }

        /**
         * Unregisters the GCM token with the CopyIt server
         *
         * @return An {@link net.mms_projects.copy_it.sdk.api.v1.Android.Responses.Unregister} containing the server response
         * @throws ApiException This gets thrown if something bad happened. The cause will be given.
         */
        public Responses.Unregister gcmUnregister(String gcmToken) throws ApiException, HttpException {
            // Create a request
            OAuthRequest request = new OAuthRequest(Verb.POST, this.getEndpointUrl() + "unregister");
            // Add the required parameters
            request.addBodyParameter("gcm_token", gcmToken);
            // Sign the request
            this.signRequest(request);
            // Send the request
            Response response = request.send();

            return this.handleServerResponse(response);
        }

        @Override
        public Responses.Unregister handleServerResponse(Response response) throws ApiException, HttpException {
            if (response.isSuccessful()) {
                try {
                    return this.parseJson(response.getBody(), Responses.Unregister.class);
                } catch (JsonParseException jsonException) {
                    throw new ApiException(jsonException);
                }
            }

            HttpException exception = this.generateErrorException(response);

            throw new ApiException(exception);
        }

    }

    //<editor-fold desc="API responses">

    static public class Responses {
        static public class Register {
        }

        static public class Unregister {

        }
    }

    //</editor-fold>

}
