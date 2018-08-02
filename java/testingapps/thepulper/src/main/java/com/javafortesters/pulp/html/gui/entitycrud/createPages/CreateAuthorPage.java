package com.javafortesters.pulp.html.gui.entitycrud.createPages;

import com.javafortesters.pulp.html.templates.MyTemplate;
import com.javafortesters.pulp.reader.ResourceReader;

public class CreateAuthorPage {

    private String output="";

    public void setOutput(final String output) {
        this.output=output;
    }

    public String asHTMLString() {
        String pageToRender = new ResourceReader().asString("/web/apps/pulp/page-template/entity-crud/create/create-book-author-content.html");

        MyTemplate template = new MyTemplate(pageToRender);

        template.replace("<!-- OUTPUT GOES HERE -->", output);
        template.replace("<!-- FOOTER GOES HERE -->", new ResourceReader().asString("/web/apps/pulp/page-template/reports-list-widget.html"));

        return template.toString();

    }

}
