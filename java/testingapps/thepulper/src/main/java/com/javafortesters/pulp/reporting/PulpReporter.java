package com.javafortesters.pulp.reporting;

import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.reporting.reporters.*;
import com.javafortesters.pulp.spark.app.versioning.AppVersion;

import java.util.*;

public class PulpReporter {
    private final PulpData data;
    BookReporter reporter;
    private ReportConfig reportConfig;

    public PulpReporter(PulpData books, AppVersion appversion) {
        this.data = books;
        configure(ReportConfig.justStrings(appversion));
    }

    public PulpData data() {
        return data;
    }


    public Collection<String> getBooksAsStrings() {
        return reporter.getBooksAsLines(data.books().getAll());
    }

    public Collection<String> getAuthorsAsStrings() {
        AuthorReporter aReporter = new AuthorReporter(reportConfig);
        return aReporter.getAsStrings(data.authors().getAllOrderedByName());
    }

    public Collection<String> getPublishersAsStrings() {

        PublisherReporter publisher_reporter = new PublisherReporter(reportConfig);
        return publisher_reporter.getAsStrings(data.publishers().getAllOrderedByName());
    }


    public Collection<String> getYearsAsStrings() {

        YearReporter yreporter = new YearReporter(reportConfig);
        return yreporter.getAsLines(data.books().getYears());

    }

    public Collection<String> getSeriesNamesAsStrings() {

        SeriesReporter series = new SeriesReporter(reportConfig);
        return series.getAsStrings(data.series().getAllOrderedByName());
    }


    public void configure(ReportConfig reportConfig) {
        this.reportConfig = reportConfig;
        reporter = new BookReporter(reportConfig, data.authors(), data.publishers(), data.series());
    }



}
