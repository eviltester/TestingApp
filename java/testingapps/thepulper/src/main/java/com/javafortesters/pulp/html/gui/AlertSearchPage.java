package com.javafortesters.pulp.html.gui;

import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.html.gui.snippets.AppPageBuilder;
import com.javafortesters.pulp.html.templates.FilledHTMLTemplate;
import com.javafortesters.pulp.html.templates.MyTemplate;
import com.javafortesters.pulp.reader.VersionedResourceReader;
import com.javafortesters.pulp.reporting.reporters.BookReporter;
import com.javafortesters.pulp.reporting.ReportConfig;
import com.javafortesters.pulp.reporting.filtering.BookFilter;
import com.javafortesters.pulp.spark.app.versioning.AppVersion;


import java.util.Collection;

public class AlertSearchPage {


    private final AppVersion appversion;
    private String searchWhat="";
    private String searchHow="";
    private String searchTerm="";
    private boolean confirmSearch=true;
    private PulpData data;

    public AlertSearchPage(AppVersion appversion){
        this.appversion = appversion;
    }

    public String asHTMLString(ReportConfig config) {

        AppPageBuilder page = new AppPageBuilder("Search Page", appversion);


        String pageToRender = new VersionedResourceReader(appversion).asString("/page-template/alert-search-page-body-content.html");
        //String pageToRender = new ResourceReader().asString("/web/apps/pulp/" + appversion.getAppVersion() + "/page-template/alert-search-page-body-content.html");

        StringBuilder dataOutput = new StringBuilder();

        MyTemplate template = new MyTemplate(pageToRender);
        template.replace("!!searchterm!!", searchTerm);
        String checked = confirmSearch ? "checked" : "notchecked";
        template.replace("!!checked!!", checked);



        if(data!=null && searchWhat.length()>0 && searchHow.length()>0){

            BookReporter reporter = new BookReporter(config, data.authors(), data.publishers(), data.series());

            BookFilter filter = new BookFilter();

            if(searchWhat.equalsIgnoreCase("title") && searchHow.equalsIgnoreCase("contains")){
                filter.titleContains(searchTerm);
            }


            Collection<String> outputList = reporter.getBooksAsLines(data.books().filteredBy(filter));

            if(outputList.size()==0){


                dataOutput.append(new FilledHTMLTemplate(appversion).
                                    searchResultMessage(
                                            "No Books found Matching Search Criteria"));

            }else {

                dataOutput.append("<ul>\n");
                int line=0;
                for (String output : outputList) {
                    dataOutput.append(new FilledHTMLTemplate(appversion).li(output, "bookline" + line, "booklistitem"));
                    line++;
                }
                dataOutput.append("</ul>");
            }

        }


        template.replace("<!-- OUTPUT GOES HERE -->", dataOutput.toString());

        page.appendToBody(template.toString());
        return page.toString();

    }

    public AlertSearchPage setSearchTerms(String searchWhat, String searchHow, String searchTerm) {
        this.searchWhat = searchWhat;
        this.searchHow = searchHow;
        this.searchTerm = searchTerm;
        return this;
    }

    public AlertSearchPage setConfirmSearch(boolean doWeHaveToConfirmSearch) {
        confirmSearch = doWeHaveToConfirmSearch;
        return this;
    }

    public AlertSearchPage setDataFrom(PulpData data) {
        this.data = data;
        return this;
    }
}
