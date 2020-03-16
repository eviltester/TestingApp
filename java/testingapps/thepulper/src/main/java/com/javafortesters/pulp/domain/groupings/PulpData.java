package com.javafortesters.pulp.domain.groupings;

import com.javafortesters.pulp.spark.app.versioning.AppVersion;

public class PulpData {
    private final AppVersion appVersion;
    private PulpAuthors theAuthors;
    private PulpPublishers thePublishers;
    private PulpSeriesCollection theSerieses;
    private PulpBooks theBooks;

    public PulpData(AppVersion appVersion){
        theAuthors = new PulpAuthors();
        thePublishers = new PulpPublishers();
        theSerieses = new PulpSeriesCollection();
        theBooks = new PulpBooks(appVersion);
        this.appVersion = appVersion;
    }

    public static PulpData Empty() {
        return new PulpData(new AppVersion(AppVersion.DEFAULT_VERSION));
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


    public boolean deletePublisher(final String id) {
        // delete the publisher from publishers collection
        final boolean deletedPublisher = thePublishers.delete(id);

        // delete any books published by the publisher
        final boolean deletedBooks = theBooks.deletePublishedBy(id);

        return deletedPublisher && deletedBooks;
    }

    public boolean deleteAuthor(final String id) {
        final boolean deletedAuthor = theAuthors.delete(id);
        // delete any books authored by the author if they are the only author
        final boolean deletedBooks = theBooks.deleteAuthoredBy(id);

        return deletedAuthor && deletedBooks;
    }

    public boolean deleteSeries(final String id) {

        final boolean deletedSeries = theSerieses.delete(id);

        final boolean deletedBooks =theBooks.deleteAnyInSeries(id);

        return deletedSeries && deletedBooks;
    }

    public boolean deleteBook(final String id) {
        return theBooks.delete(id);
    }

    public AppVersion getAppVersion() {
        return appVersion;
    }
}
