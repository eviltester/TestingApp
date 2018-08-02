package com.javafortesters.pulp.reporting;


public class ReportConfig {
    private boolean areAuthorNamesLinks;
    private boolean areYearsLinks;
    private boolean arePublisherNamesLinks;
    private boolean areSeriesNamesLinks;
    private String reportPath = "/apps/pulp/gui/reports/";
    private String linksPostFix;
    private boolean includeFaqLinks=false; // by default do not do this
    private boolean areTitlesLinks=false; // by default no

    private boolean authorAmendLink = true;
    private boolean publisherAmendLink=true;
    private boolean bookAmendLink=false;
    private boolean seriesAmendLink=true;


    // TODO need to set this more granually to configure Name display, title display, series display more idividually e.g. isLink, hasAmend, hasDelete etc.
    public ReportConfig(boolean areAuthorNamesLinks, boolean areYearsLinks, boolean arePublisherNamesLinks, boolean areSeriesNamesLinks, boolean areTitlesLinks) {
        this.areAuthorNamesLinks = areAuthorNamesLinks;
        this.areYearsLinks = areYearsLinks;
        this.arePublisherNamesLinks = arePublisherNamesLinks;
        this.areSeriesNamesLinks = areSeriesNamesLinks;
        this.areTitlesLinks = areTitlesLinks;
    }

    public ReportConfig(ReportConfig reportConfig) {
        this.areAuthorNamesLinks = reportConfig.areAuthorNamesLinks();
        this. areYearsLinks = reportConfig.areYearsLinks();
        this. arePublisherNamesLinks = reportConfig.arePublishersLinks();
        this.areSeriesNamesLinks = reportConfig.areSeriesNamesLinks();
        this.reportPath = reportConfig.getReportPath();
        this.linksPostFix = reportConfig.getLinksPostFix();
        this.areTitlesLinks = reportConfig.areTitlesLinks();
        this.authorAmendLink = reportConfig.areAuthorAmendLinksShown();
        this.publisherAmendLink = reportConfig.arePublisherAmendLinksShown();
        this.bookAmendLink = reportConfig.areBookAmendLinksShown();
        this.seriesAmendLink = reportConfig.areSeriesAmendLinksShown();
    }

    private String getLinksPostFix() {
        return linksPostFix;
    }

    public static ReportConfig justStrings() {

        return new ReportConfig(false, false, false, false, false);
    }


    public static ReportConfig allHTML(String reportPath) {
        ReportConfig config = new ReportConfig(true, true, true, true, true);
        config.reportPath = reportPath;
        return config;
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

}
