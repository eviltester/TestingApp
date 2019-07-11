package com.javafortesters.pulp.api.entities.lists;

import com.javafortesters.pulp.api.entities.single.BookEntity;
import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.domain.objects.PulpBook;

import java.util.ArrayList;
import java.util.List;

public class BooksListEntity {

    public List<BookEntity> books;

    public BooksListEntity(final PulpData allData) {
        this.books = new ArrayList();

        new ListPopulator().populate(books, allData.books(), allData);

    }
}
