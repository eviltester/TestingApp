package com.javafortesters.pulp.html;

import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.html.gui.snippets.AppPageBuilder;
import com.javafortesters.pulp.html.gui.snippets.PageSnippets;
import com.javafortesters.pulp.html.templates.MyTemplate;
import com.javafortesters.pulp.html.templates.PaginatorRender;
import com.javafortesters.pulp.reader.VersionedResourceReader;
import com.javafortesters.pulp.reporting.PulpReporter;
import com.javafortesters.pulp.reporting.ReportConfig;
import com.javafortesters.pulp.reporting.filtering.BookFilter;
import com.javafortesters.pulp.reporting.reporters.BookReporter;
import com.javafortesters.pulp.spark.app.versioning.AppVersion;
import com.javafortesters.pulp.spark.app.versioning.AppVersionSettings;


import java.util.Collection;
import java.util.List;

public class HtmlReports {
    private final PulpReporter reporter;
    private final PulpData data;
    private final AppVersion appversion;
    private final String apikey;
    private ReportConfig reportConfig;

    public HtmlReports(PulpData books, AppVersion appversion, String apikey) {
        this.data = books;
        this.appversion = appversion;
        this.apikey = apikey;
        reporter = new PulpReporter(books, appversion);

        reportConfig = ReportConfig.justStrings(appversion);
        reporter.configure(reportConfig);

    }

    public BookReporter bookReporter(){
        return new BookReporter(reportConfig, data.authors(), data.publishers(), data.series() );
    }

    public ReportConfig getReportConfig(){
        return new ReportConfig(this.reportConfig);
    }
    public String getBooksAsHtmlList(BookFilter filter) {
        BookReporter bReporter = bookReporter();
        return  reportCollectionAsLiPage(bReporter.getBooksAsLines(this.data.books().filteredBy(filter)), "Books", "books");
    }

    public String getBooksAsHtmlTable(BookFilter filter) {
        BookReporter bReporter = bookReporter();
        return reportCollectionAsTablePage(bReporter.getBooksAsTable(this.data.books().filteredBy(filter)), "Books", "books");
    }


    public String getBooksAsHtmlList() {
        return getBooksAsHtmlList(new BookFilter());
    }

    // TODO: this is all getting very hacky - refactor this into the reporter config and allow local adjustments from urls
    public String getAuthorsAsHtmlList() {
        return reportCollectionAsLiPage(reporter.getAuthorsAsStrings(), "Authors", "authors");
    }

    public String getPublishersAsHtmlList() {
        return reportCollectionAsLiPage(reporter.getPublishersAsStrings(), "Publishers", "publishers");
    }

    public String getYearsAsHtmlList() {
        return reportCollectionAsLiPage(reporter.getYearsAsStrings(), "Years", "years");
    }

    public String getSeriesNamesAsHtmlList() {
        return reportCollectionAsLiPage(reporter.getSeriesNamesAsStrings(), "Series", "series");
    }



    private String reportCollectionAsLiPage(Collection<String> simpleReport, String listOfWhat, String urlArg){

        AppPageBuilder page = new AppPageBuilder(String.format(String.format("List of %s", listOfWhat)), appversion);

        page.appendToBody(String.format("<h1>List of %s</h1>%n", listOfWhat));

        String ulclass = "";
        if(reportConfig.includeFaqLinks() || reportConfig.areTitlesLinks() || reportConfig.areAuthorNamesLinks() || reportConfig.arePublishersLinks() || reportConfig.areSeriesNamesLinks() || reportConfig.areYearsLinks()){
            ulclass="menu";
        }
        page.appendToBody(new HTMLReporter(appversion).getAsUl(simpleReport, listOfWhat, ulclass));

        String style = reportConfig.areYearsLinks() ? "navigation" : "static";

        if(appversion.are(AppVersionSettings.LISTS_SHOWING_CORRECT_NUMBER_OF_THINGS)){
            page.appendToBody(new PaginatorRender(SimplePaginator.getPaginationDetails(simpleReport, listOfWhat)).renderAsClickable(String.format("%s%s/list/%s", reportConfig.getReportPath(), urlArg, style)));
        }else {
            // initial hacky code with unfinished pagination
            page.appendToBody(new PaginatorRender(this.data.books().getPaginationDetails()).renderAsClickable(String.format("%s%s/list/%s", reportConfig.getReportPath(), urlArg, style)));
        }

        return page.toString();

    }

