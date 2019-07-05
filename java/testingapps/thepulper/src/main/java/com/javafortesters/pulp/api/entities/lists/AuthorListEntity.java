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

        for(PulpAuthor author : dataauthors.getAll()){
            authors.add(new AuthorEntity(author.getId(), author.getName()));
        }
    }
}
