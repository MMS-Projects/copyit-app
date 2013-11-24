package net.mms_projects.copy_it.sdk.api.exceptions.http.client;

import net.mms_projects.copy_it.sdk.api.exceptions.http.ClientErrorException;

public class TooManyRequestsException extends ClientErrorException {

    public final static int HTTP_CODE = 429;

    public TooManyRequestsException() {
        super(HTTP_CODE);
    }

}
