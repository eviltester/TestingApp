package com.javafortesters.pulp.domain.groupings;

import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpBook;
import com.javafortesters.pulp.domain.objects.PulpPublisher;
import com.javafortesters.pulp.reporting.filtering.BookFilter;
import com.javafortesters.pulp.reporting.filtering.BooksFilter;
import com.javafortesters.pulp.reporting.filtering.PaginationDetails;

import java.util.*;

public class PulpBooks {

    private int key;
    private ArrayList<PulpBook> books;
    private BooksFilter booksFilter;

    public PulpBooks(){
        books = new ArrayList<>();
        key = 1;
        booksFilter = new BooksFilter(new ArrayList<>());

    }
    public int count() {
        return books.size();
    }

    public PulpBook add(String series, String author, String houseAuthor, String title, String seriesId, int year, String publisher) {
        PulpBook book = getNextBook(series, author, houseAuthor, title, seriesId, year, publisher);
        books.add(book);
        return book;
    }

    private PulpBook getNextBook(String series, String author, String houseAuthor, String title, String seriesId, int year, String publisher) {
        return new PulpBook(String.valueOf(key++), series, author, houseAuthor, title, seriesId, year, publisher);
    }

    public PulpBook get(String key) {

        if(key==null){
            return PulpBook.UNKNOWN_BOOK;
        }

        for(PulpBook book : books){
            if(book.getId().contentEquals(key)){
                return book;
            }
        }

        return PulpBook.UNKNOWN_BOOK;
    }

    public PulpBook findByName(String title) {

        if(title==null){
            return PulpBook.UNKNOWN_BOOK;
        }

        for(PulpBook book : books){
            if(book.getTitle().equalsIgnoreCase(title)){
                return book;
            }
        }

        return PulpBook.UNKNOWN_BOOK;
    }

    public PulpBook findByTitle(String title) {
        return findByName(title);
    }

    public List<String> keys() {
        List<String> bookKeys = new ArrayList<>();
        for(PulpBook book : books){
            bookKeys.add(book.getId());
        }
        return bookKeys;
    }

    public List<PulpBook> findByAuthorId(String authorId) {

        List<PulpBook> authored = new ArrayList<>();

        if(authorId==null){
            return authored;
        }

        for(PulpBook book : books){
            if(book.getAllAuthorIndexes().contains(authorId)){
                authored.add(book);
            }
        }

        return authored;
    }

    public List<PulpBook> filteredBy(BookFilter filter) {
        booksFilter = new BooksFilter(books);
        return booksFilter.filteredBy(filter);
    }

    public PaginationDetails getPaginationDetails(){
        return booksFilter.getPaginationDetails();
    }

    public List<PulpBook> getAll() {
        return books;
    }

    public Collection<Integer> getYears() {

        Set<Integer> years = new TreeSet<>();

        for(PulpBook book : books) {
            years.add(Integer.valueOf(book.getPublicationYear()));
        }

        return years;
    }

    public boolean delete(final String id) {
        if(id==null){
            return false;
        }

        if(id.isEmpty()){
            return false;
        }

        return books.remove(get(id));
    }

    public boolean deletePublishedBy(final String id) {

        if(id==null){
            return false;
        }

        if(id.isEmpty()){
            return false;
        }

        List<PulpBook> removeThese = new ArrayList<>();
        for(PulpBook aBook : books){
            if(aBook.getPublisherIndex().contentEquals(id)){
                removeThese.add(aBook);
            }
        }

        // TODO: add any error reporting if cannot remove
        // currently this is just a boolean false with no addition information about what error happened

        boolean removed = true;

        for(PulpBook aBook : removeThese){
            removed = removed && books.remove(aBook);
        }

        return removed;
    }

    public boolean deleteAuthoredBy(final String id) {

        if(id==null){
            return false;
        }

        if(id.isEmpty()){
            return false;
        }

        // remove this author from any books authored list
        List<PulpBook> removeThese = new ArrayList<>();
        for(PulpBook aBook : books){
            if(aBook.isAuthoredBy(id)){
                aBook.removeAuthor(id);
                if(aBook.getHouseAuthorIndex()==null) {
                    removeThese.add(aBook);
                }else{
                    if(aBook.getAuthorIndexes().size()==0){
                        removeThese.add(aBook);
                    }
                }
            }
        }

        // TODO: add any error reporting if cannot remove
        // currently this is just a boolean false with no addition information about what error happened

        boolean removed = true;

        // if book has no authors afterwards then delete the book
        for(PulpBook aBook : removeThese){
            removed = removed && books.remove(aBook);
        }

        return removed;
    }

    public boolean deleteAnyInSeries(final String id) {
        if(id==null){
            return false;
        }

        if(id.isEmpty()){
            return false;
        }

        List<PulpBook> removeThese = new ArrayList<>();
        for(PulpBook aBook : books){
            if(aBook.getSeriesIndex().contentEquals(id)){
                removeThese.add(aBook);
            }
        }

        // TODO: add any error reporting if cannot remove
        // currently this is just a boolean false with no addition information about what error happened

        boolean removed = true;

        for(PulpBook aBook : removeThese){
            removed = removed && books.remove(aBook);
        }

        return removed;
    }
}
