package com.javafortesters.pulp.html.gui.entitycrud.createPages;

import com.javafortesters.pulp.html.gui.snippets.AppPageBuilder;
import com.javafortesters.pulp.html.templates.MyTemplate;
import com.javafortesters.pulp.reader.ResourceReader;

public class CreateAuthorPage {

    private final String appversion;
    private String output="";

    public CreateAuthorPage(final String appversion) {
        this.appversion = appversion;
    }

    public void setOutput(final String output) {
        this.output=output;
    }

    public String asHTMLString() {

        AppPageBuilder page = new AppPageBuilder("Create Author", appversion);

        String pageToRender = new ResourceReader().asString("/web/apps/pulp/" + appversion + "/page-template/entity-crud/create/create-book-author-content.html");
        MyTemplate template = new MyTemplate(pageToRender);
        template.replace("<!-- OUTPUT GOES HERE -->", output);

        page.appendToBody(template.toString());
        return page.toString();
    }

}
