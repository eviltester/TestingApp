package com.javafortesters.pulp.api;

import com.google.gson.Gson;
import com.javafortesters.pulp.api.entities.lists.BooksListEntity;
import com.javafortesters.pulp.api.entities.lists.PublisherListEntity;
import com.javafortesters.pulp.api.entities.lists.SeriesListEntity;
import com.javafortesters.pulp.api.entities.single.*;
import com.javafortesters.pulp.api.entities.lists.AuthorListEntity;
import com.javafortesters.pulp.domain.groupings.PulpAuthors;
import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.domain.groupings.PulpPublishers;
import com.javafortesters.pulp.domain.groupings.PulpSeriesCollection;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpBook;
import com.javafortesters.pulp.domain.objects.PulpPublisher;
import com.javafortesters.pulp.domain.objects.PulpSeries;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO Refactor this for common elements

public class PulpEntities {
    private final PulpData bookdata;
    private final DomainToEntityConvertor convertor;;
    private String rooturl;

    public PulpEntities(final PulpData books) {
        this.bookdata = books;
        convertor = new DomainToEntityConvertor();
        rooturl = "";
    }

    public EntityResponse getAuthor(final String authorid, final String acceptformat) {
        final PulpAuthor author = bookdata.authors().get(authorid);
        final EntityResponse response = new EntityResponse();

        if(author==PulpAuthor.UNKNOWN_AUTHOR){
            response.setErrorStatus(404, String.format("Author %s not found", authorid));
            return response;
        }

        response.setSuccessStatus(200,convertor.toJson(author));

        return response;
    }

    public EntityResponse getAuthors(final String acceptformat) {
        final EntityResponse response = new EntityResponse();

        AuthorListEntity entity = new AuthorListEntity(bookdata.authors());

        response.setSuccessStatus(200,new Gson().toJson(entity));
        return response;
    }

