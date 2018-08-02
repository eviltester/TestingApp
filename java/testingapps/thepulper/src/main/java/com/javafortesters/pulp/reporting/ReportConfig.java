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
}
