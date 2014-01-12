package net.mms_projects.copy_it.sdk.api.v1;

import net.mms_projects.copy_it.sdk.api.exceptions.ApiException;
import net.mms_projects.copy_it.sdk.api.exceptions.http.HttpException;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public class Profile extends ApiEndpoint {

    //<editor-fold desc="Constructor">

    public Profile(Token accessToken, OAuthService service, String server) {
        super(accessToken, service, server);
    }

    //</editor-fold>

    /**
     * Gets the user's profile information
     *
     * @return An {@link net.mms_projects.copy_it.sdk.api.v1.Profile.Responses.Get} containing the server response
     * @throws ApiException This gets thrown if something bad happened. The cause will be given.
     */
    public Responses.Get get() throws ApiException {
        // Create a request
        OAuthRequest request = new OAuthRequest(Verb.GET, this.getEndpointUrl() + "/1/user/profile");
        // Sign the request
        this.signRequest(request);
        // Send the request
        Response response = request.send();

        if (response.isSuccessful()) {
            return this.parseJson(response.getBody(), Responses.Get.class);
        }

        HttpException exception = this.generateErrorException(response);

        System.out.println(exception);

        throw new ApiException(exception);
    }

    //<editor-fold desc="API responses">

    static public class Responses {
        static public class Get {
            int id;
            String email;
            int signed_up;

            public int getId() {
                return this.id;
            }

            public String getEmail() {
                return this.email;
            }

            public int getSignedUp() {
                return this.signed_up;
            }

            @Override
            public String toString() {
                return "Get{" +
                        "id=" + id +
                        ", email='" + email + '\'' +
                        ", signed_up=" + signed_up +
                        '}';
            }
        }
    }

    //</editor-fold>

}
