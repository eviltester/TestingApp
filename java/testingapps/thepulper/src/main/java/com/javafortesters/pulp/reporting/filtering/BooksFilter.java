package com.javafortesters.pulp.reporting.filtering;

import com.javafortesters.pulp.domain.objects.PulpBook;

import java.util.ArrayList;
import java.util.List;

public class BooksFilter {
    private final List<PulpBook> books;
    private PaginationDetails pagination;

    public BooksFilter(List<PulpBook> books) {
        this.books = books;
        pagination = new PaginationDetails();
        pagination.setPaginated(false);
        pagination.setTotalItems(books.size());
    }

    public List<PulpBook> filteredBy(BookFilter filter) {
        List<PulpBook> filteredResultSet = new ArrayList<>();
        pagination = new PaginationDetails();
        pagination.setFromFilter(filter);

        int bookCount = 0;

        for(PulpBook book : books){

            if(book.matches(filter)){

                // is it on the page?
                if(filter.isPaging()){
                    // on page if bookCount/perpage==pageNumber-1
                    // bookCount must start at 0 for this to work otherwise first page will be 1 less than it ought to
                    if((bookCount/filter.getNumberPerPage()) == (filter.getCurrentPage()-1)){
                        filteredResultSet.add(book);
                    }

                }else{
                    filteredResultSet.add(book);
                }

                bookCount++;
            }


        }

        pagination.setTotalItems(bookCount);
        return filteredResultSet;
    }

    public PaginationDetails getPaginationDetails(){
        return pagination;
    }
}
