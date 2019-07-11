package com.javafortesters.pulp.api.entities.payloads.responses;

import com.javafortesters.pulp.api.entities.single.AuthorEntity;
import com.javafortesters.pulp.api.entities.single.BookEntity;
import com.javafortesters.pulp.api.entities.single.PublisherEntity;
import com.javafortesters.pulp.api.entities.single.SeriesEntity;

import java.util.ArrayList;
import java.util.List;

public class EntityLists {

    public List<BookEntity> books;
    public List<AuthorEntity> authors;
    public List<PublisherEntity> publishers;
    public List<SeriesEntity> series;

}
