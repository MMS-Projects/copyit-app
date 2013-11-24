package net.mms_projects.copy_it.sdk.api.exceptions.http.client;

import net.mms_projects.copy_it.sdk.api.exceptions.http.ClientErrorException;

public class ImATeapotException extends ClientErrorException {

    public final static int HTTP_CODE = 418;

    public ImATeapotException() {
        super(HTTP_CODE);
    }

}
