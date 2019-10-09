package com.javafortesters.pulp.html.gui;

import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.html.HTMLReporter;
import com.javafortesters.pulp.html.gui.snippets.AppPageBuilder;
import com.javafortesters.pulp.html.templates.MyTemplate;
import com.javafortesters.pulp.html.templates.PaginatorRender;
import com.javafortesters.pulp.reader.VersionedResourceReader;
import com.javafortesters.pulp.reporting.ReportConfig;
import com.javafortesters.pulp.reporting.filtering.BookFilter;
import com.javafortesters.pulp.reporting.reporters.BookReporter;
import com.javafortesters.pulp.spark.app.versioning.AppVersion;


import java.util.ArrayList;
import java.util.Collection;

public class FilterTestPage {

    private final boolean showAsList;
    private final boolean fieldsAreNavigable;
    private final boolean isSearch;
    private final boolean isPaginated;
    private final AppVersion appversion;
    private BookFilter filter;
    private PulpData data;
    private boolean doReport;
    private boolean showTitle;
    private boolean showOutput;
    private boolean showPaging;
    private boolean showFooter;

    /*

    NOTE -
        I haven't templated this for different app versions because this is
        designed for exploratory testing
     */
    public FilterTestPage(boolean isList, boolean navigation, boolean isSearch, boolean isPaginated, AppVersion appversion) {
        filter = new BookFilter();
        this.showAsList = isList;
        this.fieldsAreNavigable = navigation;
        this.isSearch = isSearch;
        this.isPaginated = isPaginated;
        this.appversion= appversion;
    }

    public String asHTMLString() {

        AppPageBuilder page = new AppPageBuilder("Filter Test Page", appversion);

        String pageToRender = new VersionedResourceReader(appversion).asString("/page-template/filter-test-page-body-content.html");
        //String pageToRender = new ResourceReader().asString("/web/apps/pulp/" + appversion.getAppVersion() + "/page-template/filter-test-page-body-content.html");
        MyTemplate template = new MyTemplate(pageToRender);


        // SETUP THE FORM WITH PARAMS Passed in

        template.replaceSection("<!-- PAGE-OPTIONS -->",getPageNumberOptions(filter.getCurrentPage()).toString());
        template.replaceSection("<!-- PAGE-LIMIT-OPTIONS -->",getPageNumberOptions(filter.getNumberPerPage()).toString());


        if(showAsList) {
            template.replace("value='list'", "value='list' checked");
        }else{
            template.replace("value='table'", "value='table' checked");
        }

        if(fieldsAreNavigable){
            template.replace("value='navigation'", "value='navigation' checked");
        }else{
            template.replace("value='static'", "value='static' checked");
        }

        if(isSearch){
            template.replace("value='cansearch'", "value='cansearch' checked");
            if(filter.isByPartialTitleMatch()){
                template.replace("name='searchterm'", "name='searchterm' value='" + filter.getPartialTitleMatchString() + "'");
            }
        }

        if(isPaginated){
            template.replace("value='ispaged'", "value='ispaged' checked");
        }

        // The Form is setup with what it was

        String booksOutput = "";

        Collection<String> books = new ArrayList<>();

        if(data!=null){
            ReportConfig config = ReportConfig.justStrings(appversion);

            String ulmenuclass="";
            if(fieldsAreNavigable){
                config = new ReportConfig(appversion, true, true, true, true, true);
                ulmenuclass="menu";
            }

            BookReporter reporter = new BookReporter(config, data.authors(), data.publishers(), data.series());
            books = reporter.getBooksAsLines(data.books().filteredBy(filter));
            if(showAsList){
                booksOutput = new HTMLReporter(appversion).getAsUl(books, "filtered-bookslist", ulmenuclass);
            }else{
                booksOutput = reporter.getBooksAsTable(data.books().filteredBy(filter));
            }
        }

        if(doReport){
            if(showTitle){
                template.replace("<!-- TITLE GOES HERE -->", "<h2>List of Books</h2>");
                template.replace("<option value='title'>", "<option value='title' selected>");
            }
            if(showOutput) {
                template.replace("<!-- DATA GOES HERE -->", booksOutput);
            }else{
                template.replace("<option value='list' selected>", "<option value='list'>");
            }
            if(showPaging) {
                template.replace("<option value='paging'>", "<option value='paging' selected>");

                PaginatorRender pagingR = new PaginatorRender(data.books().getPaginationDetails());

                String lt = showAsList ? "list" : "table";
                String nav = fieldsAreNavigable ? "navigation" : "static";

                template.replace("<!-- PAGING GOES HERE -->", pagingR.renderAsClickable(String.format("books/%s/%s", lt, nav) ));
                //template.replace("<!-- PAGING GOES HERE -->", pagingR.renderAsHtml());
            }
            if(showFooter){
                template.replace("<option value='footer'>", "<option value='footer' selected>");
            }
        }

        page.appendToBody(template.toString());
        return page.toString();

    }

    private String getPageNumberOptions(int checkedVal) {
        StringBuilder pageNumberOptions = new StringBuilder();

        for(int x=1; x<=20 ; x++){
            if(x==checkedVal){
                pageNumberOptions.append(String.format("<option value='%1$d' selected='selected'>%1$d</option>%n", x));
            }else {
                pageNumberOptions.append(String.format("<option value='%1$d'>%1$d</option>%n", x));
            }
        }

        return pageNumberOptions.toString();
    }

    public FilterTestPage setFilter(BookFilter filter) {
        this.filter = filter;
        if(!isSearch){
            filter.titleContains(null);
        }
        if(!isPaginated){
            filter.numberPerPage(null);
            filter.currentPage(null);
        }
        return this;
    }

    public FilterTestPage setData(PulpData data) {
        this.data=data;
        return this;
    }

    public FilterTestPage setShowData(boolean doReport) {
        this.doReport = doReport;
        return this;
    }

    public FilterTestPage setShowThese(String[] templateElements) {
        for(String element : templateElements){
            if(element.equalsIgnoreCase("Title")) {
                this.showTitle = true;
            }
            if(element.equalsIgnoreCase("List")) {
                this.showOutput = true;
            }
            if(element.equalsIgnoreCase("Paging")) {
                this.showPaging = true;
            }
            if(element.equalsIgnoreCase("Footer")) {
                this.showFooter = true;
            }
        }
        return this;
    }
}
