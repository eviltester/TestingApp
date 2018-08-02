package com.javafortesters.pulp.domain;


import com.javafortesters.pulp.domain.objects.*;
import org.junit.Assert;
import org.junit.Test;

public class BasicDomainObjectsTest {


    @Test
    public void canCreateASeries(){
        PulpSeries docSavage = new PulpSeries("Doc Savage");
        Assert.assertEquals("Doc Savage", docSavage.getName());
        Assert.assertEquals("unknown", docSavage.getId());

        PulpSeries spider = new PulpSeries("1", "The Spider");
        Assert.assertEquals("The Spider", spider.getName());
        Assert.assertEquals("1", spider.getId());
    }


    @Test
    public void canCreateABook(){

        // indexes do not have spaces
        PulpBook savageOne = new PulpBook("1","Doc Savage", "Lester Dent", "Kenneth Robeson","The Man of Bronze", "March, 1933", 1933, "Street And Smith");
        Assert.assertEquals("1", savageOne.getId());
        Assert.assertEquals("Doc Savage", savageOne.getSeriesIndex());
        Assert.assertEquals("Lester Dent", savageOne.getAuthorIndexesAsString());
        Assert.assertEquals("Kenneth Robeson", savageOne.getHouseAuthorIndex());
        Assert.assertEquals("The Man of Bronze", savageOne.getTitle());
        Assert.assertEquals("March, 1933", savageOne.getSeriesId());
        Assert.assertEquals(1933, savageOne.getPublicationYear());
        Assert.assertEquals("Street And Smith", savageOne.getPublisherIndex());
    }

    @Test
    public void canCreateAnAuthor(){
        PulpAuthor lesterDent = new PulpAuthor("Lester Dent");
        Assert.assertEquals("Lester Dent", lesterDent.getName());
        Assert.assertEquals("unknown", lesterDent.getId());

        PulpAuthor callMeKenneth = new PulpAuthor("Kenneth", "Kenneth Robeson");
        Assert.assertEquals("Kenneth Robeson", callMeKenneth.getName());
        Assert.assertEquals("Kenneth", callMeKenneth.getId());
    }

    @Test
    public void canCreateAPublisher(){
        PulpPublisher sas = new PulpPublisher("Street And Smith");
        Assert.assertEquals("Street And Smith", sas.getName());
        Assert.assertEquals("unknown", sas.getId());

        sas = new PulpPublisher("SAS", "Street and Smith");
        Assert.assertEquals("Street and Smith", sas.getName());
        Assert.assertEquals("SAS", sas.getId());
    }







    // I could use an in memory database but I'm much more likely to make a mistake if I don't, and this is a test app so mistakes are OK




}
