package com.javafortesters.pulp.api.entities.lists;

import com.javafortesters.pulp.api.entities.single.AuthorEntity;
import com.javafortesters.pulp.api.entities.single.PublisherEntity;
import com.javafortesters.pulp.domain.groupings.PulpAuthors;
import com.javafortesters.pulp.domain.groupings.PulpPublishers;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpPublisher;

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
}
