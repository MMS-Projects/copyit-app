package net.mms_projects.copy_it.sdk.api.exceptions.http;

abstract public class SuccessException extends HttpException {

    protected SuccessException(int httpResponseCode) {
        super(httpResponseCode);
    }

}
