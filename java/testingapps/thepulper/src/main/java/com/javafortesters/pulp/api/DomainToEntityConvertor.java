package com.javafortesters.pulp.api;

import com.google.gson.Gson;
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

    public String toJson(final PulpAuthor author) {

        AuthorEntity entity = new AuthorEntity(author.getId(), author.getName());
        return new Gson().toJson(entity);

    }

    public String toJson(final PulpSeries series) {

        SeriesEntity entity = new SeriesEntity(series.getId(), series.getName());
        return new Gson().toJson(entity);
    }

    public String toJson(final PulpPublisher publisher) {

        PublisherEntity entity = new PublisherEntity(publisher.getId(), publisher.getName());
        return new Gson().toJson(entity);
    }

    public String toJson(final PulpBook book, final PulpData bookdata) {
        BookEntity entity = new BookEntity(book.getId(), book.getTitle(), book.getPublicationYear(), book.getSeriesId(),
                bookdata.series().get(book.getSeriesIndex()),
                bookdata.authors().getAll(book.getAuthorIndexes()),
                bookdata.publishers().get(book.getPublisherIndex())
        );

        return new Gson().toJson(entity);
    }
}
