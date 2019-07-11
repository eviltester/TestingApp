package com.javafortesters.pulp.api.actions;

import com.google.gson.Gson;
import com.javafortesters.pulp.api.DomainToEntityConvertor;
import com.javafortesters.pulp.api.EntityResponse;
import com.javafortesters.pulp.api.entities.lists.AuthorListEntity;
import com.javafortesters.pulp.api.entities.payloads.ApiResponseBuilder;
import com.javafortesters.pulp.api.entities.single.AuthorEntity;
import com.javafortesters.pulp.domain.groupings.PulpAuthors;
import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.domain.objects.PulpAuthor;

import java.util.ArrayList;
import java.util.List;

public class AuthorActions {
    private final PulpData bookdata;
    private final DomainToEntityConvertor convertor;
    private final String rooturl;

    public AuthorActions(final PulpData bookdata, final DomainToEntityConvertor convertor, final String rooturl) {
        this.bookdata = bookdata;
        this.convertor = convertor;
        this.rooturl = rooturl;
    }

    public EntityResponse getSingle(final String authorid, final String acceptformat) {
        final PulpAuthor author = bookdata.authors().get(authorid);
        final EntityResponse response = new EntityResponse();

        if(author==PulpAuthor.UNKNOWN_AUTHOR){
            response.setErrorStatus(404, String.format("Author %s not found", authorid));
            return response;
        }


        response.setSuccessStatus(200,
                        new Gson().toJson(
                            new ApiResponseBuilder(bookdata).addData(author).getApiResponse())
                        );

        return response;
    }

    public EntityResponse getAll(final String acceptformat) {
        final EntityResponse response = new EntityResponse();

        response.setSuccessStatus(200,
                new Gson().toJson(
                        new ApiResponseBuilder(bookdata).addData(bookdata.authors()).getApiResponse())
        );

        return response;

    }

    public EntityResponse deleteSingle(final String authorid, final String accept) {
        final EntityResponse response = new EntityResponse();

        final PulpAuthor author = bookdata.authors().get(authorid);

        if(author==PulpAuthor.UNKNOWN_AUTHOR){
            response.setErrorStatus(404, String.format("Author %s not found", authorid));
            return response;
        }

        if(bookdata.deleteAuthor(authorid)){
            response.setSuccessStatus(204, ""); // String.format("Author %s deleted", authorid));
        }else {
            response.setErrorStatus(500, String.format("Unknown error deleting Author %s", authorid));
        }

        return response;
    }

    public EntityResponse createAmend(final String body, final String contentType, final String accept) {
        if(contentType==null || (!contentType.endsWith("json"))){
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content-type %s", contentType));
        }

        String errorMessage = "";

        AuthorEntity author=null;
        AuthorListEntity authorList= new AuthorListEntity(new PulpAuthors());

        try {
            author = new Gson().fromJson(body, AuthorEntity.class);
        }catch (Exception e) {
            // ok, it isn't an author, is it a list of authors?
            errorMessage = e.getMessage();
        }

        try {
            authorList = new Gson().fromJson(body, AuthorListEntity.class);
        }catch (Exception e2){
            // nope - can't accept this then
            errorMessage = errorMessage + " , " + e2.getMessage();
        }

        if(errorMessage.length()>0){
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content as Authors %s", errorMessage));
        }

        ActionProcessor actioner = new ActionProcessor(bookdata, convertor, rooturl);
        List<ActionEntityResponsePair> responses = new ArrayList<>();

        // did we get a single author?
        if(author!=null && author.name!=null){

            ActionToDo action = identifyCreateAmendActionForAuthorEntity(author);
            responses.add(new ActionEntityResponsePair(action, actioner.process(action)));

        }else{

            if(authorList == null || authorList.authors == null){
                // that was not an authors list
                return new EntityResponse().setErrorStatus(400, String.format("Cannot process content as Authors %s", errorMessage));
            }

            List<ActionToDo> actions = new ArrayList();

            // process author list
            for( AuthorEntity anAuthor : authorList.authors){
                actions.add(identifyCreateAmendActionForAuthorEntity(anAuthor));
            }

            for(ActionToDo action : actions){
                responses.add(new ActionEntityResponsePair(action, actioner.process(action)));
            }
        }

        return new BulkResponse(responses, bookdata).asEntityResponse();
    }

    private ActionToDo identifyCreateAmendActionForAuthorEntity(final AuthorEntity author) {

        final ActionToDo action = new ActionToDo();

        // ACTION: ERROR, 400, message
        if(author.name == null || author.name.length()==0){
            return action.isError(400, String.format("Author name cannot be empty")).withAuthor(author);

        }

        // does it have an id?
        if(author.id ==null || author.id.length()==0){

            PulpAuthor existingAuthor = bookdata.authors().findByName(author.name);

            if(existingAuthor!=PulpAuthor.UNKNOWN_AUTHOR){
                return action.isError(409, String.format("Cannot create author. Author '%s' already exists with id %s.", existingAuthor.getName(), existingAuthor.getId())).
                        withHeader("location", getLocationHeaderFor(existingAuthor)).withAuthor(author);
            }

            // OK, we will create this
            return action.isCreate(author);

        }else{
            // treat this as an amend
            if(bookdata.authors().get(author.id) == PulpAuthor.UNKNOWN_AUTHOR){
                return action.isError(404, String.format("Unknown Author %s", author.id)).withAuthor(author);
            }

            return action.isAmend(author);
        }
    }

