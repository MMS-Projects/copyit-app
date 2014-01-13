package net.mms_projects.copy_it.sdk.api.exceptions.http.server;

import net.mms_projects.copy_it.sdk.api.exceptions.http.ServerErrorException;

public class InternalServerErrorException extends ServerErrorException {

    public final static int HTTP_CODE = 500;

    public InternalServerErrorException() {
        super(HTTP_CODE);
    }

    public InternalServerErrorException(String body) {
        this();

        this.setHttpResponseBody(body);
    }

}
