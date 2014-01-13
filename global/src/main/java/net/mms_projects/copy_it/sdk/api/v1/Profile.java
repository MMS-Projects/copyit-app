package net.mms_projects.copy_it.sdk.api.v1;

import net.mms_projects.copy_it.sdk.api.exceptions.ApiException;
import net.mms_projects.copy_it.sdk.api.exceptions.http.HttpException;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public class Profile {

    private Get getEndpoint;

    //<editor-fold desc="Constructor">

    public Profile(Token accessToken, OAuthService service, String server) {
        this.getEndpoint = new Get(accessToken, service, server);
    }

    //</editor-fold>

    static public class Get extends ApiEndpoint<Responses.Get> {

        public Get() {
            super();
        }

        public Get(Token accessToken, OAuthService service, String endpointUrl) {
            super(accessToken, service, endpointUrl);
        }

        /**
         * Gets the user's profile information
         *
         * @return An {@link net.mms_projects.copy_it.sdk.api.v1.Profile.Responses.Get} containing the server response
         * @throws ApiException This gets thrown if something bad happened. The cause will be given.
         */
        public Responses.Get get() throws ApiException, HttpException {
            // Create a request
            OAuthRequest request = new OAuthRequest(Verb.GET, this.getEndpointUrl() + "/1/user/profile");
            // Sign the request
            this.signRequest(request);
            // Send the request
            Response response = request.send();

            return this.handleServerResponse(response);
        }

        @Override
        public Responses.Get handleServerResponse(Response response) throws ApiException, HttpException {
            if (response.isSuccessful()) {
                return this.parseJson(response.getBody(), Responses.Get.class);
            }

            HttpException exception = this.generateErrorException(response);

            System.out.println(exception);

            throw new ApiException(exception);
        }
    }

    public Responses.Get get() throws ApiException, HttpException {
        return this.getEndpoint.get();
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