    public EntityResponse deleteAuthor(final String authorid, final String accept) {

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

    public EntityResponse getBooks(final String acceptFormat) {
        final EntityResponse response = new EntityResponse();

        BooksListEntity entity = new BooksListEntity(bookdata);

        response.setSuccessStatus(200,new Gson().toJson(entity));
        return response;

    }

    public EntityResponse getBook(final String bookid, final String acceptformat) {
        final PulpBook book = bookdata.books().get(bookid);
        final EntityResponse response = new EntityResponse();

        if(book==PulpBook.UNKNOWN_BOOK){
            response.setErrorStatus(404, String.format("Book %s not found", bookid));
            return response;
        }

        return new EntityResponse().setSuccessStatus(201, convertor.toJson(book, bookdata));



    }

    public EntityResponse deleteBook(final String bookid, final String accept) {

        final EntityResponse response = new EntityResponse();

        if(bookdata.books().get(bookid)==PulpBook.UNKNOWN_BOOK){
            response.setErrorStatus(404, String.format("Book %s not found", bookid));
            return response;
        }

        if(bookdata.deleteBook(bookid)){
            response.setSuccessStatus(204, ""); // String.format("Book %s deleted", bookid));
        }else{
            response.setErrorStatus(500, String.format("Unknown error deleting Book %s", bookid));
        }

        return response;
    }

    public EntityResponse getSeries(final String seriesid, final String acceptformat) {
        final PulpSeries series = bookdata.series().get(seriesid);
        final EntityResponse response = new EntityResponse();

        if(series==PulpSeries.UNKNOWN_SERIES){
            response.setErrorStatus(404, String.format("Series %s not found", seriesid));
            return response;
        }

        return response.setSuccessStatus(200, convertor.toJson(series));

    }

    public EntityResponse getAllSeries(final String acceptformat) {
        final EntityResponse response = new EntityResponse();

        SeriesListEntity entity = new SeriesListEntity(bookdata.series());

        response.setSuccessStatus(200,new Gson().toJson(entity));
        return response;
    }

    public EntityResponse deleteSeries(final String seriesid, final String accept) {
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

    public EntityResponse getPublisher(final String publisherid, final String accept) {
        final PulpPublisher publisher = bookdata.publishers().get(publisherid);
        final EntityResponse response = new EntityResponse();

        if(publisher==PulpPublisher.UNKNOWN_PUBLISHER){
            response.setErrorStatus(404, String.format("Publisher %s not found", publisherid));
            return response;
        }

        return response.setSuccessStatus(200, convertor.toJson(publisher));

    }

    public EntityResponse getPublishers(final String accept) {
        final EntityResponse response = new EntityResponse();

        PublisherListEntity entity = new PublisherListEntity(bookdata.publishers());

        response.setSuccessStatus(200,new Gson().toJson(entity));
        return response;
    }

    public EntityResponse deletePublisher(final String publisherid, final String accept) {

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

    private EntityResponse canProcessContentType(String contentType){
        if(contentType==null || (!contentType.endsWith("json"))){
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content-type %s", contentType));
        }
        return null;
    }




    private class ActionToDo{

        public String actionName;
        public int errorStatus;
        public String errorMessage=null;
        public Map<String, String> headers = new HashMap<>();
        public AuthorEntity authorEntityToActOn;
        public SeriesEntity seriesEntityToActOn;
        private PublisherEntity publisherEntityToActOn;
        private BookEntity bookEntityToActOn;

        public ActionToDo isError(final int status, final String message) {
            actionName = "ERROR";
            errorStatus = status;
            errorMessage=message;
            return this;
        }

        public ActionToDo withHeader(final String key, final String value) {
            this.headers.put(key, value);
            return this;
        }

        public ActionToDo isCreate(final AuthorEntity author) {
            actionName = "CREATE";
            authorEntityToActOn = author;
            return this;
        }

        public ActionToDo isAmend(final AuthorEntity author) {
            actionName = "AMEND";
            authorEntityToActOn = author;
            return this;
        }

        public ActionToDo isCreate(final SeriesEntity series) {
            actionName = "CREATE";
            seriesEntityToActOn = series;
            return this;
        }

        public ActionToDo isAmend(final SeriesEntity series) {
            actionName = "AMEND";
            seriesEntityToActOn = series;
            return this;
        }

        public ActionToDo isCreate(final PublisherEntity publisher) {
            actionName = "CREATE";
            publisherEntityToActOn = publisher;
            return this;
        }

        public ActionToDo isAmend(final PublisherEntity publisher) {
            actionName = "AMEND";
            publisherEntityToActOn = publisher;
            return this;
        }

        public ActionToDo isCreate(final BookEntity book) {
            actionName = "CREATE";
            bookEntityToActOn = book;
            return this;
        }

        public ActionToDo isAmend(final BookEntity book) {
            actionName = "AMEND";
            bookEntityToActOn = book;
            return this;
        }
    }

    public EntityResponse createAmendAuthor(final String body, final String contentType, final String accept) {

        EntityResponse errorResponse = canProcessContentType(contentType);
        if(errorResponse!=null){
            return errorResponse;
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


        // did we get a single author?
        if(author!=null && author.name!=null){

            ActionToDo authorAction = identifyCreateAmendActionForAuthorEntity(author);
            return processCreateAmendAction(authorAction);

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

            List<EntityResponse> responses = new ArrayList<>();
            for(ActionToDo action : actions){

                responses.add(processCreateAmendAction(action));
            }

            // TODO - fix this egregious hack and create a proper bulk report entity
            return new EntityResponse().setSuccessStatus(200, new Gson().toJson(responses));

        }

    }

    public EntityResponse createReplaceAuthor(final String authorid, final String body, final String contentType, final String accept) {

        EntityResponse errorResponse = canProcessContentType(contentType);
        if(errorResponse!=null){
            return errorResponse;
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


            return processCreateAmendAction(
                            new ActionToDo().isAmend(
                                        new AuthorEntity(actualAuthor.getId(), author.name)));

        }else{

            // that was not an author
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content as Author %s", errorMessage));
        }

    }

    private EntityResponse processCreateAmendAction(final ActionToDo action) {

        String exceptionMessage = "";

        try{
            if(action.errorMessage!=null){
                EntityResponse response = new EntityResponse().setErrorStatus(action.errorStatus, action.errorMessage);
                for(String headerKey : action.headers.keySet()){
                    response.addHeader(headerKey, action.headers.get(headerKey));
                }
                return response;
            }

            if(action.actionName.contentEquals("CREATE")){
                if(action.authorEntityToActOn!=null){
                    final PulpAuthor actualAuthor = bookdata.authors().add(action.authorEntityToActOn.name);
                    EntityResponse response = new EntityResponse().setSuccessStatus(201, convertor.toJson(actualAuthor));
                    response.addHeader("location", getLocationHeaderFor(actualAuthor));
                    return response;
                }
                if(action.seriesEntityToActOn!=null){
                    final PulpSeries created = bookdata.series().add(action.seriesEntityToActOn.name);
                    EntityResponse response = new EntityResponse().setSuccessStatus(201, convertor.toJson(created));
                    response.addHeader("location", getLocationHeaderFor(created));
                    return response;
                }
                if(action.publisherEntityToActOn!=null){
                    final PulpPublisher created = bookdata.publishers().add(action.publisherEntityToActOn.name);
                    EntityResponse response = new EntityResponse().setSuccessStatus(201, convertor.toJson(created));
                    response.addHeader("location", getLocationHeaderFor(created));
                    return response;
                }
                if(action.bookEntityToActOn!=null){
                    return processCreateAmendBookAction(action);
                }

            }
            if(action.actionName.contentEquals("AMEND")){
                if(action.authorEntityToActOn!=null){
                    final PulpAuthor actual = bookdata.authors().get(action.authorEntityToActOn.id);
                    actual.amendName(action.authorEntityToActOn.name);
                    EntityResponse response = new EntityResponse().setSuccessStatus(200, convertor.toJson(actual));
                    return response;
                }
                if(action.seriesEntityToActOn!=null){
                    final PulpSeries actual = bookdata.series().get(action.seriesEntityToActOn.id);
                    actual.amendName(action.seriesEntityToActOn.name);
                    EntityResponse response = new EntityResponse().setSuccessStatus(200, convertor.toJson(actual));
                    return response;
                }
                if(action.publisherEntityToActOn!=null){
                    final PulpPublisher actual = bookdata.publishers().get(action.publisherEntityToActOn.id);
                    actual.amendName(action.publisherEntityToActOn.name);
                    EntityResponse response = new EntityResponse().setSuccessStatus(200, convertor.toJson(actual));
                    return response;
                }
                if(action.bookEntityToActOn!=null){
                    return processCreateAmendBookAction(action);
                }
            }
        }catch(Exception e) {
            exceptionMessage = e.getMessage();
        }

        return new EntityResponse().setErrorStatus(500, "Could not process action : " + new Gson().toJson(action) + " " + exceptionMessage);


    }



    private ActionToDo identifyCreateAmendActionForAuthorEntity(final AuthorEntity author) {

        final ActionToDo action = new ActionToDo();

        // ACTION: ERROR, 400, message
        if(author.name == null || author.name.length()==0){
            return action.isError(400, String.format("Author name cannot be empty"));
        }

        // ACTION: ERROR, 409, message, headers
        // ACTION: ERROR, 404, message
        // ACTION: CREATE, AuthorEntity
        // ACTION: AMEND, AuthorEntity

        // does it have an id?
        if(author.id ==null || author.id.length()==0){

            PulpAuthor existingAuthor = bookdata.authors().findByName(author.name);

            if(existingAuthor!=PulpAuthor.UNKNOWN_AUTHOR){
                return action.isError(409, String.format("Cannot create author. Author '%s' already exists with id %s.", existingAuthor.getName(), existingAuthor.getId())).
                        withHeader("location", getLocationHeaderFor(existingAuthor));
            }

            // OK, we will create this
            return action.isCreate(author);

        }else{
            // treat this as an amend
            if(bookdata.authors().get(author.id) == PulpAuthor.UNKNOWN_AUTHOR){
                return action.isError(404, String.format("Unknown Author %s", author.id));
            }

            return action.isAmend(author);
        }
    }

    private ActionToDo identifyCreateAmendActionForSeriesEntity(final SeriesEntity series) {
        final ActionToDo action = new ActionToDo();

        // ACTION: ERROR, 400, message
        if(series.name == null || series.name.length()==0){
            return action.isError(400, String.format("Series name cannot be empty"));
        }

        // ACTION: ERROR, 409, message, headers
        // ACTION: ERROR, 404, message
        // ACTION: CREATE, AuthorEntity
        // ACTION: AMEND, AuthorEntity

        // does it have an id?
        if(series.id ==null || series.id.length()==0){

            PulpSeries existing = bookdata.series().findByName(series.name);

            if(existing!=PulpSeries.UNKNOWN_SERIES){
                return action.isError(409, String.format("Cannot create series. Series '%s' already exists with id %s.", existing.getName(), existing.getId())).
                        withHeader("location", getLocationHeaderFor(existing));
            }

            // OK, we will create this
            return action.isCreate(series);

        }else{
            // treat this as an amend
            if(bookdata.series().get(series.id) == PulpSeries.UNKNOWN_SERIES){
                return action.isError(404, String.format("Unknown Series %s", series.id));
            }

            return action.isAmend(series);
        }
    }

    private ActionToDo identifyCreateAmendActionForPublisherEntity(final PublisherEntity single) {
        final ActionToDo action = new ActionToDo();

        // ACTION: ERROR, 400, message
        if(single.name == null || single.name.length()==0){
            return action.isError(400, String.format("Publisher name cannot be empty"));
        }

        // ACTION: ERROR, 409, message, headers
        // ACTION: ERROR, 404, message
        // ACTION: CREATE, AuthorEntity
        // ACTION: AMEND, AuthorEntity

        // does it have an id?
        if(single.id ==null || single.id.length()==0){

            PulpPublisher existing = bookdata.publishers().findByName(single.name);

            // check for duplicate name
            if(existing!=PulpPublisher.UNKNOWN_PUBLISHER){
                return action.isError(409, String.format("Cannot create publisher. Publisher '%s' already exists with id %s.", existing.getName(), existing.getId())).
                        withHeader("location", getLocationHeaderFor(existing));
            }

            // OK, we will create this
            return action.isCreate(single);

        }else{
            // treat this as an amend
            if(bookdata.publishers().get(single.id) == PulpPublisher.UNKNOWN_PUBLISHER){
                return action.isError(404, String.format("Unknown Publisher %s", single.id));
            }

            return action.isAmend(single);
        }
    }

    private ActionToDo identifyCreateAmendActionForBookEntity(final BookEntity single) {


        if(single.id !=null && single.id.length()>=0) {

            final PulpBook existingBook = bookdata.books().get(single.id);
            if(existingBook != null && existingBook!= PulpBook.UNKNOWN_BOOK) {
                // OK, we will amend this - allow duplicate titles - potentially duplicate books
                return new ActionToDo().isAmend(single);
            }else{
                return new ActionToDo().isError(404, String.format("Unknown Book %s %s", single.id, single.title));
            }
        }

        // Book mandatory
        // id
        // title
        // a series index to identify the series
        // at least one author index to identify the author
        // what to do for house author index?
        // a series id
        // a publication year
        // a publisher index to identify the publisher

        // ACTION: ERROR, 400, message
        if(single.title == null || single.title.length()==0){
            return new ActionToDo().isError(400, String.format("Series title cannot be empty"));
        }
        if(single.series==null || (single.series.id == null && single.series.name == null)){
            return new ActionToDo().isError(400, String.format("Series cannot be empty"));
        }
        if((single.series.id + single.series.name).length()==0){
            return new ActionToDo().isError(400, String.format("Series must be identifiable"));
        }
        if(single.publisher==null || (single.publisher.id == null && single.publisher.name == null)){
            return new ActionToDo().isError(400, String.format("Publisher cannot be empty"));
        }
        if((single.publisher.id + single.publisher.name).length()==0){
            return new ActionToDo().isError(400, String.format("Publisher must be identifiable"));
        }
        if(single.authors==null || single.authors.size()==0){
            return new ActionToDo().isError(400, String.format("Book must have authors"));
        }
        if(single.seriesId == null || single.seriesId.length()==0){
            return new ActionToDo().isError(400, String.format("Book must have a series id"));
        }
        if(single.publicationYear<=0){
            return new ActionToDo().isError(400, String.format("Book must have a publication year"));
        }


        // This is too complicated
        // publishers, series and authors must exist

        PulpPublisher publisher;
        if(single.publisher.id==null){
            publisher = bookdata.publishers().findByName(single.publisher.name);
        }else{
            publisher = bookdata.publishers().get(single.publisher.id);
        }

        if(publisher==null || publisher==PulpPublisher.UNKNOWN_PUBLISHER){
            return new ActionToDo().isError(404, String.format("Cannot find publisher %s named: %s", single.publisher.id, single.publisher.name));
        }

        PulpSeries series;
        if(single.series.id==null){
            series = bookdata.series().findByName(single.series.name);
        }else{
            series = bookdata.series().get(single.series.id);
        }

        if(series==null || series==PulpSeries.UNKNOWN_SERIES){
            return new ActionToDo().isError(404, String.format("Cannot find series %s named: %s", single.series.id, single.series.name));
        }


        for(AuthorEntity anAuthor : single.authors){

            PulpAuthor author;
            if(anAuthor.id==null){
                author = bookdata.authors().findByName(anAuthor.name);
            }else{
                author = bookdata.authors().get(anAuthor.id);
            }

            if(author==null || author==PulpAuthor.UNKNOWN_AUTHOR){
                return new ActionToDo().isError(404, String.format("Cannot find author %s named: %s", anAuthor.id, anAuthor.name));
            }
        }

        // OK, we will create this - allow duplicate titles - potentially duplicate books
        return new ActionToDo().isCreate(single);

    }

    private EntityResponse processCreateAmendBookAction(final ActionToDo action) {

        final BookEntity book = action.bookEntityToActOn;

        PulpPublisher publisher = null;
        if(book.publisher != null) {
            if (book.publisher.id == null) {
                publisher = bookdata.publishers().findByName(book.publisher.name);
            } else {
                publisher = bookdata.publishers().get(book.publisher.id);
            }
        }

        PulpSeries series=null;
        if(book.series != null) {
            if (book.series != null && book.series.id == null) {
                series = bookdata.series().findByName(book.series.name);
            } else {
                series = bookdata.series().get(book.series.id);
            }
        }


        List<PulpAuthor> authors = new ArrayList<PulpAuthor>();
        List<String> authorIds = new ArrayList<>();

        if(book.authors!=null) {
            for (AuthorEntity anAuthor : book.authors) {

                PulpAuthor author;
                if (anAuthor.id == null) {
                    author = bookdata.authors().findByName(anAuthor.name);
                } else {
                    author = bookdata.authors().get(anAuthor.id);
                }

                if (author == null || author == PulpAuthor.UNKNOWN_AUTHOR) {
                    return new EntityResponse().setErrorStatus(404, String.format("Cannot find author %s named: %s", anAuthor.id, anAuthor.name));
                }

                authors.add(author);
                authorIds.add(author.getId());
            }
        }

        if(action.actionName.contentEquals("CREATE")) {

            if(publisher==null || publisher==PulpPublisher.UNKNOWN_PUBLISHER){
                return new EntityResponse().setErrorStatus(400, String.format("Cannot find publisher %s named: %s", book.publisher.id, book.publisher.name));
            }

            if(series==null || series==PulpSeries.UNKNOWN_SERIES){
                return new EntityResponse().setErrorStatus(400, String.format("Cannot find series %s named: %s", book.series.id, book.series.name));
            }

            if(authors.size()==0){
                return new EntityResponse().setErrorStatus(400, String.format("Cannot create a book without authors"));
            }

            final PulpBook actualBook = bookdata.books().add(series.getId(), authors.get(0).getId(), "", book.title, book.seriesId, book.publicationYear, publisher.getId());

            for (PulpAuthor addAuthor : authors) {
                actualBook.addCoAuthor(addAuthor.getId());
            }

            return new EntityResponse().setSuccessStatus(201, convertor.toJson(actualBook, bookdata));

        }

        if(action.actionName.contentEquals("AMEND")) {

            if(publisher!=null && publisher==PulpPublisher.UNKNOWN_PUBLISHER){
                return new EntityResponse().setErrorStatus(400, String.format("Cannot find publisher %s named: %s", book.publisher.id, book.publisher.name));
            }

            if(series!=null && series==PulpSeries.UNKNOWN_SERIES){
                return new EntityResponse().setErrorStatus(400, String.format("Cannot find series %s named: %s", book.series.id, book.series.name));
            }

            final BookEntity bookDetails = action.bookEntityToActOn;

            if(bookDetails.authors!=null && authorIds.size()==0){
                return new EntityResponse().setErrorStatus(400, String.format("Cannot remove all authors"));
            }


            final PulpBook actualBook = bookdata.books().get(action.bookEntityToActOn.id);
            actualBook.amendTitle(bookDetails.title);
            if(bookDetails.publicationYear>0) {
                actualBook.amendPublicationYear(String.valueOf(bookDetails.publicationYear));
            }

            if(publisher!=null) {
                actualBook.amendPublisher(publisher.getId());
            }
            if(series!=null) {
                actualBook.amendSeries(series.getId());
            }
            actualBook.amendPatchAuthors(authorIds);
            actualBook.amendSeriesIdentifier(bookDetails.seriesId);
            return new EntityResponse().setSuccessStatus(200, convertor.toJson(actualBook, bookdata));
        }

        return new EntityResponse().setErrorStatus(500, String.format("Error processing action %s", new Gson().toJson(action)));
    }


    public EntityResponse createAmendSeries(final String body, final String contentType, final String accept) {

        EntityResponse errorResponse = canProcessContentType(contentType);
        if(errorResponse!=null){
            return errorResponse;
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


        // did we get a single series?
        if(series!=null && series.name!=null){

            ActionToDo action = identifyCreateAmendActionForSeriesEntity(series);
            return processCreateAmendAction(action);

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

            List<EntityResponse> responses = new ArrayList<>();
            for(ActionToDo action : actions){

                responses.add(processCreateAmendAction(action));
            }

            // TODO - fix this egregious hack and create a proper bulk report entity
            return new EntityResponse().setSuccessStatus(200, new Gson().toJson(responses));

        }
    }

    public EntityResponse createAmendPublisher(final String body, final String contentType, final String accept) {

        EntityResponse errorResponse = canProcessContentType(contentType);
        if(errorResponse!=null){
            return errorResponse;
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


        // did we get a single item?
        if(single!=null && single.name!=null){

            ActionToDo action = identifyCreateAmendActionForPublisherEntity(single);
            return processCreateAmendAction(action);

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

            List<EntityResponse> responses = new ArrayList<>();
            for(ActionToDo action : actions){

                responses.add(processCreateAmendAction(action));
            }

            // TODO - fix this egregious hack and create a proper bulk report entity
            return new EntityResponse().setSuccessStatus(200, new Gson().toJson(responses));

        }
    }

    public EntityResponse createAmendBook(final String body, final String contentType, final String accept) {

        // Book is more complicated because it has sub relationships

        EntityResponse errorResponse = canProcessContentType(contentType);
        if(errorResponse!=null){
            return errorResponse;
        }

        String errorMessage = "";

        BookEntity single=null;
        BooksListEntity list= new BooksListEntity(new PulpData());

        try {
            single = new Gson().fromJson(body, BookEntity.class);
        }catch (Exception e) {
            errorMessage = e.getMessage();
        }

        try {
            list = new Gson().fromJson(body, BooksListEntity.class);
        }catch (Exception e2){
            // nope - can't accept this then
            errorMessage = errorMessage + " , " + e2.getMessage();
        }

        if(errorMessage.length()>0){
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content as Books %s", errorMessage));
        }


        // Minimum for a book

        // did we get a single item?
        if(single!=null && (single.title!=null || single.id!=null || single.seriesId!=null || single.authors!=null ||
                            single.series!=null || single.publisher!=null || single.publicationYear>0)){

            ActionToDo action = identifyCreateAmendActionForBookEntity(single);
            return processCreateAmendAction(action);

        }else{

            if(list == null || list.books == null){
                // that was not a Series list
                return new EntityResponse().setErrorStatus(400, String.format("Cannot process content as Books %s", errorMessage));
            }

            List<ActionToDo> actions = new ArrayList();

            // process book list
            for( BookEntity aSingleItem : list.books){
                actions.add(identifyCreateAmendActionForBookEntity(aSingleItem));
            }

            List<EntityResponse> responses = new ArrayList<>();
            for(ActionToDo action : actions){

                responses.add(processCreateAmendAction(action));
            }

            // TODO - fix this egregious hack and create a proper bulk report entity
            return new EntityResponse().setSuccessStatus(200, new Gson().toJson(responses));

        }
    }




    private String getLocationHeaderFor(final PulpAuthor theAuthor) {
        return rooturl + "/authors/" + theAuthor.getId();
    }
    private String getLocationHeaderFor(final PulpSeries existing) {
        return rooturl + "/series/" + existing.getId();
    }
    private String getLocationHeaderFor(final PulpPublisher existing) {
        return rooturl + "/publishers/" + existing.getId();
    }


    public PulpEntities setRootUrl(final String rootUrl) {
        this.rooturl = rootUrl;
        return this;
    }


}
