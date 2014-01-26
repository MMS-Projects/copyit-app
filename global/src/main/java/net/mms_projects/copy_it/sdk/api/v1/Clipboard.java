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

public class Clipboard {

    private Get getEndpoint;
    private Update updateEndpoint;

    //<editor-fold desc="Constructor">

    public Clipboard(Token accessToken, OAuthService service, String endpointUrl) {
        this.getEndpoint = new Get(accessToken, service, endpointUrl);
        this.updateEndpoint = new Update(accessToken, service, endpointUrl);
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
         * Gets the current clipboard content from the server
         *
         * @return An {@link net.mms_projects.copy_it.sdk.api.v1.Clipboard.Responses.Get} containing the server response
         * @throws ApiException This gets thrown if something bad happened. The cause will be given.
         * @throws NoContentException This gets thrown if no data clipboard data is set on the server
         */
        public Responses.Get get() throws ApiException, NoContentException {
            // Create a request
            OAuthRequest request = new OAuthRequest(Verb.GET, this.getEndpointUrl() + "get");
            // Sign the request
            this.signRequest(request);
            // Send the request
            Response response = request.send();

            return this.handleServerResponse(response);
        }

        @Override
        public Responses.Get handleServerResponse(Response response) throws ApiException, NoContentException {
            if (response.getCode() == NoContentException.HTTP_CODE) {
                throw new NoContentException();
            }
            if (response.isSuccessful()) {
                try {
                    return this.parseJson(response.getBody(), Responses.Get.class);
                } catch (JsonParseException jsonException) {
                    throw new ApiException(jsonException);
                }
            }

            HttpException exception = this.generateErrorException(response);

            System.out.println(exception);

            throw new ApiException(exception);
        }
    }

    static public class Update extends ApiEndpoint<Responses.Update> {

        public Update() {
            super();
        }

        public Update(Token accessToken, OAuthService service, String endpointUrl) {
            super(accessToken, service, endpointUrl);
        }

        /**
         * Updated the current clipboard content on the server
         * @param content The content to send to the server
         * @param contentType The content type of the content to be set
         * @return An {@link net.mms_projects.copy_it.sdk.api.v1.Clipboard.Responses.Update} containing the server response
         * @throws ApiException This gets thrown if something bad happened. The cause will be given.
         */
        public Responses.Update update(String content, String contentType) throws ApiException, HttpException {
            // Create a request
            OAuthRequest request = new OAuthRequest(Verb.POST, this.getEndpointUrl() + "update");
            // Add the required parameters
            request.addBodyParameter("data", content);
            request.addBodyParameter("content_type", contentType);
            // Sign the request
            this.signRequest(request);
            // Send the request
            Response response = request.send();

            return this.handleServerResponse(response);
        }

        @Override
        public Responses.Update handleServerResponse(Response response) throws ApiException, HttpException {
            if (response.isSuccessful())  {
                try {
                    return this.parseJson(response.getBody(), Responses.Update.class);
                } catch (JsonParseException jsonException) {
                    throw new ApiException(jsonException);
                }
            }

            HttpException exception = this.generateErrorException(response);

            throw new ApiException(exception);
        }
    }

    public Responses.Get get() throws ApiException, NoContentException {
        return this.getEndpoint.get();
    }

    public Responses.Update update(String content, String contentType) throws ApiException, HttpException {
        return this.updateEndpoint.update(content, contentType);
    }

    //<editor-fold desc="API responses">

    static public class Responses {
        static public class Get {
            String content;
            int last_updated;

            public String getContent() {
                return this.content;
            }

            public int getLastUpdated() {
                return this.last_updated;
            }

            @Override
            public String toString() {
                return "Get{" +
                        "content='" + content + '\'' +
                        ", last_updated='" + last_updated + '\'' +
                        '}';
            }
        }
        static public class Update {

        }
    }

    //</editor-fold>

}