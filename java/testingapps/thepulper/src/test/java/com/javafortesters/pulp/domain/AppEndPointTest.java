package com.javafortesters.pulp.domain;

import com.javafortesters.pulp.PulpApp;
import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpBook;
import com.javafortesters.pulp.domain.objects.PulpPublisher;
import com.javafortesters.pulp.domain.objects.PulpSeries;
import com.javafortesters.pulp.reader.PulpDataPopulator;
import com.javafortesters.pulp.reader.forseries.SavageReader;
import com.javafortesters.pulp.reporting.filtering.BookFilter;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class AppEndPointTest {

    @Test
    public void canGetBooksByAuthor(){

        PulpData books = new PulpData();
        PulpDataPopulator populator = new PulpDataPopulator(books);
        SavageReader reader = new SavageReader("/data/pulp/doc_savage_test.csv");
        populator.populateFrom(reader);

        PulpAuthor will = books.authors().findByName("Will Murray");

        List<PulpBook> authored = books.books().findByAuthorId(will.getId());

        Assert.assertEquals(1, authored.size());

        PulpAuthor lester = books.authors().findByName("Lester Dent");
        authored = books.books().findByAuthorId(lester.getId());
        Assert.assertEquals(5, authored.size());
    }

    @Test
    public void haveBasicAppWrapperForBooksByAuthor(){
        PulpApp app = new PulpApp();
        app.setAppVersion(1);
        app.readData(new SavageReader("/data/pulp/doc_savage_test.csv"));

        PulpAuthor will = app.books().authors().findByName("Will Murray");

        String report = app.reports().getBooksAsHtmlList(new BookFilter().where().author(will.getId()));

        System.out.println(report);
        Assert.assertTrue(report.contains("<li>The Angry Canary"));

        PulpAuthor lester = app.books().authors().findByName("Lester Dent");

        report = app.reports().getBooksAsHtmlList(new BookFilter().where().author(lester.getId()));

        System.out.println(report);
        Assert.assertTrue(report.contains("<li>The Angry Canary"));
        Assert.assertTrue(report.contains("<li>Up From Earth's Center"));
    }


    @Test
    public void haveBasicAppWrapperForBooksByPublisher(){
        PulpApp app = new PulpApp();
        app.setAppVersion(1);
        app.readData(new SavageReader("/data/pulp/doc_savage_test.csv"));

        PulpPublisher pub = app.books().publishers().findByName("Street And Smith");

        String report = app.reports().getBooksAsHtmlList(new BookFilter().where().publishedBy(pub.getId()));

        System.out.println(report);
        Assert.assertTrue(report.contains("<li>The Angry Canary"));

    }

    @Test
    public void haveBasicAppWrapperForBooksBySeries(){
        PulpApp app = new PulpApp();
        app.setAppVersion(1);
        app.readData( new SavageReader("/data/pulp/doc_savage_test.csv"));

        PulpSeries series = app.books().series().findByName("Doc Savage");

        String report = app.reports().getBooksAsHtmlList(new BookFilter().where().partOfSeries(series.getId()));

        System.out.println(report);
        Assert.assertTrue(report.contains("<li>The Angry Canary"));

    }
}
