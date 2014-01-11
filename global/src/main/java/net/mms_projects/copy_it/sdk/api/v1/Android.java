package net.mms_projects.copy_it.sdk.api.v1;

import net.mms_projects.copy_it.sdk.api.exceptions.ApiException;
import net.mms_projects.copy_it.sdk.api.exceptions.http.HttpException;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public class Android extends ApiEndpoint {

    //<editor-fold desc="Constructor">

    public Android(Token accessToken, OAuthService service, String endpointUrl) {
        super(accessToken, service, endpointUrl);
    }

    //</editor-fold>

    /**
     * Registers the GCM token with the CopyIt server
     *
     * @return An {@link net.mms_projects.copy_it.sdk.api.v1.Android.Responses.Register} containing the server response
     * @throws ApiException This gets thrown if something bad happened. The cause will be given.
     */
    public Responses.Register gcmRegister(String gcmToken) throws ApiException {
        // Create a request
        OAuthRequest request = new OAuthRequest(Verb.POST, this.getEndpointUrl() + "register");
        // Add the required parameters
        request.addBodyParameter("gcm_token", gcmToken);
        // Sign the request
        this.signRequest(request);
        // Send the request
        Response response = request.send();

        if (response.isSuccessful()) {
            return this.parseJson(response.getBody(), Responses.Register.class);
        }

        HttpException exception = this.generateErrorException(response);

        throw new ApiException(exception);
    }

    /**
     * Unregisters the GCM token with the CopyIt server
     *
     * @return An {@link net.mms_projects.copy_it.sdk.api.v1.Android.Responses.Unregister} containing the server response
     * @throws ApiException This gets thrown if something bad happened. The cause will be given.
     */
    public Responses.Unregister gcmUnregister(String gcmToken) throws ApiException {
        // Create a request
        OAuthRequest request = new OAuthRequest(Verb.POST, this.getEndpointUrl() + "unregister");
        // Add the required parameters
        request.addBodyParameter("gcm_token", gcmToken);
        // Sign the request
        this.signRequest(request);
        // Send the request
        Response response = request.send();

        if (response.isSuccessful()) {
            return this.parseJson(response.getBody(), Responses.Unregister.class);
        }

        HttpException exception = this.generateErrorException(response);

        throw new ApiException(exception);
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
