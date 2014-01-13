package net.mms_projects.copy_it.sdk.api.v1;

import com.google.gson.Gson;
import net.mms_projects.copy_it.sdk.api.exceptions.ApiException;
import net.mms_projects.copy_it.sdk.api.exceptions.JsonParseException;
import net.mms_projects.copy_it.sdk.api.exceptions.http.HttpException;
import net.mms_projects.copy_it.sdk.api.exceptions.http.client.ImATeapotException;
import net.mms_projects.copy_it.sdk.api.exceptions.http.client.MethodNotAllowedException;
import net.mms_projects.copy_it.sdk.api.exceptions.http.client.UnsupportedMediaTypeException;
import net.mms_projects.copy_it.sdk.api.exceptions.http.server.InternalServerErrorException;
import net.mms_projects.copy_it.sdk.api.exceptions.http.server.ServiceUnavailableException;
import net.mms_projects.copy_it.sdk.api.exceptions.http.client.TooManyRequestsException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

abstract public class ApiEndpoint<ParsedResponse> {

    protected Token accessToken;
    protected OAuthService service;
    protected String endpointUrl;

    public ApiEndpoint() {
    }

    public ApiEndpoint(Token accessToken, OAuthService service, String endpointUrl) {
        this.setAccessToken(accessToken);
        this.setService(service);
        this.setEndpointUrl(endpointUrl);
    }

    /**
     * Parses JSON and returns the data as a Java object
     *
     * @param json The JSON to parse
     * @param type The class literal to generate a class instance of
     * @param <ResponseFormat>  The class type used for the object
     * @throws JsonParseException This gets thrown when there's an error while parsing the JSON
     * @return A object representing the JSON
     */
    protected <ResponseFormat> ResponseFormat parseJson(String json, Class<ResponseFormat> type) throws JsonParseException {
        try {
            return new Gson().fromJson(json, type);
        } catch (com.google.gson.JsonSyntaxException exception) {
            throw new JsonParseException(exception);
        }
    }

    /**
     * This method signs the request using the {@link OAuthService} and access token
     *
     * @param request The request to sign
     */
    protected void signRequest(OAuthRequest request) {
        this.getService().signRequest(this.getAccessToken(), request);
    }

    /**
     * Check the server response to see what errors occurred
     *
     * @param response The server response to check for errors
     * @return The generated exception
     */
    protected HttpException generateErrorException(Response response) {
        if (response.isSuccessful()) {
            return null;
        }

        switch (response.getCode()) {
            //<editor-fold desc="Client exceptions">

            // 405 Method Not Allowed
            case MethodNotAllowedException.HTTP_CODE:
                return new MethodNotAllowedException();
            // 415 Unsupported Media Type
            case UnsupportedMediaTypeException.HTTP_CODE:
                return new UnsupportedMediaTypeException();
            // 418 I'm A Teapot
            case ImATeapotException.HTTP_CODE:
                return new ImATeapotException();
            // 429 Too Many Requests
            case TooManyRequestsException.HTTP_CODE:
                return new TooManyRequestsException();

            //</editor-fold>

            //<editor-fold desc="Server exceptions">

            // 500 Internal Server Error
            case InternalServerErrorException.HTTP_CODE:
                return new InternalServerErrorException(response.getBody());
            // 503 Service Unavailable
            case ServiceUnavailableException.HTTP_CODE:
                return new ServiceUnavailableException();

            //</editor-fold>
        }

        return null;
    }

    abstract public ParsedResponse handleServerResponse(Response response) throws ApiException, HttpException;

    //<editor-fold desc="Getter and setter methods">
    public Token getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(Token accessToken) {
        this.accessToken = accessToken;
    }

    public OAuthService getService() {
        return this.service;
    }

    public void setService(OAuthService service) {
        this.service = service;
    }

    public String getEndpointUrl() {
        return this.endpointUrl;
    }

    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }
    //</editor-fold>

}
