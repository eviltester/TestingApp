package com.javafortesters.pulp.html;

import com.javafortesters.pulp.html.templates.FilledHTMLTemplate;
import com.javafortesters.pulp.reporting.ReportConfig;
import com.javafortesters.pulp.spark.app.versioning.AppVersion;

import java.util.Collection;

public class HTMLReporter {

    private final AppVersion appversion;

    public HTMLReporter(final AppVersion appversion) {
        this.appversion = appversion;
    }

    public String getAsUl(Collection<String> simpleReport, String listname) {
        return getAsUl(simpleReport, listname, "");
    }

    public String getAsUl(final Collection<String> simpleReport, final String listname, final String ulclass) {
        StringBuilder sb = new StringBuilder();

        sb.append(new FilledHTMLTemplate(appversion).ul(listname + "-list", ulclass));

        sb.append("\n");

        int line=0;
        for(String reportLine : simpleReport){
            sb.append(new FilledHTMLTemplate(appversion).li(reportLine, listname+"-list-item-"+line, listname + "-list-item"));
            line++;
        }

        sb.append("</ul>");

        return sb.toString();
    }
}
