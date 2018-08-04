package com.javafortesters.pulp.html.gui;

import com.javafortesters.pulp.domain.faq.Faqs;
import com.javafortesters.pulp.domain.faq.SearchEngine;
import com.javafortesters.pulp.html.HTMLReporter;
import com.javafortesters.pulp.html.gui.snippets.AppPageBuilder;
import com.javafortesters.pulp.html.gui.snippets.PageSnippets;
import com.javafortesters.pulp.html.templates.MyTemplate;
import com.javafortesters.pulp.html.templates.MyUrlEncoder;
import com.javafortesters.pulp.reader.ResourceReader;


import java.util.ArrayList;
import java.util.List;

public class FaqRenderPage {

    private final boolean showiframe;
    private final String appversion;
    String searchFaqTerm = "Doc Savage";
    SearchEngine searchEngine = SearchEngine.getDefault();
    String faqsFor = "author";

    public FaqRenderPage(String typeOfFaq, String forTerm, boolean showiframe, String appversion) {
        this.searchFaqTerm=forTerm;
        faqsFor=typeOfFaq;
        this.showiframe = showiframe;
        this.appversion = appversion;
    }

    public String asHTMLString() {

        AppPageBuilder page = new AppPageBuilder("Authors with FAQs", appversion);


        String pageToRender = new ResourceReader().asString("/web/apps/pulp/" + appversion + "/page-template/faqs-page-body-content.html");
        String iframeSection = new ResourceReader().asString("/web/apps/pulp/" + appversion + "/page-template/query-iframe.html");

        MyTemplate pageTemplate = new MyTemplate(pageToRender);
        pageTemplate.replace("<!-- TITLE GOES HERE -->", String.format("<h1>List of FAQs for %s: %s</h1>", faqsFor, searchFaqTerm));

        List<String> faqs = asSearchEngineAnchors(Faqs.getFaqsFor(faqsFor, searchFaqTerm));
        pageTemplate.replace("<!-- LIST OF FAQS GO HERE -->", new HTMLReporter().getAsUl(faqs));

        if(showiframe) {
            MyTemplate iframeTemplate = new MyTemplate(iframeSection);
            iframeTemplate.replace("!!THE-SEARCH-TERM!!", searchFaqTerm);
            iframeTemplate.replaceSection("// The Search Engine", String.format("searchEngine: '%s',", searchEngine.getSearchTerm()));
            pageTemplate.replace("<!-- IFRAME DATA GOES HERE -->", iframeTemplate.toString());
        }

        page.appendToBody(pageTemplate.toString());
        return page.toString();

    }

    private List<String> asSearchEngineAnchors(List<String> faqsFor) {
        List<String> faqAnchors = new ArrayList<>();

        MyTemplate template = new MyTemplate("<a target='_blank' href='" + this.searchEngine.getSearchTerm() + "!!urlencoded!!'>!!searchTerm!!</a>");
        for(String faq: faqsFor){

            template.replace("!!urlencoded!!", MyUrlEncoder.encode(faq));
            template.replace("!!searchTerm!!", faq);
            faqAnchors.add(template.toString());
            template.reset();
        }
        return faqAnchors;
    }

    // TODO: replace ul strings with links to Search engine
}
