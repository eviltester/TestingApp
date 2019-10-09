package com.javafortesters.pulp.reporting;


import com.javafortesters.pulp.spark.app.versioning.AppVersion;

public class ReportConfig {
    private AppVersion appversion;
    private boolean areAuthorNamesLinks;
    private boolean areYearsLinks;
    private boolean arePublisherNamesLinks;
    private boolean areSeriesNamesLinks;
    private String reportPath = "/apps/pulp/gui/reports/";
    private String linksPostFix;
    private boolean includeFaqLinks=false; // by default do not do this
    private boolean areTitlesLinks=false; // by default no

    private boolean authorAmendLink = false;
    private boolean publisherAmendLink=false;
    private boolean bookAmendLink=false;
    private boolean seriesAmendLink=false;

    private boolean allowDeleteAuthor = false;
    private boolean allowDeleteBook = false;
    private boolean allowDeleteSeries = false;
    private boolean allowDeletePublisher = false;

    // TODO need to set this more granually to configure Name display, title display, series display more individually e.g. isLink, hasAmend, hasDelete etc.
    public ReportConfig(AppVersion appversion, boolean areAuthorNamesLinks, boolean areYearsLinks, boolean arePublisherNamesLinks, boolean areSeriesNamesLinks, boolean areTitlesLinks) {
        this.appversion = appversion;
        this.areAuthorNamesLinks = areAuthorNamesLinks;
        this.areYearsLinks = areYearsLinks;
        this.arePublisherNamesLinks = arePublisherNamesLinks;
        this.areSeriesNamesLinks = areSeriesNamesLinks;
        this.areTitlesLinks = areTitlesLinks;
    }

    public ReportConfig(ReportConfig reportConfig) {
        this.appversion = reportConfig.getAppVersion();
        this.areAuthorNamesLinks = reportConfig.areAuthorNamesLinks();
        this.areYearsLinks = reportConfig.areYearsLinks();
        this.arePublisherNamesLinks = reportConfig.arePublishersLinks();
        this.areSeriesNamesLinks = reportConfig.areSeriesNamesLinks();
        this.reportPath = reportConfig.getReportPath();
        this.linksPostFix = reportConfig.getLinksPostFix();
        this.areTitlesLinks = reportConfig.areTitlesLinks();
        this.authorAmendLink = reportConfig.areAuthorAmendLinksShown();
        this.publisherAmendLink = reportConfig.arePublisherAmendLinksShown();
        this.bookAmendLink = reportConfig.areBookAmendLinksShown();
        this.seriesAmendLink = reportConfig.areSeriesAmendLinksShown();
        this.includeFaqLinks = reportConfig.includeFaqLinks();
        this.allowDeleteAuthor = reportConfig.allowDeleteAuthor();
    }

    public boolean allowDeleteAuthor() {
        return allowDeleteAuthor;
    }

    public boolean allowDeleteBook() {
        return allowDeleteBook;
    }

    public boolean allowDeletePublisher() {
        return allowDeletePublisher;
    }

    public boolean allowDeleteSeries() {
        return allowDeleteSeries;
    }

    public AppVersion getAppVersion() {
        return appversion;
    }

    private String getLinksPostFix() {
        return linksPostFix;
    }

    public static ReportConfig justStrings(AppVersion appversion) {

        return new ReportConfig(appversion, false, false, false, false, false);
    }

    public void setReportPath(String reportPath){
        this.reportPath = reportPath;
    }


    public boolean areAuthorNamesLinks() {
        return areAuthorNamesLinks;
    }

    public boolean areYearsLinks() {
        return areYearsLinks;
    }

    public String getReportPath() {
        return reportPath;
    }

    public boolean arePublishersLinks() {
        return arePublisherNamesLinks;
    }

    public boolean areSeriesNamesLinks() {
        return areSeriesNamesLinks;
    }

    public ReportConfig setPostFixPath(String linksPostFix) {
        this.linksPostFix = linksPostFix;
        return this;
    }

    public String getReportPath(String path) {
        StringBuilder retPath = new StringBuilder();
        retPath.append(String.format("%s%s",reportPath,path));
        if(linksPostFix!=null){
            retPath.append(linksPostFix);
        }
        return retPath.toString();
    }

    public ReportConfig setIncludeFaqLinks(boolean includeFaqs) {
        this.includeFaqLinks = includeFaqs;
        return this;
    }

    public boolean includeFaqLinks() {
        return includeFaqLinks;
    }

    public boolean areTitlesLinks() {
        return areTitlesLinks;
    }

    public ReportConfig withoutPostLink() {
        return new ReportConfig(this).setPostFixPath("");
    }

    // TODO - this seems like a complete hack we are missing a way to get a base app path from this or somewhere else
    public ReportConfig withoutReportInPath() {
        final ReportConfig config = new ReportConfig(this);
        config.reportPath = config.reportPath.replace("reports/", "");
        return config;
    }

    public ReportConfig setTitlesAreLinks(final boolean theyAre) {
        areTitlesLinks = theyAre;
        return this;
    }

    public ReportConfig showAuthorAmendLink(final boolean showIt) {
        authorAmendLink = showIt;
        return this;
    }

    public ReportConfig showPublisherAmendink(final boolean showIt) {
        publisherAmendLink = showIt;
        return this;
    }


    public ReportConfig showSeriesAmendLink(final boolean showIt) {
        seriesAmendLink = showIt;
        return this;
    }

    public ReportConfig showBookAmendLink(final boolean showIt) {
        bookAmendLink = showIt;
        return this;
    }

    public ReportConfig showAmendLinks(final boolean showIt){
        showAuthorAmendLink(showIt);
        showBookAmendLink(showIt);
        showPublisherAmendink(showIt);
        showSeriesAmendLink(showIt);
        return this;
    }

    public boolean areBookAmendLinksShown() {
        return bookAmendLink;
    }

    public boolean areAuthorAmendLinksShown() {
        return authorAmendLink;
    }

    public boolean areSeriesAmendLinksShown() {
        return seriesAmendLink;
    }

    public boolean arePublisherAmendLinksShown() {
        return publisherAmendLink;
    }

    public void setAuthorNamesLinks(final boolean arelinks) {
        areAuthorNamesLinks = arelinks;
    }
    public void setPublisherNamesLinks(final boolean arelinks) {
        arePublisherNamesLinks = arelinks;
    }
    public void setYearsAsLinks(final boolean arelinks) {
        areYearsLinks = arelinks;
    }
    public void setSeriesNamesLinks(final boolean arelinks) {
        areSeriesNamesLinks = arelinks;
    }

    public void setAllowDeleteBook(final boolean allowDelete) {
        allowDeleteBook = allowDelete;
    }

    public void setAllowDeleteAuthor(final boolean allowDelete) {
        allowDeleteAuthor = allowDelete;
    }

    public void setAllowDeleteSeries(final boolean allowDelete) {
        allowDeleteSeries = allowDelete;
    }

    public void setAllowDeletePublisher(final boolean allowDelete) {
        allowDeletePublisher = allowDelete;
    }

    public void allowDelete(final boolean allowDelete) {
        allowDeleteAuthor = allowDelete;
        allowDeleteBook = allowDelete;
        allowDeletePublisher = allowDelete;
        allowDeleteSeries = allowDelete;
    }
}
