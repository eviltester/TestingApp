package com.javafortesters.pulp;

import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.html.HtmlReports;
import com.javafortesters.pulp.html.gui.AppPages;
import com.javafortesters.pulp.reader.PulpDataPopulator;
import com.javafortesters.pulp.reader.PulpSeriesCSVReader;
import com.javafortesters.pulp.reporting.ReportConfig;

public class PulpApp {
    private static final int MAX_VERSION = 2;
    private final PulpData books;
    private String appversion;

    HtmlReports reports;

    public PulpApp() {

        books = new PulpData();
        //reports = new HtmlReports(books, this.appversion);
        setAppVersion(2);
    }

    public void setAppVersion(int version){
        if(version > MAX_VERSION){
            return;
        }
        appversion = String.format("v%03d", version);
        reports = new HtmlReports(books, this.appversion);

    }
    public void readData(PulpSeriesCSVReader reader) {
        PulpDataPopulator populator = new PulpDataPopulator(books);
        populator.populateFrom(reader);
    }

    public HtmlReports reports() {
        return reports;
    }

    public HtmlReports reports(ReportConfig config) {
        return new HtmlReports(books, appversion).configure(config);
    }

    public PulpData books() {
        return books;
    }

    public AppPages page() {
        return new AppPages(books, appversion);

    }

    public String getAppVersion() {
        return appversion;
    }

    public HtmlReports stringReports() {
        return reports(ReportConfig.justStrings(appversion));
    }
}
