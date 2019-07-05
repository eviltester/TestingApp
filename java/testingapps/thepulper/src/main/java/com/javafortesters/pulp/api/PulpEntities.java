package com.javafortesters.pulp.api;

import com.google.gson.Gson;
import com.javafortesters.pulp.api.entities.lists.BooksListEntity;
import com.javafortesters.pulp.api.entities.lists.PublisherListEntity;
import com.javafortesters.pulp.api.entities.lists.SeriesListEntity;
import com.javafortesters.pulp.api.entities.single.*;
import com.javafortesters.pulp.api.entities.lists.AuthorListEntity;
import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpBook;
import com.javafortesters.pulp.domain.objects.PulpPublisher;
import com.javafortesters.pulp.domain.objects.PulpSeries;

public class PulpEntities {
    private final PulpData bookdata;

    public PulpEntities(final PulpData books) {
        this.bookdata = books;
    }

    public EntityResponse getAuthor(final String authorid, final String acceptformat) {
        final PulpAuthor author = bookdata.authors().get(authorid);
        final EntityResponse response = new EntityResponse();

        if(author==PulpAuthor.UNKNOWN_AUTHOR){
            response.setErrorStatus(404, String.format("Author %s not found", authorid));
            return response;
        }

        AuthorEntity entity = new AuthorEntity(author.getId(), author.getName());

        response.setSuccessStatus(200,new Gson().toJson(entity));
        return response;
    }

    public EntityResponse getAuthors(final String acceptformat) {
        final EntityResponse response = new EntityResponse();

        AuthorListEntity entity = new AuthorListEntity(bookdata.authors());

        response.setSuccessStatus(200,new Gson().toJson(entity));
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
}
