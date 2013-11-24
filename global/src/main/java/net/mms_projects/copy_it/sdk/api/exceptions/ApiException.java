package net.mms_projects.copy_it.sdk.api.exceptions;

import net.mms_projects.copy_it.sdk.api.exceptions.http.HttpException;

public class ApiException extends Exception {

    public ApiException(HttpException cause) {
        super(cause);
    }

}
