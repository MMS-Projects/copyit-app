package net.mms_projects.copy_it.sdk.api.exceptions.http.server;

import net.mms_projects.copy_it.sdk.api.exceptions.http.ServerErrorException;

public class ServiceUnavailableException extends ServerErrorException {

    public final static int HTTP_CODE = 503;

    public ServiceUnavailableException() {
        super(HTTP_CODE);
    }

}
