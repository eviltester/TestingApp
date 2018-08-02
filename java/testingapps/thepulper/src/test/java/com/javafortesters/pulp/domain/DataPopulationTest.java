package com.javafortesters.pulp.domain;

import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.domain.objects.PulpBook;
import com.javafortesters.pulp.reader.PulpDataPopulator;
import com.javafortesters.pulp.reader.PulpSeriesCSVReader;
import com.javafortesters.pulp.reader.forseries.SavageReader;
import com.javafortesters.pulp.reader.forseries.SpiderReader;
import org.junit.Assert;
import org.junit.Test;

public class DataPopulationTest {


    @Test
    public void canHaveAStaticDataPopulator(){

        PulpData books = new PulpData();
        PulpDataPopulator populator = new PulpDataPopulator(books);
        SavageReader reader = new SavageReader("/data/pulp/doc_savage_test.csv");
        populator.populateFrom(reader);

        Assert.assertEquals(3, books.authors().count());

        // not convinced this will always be true - ordering may be different on some machines
        Assert.assertEquals("Lester Dent", books.authors().get("1").getName());

        Assert.assertEquals("Lester Dent", books.authors().findByName("Lester Dent").getName());


        Assert.assertEquals(1, books.publishers().count());
        Assert.assertEquals("Street And Smith", books.publishers().get("1").getName());

        Assert.assertEquals(1, books.series().count());
        Assert.assertEquals("Doc Savage", books.series().get("1").getName());

        Assert.assertEquals(5, books.books().count());

        Assert.assertEquals("Kenneth Robeson", books.authors().get(books.books().get("1").getHouseAuthorIndex()).getName());
        Assert.assertEquals("Lester Dent", books.authors().get(books.books().get("1").getAuthorIndexes().get(0)).getName());
        Assert.assertEquals("Will Murray", books.authors().get(books.books().get("1").getAuthorIndexes().get(1)).getName());

        String bookID = books.books().keys().get(0);
        PulpBook book = books.books().get(bookID);
        Assert.assertEquals("Doc Savage", books.series().get(book.getSeriesIndex()).getName());

        Assert.assertEquals("The Angry Canary", books.books().get("1").getTitle());
        Assert.assertEquals("The Swooning Lady", books.books().get("2").getTitle());


    }




    @Test
    public void canHaveAStaticDataPopulatorFromTheSpider(){

        PulpData books = new PulpData();
        PulpDataPopulator populator = new PulpDataPopulator(books);
        PulpSeriesCSVReader reader = new SpiderReader("/data/pulp/the_spider_test.csv");
        populator.populateFrom(reader);

        Assert.assertEquals(4, books.authors().count());

        // not convinced this will always be true - ordering may be different on some machines
        Assert.assertEquals("Grant Stockbridge", books.authors().get("1").getName());

        Assert.assertEquals("Norvell Page", books.authors().findByName("Norvell Page").getName());


        Assert.assertEquals(1, books.publishers().count());
        Assert.assertEquals("Popular Publications", books.publishers().get("1").getName());

        Assert.assertEquals(1, books.series().count());
        Assert.assertEquals("The Spider", books.series().get("1").getName());

        Assert.assertEquals(6, books.books().count());

        Assert.assertEquals("R.T.M. Scott", books.authors().get(books.books().get("1").getHouseAuthorIndex()).getName());
        Assert.assertEquals("R.T.M. Scott", books.authors().get(books.books().get("1").getAuthorIndexes().get(0)).getName());

        String bookID = books.books().keys().get(0);
        PulpBook book = books.books().get(bookID);
        Assert.assertEquals("The Spider", books.series().get(book.getSeriesIndex()).getName());

        Assert.assertEquals("The Spider Strikes", books.books().get("1").getTitle());
        Assert.assertEquals("The Wheel of Death", books.books().get("2").getTitle());


    }}