    private String reportCollectionAsTablePage(String thingsAsTable, String things, String urlArg) {

        AppPageBuilder page = new AppPageBuilder(String.format("Table of %s", things), appversion);

        page.appendToBody(String.format("<h1>Table of %s</h1>%n", things));

        page.appendToBody(thingsAsTable);

        String style = reportConfig.areYearsLinks() ? "navigation" : "static";

        page.appendToBody(new PaginatorRender(this.data.books().getPaginationDetails()).renderAsClickable(String.format("%s%s/table/%s", reportConfig.getReportPath(), urlArg, style)));

        return page.toString();
    }


    public String getIndexPage(){

        return getSnippetPage("Pulp App Main Menu",
                "menu-screen-books-reports-list.html",
                            "menu-screen-authors-reports-list.html",
                            "menu-screen-series-publisher-etc-list.html",
                            "menu-screen-search.html",
                            "menu-screen-create.html"
                );
    }

    public String getSnippetPage(String title, String ... snippets){

        AppPageBuilder page = new AppPageBuilder(title, appversion);

        page.appendToBody(String.format("<h1>%s</h1>%n", title));


        for(String snippet : snippets) {
            try {
                page.appendToBody(new PageSnippets(appversion).getSnippet(snippet));
            }catch(Exception e){
                System.out.println("SNIPPET: " + snippet);
                System.out.println(e.getMessage());
            }
        }

        MyTemplate template = new MyTemplate(page.toString());
        template.replace("<!-- API-SECRET-KEY -->", apikey);
        template.replace("<!-- VERSIONSELECTLIST -->", getVersionSelect());

        return template.toString();
    }


    public HtmlReports configure(ReportConfig reportConfig) {
        this.reportConfig = reportConfig;
        this.reporter.configure(this.reportConfig);
        return this;
    }


    public HtmlReports configurePostFixPath(String linksPostFix) {
        this.reportConfig.setPostFixPath(linksPostFix);
        this.reporter.configure(this.reportConfig);
        return this;
    }

    public String getVersionHistory(){

        StringBuilder versionHistory = new StringBuilder();
        for(int version = AppVersion.MAX_VERSION; version>0; version--){
            String versionHelp = new VersionedResourceReader(appversion).asString(String.format("/content/help-version-%03d.html", version));
            versionHistory.append(versionHelp);
            versionHistory.append("\n");
        }

        return versionHistory.toString();
    }

    public String getVersionSelect(){
        StringBuilder versionselect = new StringBuilder();
        versionselect.append("\n<p><strong>Select Version</strong></p>\n<ul>\n");
        for(int version=1; version <= AppVersion.MAX_VERSION; version++){
            versionselect.append(String.format("<li id=\"help-list-set-version-%d\"><a href=\"/apps/pulp/gui/admin/version/%d\">%s</a></li>\n", version, version, AppVersion.asPathVersion(version)));
        }
        versionselect.append("</ul>\n");
        return versionselect.toString();
    }

    public String getHelpPage() {

        AppPageBuilder page = new AppPageBuilder("Help", appversion);


        String pageTemplate = new VersionedResourceReader(appversion).asString("/content/help.html");
        //String pageToRender = new ResourceReader().asString("/web/apps/pulp/" + appversion + "/content/help.html");

        MyTemplate template = new MyTemplate(pageTemplate);

        List<String> capabilities = appversion.getCapabilities(true);
        StringBuilder ul = new StringBuilder();

        ul.append("<h3>Current Version Capabilities</h3>\n");
        ul.append("<ul>\n");
        for(String summary : capabilities){
            ul.append(String.format("<li>%s</li>%n", summary));
        }
        ul.append("</ul>\n");

        template.replace("<!-- CAPABILITYLIST -->", ul.toString());




        template.replace("<!-- VERSIONHISTORY -->", getVersionHistory());
        template.replace("<!-- VERSIONSELECTLIST -->", getVersionSelect());


        page.appendToBody(template.toString());
        return page.toString();


    }
}
