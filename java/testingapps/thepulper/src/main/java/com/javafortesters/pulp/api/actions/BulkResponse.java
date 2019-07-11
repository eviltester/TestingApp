package com.javafortesters.pulp.api.actions;

import com.google.gson.Gson;
import com.javafortesters.pulp.api.EntityResponse;
import com.javafortesters.pulp.api.entities.payloads.ApiResponseBuilder;
import com.javafortesters.pulp.api.entities.payloads.responses.ApiErrorMessage;
import com.javafortesters.pulp.api.entities.payloads.responses.ApiResponse;
import com.javafortesters.pulp.domain.groupings.PulpData;

import java.util.ArrayList;
import java.util.List;

public class BulkResponse {
    private final List<ActionEntityResponsePair> responses;
    ApiResponseBuilder apiResponse;


    public BulkResponse(final List<ActionEntityResponsePair> responses, PulpData bookdata) {
        this.responses = responses;
        apiResponse = new ApiResponseBuilder(bookdata);
    }


    public EntityResponse asEntityResponse() {

        boolean have_201=false;
        boolean have_200=false;

        for(ActionEntityResponsePair entityResponse : responses){
            if(entityResponse.response.isError()){
                addErrorResponse(entityResponse);
            }else{
                if(entityResponse.response.getStatusCode()==201){
                    have_201=true;
                }
                if(entityResponse.response.getStatusCode()==200){
                    have_200=true;
                }

                addSuccessResponse(entityResponse);
            }
        }

        final ApiResponse response = apiResponse.getApiResponse();
        EntityResponse returnedResponse;

        if(response.errors==null) {
            int statusCode = 200;  // 200 is more generic than 201, so if we have a mix then return 200
            if(have_201 && !have_200){
                statusCode=201;
            }
            returnedResponse = new EntityResponse().setSuccessStatus(statusCode, new Gson().toJson(response));
        }else{
            // use success status because we are midway through a refactoring and we want to set the response and status here
            returnedResponse = new EntityResponse().setSuccessStatus(400, new Gson().toJson(response));
        }

        if(responses.size()==1){
            // if there is only one then copy in the headers
            returnedResponse.setHeaders(responses.get(0).response.getHeaders());
            returnedResponse.setStatusCode(responses.get(0).response.getStatusCode());
        }

        return returnedResponse;
    }

    private void addSuccessResponse(final ActionEntityResponsePair entityResponse) {
        if(entityResponse.response.getStatusCode()==201){
            // created
            if(entityResponse.action.authorEntityToActOn!=null) {
                apiResponse.addCreated(entityResponse.action.actualAuthor);
            }
            if(entityResponse.action.seriesEntityToActOn!=null) {
                apiResponse.addCreated(entityResponse.action.actualSeries);
            }
            if(entityResponse.action.publisherEntityToActOn!=null) {
                apiResponse.addCreated(entityResponse.action.actualPublisher);
            }
            if(entityResponse.action.bookEntityToActOn!=null) {
                apiResponse.addCreated(entityResponse.action.actualBook);
            }
        }
        if(entityResponse.response.getStatusCode()==200){
            // Amended
            if(entityResponse.action.authorEntityToActOn!=null) {
                apiResponse.addAmended(entityResponse.action.actualAuthor);
            }
            if(entityResponse.action.seriesEntityToActOn!=null) {
                apiResponse.addAmended(entityResponse.action.actualSeries);
            }
            if(entityResponse.action.publisherEntityToActOn!=null) {
                apiResponse.addAmended(entityResponse.action.actualPublisher);
            }
            if(entityResponse.action.bookEntityToActOn!=null) {
                apiResponse.addAmended(entityResponse.action.actualBook);
            }
        }
    }

    private void addErrorResponse(final ActionEntityResponsePair entityResponse) {

        final ApiErrorMessage errorMessage = new ApiErrorMessage();

        errorMessage.addMessage(entityResponse.action.errorMessage);

        if(entityResponse.action.authorEntityToActOn!=null) {
            errorMessage.author = entityResponse.action.authorEntityToActOn;
        }
        if(entityResponse.action.seriesEntityToActOn!=null) {
            errorMessage.series = entityResponse.action.seriesEntityToActOn;
        }
        if(entityResponse.action.publisherEntityToActOn!=null) {
            errorMessage.publisher = entityResponse.action.publisherEntityToActOn;
        }
        if(entityResponse.action.bookEntityToActOn!=null) {
            errorMessage.book = entityResponse.action.bookEntityToActOn;
        }

        apiResponse.addError(errorMessage);
    }


}
