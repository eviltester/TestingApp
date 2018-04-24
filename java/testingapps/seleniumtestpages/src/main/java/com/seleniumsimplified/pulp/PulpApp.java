package com.seleniumsimplified.pulp;

import com.seleniumsimplified.pulp.domain.groupings.PulpData;
import com.seleniumsimplified.pulp.html.HtmlReports;
import com.seleniumsimplified.pulp.reader.PulpDataPopulator;
import com.seleniumsimplified.pulp.reader.PulpSeriesCSVReader;
import com.seleniumsimplified.pulp.reporting.ReportConfig;

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
