package com.javafortesters.pulp;

import com.javafortesters.pulp.api.PulpEntities;
import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.html.HtmlReports;
import com.javafortesters.pulp.html.gui.AppPages;
import com.javafortesters.pulp.reader.PulpDataPopulator;
import com.javafortesters.pulp.reader.PulpSeriesCSVReader;
import com.javafortesters.pulp.reporting.ReportConfig;
import com.javafortesters.pulp.spark.app.versioning.AppVersion;
import com.javafortesters.pulp.spark.app.versioning.KnownBugs;

import java.util.UUID;

public class PulpApp {


    private PulpData books;
    private PulpEntities entities;
    private String apisecret;
    private AppVersion appVersion;


    HtmlReports reports;
    private String apiRootUrl;

    public PulpApp() {

        apisecret = UUID.randomUUID().toString();
        reset();
    }


    public void readData(PulpSeriesCSVReader reader) {
        PulpDataPopulator populator = new PulpDataPopulator(books);
        populator.populateFrom(reader);
    }

    public HtmlReports reports() {
        return reports;
    }

    public HtmlReports reports(ReportConfig config) {
        return new HtmlReports(books, appVersion, apisecret).configure(config);
    }

    public PulpData books() {
        return books;
    }

    public AppPages page() {
        return new AppPages(books, appVersion);

    }

    public AppVersion getAppVersion() {
        return appVersion;
    }

    public HtmlReports stringReports() {
        return reports(ReportConfig.justStrings(appVersion));
    }

    public void setAppVersion(final int version) {

        appVersion.setAppVersion(version);
        reports = new HtmlReports(books, appVersion, apisecret);
    }

    public PulpEntities entities() {
        return entities;
    }

    public String getAPISecret() {
        return apisecret;
    }

    public void setApiSecret(final String xapiauth_sessionid) {
        apisecret = xapiauth_sessionid;
        reports = new HtmlReports(books, appVersion, apisecret);
    }

    public PulpApp setApiRootUrl(final String rootUrl) {
        this.apiRootUrl = rootUrl;
        entities.setRootUrl(rootUrl);
        return this;
    }


    public void reset() {

        // reset the data, keep the sessions and api secret the same
        appVersion = new AppVersion(AppVersion.DEFAULT_VERSION); //default to version
        books = new PulpData(appVersion);
        //reports = new HtmlReports(books, this.appversion);
        reports = new HtmlReports(books, appVersion, apisecret);
        entities = new PulpEntities(books);
    }
}
