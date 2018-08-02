package com.javafortesters.pulp.domain;

import com.javafortesters.pulp.domain.groupings.PulpAuthors;
import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpBook;
import com.javafortesters.pulp.domain.objects.PulpPublisher;
import com.javafortesters.pulp.domain.objects.PulpSeries;
import com.javafortesters.pulp.reader.PulpDataPopulator;
import com.javafortesters.pulp.reader.PulpSeriesCSVReader;
import com.javafortesters.pulp.reader.forseries.SavageReader;
import com.javafortesters.pulp.reader.forseries.SpiderReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DataDeletionTest {

    PulpPublisher deletepublisher;
    PulpData books;
    private PulpAuthor deleteauthor;
    private PulpAuthor houseauthor;
    PulpSeries deleteseries;
    private List<PulpBook> deletedBooks;

    @Before
    public void createData(){
        books = new PulpData();

        PulpDataPopulator populator = new PulpDataPopulator(books);
        SavageReader reader = new SavageReader("/data/pulp/deletion_savage_test.csv");
        populator.populateFrom(reader);

        deletepublisher = books.publishers().add("My Publisher To Delete");
        deleteauthor = books.authors().add("My Author To Delete");
        houseauthor = books.authors().add("House Author");
        deleteseries = books.series().add("My Series To Delete");

        deletedBooks = new ArrayList<>();

        deletedBooks.add(books.books().add(deleteseries.getId(), deleteauthor.getId(), houseauthor.getId(), "Title One", deleteseries.getId(), 2018, deletepublisher.getId()));
        deletedBooks.add(books.books().add(deleteseries.getId(), deleteauthor.getId(), houseauthor.getId(), "Title Two", deleteseries.getId(), 2018, deletepublisher.getId()));
        deletedBooks.add(books.books().add(deleteseries.getId(), deleteauthor.getId(), houseauthor.getId(), "Title Three", deleteseries.getId(), 2018, deletepublisher.getId()));



    }

    @Test
    public void canDeleteAPublisher(){

        int currentBooks = books.books().count();
        int currentPublishers = books.publishers().count();
        int currentSeries = books.series().count();
        int currentAuthors = books.authors().count();

        books.deletePublisher(deletepublisher.getId());



        Assert.assertEquals(currentBooks-3, books.books().count());
        Assert.assertEquals(currentPublishers-1, books.publishers().count());
        Assert.assertEquals(currentSeries, books.series().count());
        Assert.assertEquals(currentAuthors, books.authors().count());

        for(PulpBook book : deletedBooks){
            Assert.assertEquals(PulpBook.UNKNOWN_BOOK, books.books().get(book.getId()));
        }

        Assert.assertEquals(PulpPublisher.UNKNOWN_PUBLISHER, books.publishers().get(deletepublisher.getId()));
    }

    @Test
    public void canDeleteAnAuthor(){

        int currentBooks = books.books().count();
        int currentPublishers = books.publishers().count();
        int currentSeries = books.series().count();
        int currentAuthors = books.authors().count();

        books.deleteAuthor(deleteauthor.getId());

        Assert.assertEquals(currentBooks-3, books.books().count());
        Assert.assertEquals(currentPublishers, books.publishers().count());
        Assert.assertEquals(currentSeries, books.series().count());
        Assert.assertEquals(currentAuthors-1, books.authors().count());

        for(PulpBook book : deletedBooks){
            Assert.assertEquals(PulpBook.UNKNOWN_BOOK, books.books().get(book.getId()));
        }

        Assert.assertEquals(PulpAuthor.UNKNOWN_AUTHOR, books.authors().get(deleteauthor.getId()));
    }

    @Test
    public void canDeleteAnHouseAuthor(){

        int currentBooks = books.books().count();
        int currentPublishers = books.publishers().count();
        int currentSeries = books.series().count();
        int currentAuthors = books.authors().count();

        books.deleteAuthor(houseauthor.getId());

        Assert.assertEquals(currentBooks-3, books.books().count());
        Assert.assertEquals(currentPublishers, books.publishers().count());
        Assert.assertEquals(currentSeries, books.series().count());
        Assert.assertEquals(currentAuthors-1, books.authors().count());

        for(PulpBook book : deletedBooks){
            Assert.assertEquals(PulpBook.UNKNOWN_BOOK, books.books().get(book.getId()));
        }

        Assert.assertEquals(PulpAuthor.UNKNOWN_AUTHOR, books.authors().get(houseauthor.getId()));
    }

    @Test
    public void canDeleteASeries(){

        int currentBooks = books.books().count();
        int currentPublishers = books.publishers().count();
        int currentSeries = books.series().count();
        int currentAuthors = books.authors().count();

        books.deleteSeries(deleteseries.getId());

        Assert.assertEquals(currentBooks-3, books.books().count());
        Assert.assertEquals(currentPublishers, books.publishers().count());
        Assert.assertEquals(currentSeries-1, books.series().count());
        Assert.assertEquals(currentAuthors, books.authors().count());

        for(PulpBook book : deletedBooks){
            Assert.assertEquals(PulpBook.UNKNOWN_BOOK, books.books().get(book.getId()));
        }

        Assert.assertEquals(PulpSeries.UNKNOWN_SERIES, books.series().get(deleteseries.getId()));
    }

    @Test
    public void canDeleteABook(){

        int currentBooks = books.books().count();
        int currentPublishers = books.publishers().count();
        int currentSeries = books.series().count();
        int currentAuthors = books.authors().count();

        books.deleteBook(deletedBooks.get(0).getId());

        Assert.assertEquals(currentBooks-1, books.books().count());
        Assert.assertEquals(currentPublishers, books.publishers().count());
        Assert.assertEquals(currentSeries, books.series().count());
        Assert.assertEquals(currentAuthors, books.authors().count());


        Assert.assertEquals(PulpBook.UNKNOWN_BOOK, books.books().get(deletedBooks.get(0).getId()));
        Assert.assertNotEquals(PulpBook.UNKNOWN_BOOK, books.books().get(deletedBooks.get(1).getId()));
        Assert.assertNotEquals(PulpBook.UNKNOWN_BOOK, books.books().get(deletedBooks.get(2).getId()));

    }
}
