package com.javafortesters.pulp.api.entities.lists;

import com.javafortesters.pulp.api.entities.single.AuthorEntity;
import com.javafortesters.pulp.api.entities.single.BookEntity;
import com.javafortesters.pulp.domain.groupings.PulpBooks;
import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpBook;

import java.util.ArrayList;
import java.util.List;

public class BooksListEntity {

    public List<BookEntity> books;

    public BooksListEntity(final PulpData allData) {
        this.books = new ArrayList();

        for(PulpBook book : allData.books().getAll()){
            books.add(new BookEntity(   book.getId(),
                                        book.getTitle(),
                                        book.getPublicationYear(),
                                        book.getSeriesId(),
                                        allData.series().get(book.getSeriesIndex()),
                                        allData.authors().getAll(book.getAllAuthorIndexes()),
                                        allData.publishers().get(book.getPublisherIndex())
                    ));
        }
    }
}
