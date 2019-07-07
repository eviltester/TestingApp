package com.javafortesters.pulp.api;

import com.google.gson.Gson;
import com.javafortesters.pulp.api.entities.single.AuthorEntity;
import com.javafortesters.pulp.api.entities.single.SeriesEntity;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
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
}
