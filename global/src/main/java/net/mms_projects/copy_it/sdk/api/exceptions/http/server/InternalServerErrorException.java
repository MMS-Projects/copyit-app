package net.mms_projects.copy_it.sdk.api.exceptions.http.server;

import net.mms_projects.copy_it.sdk.api.exceptions.http.ServerErrorException;

public class InternalServerErrorException extends ServerErrorException {

    public final static int HTTP_CODE = 500;

    public InternalServerErrorException(String body) {
        super(HTTP_CODE);

        this.setHttpResponseBody(body);
    }

}
