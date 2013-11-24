package net.mms_projects.copy_it.sdk.api.exceptions.http.client;

import net.mms_projects.copy_it.sdk.api.exceptions.http.ClientErrorException;

public class MethodNotAllowedException extends ClientErrorException {

    public final static int HTTP_CODE = 405;

    public MethodNotAllowedException() {
        super(HTTP_CODE);
    }

}
