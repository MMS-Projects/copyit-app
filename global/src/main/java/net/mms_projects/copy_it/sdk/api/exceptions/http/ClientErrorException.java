package net.mms_projects.copy_it.sdk.api.exceptions.http;

abstract public class ClientErrorException extends HttpException {

    protected ClientErrorException(int httpResponseCode) {
        super(httpResponseCode);
    }

}
