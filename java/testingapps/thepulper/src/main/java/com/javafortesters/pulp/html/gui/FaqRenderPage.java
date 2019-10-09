package com.javafortesters.pulp.html.gui;

import com.javafortesters.pulp.domain.faq.Faqs;
import com.javafortesters.pulp.domain.faq.SearchEngine;
import com.javafortesters.pulp.html.HTMLReporter;
import com.javafortesters.pulp.html.gui.snippets.AppPageBuilder;
import com.javafortesters.pulp.html.templates.FilledHTMLTemplate;
import com.javafortesters.pulp.html.templates.MyTemplate;
import com.javafortesters.pulp.html.templates.MyUrlEncoder;
import com.javafortesters.pulp.reader.VersionedResourceReader;
import com.javafortesters.pulp.spark.app.versioning.AppVersion;


import java.util.ArrayList;
import java.util.List;

public class FaqRenderPage {

    private final boolean showiframe;
    private final AppVersion appversion;
    String searchFaqTerm = "Doc Savage";
    SearchEngine searchEngine = SearchEngine.getDefault();
    String faqsForType = "author";

    public FaqRenderPage(String typeOfFaq, String forTerm, boolean showiframe, AppVersion appversion) {
        this.searchFaqTerm=forTerm;
        faqsForType=typeOfFaq;
        this.showiframe = showiframe;
        this.appversion = appversion;
    }

    public String asHTMLString() {

        AppPageBuilder page = new AppPageBuilder("Authors with FAQs", appversion);


        final VersionedResourceReader versionedReader = new VersionedResourceReader(appversion);
        String pageToRender = versionedReader.asString("/page-template/faqs-page-body-content.html");
        String iframeSection = versionedReader.asString("/page-template/query-iframe.html");
        //String pageToRender = new ResourceReader().asString("/web/apps/pulp/" + appversion.getAppVersion() + "/page-template/faqs-page-body-content.html");
        //String iframeSection = new ResourceReader().asString("/web/apps/pulp/" + appversion.getAppVersion() + "/page-template/query-iframe.html");

        MyTemplate pageTemplate = new MyTemplate(pageToRender);
        pageTemplate.replace("<!-- TITLE GOES HERE -->", String.format("<h1>List of FAQs for %s: %s</h1>", faqsForType, searchFaqTerm));

        List<String> faqs = asSearchEngineAnchors(Faqs.getFaqsFor(faqsForType, searchFaqTerm));
        pageTemplate.replace("<!-- LIST OF FAQS GO HERE -->", new HTMLReporter(appversion).getAsUl(faqs, faqsForType+"-faq-list", "menu"));

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


        for(String faq: faqsFor){
            ;
            faqAnchors.add(new FilledHTMLTemplate(appversion).
                                namedNewTabLink(
                                        this.searchEngine.getSearchTerm() + MyUrlEncoder.encode(faq),
                                        faq,
                                        faqsForType + "faq"));
        }
        return faqAnchors;
    }

    // TODO: replace ul strings with links to Search engine
}
