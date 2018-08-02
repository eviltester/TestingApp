package com.javafortesters.pulp.domain.groupings;

import com.javafortesters.pulp.domain.objects.PulpBook;

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


    public void deletePublisher(final String id) {
        // delete the publisher from publishers collection
        thePublishers.delete(id);

        // delete any books published by the publisher
        theBooks.deletePublishedBy(id);
    }

    public void deleteAuthor(final String id) {
        theAuthors.delete(id);
        // delete any books authored by the author if they are the only author
        theBooks.deleteAuthoredBy(id);
    }

    public void deleteSeries(final String id) {
        theSerieses.delete(id);
        theBooks.deleteAnyInSeries(id);
    }

    public void deleteBook(final String id) {
        theBooks.delete(id);
    }
}
