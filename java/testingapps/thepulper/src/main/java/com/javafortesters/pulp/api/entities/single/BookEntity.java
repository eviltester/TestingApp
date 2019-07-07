package com.javafortesters.pulp.api.entities.single;

import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpPublisher;
import com.javafortesters.pulp.domain.objects.PulpSeries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BookEntity {
    public final String id;
    public final String title;
    public final int publicationYear;
    public final String seriesId;
    public final List<AuthorEntity> authors;
    public final SeriesEntity series;
    public final PublisherEntity publisher;

    public BookEntity(final String id, final String title, final int publicationYear, final String seriesId,
                      final PulpSeries pulpSeries, final Collection<PulpAuthor> allAuthors, final PulpPublisher pulpPublisher) {
        this.id = id;
        this.title = title;
        this.publicationYear = publicationYear;
        this.seriesId = seriesId;

        this.series = new SeriesEntity(pulpSeries.getId(), pulpSeries.getName());

        this.authors = new ArrayList();

        for(PulpAuthor author : allAuthors){
            authors.add(new AuthorEntity(author.getId(), author.getName()));
        }

        this.publisher = new PublisherEntity(pulpPublisher.getId(), pulpPublisher.getName());
    }
}
