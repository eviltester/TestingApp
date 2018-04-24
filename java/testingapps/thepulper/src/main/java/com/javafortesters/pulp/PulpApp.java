package com.javafortesters.pulp;

import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.html.HtmlReports;
import com.javafortesters.pulp.reader.PulpDataPopulator;
import com.javafortesters.pulp.reader.PulpSeriesCSVReader;
import com.javafortesters.pulp.reporting.ReportConfig;

public class PulpApp {
    private final PulpData books;

    HtmlReports reports;

    public PulpApp() {

        books = new PulpData();
        reports = new HtmlReports(books);
    }

    public void readData(PulpSeriesCSVReader reader) {
        PulpDataPopulator populator = new PulpDataPopulator(books);
        populator.populateFrom(reader);
    }

    public HtmlReports reports() {
        return reports;
    }

    public HtmlReports reports(ReportConfig config) {
        return new HtmlReports(books).configure(config);
    }

    public PulpData books() {
        return books;
    }
}
