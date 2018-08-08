package com.javafortesters.pulp;

import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.html.HtmlReports;
import com.javafortesters.pulp.html.gui.AppPages;
import com.javafortesters.pulp.reader.PulpDataPopulator;
import com.javafortesters.pulp.reader.PulpSeriesCSVReader;
import com.javafortesters.pulp.reporting.ReportConfig;
import com.javafortesters.pulp.spark.AppVersion;

public class PulpApp {

    private final PulpData books;
    private AppVersion appVersion;

    HtmlReports reports;

    public PulpApp() {

        books = new PulpData();
        //reports = new HtmlReports(books, this.appversion);
        appVersion = new AppVersion(2);
        reports = new HtmlReports(books, appVersion);
    }


    public void readData(PulpSeriesCSVReader reader) {
        PulpDataPopulator populator = new PulpDataPopulator(books);
        populator.populateFrom(reader);
    }

    public HtmlReports reports() {
        return reports;
    }

    public HtmlReports reports(ReportConfig config) {
        return new HtmlReports(books, appVersion).configure(config);
    }

    public PulpData books() {
        return books;
    }

    public AppPages page() {
        return new AppPages(books, appVersion);

    }

    public String getAppVersion() {
        return appVersion.getAppVersion();
    }

    public HtmlReports stringReports() {
        return reports(ReportConfig.justStrings(appVersion));
    }

    public int getAppVersionInt() {
        return appVersion.getAppVersionInt();
    }

    public void setAppVersion(final int version) {

        appVersion.setAppVersion(version);
        reports = new HtmlReports(books, appVersion);
    }
}
