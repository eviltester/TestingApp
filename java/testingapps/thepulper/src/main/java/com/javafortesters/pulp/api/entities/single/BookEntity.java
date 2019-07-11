package com.javafortesters.pulp.api.entities.single;

import com.javafortesters.pulp.api.entities.IncludeFieldNames;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpPublisher;
import com.javafortesters.pulp.domain.objects.PulpSeries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BookEntity {
    public String id;
    public String title;
    public Integer publicationYear;
    public String seriesId;
    public List<AuthorEntity> authors;
    public SeriesEntity series;
    public PublisherEntity publisher;

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

    public BookEntity(final String id, final String title, final int publicationYear, final String seriesId,
                      final SeriesEntity pulpSeries, final List<AuthorEntity> allAuthors, final PublisherEntity pulpPublisher) {
        this.id = id;
        this.title = title;
        this.publicationYear = publicationYear;
        this.seriesId = seriesId;

        this.series = pulpSeries;

        this.authors = allAuthors;

        this.publisher = pulpPublisher;
    }

    public void includeOnlyFields(final IncludeFieldNames includeFieldNames) {
        List<String> fieldNames = includeFieldNames.getNames();

        if(!fieldNames.contains("id")){
            this.id=null;
        }

        if(!fieldNames.contains("title")){
            this.title=null;
        }

        if(!fieldNames.contains("publicationYear")){
            this.publicationYear=null;
        }

        if(!fieldNames.contains("seriesId")){
            this.seriesId=null;
        }

        if(!fieldNames.contains("series")){
            this.series=null;
        }

        if(!fieldNames.contains("authors")){
            this.authors=null;
        }

        if(!fieldNames.contains("publisher")){
            this.publisher=null;
        }


    }
}
