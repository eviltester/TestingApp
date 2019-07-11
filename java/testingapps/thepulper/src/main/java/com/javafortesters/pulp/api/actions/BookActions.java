package com.javafortesters.pulp.api.actions;

import com.google.gson.Gson;
import com.javafortesters.pulp.api.DomainToEntityConvertor;
import com.javafortesters.pulp.api.EntityResponse;
import com.javafortesters.pulp.api.entities.lists.BooksListEntity;
import com.javafortesters.pulp.api.entities.payloads.ApiResponseBuilder;
import com.javafortesters.pulp.api.entities.single.AuthorEntity;
import com.javafortesters.pulp.api.entities.single.BookEntity;
import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpBook;
import com.javafortesters.pulp.domain.objects.PulpPublisher;
import com.javafortesters.pulp.domain.objects.PulpSeries;

import java.util.ArrayList;
import java.util.List;

public class BookActions {

    private final PulpData bookdata;
    private final DomainToEntityConvertor convertor;
    private final String rooturl;

    public BookActions(final PulpData bookdata, final DomainToEntityConvertor convertor, final String rooturl) {
        this.bookdata = bookdata;
        this.convertor = convertor;
        this.rooturl = rooturl;
    }

    public EntityResponse getAll(final String acceptFormat) {
        final EntityResponse response = new EntityResponse();

        BooksListEntity entity = new BooksListEntity(bookdata);

        response.setSuccessStatus(200,
                new Gson().toJson(
                        new ApiResponseBuilder(bookdata).addData(bookdata.books()).getApiResponse())
        );

        //response.setSuccessStatus(200,new Gson().toJson(entity));
        return response;
    }

    public EntityResponse getSingle(final String bookid, final String acceptformat) {
        final PulpBook book = bookdata.books().get(bookid);
        final EntityResponse response = new EntityResponse();

        if(book==PulpBook.UNKNOWN_BOOK){
            response.setErrorStatus(404, String.format("Book %s not found", bookid));
            return response;
        }

        return response.setSuccessStatus(200,
                new Gson().toJson(
                        new ApiResponseBuilder(bookdata).addData(book).getApiResponse())
                );

        //return new EntityResponse().setSuccessStatus(200, convertor.toJson(book));
    }

