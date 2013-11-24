package net.mms_projects.copy_it.sdk.api.exceptions.http;

abstract public class HttpException extends Exception {

    private int httpResponseCode;
    private String httpResponseBody;

    protected HttpException(int httpResponseCode) {
        this.setHttpResponseCode(httpResponseCode);
    }

    //<editor-fold desc="Getter and setter methods">
    public int getHttpResponseCode() {
        return httpResponseCode;
    }

    public void setHttpResponseCode(int httpResponseCode) {
        this.httpResponseCode = httpResponseCode;
    }

    public String getHttpResponseBody() {
        return this.httpResponseBody;
    }

    public void setHttpResponseBody(String httpResponseBody) {
        this.httpResponseBody = httpResponseBody;
    }

    //</editor-fold>

}
