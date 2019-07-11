package com.javafortesters.pulp.api.actions;

import com.google.gson.Gson;
import com.javafortesters.pulp.api.DomainToEntityConvertor;
import com.javafortesters.pulp.api.EntityResponse;
import com.javafortesters.pulp.api.entities.lists.SeriesListEntity;
import com.javafortesters.pulp.api.entities.payloads.ApiResponseBuilder;
import com.javafortesters.pulp.api.entities.single.AuthorEntity;
import com.javafortesters.pulp.api.entities.single.SeriesEntity;
import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.domain.groupings.PulpSeriesCollection;
import com.javafortesters.pulp.domain.objects.PulpSeries;

import java.util.ArrayList;
import java.util.List;

public class SeriesActions {

    private final PulpData bookdata;
    private final DomainToEntityConvertor convertor;
    private final String rooturl;

    public SeriesActions(final PulpData bookdata, final DomainToEntityConvertor convertor, final String rooturl) {
        this.bookdata = bookdata;
        this.convertor = convertor;
        this.rooturl = rooturl;
    }

    public EntityResponse getSingle(final String seriesid, final String acceptformat) {
        final PulpSeries series = bookdata.series().get(seriesid);
        final EntityResponse response = new EntityResponse();

        if(series==PulpSeries.UNKNOWN_SERIES){
            response.setErrorStatus(404, String.format("Series %s not found", seriesid));
            return response;
        }

        return response.setSuccessStatus(200,
                new Gson().toJson(
                        new ApiResponseBuilder(bookdata).addData(series).getApiResponse())
        );

    }

    public EntityResponse getAll(final String acceptformat) {
        final EntityResponse response = new EntityResponse();

        response.setSuccessStatus(200,
                new Gson().toJson(
                        new ApiResponseBuilder(bookdata).addData(bookdata.series()).getApiResponse())
        );

        return response;
    }

    public EntityResponse deleteSingle(final String seriesid, final String accept) {
        final PulpSeries series = bookdata.series().get(seriesid);
        final EntityResponse response = new EntityResponse();

        if(series==PulpSeries.UNKNOWN_SERIES){
            response.setErrorStatus(404, String.format("Series %s not found", seriesid));
            return response;
        }

        if(bookdata.deleteSeries(seriesid)){
            response.setSuccessStatus(204, ""); // String.format("Book %s deleted", bookid));
        }else{
            response.setErrorStatus(500, String.format("Unknown error deleting Series %s", seriesid));
        }

        return response;
    }

    public EntityResponse createReplace(final String seriesId, final String body, final String contentType, final String accept) {
        if(contentType==null || (!contentType.endsWith("json"))){
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content-type %s", contentType));
        }

        String errorMessage = "";

        SeriesEntity series=null;

        try {
            series = new Gson().fromJson(body, SeriesEntity.class);
        }catch (Exception e) {
            // ok, it isn't an series, is it a list of series?
            errorMessage = e.getMessage();
        }

        if(errorMessage.length()>0){
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content as Series %s", errorMessage));
        }

        PulpSeries actualSeries = bookdata.series().get(seriesId);
        if(actualSeries==null || actualSeries==PulpSeries.UNKNOWN_SERIES){
            return new EntityResponse().setErrorStatus(404, String.format("Cannot find Series %s", seriesId));
        }

        // did we get a single series?
        if(series!=null && series.name!=null){

            if(series.id!=null && series.id.length()>0){
                // do not allow creation of series with PUT
                return new EntityResponse().setErrorStatus(400, String.format("Cannot create Series '%s' with a defined id %s", series.name, series.id));
            }

            if(series.name.length()<=0){
                // do not allow creation of series with PUT with invalid name
                return new EntityResponse().setErrorStatus(400, String.format("Invalid Series Name '%s'", series.name));
            }

            // do not allow amendment to duplicate a series name
            final PulpSeries existingNamedSeries = bookdata.series().findByName(series.name);
            if( existingNamedSeries != actualSeries && existingNamedSeries!= PulpSeries.UNKNOWN_SERIES ){
                return new EntityResponse().setErrorStatus(409, String.format("Cannot rename Series '%s', %s to same name as %s", actualSeries.getName(), actualSeries.getId(), existingNamedSeries.getId()));
            }

            ActionProcessor actioner = new ActionProcessor(bookdata, convertor, rooturl);
            List<ActionEntityResponsePair> responses = new ArrayList<>();
            ActionToDo action = new ActionToDo().isAmend(new SeriesEntity(actualSeries.getId(), series.name));
            responses.add(new ActionEntityResponsePair(action, actioner.process(action)));
            return new BulkResponse(responses, bookdata).asEntityResponse();

        }else{
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content as Series %s", errorMessage));
        }
    }

