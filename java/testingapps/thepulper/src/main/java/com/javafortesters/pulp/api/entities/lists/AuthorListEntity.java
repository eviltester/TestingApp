package com.javafortesters.pulp.api.entities.lists;

import com.javafortesters.pulp.api.entities.single.AuthorEntity;
import com.javafortesters.pulp.domain.groupings.PulpAuthors;
import com.javafortesters.pulp.domain.objects.PulpAuthor;

import java.util.ArrayList;
import java.util.List;

public class AuthorListEntity {

    public final List<AuthorEntity> authors;

    public AuthorListEntity(final PulpAuthors dataauthors) {
        this.authors = new ArrayList();

        new ListPopulator().populate(this.authors, dataauthors);


    }
}
