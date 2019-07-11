package com.javafortesters.pulp.api;

import com.google.gson.Gson;
import com.javafortesters.pulp.api.actions.ActionEntityResponsePair;
import com.javafortesters.pulp.api.actions.ActionToDo;
import com.javafortesters.pulp.api.actions.BulkResponse;
import com.javafortesters.pulp.api.entities.EntityResponseErrorMessage;
import com.javafortesters.pulp.api.entities.payloads.responses.ApiErrorMessage;
import com.javafortesters.pulp.domain.groupings.PulpData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EntityResponse {

    private int statusCode;
    private String errorMessage;
    private boolean errorResponse;
    private String responseBody;
    private String format = "application/json";
    private Map<String, String> headers;

    public EntityResponse(){
        statusCode = 200;
        errorMessage = "";
        errorResponse=false;
        responseBody="{}";
        headers = new HashMap<>();
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
            // quick hack to put all individual error messages into new format
            // TODO: use proper API error message reporting throughout
            
            final ArrayList<ActionEntityResponsePair> errorResponses = new ArrayList<ActionEntityResponsePair>();
            errorResponses.add(new ActionEntityResponsePair(new ActionToDo().isError(statusCode, errorMessage),
                                new EntityResponse().setErrorStatus(statusCode, errorMessage)));

            final BulkResponse bulkResponse = new BulkResponse(errorResponses, new PulpData());
            return bulkResponse.asEntityResponse().responseBody;

            //return new Gson().toJson(new EntityResponseErrorMessage(errorMessage));
        }else{
            return errorMessage;
        }

    }

    public String getResponseBody() {
        if(isError()){
            return getErrorMessage();
        }else{
            return responseBody;
        }
    }

    public String getContentType() {
        return format;
    }

    public EntityResponse addHeader(final String key, final String value) {
        headers.put(key, value);
        return this;
    }

    public Set<String> getHeaderNames() {
        return headers.keySet();
    }

    public String getHeaderValue(final String headerkey) {
        return headers.get(headerkey);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(final Map<String, String> headers) {
        this.headers.putAll(headers);
    }
}
