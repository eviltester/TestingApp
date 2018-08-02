package com.javafortesters.pulp.html.gui.entitycrud.updatePages;

import com.javafortesters.pulp.domain.objects.PulpSeries;
import com.javafortesters.pulp.html.templates.MyTemplate;
import com.javafortesters.pulp.reader.ResourceReader;

public class AmendSeriesPage {

    private final PulpSeries series;
    private String output="";

    public void setOutput(final String output) {
        this.output=output;
    }

    public AmendSeriesPage(PulpSeries aSeries){
        this.series = aSeries;
    }

    public String asHTMLString() {
        String pageToRender = new ResourceReader().asString("/web/apps/pulp/page-template/entity-crud/update/edit-book-series-content.html");

        MyTemplate template = new MyTemplate(pageToRender);

        template.replace("!!SERIESID!!", series.getId());
        template.replace("!!SERIESNAME!!", series.getName());

        template.replace("<!-- OUTPUT GOES HERE -->", output);
        template.replace("<!-- FOOTER GOES HERE -->", new ResourceReader().asString("/web/apps/pulp/page-template/reports-list-widget.html"));

        return template.toString();

    }
}
