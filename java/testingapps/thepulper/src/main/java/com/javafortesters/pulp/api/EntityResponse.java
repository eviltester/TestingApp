package com.javafortesters.pulp.api;

import com.google.gson.Gson;
import com.javafortesters.pulp.api.entities.EntityResponseErrorMessage;

public class EntityResponse {

    private int statusCode;
    private String errorMessage;
    private boolean errorResponse;
    private String responseBody;
    private String format = "application/json";

    public EntityResponse(){
        statusCode = 200;
        errorMessage = "";
        errorResponse=false;
        responseBody="{}";
    }

    private void setStatus(final int statusCode) {
        this.statusCode = statusCode;
    }

    private void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public EntityResponse setErrorStatus(final int statusCode, final String errorMessage) {
        errorResponse=true;
        setStatus(statusCode);
        setErrorMessage(errorMessage);
        return this;
    }

    public EntityResponse setSuccessStatus(final int statusCode, final String responseBody) {
        errorResponse=false;
        setStatus(statusCode);
        setResponseBody(responseBody);
        return this;
    }

    private void setResponseBody(final String responseBody) {
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public boolean isError() {
        return errorResponse;
    }

    public String getErrorMessage() {

        if(format.endsWith("json")){
            return new Gson().toJson(new EntityResponseErrorMessage(errorMessage));
        }else{
            return errorMessage;
        }

    }

    public String getResponseBody() {
        return responseBody;
    }

    public String getContentType() {
        return format;
    }
}
