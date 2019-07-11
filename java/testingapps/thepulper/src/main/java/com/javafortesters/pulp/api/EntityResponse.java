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

        final ArrayList<ActionEntityResponsePair> errorResponses = new ArrayList<ActionEntityResponsePair>();
        errorResponses.add(new ActionEntityResponsePair(new ActionToDo().isError(statusCode, errorMessage),
                new EntityResponse().asError()));

        final BulkResponse bulkResponse = new BulkResponse(errorResponses, new PulpData());

        this.errorMessage = errorMessage;
        this.responseBody = bulkResponse.asEntityResponse().responseBody;
    }

    private EntityResponse asError() {
        errorResponse=true;
        return this;
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


    public String getResponseBody() {
            return responseBody;
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

    public void setStatusCode(final int statusCode) {
        this.statusCode = statusCode;
    }
}
