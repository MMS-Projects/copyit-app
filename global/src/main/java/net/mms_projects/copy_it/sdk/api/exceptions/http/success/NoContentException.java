package net.mms_projects.copy_it.sdk.api.exceptions.http.success;

import net.mms_projects.copy_it.sdk.api.exceptions.http.SuccessException;

public class NoContentException extends SuccessException {

    public final static int HTTP_CODE = 204;

    public NoContentException() {
        super(HTTP_CODE);
    }

}
