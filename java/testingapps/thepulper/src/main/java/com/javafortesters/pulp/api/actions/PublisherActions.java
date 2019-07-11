package com.javafortesters.pulp.api.actions;

import com.google.gson.Gson;
import com.javafortesters.pulp.api.DomainToEntityConvertor;
import com.javafortesters.pulp.api.EntityResponse;
import com.javafortesters.pulp.api.entities.lists.PublisherListEntity;
import com.javafortesters.pulp.api.entities.payloads.ApiResponseBuilder;
import com.javafortesters.pulp.api.entities.single.AuthorEntity;
import com.javafortesters.pulp.api.entities.single.PublisherEntity;
import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.domain.groupings.PulpPublishers;
import com.javafortesters.pulp.domain.objects.PulpPublisher;

import java.util.ArrayList;
import java.util.List;

public class PublisherActions {
    private final PulpData bookdata;
    private final DomainToEntityConvertor convertor;
    private final String rooturl;

    public PublisherActions(final PulpData bookdata, final DomainToEntityConvertor convertor, final String rooturl) {
        this.bookdata = bookdata;
        this.convertor = convertor;
        this.rooturl = rooturl;
    }

    public EntityResponse getSingle(final String publisherid, final String accept) {
        final PulpPublisher publisher = bookdata.publishers().get(publisherid);
        final EntityResponse response = new EntityResponse();

        if(publisher==PulpPublisher.UNKNOWN_PUBLISHER){
            response.setErrorStatus(404, String.format("Publisher %s not found", publisherid));
            return response;
        }

        return response.setSuccessStatus(200,
                new Gson().toJson(
                        new ApiResponseBuilder(bookdata).addData(publisher).getApiResponse())
        );

    }

    public EntityResponse getAll(final String accept) {
        final EntityResponse response = new EntityResponse();

        PublisherListEntity entity = new PublisherListEntity(bookdata.publishers());

        return response.setSuccessStatus(200,
                new Gson().toJson(
                        new ApiResponseBuilder(bookdata).addData(bookdata.publishers()).getApiResponse())
        );

    }

    public EntityResponse deleteSingle(final String publisherid, final String accept) {
        final PulpPublisher publisher = bookdata.publishers().get(publisherid);
        final EntityResponse response = new EntityResponse();

        if(publisher==PulpPublisher.UNKNOWN_PUBLISHER){
            response.setErrorStatus(404, String.format("Publisher %s not found", publisherid));
            return response;
        }

        if(bookdata.deletePublisher(publisherid)){
            response.setSuccessStatus(204, ""); // String.format("Publisher %s deleted", publisherid));
        }else{
            response.setErrorStatus(500, String.format("Unknown error deleting Publisher %s", publisherid));
        }

        return response;
    }

    public EntityResponse createReplace(final String publisherid, final String body, final String contentType, final String accept) {
        if(contentType==null || (!contentType.endsWith("json"))){
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content-type %s", contentType));
        }

        String errorMessage = "";

        PublisherEntity publisher=null;

        try {
            publisher = new Gson().fromJson(body, PublisherEntity.class);
        }catch (Exception e) {
            // ok, it isn't an publisher
            errorMessage = e.getMessage();
        }

        if(errorMessage.length()>0){
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content as Publisher %s", errorMessage));
        }

        PulpPublisher actualPublisher = bookdata.publishers().get(publisherid);
        if(actualPublisher==null || actualPublisher==PulpPublisher.UNKNOWN_PUBLISHER){
            return new EntityResponse().setErrorStatus(404, String.format("Cannot find Publisher %s", publisherid));
        }

        // did we get a single publisher?
        if(publisher!=null && publisher.name!=null){

            if(publisher.id!=null && publisher.id.length()>0){
                // do not allow creation of series with PUT
                return new EntityResponse().setErrorStatus(400, String.format("Cannot create Publisher '%s' with a defined id %s", publisher.name, publisher.id));
            }

            if(publisher.name.length()<=0){
                // do not allow creation of publisher with PUT with invalid name
                return new EntityResponse().setErrorStatus(400, String.format("Invalid Publisher Name '%s'", publisher.name));
            }

            // do not allow amendment to duplicate an existing name
            final PulpPublisher existingNamed = bookdata.publishers().findByName(publisher.name);
            if( existingNamed != actualPublisher && existingNamed!= PulpPublisher.UNKNOWN_PUBLISHER ){
                return new EntityResponse().setErrorStatus(409, String.format("Cannot rename Publisher '%s', %s to same name as %s", actualPublisher.getName(), actualPublisher.getId(), existingNamed.getId()));
            }

            ActionProcessor actioner = new ActionProcessor(bookdata, convertor, rooturl);
            List<ActionEntityResponsePair> responses = new ArrayList<>();
            ActionToDo action = new ActionToDo().isAmend(new PublisherEntity(actualPublisher.getId(), publisher.name));
            responses.add(new ActionEntityResponsePair(action, actioner.process(action)));
            return new BulkResponse(responses, bookdata).asEntityResponse();

        }else{
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content as Publisher %s", errorMessage));
        }
    }

