package com.javafortesters.pulp.reporting.reporters;

import com.javafortesters.pulp.domain.objects.PulpSeries;
import com.javafortesters.pulp.html.templates.FilledHTMLTemplate;
import com.javafortesters.pulp.html.templates.MyUrlEncoder;
import com.javafortesters.pulp.reporting.ReportConfig;
import com.javafortesters.pulp.spark.app.versioning.AppVersionSettings;

import java.util.*;

public class SeriesReporter {
    private final ReportConfig reportConfig;

    public SeriesReporter(ReportConfig reportConfig) {
        this.reportConfig = reportConfig;
    }

    public List<String> getAsStrings(Collection<PulpSeries> series) {

        List<String> itemNames = new ArrayList();

        for(PulpSeries aSeries : series) {
            itemNames.add(getSeries(aSeries));
        }

        return itemNames;

    }

    public String getSeries(PulpSeries item) {

        final String defaultSeriesOutput = new FilledHTMLTemplate(reportConfig.getAppVersion()).span(String.format("series-name-%s", item.getId()), item.getName());

        if(reportConfig!=null){

            String faqs="";
            if(reportConfig.includeFaqLinks()) {
                faqs = String.format(" [<a href='%s%s?searchterm=%s'>faqs</a>]", reportConfig.getReportPath(),"series/faqs", MyUrlEncoder.encode(item.getName()));
                faqs = new FilledHTMLTemplate(reportConfig.getAppVersion()).span(String.format("series-faqs-%s", item.getId()), faqs);
            }


            String name = defaultSeriesOutput;
            if(reportConfig.areSeriesNamesLinks()) {

                if(reportConfig.getAppVersion().are(AppVersionSettings.TITLE_LINKS_TO_BOOKS_LIST)){
                    // link to books
                    name = String.format("<a href='%s?series=%s'>%s</a>", reportConfig.getReportPath("books"), item.getId(), item.getName());
                    name = new FilledHTMLTemplate(reportConfig.getAppVersion()).span(String.format("series-details-%s", item.getId()), name);
                }else{
                    // link to thing
                    name = String.format("<a href='%s%s' title='View Details'>%s</a>",
                            reportConfig.withoutPostLink().withoutReportInPath().getReportPath("view/series?series="), item.getId(), item.getName());
                    name = new FilledHTMLTemplate(reportConfig.getAppVersion()).span(String.format("series-details-%s", item.getId()), name);
                }

            }

            String amend="";
            if(reportConfig.areSeriesAmendLinksShown()) {
                amend = String.format("[<a href='%s%s' alt='Amend'>amend</a>]",
                        reportConfig.withoutPostLink().withoutReportInPath().getReportPath("amend/series?series="), item.getId(), item.getName());
                amend = new FilledHTMLTemplate(reportConfig.getAppVersion()).span(String.format("series-amend-%s", item.getId()), amend);
            }

            String delete = "";
            if (reportConfig.allowDeleteSeries()){
                delete = new FilledHTMLTemplate(reportConfig.getAppVersion()).deleteSeriesButton(item.getId(), "[x]", item.getName());
            }

            return (name + " " + faqs + " " + amend + " " + delete).trim();
        }else{
            return defaultSeriesOutput;
        }
    }
}