    private String getLocationHeaderFor(final PulpAuthor theAuthor) {
        return rooturl + "/authors/" + theAuthor.getId();
    }

    public EntityResponse createReplace(final String authorid, final String body, final String contentType, final String accept) {
        if(contentType==null || (!contentType.endsWith("json"))){
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content-type %s", contentType));
        }

        String errorMessage = "";

        AuthorEntity author=null;

        try {
            author = new Gson().fromJson(body, AuthorEntity.class);
        }catch (Exception e) {
            // ok, it isn't an author, is it a list of authors?
            errorMessage = e.getMessage();
        }

        if(errorMessage.length()>0){
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content as Author %s", errorMessage));
        }

        PulpAuthor actualAuthor = bookdata.authors().get(authorid);
        if(actualAuthor==null || actualAuthor==PulpAuthor.UNKNOWN_AUTHOR){
            return new EntityResponse().setErrorStatus(404, String.format("Cannot find Author %s", authorid));
        }

        // did we get a single author?
        if(author!=null && author.name!=null){

            if(author.id!=null && author.id.length()>0){
                // do not allow creation of author with PUT
                return new EntityResponse().setErrorStatus(400, String.format("Cannot create Author '%s' with a defined id %s", author.name, author.id));
            }

            if(author.name.length()<=0){
                // do not allow creation of author with PUT
                return new EntityResponse().setErrorStatus(400, String.format("Invalid Author Name '%s'", author.name));
            }

            // do not allow amendment to duplicate an existing name
            final PulpAuthor existingNamed = bookdata.authors().findByName(author.name);
            if( existingNamed != actualAuthor && existingNamed!= PulpAuthor.UNKNOWN_AUTHOR ){
                return new EntityResponse().setErrorStatus(409, String.format("Cannot rename Author '%s', %s to same name as %s", actualAuthor.getName(), actualAuthor.getId(), existingNamed.getId()));
            }

            ActionProcessor actioner = new ActionProcessor(bookdata, convertor, rooturl);
            List<ActionEntityResponsePair> responses = new ArrayList<>();
            ActionToDo action = new ActionToDo().isAmend(new AuthorEntity(actualAuthor.getId(), author.name));
            responses.add(new ActionEntityResponsePair(action, actioner.process(action)));
            return new BulkResponse(responses, bookdata).asEntityResponse();

        }else{

            // that was not an author
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content as Author %s", errorMessage));
        }
    }

    public EntityResponse patchAmend(final String authorid, final String body, final String contentType, final String accept) {
        if (contentType == null || (!contentType.endsWith("json"))) {
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content-type %s", contentType));
        }

        String errorMessage = "";

        AuthorEntity author = null;

        try {
            author = new Gson().fromJson(body, AuthorEntity.class);
        } catch (Exception e) {
            // ok, it isn't an author, is it a list of authors?
            errorMessage = e.getMessage();
        }

        if (errorMessage.length() > 0) {
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content as Author %s", errorMessage));
        }

        PulpAuthor actualAuthor = bookdata.authors().get(authorid);
        if (actualAuthor == null || actualAuthor == PulpAuthor.UNKNOWN_AUTHOR) {
            return new EntityResponse().setErrorStatus(404, String.format("Cannot find Author %s", authorid));
        }

        // act on fields
        // did we get a single author?
        if(author!=null && author.name!=null){

            if(author.id!=null && author.id.length()>0){
                // do not allow creation of author with PUT
                return new EntityResponse().setErrorStatus(400, String.format("Cannot change Author id"));
            }

            if(author.name.length()<=0){
                // do not allow creation of author with PUT
                return new EntityResponse().setErrorStatus(400, String.format("Invalid Author Name '%s'", author.name));
            }

            // do not allow amendment to duplicate an existing name
            final PulpAuthor existingNamed = bookdata.authors().findByName(author.name);
            if( existingNamed != actualAuthor && existingNamed!= PulpAuthor.UNKNOWN_AUTHOR ){
                return new EntityResponse().setErrorStatus(409, String.format("Cannot rename Author '%s', %s to same name as %s", actualAuthor.getName(), actualAuthor.getId(), existingNamed.getId()));
            }


            ActionProcessor actioner = new ActionProcessor(bookdata, convertor, rooturl);
            List<ActionEntityResponsePair> responses = new ArrayList<>();
            ActionToDo action = new ActionToDo().isAmend(new AuthorEntity(actualAuthor.getId(), author.name));
            responses.add(new ActionEntityResponsePair(action, actioner.process(action)));
            return new BulkResponse(responses, bookdata).asEntityResponse();


        }else{

            // that was not an author
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content as Author %s", errorMessage));
        }
    }
}
