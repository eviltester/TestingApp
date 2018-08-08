package com.javafortesters.pulp.html.gui.entitycrud.createPages;

import com.javafortesters.pulp.html.gui.snippets.AppPageBuilder;
import com.javafortesters.pulp.html.gui.snippets.PageSnippets;
import com.javafortesters.pulp.html.templates.MyTemplate;
import com.javafortesters.pulp.reader.ResourceReader;
import com.javafortesters.pulp.spark.AppVersion;

public class CreateSeriesPage {

    private final AppVersion appversion;
    private String output="";

    public CreateSeriesPage(final AppVersion appversion) {
        this.appversion = appversion;
    }

    public void setOutput(final String output) {
        this.output=output;
    }

    public String asHTMLString() {

        AppPageBuilder page = new AppPageBuilder("Create Series", appversion);

        String pageToRender = new ResourceReader().asString("/web/apps/pulp/" + appversion.getAppVersion() + "/page-template/entity-crud/create/create-book-series-content.html");
        MyTemplate template = new MyTemplate(pageToRender);
        template.replace("<!-- OUTPUT GOES HERE -->", output);

        page.appendToBody(template.toString());
        return page.toString();
    }


}
