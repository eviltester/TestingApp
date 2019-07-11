package com.javafortesters.pulp.api.entities.payloads;

import com.javafortesters.pulp.api.DomainToEntityConvertor;
import com.javafortesters.pulp.api.entities.payloads.responses.ApiErrorMessage;
import com.javafortesters.pulp.api.entities.payloads.responses.ApiResponse;
import com.javafortesters.pulp.api.entities.payloads.responses.EntityLists;
import com.javafortesters.pulp.api.entities.payloads.responses.ErrorList;
import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpBook;
import com.javafortesters.pulp.domain.objects.PulpPublisher;
import com.javafortesters.pulp.domain.objects.PulpSeries;

import java.util.ArrayList;

public class ApiResponseBuilder {

    private final DomainToEntityConvertor convertor;
    private ApiResponse response;

    public ApiResponseBuilder(final PulpData bookdata){
        convertor = new DomainToEntityConvertor(bookdata);
        response = new ApiResponse();
    }

    public ApiResponse getApiResponse() {
        return response;
    }

    // TODO: the amended and created lists could just include the id of the entity since the values are in data

    public void addCreated(final PulpBook actualBook) {
        ensureCreatedBookListExists();
        response.created.books.add(convertor.toEntity(actualBook));
        ensureDataBookListExists();
        response.data.books.add(convertor.toEntity(actualBook));
    }

    public void addCreated(final PulpAuthor actualAuthor) {
        ensureCreatedAuthorListExists();
        response.created.authors.add(convertor.toEntity(actualAuthor));
        ensureDataAuthorListExists();
        response.data.authors.add(convertor.toEntity(actualAuthor));
    }


    public void addCreated(final PulpSeries actualSeries) {
        ensureCreatedSeriesListExists();
        response.created.series.add(convertor.toEntity(actualSeries));
        ensureDataSeriesListExists();
        response.data.series.add(convertor.toEntity(actualSeries));
    }

    public void addCreated(final PulpPublisher actualPublisher) {
        ensureCreatedPublisherListExists();
        response.created.publishers.add(convertor.toEntity(actualPublisher));
        ensureDataPublisherListExists();
        response.data.publishers.add(convertor.toEntity(actualPublisher));
    }

    public void addAmended(final PulpAuthor actualAuthor) {
        ensureAmendedAuthorListExists();
        response.amended.authors.add(convertor.toEntity(actualAuthor));
        ensureDataAuthorListExists();
        response.data.authors.add(convertor.toEntity(actualAuthor));
    }

    public void addAmended(final PulpSeries actualSeries) {
        ensureAmendedSeriesListExists();
        response.amended.series.add(convertor.toEntity(actualSeries));
        ensureDataSeriesListExists();
        response.data.series.add(convertor.toEntity(actualSeries));
    }

    public void addAmended(final PulpPublisher actualPublisher) {
        ensureAmendedPublisherListExists();
        response.amended.publishers.add(convertor.toEntity(actualPublisher));
        ensureDataPublisherListExists();
        response.data.publishers.add(convertor.toEntity(actualPublisher));
    }

    public void addAmended(final PulpBook actualBook) {
        ensureAmendedBookListExists();
        response.amended.books.add(convertor.toEntity(actualBook));
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
        if(response.created==null){
            response.created = new EntityLists();
        }
    }

    private void ensureCreatedBookListExists() {
        ensureCreatedListExists();
        if(response.created.books==null){
            response.created.books = new ArrayList<>();
        }
    }

    private void ensureCreatedAuthorListExists() {
        ensureCreatedListExists();
        if(response.created.authors==null){
            response.created.authors = new ArrayList<>();
        }
    }

    private void ensureCreatedSeriesListExists() {
        ensureCreatedListExists();
        if(response.created.series==null){
            response.created.series = new ArrayList<>();
        }
    }

    private void ensureCreatedPublisherListExists() {
        ensureCreatedListExists();
        if(response.created.publishers==null){
            response.created.publishers = new ArrayList<>();
        }
    }



    private void ensureAmendedListExists() {
        if(response.amended==null){
            response.amended = new EntityLists();
        }
    }

    private void ensureAmendedBookListExists() {
        ensureAmendedListExists();
        if(response.amended.books==null){
            response.amended.books = new ArrayList<>();
        }
    }

    private void ensureAmendedAuthorListExists() {
        ensureAmendedListExists();
        if(response.amended.authors==null){
            response.amended.authors = new ArrayList<>();
        }
    }

    private void ensureAmendedSeriesListExists() {
        ensureAmendedListExists();
        if(response.amended.series==null){
            response.amended.series = new ArrayList<>();
        }
    }

    private void ensureAmendedPublisherListExists() {
        ensureAmendedListExists();
        if(response.amended.publishers==null){
            response.amended.publishers = new ArrayList<>();
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
