package com.javafortesters.pulp.api.entities.lists;

import com.javafortesters.pulp.api.entities.single.AuthorEntity;
import com.javafortesters.pulp.api.entities.single.BookEntity;
import com.javafortesters.pulp.api.entities.single.PublisherEntity;
import com.javafortesters.pulp.api.entities.single.SeriesEntity;
import com.javafortesters.pulp.domain.groupings.*;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpBook;
import com.javafortesters.pulp.domain.objects.PulpPublisher;
import com.javafortesters.pulp.domain.objects.PulpSeries;

import java.util.List;

public class ListPopulator {
    public void populate(final List<AuthorEntity> authors, final PulpAuthors dataauthors) {
        for(PulpAuthor author : dataauthors.getAll()){
            authors.add(new AuthorEntity(author.getId(), author.getName()));
        }
    }

    public void populate(final List<PublisherEntity> publishers, final PulpPublishers datapublishers) {
        for(PulpPublisher publisher : datapublishers.getAll()){
            publishers.add(new PublisherEntity(publisher.getId(), publisher.getName()));
        }

    }

    public void populate(final List<SeriesEntity> series, final PulpSeriesCollection dataseries) {
        for(PulpSeries aseries : dataseries.getAll()){
            series.add(new SeriesEntity(aseries.getId(), aseries.getName()));
        }
    }

    public void populate(final List<BookEntity> books, final PulpBooks pulpBooks, final PulpData allData) {
        for(PulpBook book : pulpBooks.getAll()){
            books.add(new BookEntity(   book.getId(),
                    book.getTitle(),
                    book.getPublicationYear(),
                    book.getSeriesId(),
                    allData.series().get(book.getSeriesIndex()),
                    allData.authors().getAll(book.getAllAuthorIndexes()),
                    allData.publishers().get(book.getPublisherIndex())
            ));
        }
    }
}
