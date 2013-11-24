package net.mms_projects.copy_it.sdk.api.exceptions.http;

abstract public class ServerErrorException extends HttpException {

    protected ServerErrorException(int httpResponseCode) {
        super(httpResponseCode);
    }

}
