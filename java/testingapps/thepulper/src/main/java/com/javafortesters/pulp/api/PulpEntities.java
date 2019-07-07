package com.javafortesters.pulp.api;

import com.google.gson.Gson;
import com.javafortesters.pulp.api.entities.lists.BooksListEntity;
import com.javafortesters.pulp.api.entities.lists.PublisherListEntity;
import com.javafortesters.pulp.api.entities.lists.SeriesListEntity;
import com.javafortesters.pulp.api.entities.single.*;
import com.javafortesters.pulp.api.entities.lists.AuthorListEntity;
import com.javafortesters.pulp.domain.groupings.PulpAuthors;
import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpBook;
import com.javafortesters.pulp.domain.objects.PulpPublisher;
import com.javafortesters.pulp.domain.objects.PulpSeries;

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

        BookEntity entity = new BookEntity(book.getId(), book.getTitle(), book.getPublicationYear(), book.getSeriesId(),
                                            bookdata.series().get(book.getSeriesIndex()),
                                            bookdata.authors().getAll(book.getAuthorIndexes()),
                                            bookdata.publishers().get(book.getPublisherIndex())
                                            );

        response.setSuccessStatus(200,new Gson().toJson(entity));
        return response;
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

        SeriesEntity entity = new SeriesEntity(series.getId(), series.getName());

        response.setSuccessStatus(200,new Gson().toJson(entity));
        return response;
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

        PublisherEntity entity = new PublisherEntity(publisher.getId(), publisher.getName());

        response.setSuccessStatus(200,new Gson().toJson(entity));
        return response;
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

    public EntityResponse createAmendAuthor(final String body, final String contentType, final String accept) {
        final EntityResponse response = new EntityResponse();

        if(contentType==null || (!contentType.endsWith("json"))){
            response.setErrorStatus(400, String.format("Cannot process content-type %s", contentType));
            return response;
        }

        String errorMessage = "";

        AuthorEntity author=null;
        AuthorListEntity authorList= new AuthorListEntity(new PulpAuthors());

        try {
            author = new Gson().fromJson(body, AuthorEntity.class);
        }catch (Exception e){
            // ok, it isn't an author, is it a list of authors?
            errorMessage = e.getMessage();
            try {
                authorList = new Gson().fromJson(body, AuthorListEntity.class);
            }catch (Exception e2){
                // nope - can't accept this then
                errorMessage = errorMessage + " , " + e2.getMessage();
            }
        }

        if(errorMessage.length()>0){
            response.setErrorStatus(400, String.format("Cannot process content as Author %s", errorMessage));
            return response;
        }

        PulpAuthor actualAuthor=null;

        // did we get an author?
        if(author!=null){

            if(author.name == null || author.name.length()==0){
                response.setErrorStatus(400, String.format("Author name cannot be empty", errorMessage));
                return response;
            }

            // does it have an id?
            if(author.id ==null || author.id.length()==0){

                PulpAuthor existingAuthor = bookdata.authors().findByName(author.name);
                if(existingAuthor!=PulpAuthor.UNKNOWN_AUTHOR){
                    response.setErrorStatus(409, String.format("Cannot create author. Author '%s' already exists with id %s.", existingAuthor.getName(), existingAuthor.getId()));
                    addLocationHeaderFor(response, existingAuthor);
                    return response;
                }

                // we need to allocate an id and treat this as a create
                actualAuthor = bookdata.authors().add(author.name);
                response.setSuccessStatus(201, convertor.toJson(actualAuthor));
                addLocationHeaderFor(response, actualAuthor);

            }else{
                // treat this as an amend
                actualAuthor = bookdata.authors().get(author.id);
                if(actualAuthor== PulpAuthor.UNKNOWN_AUTHOR){
                    response.setErrorStatus(404, String.format("Unknown Author %s", author.id));
                    return response;
                }

                actualAuthor.amendName(author.name);
                response.setSuccessStatus(200, convertor.toJson(actualAuthor));
            }

        }

        return response;
    }

    private void addLocationHeaderFor(final EntityResponse response, final PulpAuthor theAuthor) {
        response.addHeader("location", rooturl + "/authors/" + theAuthor.getId());
    }

    public PulpEntities setRootUrl(final String rootUrl) {
        this.rooturl = rootUrl;
        return this;
    }
}
