package com.javafortesters.pulp.html.gui.entitycrud.viewPages;

import com.javafortesters.pulp.domain.objects.PulpSeries;
import com.javafortesters.pulp.html.gui.snippets.AppPageBuilder;
import com.javafortesters.pulp.html.templates.MyTemplate;
import com.javafortesters.pulp.reader.ResourceReader;

public class ViewSeriesPage {

    private final PulpSeries series;
    private final String appversion;
    private String output="";

    public void setOutput(final String output) {
        this.output=output;
    }

    public ViewSeriesPage(PulpSeries aSeries, final String appversion){
        this.series = aSeries;
        this.appversion = appversion;
    }

    public String asHTMLString() {

        AppPageBuilder page = new AppPageBuilder("View Series", appversion);

        String pageToRender = new ResourceReader().asString("/web/apps/pulp/" + appversion + "/page-template/entity-crud/read/view-book-series-content.html");

        MyTemplate template = new MyTemplate(pageToRender);

        template.replace("!!ID!!", series.getId());
        template.replace("!!SERIESNAME!!", series.getName());

        template.replace("<!-- OUTPUT GOES HERE -->", output);

        page.appendToBody(template.toString());
        return page.toString();
    }
}
