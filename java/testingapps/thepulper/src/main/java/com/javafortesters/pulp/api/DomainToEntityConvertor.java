package com.javafortesters.pulp.api;

import com.google.gson.Gson;
import com.javafortesters.pulp.api.entities.single.AuthorEntity;
import com.javafortesters.pulp.domain.objects.PulpAuthor;

public class DomainToEntityConvertor {

    public String toJson(final PulpAuthor author) {

        AuthorEntity entity = new AuthorEntity(author.getId(), author.getName());
        return new Gson().toJson(entity);

    }
}