    public EntityResponse createAmend(final String body, final String contentType, final String accept) {
        if(contentType==null || (!contentType.endsWith("json"))){
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content-type %s", contentType));
        }

        String errorMessage = "";

        SeriesEntity series=null;
        SeriesListEntity seriesList= new SeriesListEntity(new PulpSeriesCollection());

        try {
            series = new Gson().fromJson(body, SeriesEntity.class);
        }catch (Exception e) {
            // ok, it isn't an series, is it a list of series?
            errorMessage = e.getMessage();
        }

        try {
            seriesList = new Gson().fromJson(body, SeriesListEntity.class);
        }catch (Exception e2){
            // nope - can't accept this then
            errorMessage = errorMessage + " , " + e2.getMessage();
        }

        if(errorMessage.length()>0){
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content as Series %s", errorMessage));
        }

        ActionProcessor actioner = new ActionProcessor(bookdata, convertor, rooturl);
        List<ActionEntityResponsePair> responses = new ArrayList<>();


        // did we get a single series?
        if(series!=null && series.name!=null){

            ActionToDo action = identifyCreateAmendActionForSeriesEntity(series);
            responses.add(new ActionEntityResponsePair(action, actioner.process(action)));

        }else{

            if(seriesList == null || seriesList.series == null){
                // that was not a Series list
                return new EntityResponse().setErrorStatus(400, String.format("Cannot process content as Series %s", errorMessage));
            }

            List<ActionToDo> actions = new ArrayList();

            // process author list
            for( SeriesEntity aSeries : seriesList.series){
                actions.add(identifyCreateAmendActionForSeriesEntity(aSeries));
            }

            for(ActionToDo action : actions){
                responses.add(new ActionEntityResponsePair(action, actioner.process(action)));
            }
        }

        return new BulkResponse(responses, bookdata).asEntityResponse();
    }

    private ActionToDo identifyCreateAmendActionForSeriesEntity(final SeriesEntity series) {
        final ActionToDo action = new ActionToDo();

        // ACTION: ERROR, 400, message
        if(series.name == null || series.name.length()==0){
            return action.isError(400, String.format("Series name cannot be empty")).withSeries(series);
        }

        // does it have an id?
        if(series.id ==null || series.id.length()==0){

            PulpSeries existing = bookdata.series().findByName(series.name);

            if(existing!=PulpSeries.UNKNOWN_SERIES){
                return action.isError(409, String.format("Cannot create series. Series '%s' already exists with id %s.", existing.getName(), existing.getId())).
                        withHeader("location", getLocationHeaderFor(existing)).withSeries(series);
            }

            // OK, we will create this
            return action.isCreate(series);

        }else{
            // treat this as an amend
            if(bookdata.series().get(series.id) == PulpSeries.UNKNOWN_SERIES){
                return action.isError(404, String.format("Unknown Series %s", series.id)).withSeries(series);
            }

            return action.isAmend(series);
        }
    }

    private String getLocationHeaderFor(final PulpSeries existing) {
        return rooturl + "/series/" + existing.getId();
    }

    public EntityResponse patchAmend(final String seriesId, final String body, final String contentType, final String accept) {

        if(contentType==null || (!contentType.endsWith("json"))){
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content-type %s", contentType));
        }

        String errorMessage = "";

        SeriesEntity series=null;

        try {
            series = new Gson().fromJson(body, SeriesEntity.class);
        }catch (Exception e) {
            // ok, it isn't an series, is it a list of series?
            errorMessage = e.getMessage();
        }

        if(errorMessage.length()>0){
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content as Series %s", errorMessage));
        }

        PulpSeries actualSeries = bookdata.series().get(seriesId);
        if(actualSeries==null || actualSeries==PulpSeries.UNKNOWN_SERIES){
            return new EntityResponse().setErrorStatus(404, String.format("Cannot find Series %s", seriesId));
        }

        // did we get a single series?
        if(series!=null && series.name!=null){

            if(series.id!=null && series.id.length()>0){
                // do not allow creation of series with PUT
                return new EntityResponse().setErrorStatus(400, String.format("Cannot change Series ID"));
            }

            if(series.name.length()<=0){
                // do not allow creation of series with PUT with invalid name
                return new EntityResponse().setErrorStatus(400, String.format("Invalid Series Name '%s'", series.name));
            }

            // do not allow amendment to duplicate a series name
            final PulpSeries existingNamedSeries = bookdata.series().findByName(series.name);
            if( existingNamedSeries != actualSeries && existingNamedSeries!= PulpSeries.UNKNOWN_SERIES ){
                return new EntityResponse().setErrorStatus(409, String.format("Cannot rename Series '%s', %s to same name as %s", actualSeries.getName(), actualSeries.getId(), existingNamedSeries.getId()));
            }

            ActionProcessor actioner = new ActionProcessor(bookdata, convertor, rooturl);
            List<ActionEntityResponsePair> responses = new ArrayList<>();
            ActionToDo action = new ActionToDo().isAmend(new SeriesEntity(actualSeries.getId(), series.name));
            responses.add(new ActionEntityResponsePair(action, actioner.process(action)));
            return new BulkResponse(responses, bookdata).asEntityResponse();

        }else{
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content as Series %s", errorMessage));
        }
    }
}
