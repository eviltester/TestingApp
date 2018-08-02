package com.javafortesters.pulp.html.gui.entitycrud.updatePages;

import com.javafortesters.pulp.domain.objects.PulpPublisher;
import com.javafortesters.pulp.html.templates.MyTemplate;
import com.javafortesters.pulp.reader.ResourceReader;

public class AmendPublisherPage {
    private final PulpPublisher publisher;

    public AmendPublisherPage(final PulpPublisher aPublisher) {
        this.publisher = aPublisher;
    }

    private String output="";

    public void setOutput(final String output) {
        this.output=output;
    }

    public String asHTMLString() {
        String pageToRender = new ResourceReader().asString("/web/apps/pulp/page-template/entity-crud/update/edit-book-publisher-content.html");

        MyTemplate template = new MyTemplate(pageToRender);

        template.replace("!!PUBLISHERID!!", publisher.getId());
        template.replace("!!PUBLISHERNAME!!", publisher.getName());

        template.replace("<!-- OUTPUT GOES HERE -->", output);
        template.replace("<!-- FOOTER GOES HERE -->", new ResourceReader().asString("/web/apps/pulp/page-template/reports-list-widget.html"));

        return template.toString();

    }
}
