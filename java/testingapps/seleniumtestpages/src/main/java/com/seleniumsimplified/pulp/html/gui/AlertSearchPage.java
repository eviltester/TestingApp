package com.seleniumsimplified.pulp.html.gui;

import com.seleniumsimplified.pulp.domain.groupings.PulpData;
import com.seleniumsimplified.pulp.html.HTMLElements;
import com.seleniumsimplified.pulp.html.templates.MyTemplate;
import com.seleniumsimplified.pulp.reporting.reporters.BookReporter;
import com.seleniumsimplified.pulp.reporting.ReportConfig;
import com.seleniumsimplified.pulp.reporting.filtering.BookFilter;
import com.seleniumsimplified.seleniumtestpages.ResourceReader;

import java.util.Collection;

public class AlertSearchPage {


    private String searchWhat="";
    private String searchHow="";
    private String searchTerm="";
    private boolean confirmSearch=true;
    private PulpData data;

    public String asHTMLString() {
        String pageToRender = new ResourceReader().asString("/web/apps/pulp/page-template/alert-search-page-body-content.html");

        MyTemplate template = new MyTemplate(pageToRender);
        template.replace("!!searchterm!!", searchTerm);
        String checked = confirmSearch ? "checked" : "notchecked";
        template.replace("!!checked!!", checked);

        StringBuilder dataOutput = new StringBuilder();

        if(data!=null && searchWhat.length()>0 && searchHow.length()>0){

            BookReporter reporter = new BookReporter(ReportConfig.justStrings(), data.authors(), data.publishers(), data.series());

            BookFilter filter = new BookFilter();

            if(searchWhat.equalsIgnoreCase("title") && searchHow.equalsIgnoreCase("contains")){
                filter.titleContains(searchTerm);
            }

            dataOutput.append("<div id='dataoutput'>");

            Collection<String> outputList = reporter.getBooksAsLines(data.books().filteredBy(filter));

            if(outputList.size()==0){
                dataOutput.append("<p>No Books found Matching Search Criteria</p>");
            }else {
                dataOutput.append(HTMLElements.startUl());
                for (String output : outputList) {
                    dataOutput.append(HTMLElements.getLi(output));
                }
                dataOutput.append(HTMLElements.endUl());
            }

            dataOutput.append("</div>");
        }


        template.replace("<!-- OUTPUT GOES HERE -->", dataOutput.toString());
        template.replace("<!-- FOOTER GOES HERE -->", new ResourceReader().asString("/web/apps/pulp/page-template/reports-list-widget.html"));

        return template.toString();

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
