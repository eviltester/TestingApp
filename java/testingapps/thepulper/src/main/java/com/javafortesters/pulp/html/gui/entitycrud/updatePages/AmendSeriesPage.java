package com.javafortesters.pulp.html.gui.entitycrud.updatePages;

import com.javafortesters.pulp.domain.objects.PulpSeries;
import com.javafortesters.pulp.html.gui.snippets.AppPageBuilder;
import com.javafortesters.pulp.html.templates.MyTemplate;
import com.javafortesters.pulp.reader.VersionedResourceReader;
import com.javafortesters.pulp.spark.app.versioning.AppVersion;

public class AmendSeriesPage {

    private final PulpSeries series;
    private final AppVersion appversion;
    private String output="";

    public void setOutput(final String output) {
        this.output=output;
    }

    public AmendSeriesPage(PulpSeries aSeries, final AppVersion appversion){
        this.series = aSeries;
        this.appversion = appversion;
    }

    public String asHTMLString() {

        AppPageBuilder page = new AppPageBuilder("Amend Series", appversion);

        final VersionedResourceReader versionedReader = new VersionedResourceReader(appversion);
        String pageToRender = versionedReader.asString( "/page-template/entity-crud/update/edit-book-series-content.html");
//        String pageToRender = new ResourceReader().asString("/web/apps/pulp/" + appversion.getAppVersion() + "/page-template/entity-crud/update/edit-book-series-content.html");

        MyTemplate template = new MyTemplate(pageToRender);

        template.replace("!!ID!!", series.getId());
        template.replace("!!SERIESNAME!!", series.getName());

        template.replace("<!-- OUTPUT GOES HERE -->", output);

        page.appendToBody(template.toString());
        return page.toString();
    }
}
