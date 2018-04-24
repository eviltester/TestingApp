package com.seleniumsimplified.pulp.domain.groupings;

public class PulpData {
    private PulpAuthors theAuthors;
    private PulpPublishers thePublishers;
    private PulpSeriesCollection theSerieses;
    private PulpBooks theBooks;

    public PulpData(){
        theAuthors = new PulpAuthors();
        thePublishers = new PulpPublishers();
        theSerieses = new PulpSeriesCollection();
        theBooks = new PulpBooks();
    }

    public PulpAuthors authors() {
        return theAuthors;
    }

    public PulpPublishers publishers() {
        return thePublishers;
    }

    public PulpSeriesCollection series() {
        return theSerieses;
    }

    public PulpBooks books() {
        return theBooks;
    }
}