    public EntityResponse deleteSingle(final String bookid, final String accept) {
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

    public EntityResponse createReplace(final String bookid, final String body, final String contentType, final String accept) {
        if(contentType==null || (!contentType.endsWith("json"))){
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content-type %s", contentType));
        }

        String errorMessage = "";

        BookEntity book=null;

        try {
            book = new Gson().fromJson(body, BookEntity.class);
        }catch (Exception e) {
            // ok, it isn't an publisher
            errorMessage = e.getMessage();
        }

        if(errorMessage.length()>0){
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content as Book %s", errorMessage));
        }

        PulpBook actualBook = bookdata.books().get(bookid);
        if(actualBook==null || actualBook==PulpBook.UNKNOWN_BOOK){
            return new EntityResponse().setErrorStatus(404, String.format("Cannot find Book %s", bookid));
        }

        // did we get a single book?
        if(book!=null && book.title!=null){

            if(book.id!=null && book.id.length()>0){
                // do not allow creation of book with PUT
                return new EntityResponse().setErrorStatus(400, String.format("Cannot create Book '%s' with a defined id %s", book.title, book.id));
            }

            if(book.title.length()<=0){
                return new EntityResponse().setErrorStatus(400, String.format("Invalid Book Title '%s'", book.title));
            }

            if(book.publicationYear == null || book.publicationYear<=0){
                return new EntityResponse().setErrorStatus(400, String.format("Invalid Book Publication Year '%d'", book.publicationYear));
            }

            if(book.seriesId==null || book.seriesId.length()==0){
                return new EntityResponse().setErrorStatus(400, String.format("Invalid SeriesId"));
            }

            if(book.series==null || (book.series.id == null && book.series.name == null)){
                return new EntityResponse().setErrorStatus(400, String.format("Series cannot be empty"));
            }

            if((book.series.id + book.series.name).length()==0){
                return new EntityResponse().setErrorStatus(400, String.format("Series must be identifiable"));
            }

            if(book.publisher==null || (book.publisher.id == null && book.publisher.name == null)){
                return new EntityResponse().setErrorStatus(400, String.format("Publisher cannot be empty"));
            }
            if((book.publisher.id + book.publisher.name).length()==0){
                return new EntityResponse().setErrorStatus(400, String.format("Publisher must be identifiable"));
            }
            if(book.authors==null || book.authors.size()==0){
                return new EntityResponse().setErrorStatus(400, String.format("Book must have authors"));
            }


            ActionProcessor actioner = new ActionProcessor(bookdata, convertor, rooturl);
            List<ActionEntityResponsePair> responses = new ArrayList<>();
            ActionToDo action = new ActionToDo().isReplace(
                            new BookEntity(actualBook.getId(), book.title, book.publicationYear, book.seriesId, book.series, book.authors, book.publisher));
            responses.add(new ActionEntityResponsePair(action, actioner.process(action)));
            return new BulkResponse(responses, bookdata).asEntityResponse();

//            return new ActionProcessor(bookdata, convertor, rooturl).process(
//                    new ActionToDo().isReplace(
//                            new BookEntity(actualBook.getId(), book.title, book.publicationYear, book.seriesId, book.series, book.authors, book.publisher)));

        }else{
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content as Book %s", errorMessage));
        }
    }

    public EntityResponse createAmend(final String body, final String contentType, final String accept) {
        // Book is more complicated because it has sub relationships

        if(contentType==null || (!contentType.endsWith("json"))){
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content-type %s", contentType));
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

        ActionProcessor actioner = new ActionProcessor(bookdata, convertor, rooturl);
        List<ActionEntityResponsePair> responses = new ArrayList<>();

        // did we get a single item?
        if(single!=null && (single.title!=null || single.id!=null || single.seriesId!=null || single.authors!=null ||
                single.series!=null || single.publisher!=null || (single.publicationYear!=null && single.publicationYear>0))){

            // treat single POST as bulk action for a bulk action response
            //ActionToDo action = identifyCreateAmendActionForBookEntity(single);
            //return new ActionProcessor(bookdata, convertor, rooturl).process(action);

            ActionToDo action = identifyCreateAmendActionForBookEntity(single);
            responses.add(new ActionEntityResponsePair(action, actioner.process(action)));

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

            for(ActionToDo action : actions){
                responses.add(new ActionEntityResponsePair(action, actioner.process(action)));
            }
        }

        return new BulkResponse(responses, bookdata).asEntityResponse();
    }

    private ActionToDo identifyCreateAmendActionForBookEntity(final BookEntity single) {


        if(single.id !=null && single.id.length()>=0) {

            final PulpBook existingBook = bookdata.books().get(single.id);
            if(existingBook != null && existingBook!= PulpBook.UNKNOWN_BOOK) {
                // OK, we will amend this - allow duplicate titles - potentially duplicate books
                return new ActionToDo().isAmend(single);
            }else{
                return new ActionToDo().isError(404, String.format("Unknown Book %s %s", single.id, single.title)).withBook(single);
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
            return new ActionToDo().isError(400, String.format("Book title cannot be empty")).withBook(single);
        }
        if(single.series==null || (single.series.id == null && single.series.name == null)){
            return new ActionToDo().isError(400, String.format("Series cannot be empty")).withBook(single);
        }
        if((single.series.id + single.series.name).length()==0){
            return new ActionToDo().isError(400, String.format("Series must be identifiable")).withBook(single);
        }
        if(single.publisher==null || (single.publisher.id == null && single.publisher.name == null)){
            return new ActionToDo().isError(400, String.format("Publisher cannot be empty")).withBook(single);
        }
        if((single.publisher.id + single.publisher.name).length()==0){
            return new ActionToDo().isError(400, String.format("Publisher must be identifiable")).withBook(single);
        }
        if(single.authors==null || single.authors.size()==0){
            return new ActionToDo().isError(400, String.format("Book must have authors")).withBook(single);
        }
        if(single.seriesId == null || single.seriesId.length()==0){
            return new ActionToDo().isError(400, String.format("Book must have a series id")).withBook(single);
        }
        if(single.publicationYear!=null && single.publicationYear<=0){
            return new ActionToDo().isError(400, String.format("Book must have a publication year")).withBook(single);
        }


        // publishers, series and authors must exist

        PulpPublisher publisher;
        if(single.publisher.id==null){
            publisher = bookdata.publishers().findByName(single.publisher.name);
        }else{
            publisher = bookdata.publishers().get(single.publisher.id);
        }

        if(publisher==null || publisher==PulpPublisher.UNKNOWN_PUBLISHER){
            return new ActionToDo().isError(404, String.format("Cannot find publisher %s named: %s", single.publisher.id, single.publisher.name)).withBook(single);
        }

        PulpSeries series;
        if(single.series.id==null){
            series = bookdata.series().findByName(single.series.name);
        }else{
            series = bookdata.series().get(single.series.id);
        }

        if(series==null || series==PulpSeries.UNKNOWN_SERIES){
            return new ActionToDo().isError(404, String.format("Cannot find series %s named: %s", single.series.id, single.series.name)).withBook(single);
        }


        for(AuthorEntity anAuthor : single.authors){

            PulpAuthor author;
            if(anAuthor.id==null){
                author = bookdata.authors().findByName(anAuthor.name);
            }else{
                author = bookdata.authors().get(anAuthor.id);
            }

            if(author==null || author==PulpAuthor.UNKNOWN_AUTHOR){
                return new ActionToDo().isError(404, String.format("Cannot find author %s named: %s", anAuthor.id, anAuthor.name)).withBook(single);
            }
        }

        // OK, we will create this - allow duplicate titles - potentially duplicate books
        return new ActionToDo().isCreate(single);

    }

    public EntityResponse patchAmend(final String bookid, final String body, final String contentType, final String accept) {
        if(contentType==null || (!contentType.endsWith("json"))){
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content-type %s", contentType));
        }

        String errorMessage = "";

        BookEntity book=null;

        try {
            book = new Gson().fromJson(body, BookEntity.class);
        }catch (Exception e) {
            // ok, it isn't an publisher
            errorMessage = e.getMessage();
        }

        if(errorMessage.length()>0){
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content as Book %s", errorMessage));
        }

        PulpBook actualBook = bookdata.books().get(bookid);
        if(actualBook==null || actualBook==PulpBook.UNKNOWN_BOOK){
            return new EntityResponse().setErrorStatus(404, String.format("Cannot find Book %s", bookid));
        }

        // did we get a single book?
        if(book!=null){

            if(book.id!=null && book.id.length()>0){
                // do not allow creation of book with PUT
                return new EntityResponse().setErrorStatus(400, String.format("Cannot change book id"));
            }

            if(book.title!=null && book.title.trim().length()<=0){
                // do not allow creation of publisher with PUT with invalid name
                return new EntityResponse().setErrorStatus(400, String.format("Invalid Book Title '%s'", book.title));
            }

            if(book.seriesId!=null && book.seriesId.length()==0){
                // do not allow creation of book with PUT
                return new EntityResponse().setErrorStatus(400, String.format("Invalid SeriesId"));
            }

            if(book.series!=null && (book.series.id == null && book.series.name == null)){
                return new EntityResponse().setErrorStatus(400, String.format("Series cannot be empty"));
            }

            if(book.series!=null && (book.series.id + book.series.name).trim().length()==0){
                return new EntityResponse().setErrorStatus(400, String.format("Series must be identifiable"));
            }

            if(book.publisher!=null && (book.publisher.id == null && book.publisher.name == null)){
                return new EntityResponse().setErrorStatus(400, String.format("Publisher cannot be empty"));
            }
            if(book.publisher!=null && (book.publisher.id + book.publisher.name).trim().length()==0){
                return new EntityResponse().setErrorStatus(400, String.format("Publisher must be identifiable"));
            }
            if(book.authors!=null && book.authors.size()==0){
                return new EntityResponse().setErrorStatus(400, String.format("Book must have authors"));
            }


            ActionProcessor actioner = new ActionProcessor(bookdata, convertor, rooturl);
            List<ActionEntityResponsePair> responses = new ArrayList<>();
            ActionToDo action = new ActionToDo().isPatch(
                    new BookEntity(actualBook.getId(), book.title, book.publicationYear, book.seriesId, book.series, book.authors, book.publisher));
            responses.add(new ActionEntityResponsePair(action, actioner.process(action)));
            return new BulkResponse(responses, bookdata).asEntityResponse();


//            return new ActionProcessor(bookdata, convertor, rooturl).process(
//                    new ActionToDo().isPatch(
//                            new BookEntity(actualBook.getId(), book.title, book.publicationYear, book.seriesId, book.series, book.authors, book.publisher)));

        }else{
            return new EntityResponse().setErrorStatus(400, String.format("Cannot process content as Book %s", errorMessage));
        }
    }
}
