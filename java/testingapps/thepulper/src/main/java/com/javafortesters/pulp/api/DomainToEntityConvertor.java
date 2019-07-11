package com.javafortesters.pulp.api;

import com.google.gson.Gson;
import com.javafortesters.pulp.api.entities.IncludeFieldNames;
import com.javafortesters.pulp.api.entities.single.AuthorEntity;
import com.javafortesters.pulp.api.entities.single.BookEntity;
import com.javafortesters.pulp.api.entities.single.PublisherEntity;
import com.javafortesters.pulp.api.entities.single.SeriesEntity;
import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpBook;
import com.javafortesters.pulp.domain.objects.PulpPublisher;
import com.javafortesters.pulp.domain.objects.PulpSeries;

public class DomainToEntityConvertor {

    private final PulpData bookdata;

    public DomainToEntityConvertor(final PulpData bookdata){
        this.bookdata = bookdata;
    }

    public String toJson(final PulpAuthor author) {
        AuthorEntity entity = toEntity(author);
        return new Gson().toJson(entity);

    }

    public String toJson(final PulpSeries series) {
        SeriesEntity entity = toEntity(series);
        return new Gson().toJson(entity);
    }

    public String toJson(final PulpPublisher publisher) {
        PublisherEntity entity = toEntity(publisher);
        return new Gson().toJson(entity);
    }

    public String toJson(final PulpBook book) {
        BookEntity entity = toEntity(book);
        return new Gson().toJson(entity);
    }

    public BookEntity toEntity(final PulpBook book) {
        return new BookEntity(book.getId(), book.getTitle(), book.getPublicationYear(), book.getSeriesId(),
                bookdata.series().get(book.getSeriesIndex()),
                bookdata.authors().getAll(book.getAuthorIndexes()),
                bookdata.publishers().get(book.getPublisherIndex())
        );
    }

    public AuthorEntity toEntity(final PulpAuthor author) {
        return new AuthorEntity(author.getId(), author.getName());
    }

    public SeriesEntity toEntity(final PulpSeries series) {
        return new SeriesEntity(series.getId(), series.getName());
    }

    public PublisherEntity toEntity(final PulpPublisher publisher) {
        return new PublisherEntity(publisher.getId(), publisher.getName());
    }

    public BookEntity toEntity(final PulpBook book, final IncludeFieldNames includeFieldNames) {
        final BookEntity entity = toEntity(book);
        entity.includeOnlyFields(includeFieldNames);
        return entity;
    }

    public AuthorEntity toEntity(final PulpAuthor author, final IncludeFieldNames includeFieldNames) {
        final AuthorEntity entity = toEntity(author);
        entity.includeOnlyFields(includeFieldNames);
        return entity;
    }

    public SeriesEntity toEntity(final PulpSeries series, final IncludeFieldNames includeFieldNames) {
        final SeriesEntity entity = toEntity(series);
        entity.includeOnlyFields(includeFieldNames);
        return entity;
    }

    public PublisherEntity toEntity(final PulpPublisher publisher, final IncludeFieldNames includeFieldNames) {
        final PublisherEntity entity = toEntity(publisher);
        entity.includeOnlyFields(includeFieldNames);
        return entity;
    }

}
