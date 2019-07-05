package com.javafortesters.pulp.api.entities.lists;

import com.javafortesters.pulp.api.entities.single.AuthorEntity;
import com.javafortesters.pulp.api.entities.single.SeriesEntity;
import com.javafortesters.pulp.domain.groupings.PulpSeriesCollection;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpSeries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SeriesListEntity {

    private final List<SeriesEntity> series;

    public SeriesListEntity(final PulpSeriesCollection dataseries) {

        this.series = new ArrayList();
        for(PulpSeries aseries : dataseries.getAll()){
            series.add(new SeriesEntity(aseries.getId(), aseries.getName()));
        }
    }
}
