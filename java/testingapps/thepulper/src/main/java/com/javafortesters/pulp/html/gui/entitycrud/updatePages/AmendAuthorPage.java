package com.javafortesters.pulp.html.gui.entitycrud.updatePages;

import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.html.templates.MyTemplate;
import com.javafortesters.pulp.reader.ResourceReader;

public class AmendAuthorPage {

    private final PulpAuthor author;
    private String output="";

    public void setOutput(final String output) {
        this.output=output;
    }

    public AmendAuthorPage(PulpAuthor anAuthor){
        this.author = anAuthor;
    }

    public String asHTMLString() {
        String pageToRender = new ResourceReader().asString("/web/apps/pulp/page-template/entity-crud/update/edit-book-author-content.html");

        MyTemplate template = new MyTemplate(pageToRender);

        template.replace("!!AUTHORID!!", author.getId());
        template.replace("!!AUTHORNAME!!", author.getName());

        template.replace("<!-- OUTPUT GOES HERE -->", output);
        template.replace("<!-- FOOTER GOES HERE -->", new ResourceReader().asString("/web/apps/pulp/page-template/reports-list-widget.html"));

        return template.toString();

    }
}
