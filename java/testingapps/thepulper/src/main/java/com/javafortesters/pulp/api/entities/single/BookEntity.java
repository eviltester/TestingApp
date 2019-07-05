package com.javafortesters.pulp.api.entities.single;

import com.javafortesters.pulp.api.entities.lists.AuthorListEntity;
import com.javafortesters.pulp.domain.groupings.PulpAuthors;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpPublisher;
import com.javafortesters.pulp.domain.objects.PulpSeries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BookEntity {
    private final String id;
    private final String title;
    private final int publicationYear;
    private final String seriesId;
    public final List<AuthorEntity> authors;

    public BookEntity(final String id, final String title, final int publicationYear, final String seriesId,
                      final PulpSeries pulpSeries, final Collection<PulpAuthor> allAuthors, final PulpPublisher pulpPublisher) {
        this.id = id;
        this.title = title;
        this.publicationYear = publicationYear;
        this.seriesId = seriesId;
        //TODO: this.pulpSeries

        this.authors = new ArrayList();

        for(PulpAuthor author : allAuthors){
            authors.add(new AuthorEntity(author.getId(), author.getName()));
        }

        // TODO this.pulpPublisher
    }
}
