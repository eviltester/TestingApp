package com.javafortesters.pulp.api.actions;

import com.javafortesters.pulp.api.PulpEntities;
import com.javafortesters.pulp.api.entities.single.AuthorEntity;
import com.javafortesters.pulp.api.entities.single.BookEntity;
import com.javafortesters.pulp.api.entities.single.PublisherEntity;
import com.javafortesters.pulp.api.entities.single.SeriesEntity;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpBook;
import com.javafortesters.pulp.domain.objects.PulpPublisher;
import com.javafortesters.pulp.domain.objects.PulpSeries;

import java.util.HashMap;
import java.util.Map;

public class ActionToDo {

    public String actionName;
    public int errorStatus;
    public String errorMessage=null;
    public Map<String, String> headers = new HashMap<>();
    public AuthorEntity authorEntityToActOn;
    public SeriesEntity seriesEntityToActOn;
    public PublisherEntity publisherEntityToActOn;
    public BookEntity bookEntityToActOn;
    public PulpAuthor actualAuthor;
    public PulpSeries actualSeries;
    public PulpPublisher actualPublisher;
    public PulpBook actualBook;

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

    public ActionToDo isReplace(final BookEntity book) {
        actionName = "REPLACE";
        bookEntityToActOn = book;
        return this;
    }

    public ActionToDo isPatch(final BookEntity book) {
        actionName = "PATCH";
        bookEntityToActOn = book;
        return this;
    }

    public ActionToDo withAuthor(final AuthorEntity author) {
        authorEntityToActOn=author;
        return this;
    }

    public ActionToDo withPublisher(final PublisherEntity publisher) {
        publisherEntityToActOn=publisher;
        return this;
    }

    public ActionToDo withSeries(final SeriesEntity series) {
        seriesEntityToActOn=series;
        return this;
    }

    public ActionToDo withBook(final BookEntity book) {
        bookEntityToActOn=book;
        return this;

    }
}
