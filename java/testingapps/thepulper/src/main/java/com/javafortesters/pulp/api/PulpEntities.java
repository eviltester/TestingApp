package com.javafortesters.pulp.api;

import com.javafortesters.pulp.api.actions.*;
import com.javafortesters.pulp.domain.groupings.PulpData;


public class PulpEntities {
    private final PulpData bookdata;
    private final DomainToEntityConvertor convertor;;
    private String rooturl;

    public PulpEntities(final PulpData books) {
        this.bookdata = books;
        convertor = new DomainToEntityConvertor(books);
        rooturl = "";
    }

    public PulpEntities setRootUrl(final String rootUrl) {
        this.rooturl = rootUrl;
        return this;
    }


    public EntityResponse getAuthor(final String authorid, final String acceptformat) {
        return new AuthorActions(bookdata, convertor, rooturl).getSingle(authorid, acceptformat);
    }

    public EntityResponse getAuthors(final String acceptformat) {
        return new AuthorActions(bookdata, convertor, rooturl).getAll(acceptformat);
    }

    public EntityResponse deleteAuthor(final String authorid, final String accept) {
        return new AuthorActions(bookdata, convertor, rooturl).deleteSingle(authorid, accept);
    }

    public EntityResponse getBooks(final String acceptFormat) {
        return new BookActions(bookdata, convertor, rooturl).getAll(acceptFormat);
    }

    public EntityResponse getBook(final String bookid, final String acceptformat) {
        return new BookActions(bookdata, convertor, rooturl).getSingle(bookid, acceptformat);
    }

    public EntityResponse deleteBook(final String bookid, final String accept) {
        return new BookActions(bookdata, convertor, rooturl).deleteSingle(bookid, accept);
    }

    public EntityResponse getSeries(final String seriesid, final String acceptformat) {
        return new SeriesActions(bookdata, convertor, rooturl).getSingle(seriesid, acceptformat);
    }

    public EntityResponse getAllSeries(final String acceptformat) {
        return new SeriesActions(bookdata, convertor, rooturl).getAll(acceptformat);
    }

    public EntityResponse deleteSeries(final String seriesid, final String accept) {
        return new SeriesActions(bookdata, convertor, rooturl).deleteSingle(seriesid, accept);
    }

    public EntityResponse getPublisher(final String publisherid, final String accept) {
        return new PublisherActions(bookdata, convertor, rooturl).getSingle(publisherid, accept);
    }

    public EntityResponse getPublishers(final String accept) {
        return new PublisherActions(bookdata, convertor, rooturl).getAll(accept);
    }

    public EntityResponse deletePublisher(final String publisherid, final String accept) {
        return new PublisherActions(bookdata, convertor, rooturl).deleteSingle(publisherid, accept);
    }

    public EntityResponse createAmendAuthor(final String body, final String contentType, final String accept) {
        return new AuthorActions(bookdata, convertor, rooturl).createAmend(body, contentType, accept);
    }

    public EntityResponse createReplaceAuthor(final String authorid, final String body, final String contentType, final String accept) {
        return new AuthorActions(bookdata, convertor, rooturl).createReplace(authorid, body, contentType, accept);
    }

    public EntityResponse createReplaceSeries(final String seriesId, final String body, final String contentType, final String accept) {
        return new SeriesActions(bookdata, convertor, rooturl).createReplace(seriesId, body, contentType, accept);
    }

    public EntityResponse createReplacePublisher(final String publisherid, final String body, final String contentType, final String accept) {
        return new PublisherActions(bookdata, convertor, rooturl).createReplace(publisherid, body, contentType, accept);
    }

    public EntityResponse createReplaceBook(final String bookid, final String body, final String contentType, final String accept) {
        return new BookActions(bookdata, convertor, rooturl).createReplace(bookid, body, contentType, accept);
    }

    public EntityResponse createAmendSeries(final String body, final String contentType, final String accept) {
        return new SeriesActions(bookdata, convertor, rooturl).createAmend(body, contentType, accept);
    }

    public EntityResponse createAmendPublisher(final String body, final String contentType, final String accept) {
        return new PublisherActions(bookdata, convertor, rooturl).createAmend(body, contentType, accept);
    }

    public EntityResponse createAmendBook(final String body, final String contentType, final String accept) {
        return new BookActions(bookdata, convertor, rooturl).createAmend(body, contentType, accept);
    }

    public EntityResponse patchAuthor(final String authorid, final String body, final String contentType, final String accept) {
        return new AuthorActions(bookdata, convertor, rooturl).patchAmend(authorid, body, contentType, accept);
    }

    public EntityResponse patchSeries(final String seriesid, final String body, final String contentType, final String accept) {
        return new SeriesActions(bookdata, convertor, rooturl).patchAmend(seriesid, body, contentType, accept);
    }

    public EntityResponse patchPublisher(final String publisherid, final String body, final String contentType, final String accept) {
        return new PublisherActions(bookdata, convertor, rooturl).patchAmend(publisherid, body, contentType, accept);
    }

    public EntityResponse patchBook(final String bookid, final String body, final String contentType, final String accept) {
        return new BookActions(bookdata, convertor, rooturl).patchAmend(bookid, body, contentType, accept);
    }
}
