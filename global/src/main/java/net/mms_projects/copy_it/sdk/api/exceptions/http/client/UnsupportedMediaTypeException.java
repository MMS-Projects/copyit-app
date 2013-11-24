package net.mms_projects.copy_it.sdk.api.exceptions.http.client;

import net.mms_projects.copy_it.sdk.api.exceptions.http.ClientErrorException;

public class UnsupportedMediaTypeException extends ClientErrorException {

    public final static int HTTP_CODE = 415;

    public UnsupportedMediaTypeException() {
        super(HTTP_CODE);
    }

}
