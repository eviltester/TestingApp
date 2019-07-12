package com.javafortesters.pulp.api.entities.payloads;

import com.javafortesters.pulp.api.DomainToEntityConvertor;
import com.javafortesters.pulp.api.entities.IncludeFieldNames;
import com.javafortesters.pulp.api.entities.lists.BooksListEntity;
import com.javafortesters.pulp.api.entities.lists.ListPopulator;
import com.javafortesters.pulp.api.entities.payloads.responses.*;
import com.javafortesters.pulp.domain.groupings.*;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpBook;
import com.javafortesters.pulp.domain.objects.PulpPublisher;
import com.javafortesters.pulp.domain.objects.PulpSeries;

import java.util.ArrayList;

public class ApiResponseBuilder {

    private final DomainToEntityConvertor convertor;
    private final PulpData bookData;
    private ApiResponse response;

    public ApiResponseBuilder(final PulpData bookdata){
        this.bookData = bookdata;
        convertor = new DomainToEntityConvertor(bookdata);
        response = new ApiResponse();
    }

    public ApiResponse getApiResponse() {
        return response;
    }

    public ApiResponseBuilder addData(final PulpAuthor author) {
        ensureDataAuthorListExists();
        response.data.authors.add(convertor.toEntity(author));
        return this;
    }

    public ApiResponseBuilder addData(final PulpAuthors authors) {
        ensureDataAuthorListExists();
        new ListPopulator().populate(response.data.authors, authors);
        return this;
    }

    public ApiResponseBuilder addData(final PulpPublisher publisher) {
        ensureDataPublisherListExists();
        response.data.publishers.add(convertor.toEntity(publisher));
        return this;
    }

    public ApiResponseBuilder addData(final PulpPublishers publishers) {
        ensureDataPublisherListExists();
        new ListPopulator().populate(response.data.publishers, publishers);
        return this;
    }

    public ApiResponseBuilder addData(final PulpSeries series) {
        ensureDataSeriesListExists();
        response.data.series.add(convertor.toEntity(series));
        return this;
    }

    public ApiResponseBuilder addData(final PulpSeriesCollection series) {
        ensureDataSeriesListExists();
        new ListPopulator().populate(response.data.series, series);
        return this;
    }

    public ApiResponseBuilder addData(final PulpBook book) {
        ensureDataBookListExists();
        response.data.books.add(convertor.toEntity(book));
        return this;
    }

    public ApiResponseBuilder addData(final PulpBooks books) {
        ensureDataBookListExists();
        new ListPopulator().populate(response.data.books, books, bookData);
        return this;
    }


    public void addCreated(final PulpBook actualBook) {
        ensureCreatedBookListExists();
        response.logs.created.books.add(convertor.toEntity(actualBook, new IncludeFieldNames("id")));
        addData(actualBook);
    }

    public void addCreated(final PulpAuthor actualAuthor) {
        ensureCreatedAuthorListExists();
        response.logs.created.authors.add(convertor.toEntity(actualAuthor, new IncludeFieldNames("id")));
        addData(actualAuthor);
    }

    public void addCreated(final PulpSeries actualSeries) {
        ensureCreatedSeriesListExists();
        response.logs.created.series.add(convertor.toEntity(actualSeries, new IncludeFieldNames("id")));
        addData(actualSeries);
    }

    public void addCreated(final PulpPublisher actualPublisher) {
        ensureCreatedPublisherListExists();
        response.logs.created.publishers.add(convertor.toEntity(actualPublisher, new IncludeFieldNames("id")));
        addData(actualPublisher);
    }

    public void addAmended(final PulpAuthor actualAuthor) {
        ensureAmendedAuthorListExists();
        response.logs.amended.authors.add(convertor.toEntity(actualAuthor, new IncludeFieldNames("id")));
        addData(actualAuthor);
    }

    public void addAmended(final PulpSeries actualSeries) {
        ensureAmendedSeriesListExists();
        response.logs.amended.series.add(convertor.toEntity(actualSeries, new IncludeFieldNames("id")));
        addData(actualSeries);
    }

    public void addAmended(final PulpPublisher actualPublisher) {
        ensureAmendedPublisherListExists();
        response.logs.amended.publishers.add(convertor.toEntity(actualPublisher, new IncludeFieldNames("id")));
        addData(actualPublisher);
    }

    public void addAmended(final PulpBook actualBook) {
        ensureAmendedBookListExists();
        response.logs.amended.books.add(convertor.toEntity(actualBook, new IncludeFieldNames("id")));
        ensureDataBookListExists();
        response.data.books.add(convertor.toEntity(actualBook));
    }



    public void ensureDataListExists() {
        if(response.data==null){
            response.data = new EntityLists();
        }
    }

    private void ensureDataBookListExists() {
        ensureDataListExists();
        if(response.data.books==null){
            response.data.books = new ArrayList<>();
        }
    }

    private void ensureDataAuthorListExists() {
        ensureDataListExists();
        if(response.data.authors==null){
            response.data.authors = new ArrayList<>();
        }
    }

    private void ensureDataSeriesListExists() {
        ensureDataListExists();
        if(response.data.series==null){
            response.data.series = new ArrayList<>();
        }
    }

    private void ensureDataPublisherListExists() {
        ensureDataListExists();
        if(response.data.publishers==null){
            response.data.publishers = new ArrayList<>();
        }
    }




    private void ensureCreatedListExists() {
        ensureLogsExists();
        if(response.logs.created==null){
            response.logs.created = new EntityLists();
        }
    }

    private void ensureCreatedBookListExists() {
        ensureCreatedListExists();
        if(response.logs.created.books==null){
            response.logs.created.books = new ArrayList<>();
        }
    }

    private void ensureLogsExists() {
        if(response.logs==null){
            response.logs = new EntityListsLog();
        }
    }

    private void ensureCreatedAuthorListExists() {
        ensureCreatedListExists();
        if(response.logs.created.authors==null){
            response.logs.created.authors = new ArrayList<>();
        }
    }

    private void ensureCreatedSeriesListExists() {
        ensureCreatedListExists();
        if(response.logs.created.series==null){
            response.logs.created.series = new ArrayList<>();
        }
    }

    private void ensureCreatedPublisherListExists() {
        ensureCreatedListExists();
        if(response.logs.created.publishers==null){
            response.logs.created.publishers = new ArrayList<>();
        }
    }



    private void ensureAmendedListExists() {
        ensureLogsExists();
        if(response.logs.amended==null){
            response.logs.amended = new EntityLists();
        }
    }

    private void ensureAmendedBookListExists() {
        ensureAmendedListExists();
        if(response.logs.amended.books==null){
            response.logs.amended.books = new ArrayList<>();
        }
    }

    private void ensureAmendedAuthorListExists() {
        ensureAmendedListExists();
        if(response.logs.amended.authors==null){
            response.logs.amended.authors = new ArrayList<>();
        }
    }

    private void ensureAmendedSeriesListExists() {
        ensureAmendedListExists();
        if(response.logs.amended.series==null){
            response.logs.amended.series = new ArrayList<>();
        }
    }

    private void ensureAmendedPublisherListExists() {
        ensureAmendedListExists();
        if(response.logs.amended.publishers==null){
            response.logs.amended.publishers = new ArrayList<>();
        }

    }


    public void addError(final ApiErrorMessage errorMessage) {
        if(response.errors==null){
            response.errors = new ErrorList();
        }
        if(response.errors.report==null){
            response.errors.report= new ArrayList<ApiErrorMessage>();
        }
        response.errors.report.add(errorMessage);
    }



}
