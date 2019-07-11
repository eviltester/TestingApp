package com.javafortesters.pulp.api.entities.lists;

import com.javafortesters.pulp.api.entities.single.PublisherEntity;
import com.javafortesters.pulp.api.entities.single.SeriesEntity;
import com.javafortesters.pulp.domain.groupings.PulpPublishers;
import com.javafortesters.pulp.domain.objects.PulpPublisher;


import java.util.ArrayList;
import java.util.List;

public class PublisherListEntity {
    public final List<PublisherEntity> publishers;

    public PublisherListEntity(final PulpPublishers datapublishers) {
        this.publishers = new ArrayList();

        new ListPopulator().populate(this.publishers, datapublishers);

    }
}