    public EntityResponse createAmend(final String body, final String contentType, final String accept) {
        if(contentType==null || (!contentType.endsWith("json"))){
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content-type %s", contentType));
        }

        String errorMessage = "";

        PublisherEntity single=null;
        PublisherListEntity list= new PublisherListEntity(new PulpPublishers());

        try {
            single = new Gson().fromJson(body, PublisherEntity.class);
        }catch (Exception e) {
            errorMessage = e.getMessage();
        }

        try {
            list = new Gson().fromJson(body, PublisherListEntity.class);
        }catch (Exception e2){
            // nope - can't accept this then
            errorMessage = errorMessage + " , " + e2.getMessage();
        }

        if(errorMessage.length()>0){
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content as Publishers %s", errorMessage));
        }

        ActionProcessor actioner = new ActionProcessor(bookdata, convertor, rooturl);
        List<ActionEntityResponsePair> responses = new ArrayList<>();

        // did we get a single item?
        if(single!=null && single.name!=null){

            ActionToDo action = identifyCreateAmendActionForPublisherEntity(single);
            responses.add(new ActionEntityResponsePair(action, actioner.process(action)));

        }else{

            if(list == null || list.publishers == null){
                // that was not a Series list
                return new EntityResponse().setErrorStatus(400, String.format("Cannot process content as Publishers %s", errorMessage));
            }

            List<ActionToDo> actions = new ArrayList();

            // process author list
            for( PublisherEntity aSingleItem : list.publishers){
                actions.add(identifyCreateAmendActionForPublisherEntity(aSingleItem));
            }

            for(ActionToDo action : actions){
                responses.add(new ActionEntityResponsePair(action, actioner.process(action)));
            }
        }

        return new BulkResponse(responses, bookdata).asEntityResponse();
    }

    private ActionToDo identifyCreateAmendActionForPublisherEntity(final PublisherEntity single) {
        final ActionToDo action = new ActionToDo();

        // ACTION: ERROR, 400, message
        if(single.name == null || single.name.length()==0){
            return action.isError(400, String.format("Publisher name cannot be empty")).withPublisher(single);
        }

        // does it have an id?
        if(single.id ==null || single.id.length()==0){

            PulpPublisher existing = bookdata.publishers().findByName(single.name);

            // check for duplicate name
            if(existing!=PulpPublisher.UNKNOWN_PUBLISHER){
                return action.isError(409, String.format("Cannot create publisher. Publisher '%s' already exists with id %s.", existing.getName(), existing.getId())).
                        withHeader("location", getLocationHeaderFor(existing)).withPublisher(single);
            }

            // OK, we will create this
            return action.isCreate(single);

        }else{
            // treat this as an amend
            if(bookdata.publishers().get(single.id) == PulpPublisher.UNKNOWN_PUBLISHER){
                return action.isError(404, String.format("Unknown Publisher %s", single.id)).withPublisher(single);
            }

            return action.isAmend(single);
        }
    }

    private String getLocationHeaderFor(final PulpPublisher existing) {
        return rooturl + "/publishers/" + existing.getId();
    }

    public EntityResponse patchAmend(final String publisherid, final String body, final String contentType, final String accept) {
        if(contentType==null || (!contentType.endsWith("json"))){
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content-type %s", contentType));
        }

        String errorMessage = "";

        PublisherEntity publisher=null;

        try {
            publisher = new Gson().fromJson(body, PublisherEntity.class);
        }catch (Exception e) {
            // ok, it isn't an publisher
            errorMessage = e.getMessage();
        }

        if(errorMessage.length()>0){
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content as Publisher %s", errorMessage));
        }

        PulpPublisher actualPublisher = bookdata.publishers().get(publisherid);
        if(actualPublisher==null || actualPublisher==PulpPublisher.UNKNOWN_PUBLISHER){
            return new EntityResponse().setErrorStatus(404, String.format("Cannot find Publisher %s", publisherid));
        }

        // did we get a single publisher?
        if(publisher!=null && publisher.name!=null){

            if(publisher.id!=null && publisher.id.length()>0){
                // do not allow creation of series with PUT
                return new EntityResponse().setErrorStatus(400, String.format("Cannot change Publisher id"));
            }

            if(publisher.name.length()<=0){
                // do not allow creation of publisher with PUT with invalid name
                return new EntityResponse().setErrorStatus(400, String.format("Invalid Publisher Name '%s'", publisher.name));
            }

            // do not allow amendment to duplicate an existing name
            final PulpPublisher existingNamed = bookdata.publishers().findByName(publisher.name);
            if( existingNamed != actualPublisher && existingNamed!= PulpPublisher.UNKNOWN_PUBLISHER ){
                return new EntityResponse().setErrorStatus(409, String.format("Cannot rename Publisher '%s', %s to same name as %s", actualPublisher.getName(), actualPublisher.getId(), existingNamed.getId()));
            }

            ActionProcessor actioner = new ActionProcessor(bookdata, convertor, rooturl);
            List<ActionEntityResponsePair> responses = new ArrayList<>();
            ActionToDo action = new ActionToDo().isAmend(new PublisherEntity(actualPublisher.getId(), publisher.name));
            responses.add(new ActionEntityResponsePair(action, actioner.process(action)));
            return new BulkResponse(responses, bookdata).asEntityResponse();

        }else{
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content as Publisher %s", errorMessage));
        }
    }
}
