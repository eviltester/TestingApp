package com.javafortesters.pulp.html.gui;

import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.html.HTMLElements;
import com.javafortesters.pulp.html.gui.snippets.PageSnippets;
import com.javafortesters.pulp.html.templates.MyTemplate;
import com.javafortesters.pulp.reader.ResourceReader;
import com.javafortesters.pulp.reporting.reporters.BookReporter;
import com.javafortesters.pulp.reporting.ReportConfig;
import com.javafortesters.pulp.reporting.filtering.BookFilter;


import java.util.Collection;

public class AlertSearchPage {


    private String searchWhat="";
    private String searchHow="";
    private String searchTerm="";
    private boolean confirmSearch=true;
    private PulpData data;

    public String asHTMLString(ReportConfig config) {

        StringBuilder pageOutput = new StringBuilder();

        pageOutput.append(new PageSnippets().getPageHead("Book Search"));
        pageOutput.append(new PageSnippets().getDropDownMenu());

        String pageToRender = new ResourceReader().asString("/web/apps/pulp/v001/page-template/alert-search-page-body-content.html");

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

        pageOutput.append(template.toString());
        pageOutput.append(new PageSnippets().getPageFooter());

        return pageOutput.toString();

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
