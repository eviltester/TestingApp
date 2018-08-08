package com.javafortesters.pulp.reporting.reporters;

import com.javafortesters.pulp.domain.objects.PulpPublisher;
import com.javafortesters.pulp.html.templates.FilledHTMLTemplate;
import com.javafortesters.pulp.html.templates.MyUrlEncoder;
import com.javafortesters.pulp.reporting.ReportConfig;
import com.javafortesters.pulp.spark.app.versioning.AppVersionSettings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PublisherReporter {
    private final ReportConfig reportConfig;

    public PublisherReporter(ReportConfig reportConfig) {
        this.reportConfig = reportConfig;
    }

    public Collection<String> getAsStrings(Collection<PulpPublisher> publishers) {
        List<String> report = new ArrayList<>();

        for(PulpPublisher publisher : publishers) {
            report.add(getPublisher(publisher));
        }

        return report;
    }

    public String getPublisher(PulpPublisher item) {

        final String defaultNameOutput = new FilledHTMLTemplate(reportConfig.getAppVersion()).span(String.format("publisher-name-%s", item.getId()), item.getName());

        if(reportConfig!=null){
            String faqs="";
            if(reportConfig.includeFaqLinks()) {
                faqs = String.format(" [<a href='%s%s?searchterm=%s'>faqs</a>]", reportConfig.getReportPath(),"publisher/faqs", MyUrlEncoder.encode(item.getName()));
                faqs = new FilledHTMLTemplate(reportConfig.getAppVersion()).span(String.format("publisher-faqs-%s", item.getId()), faqs);
            }

            String name = defaultNameOutput;
            if(reportConfig.arePublishersLinks()){
                if(reportConfig.getAppVersion().are(AppVersionSettings.TITLE_LINKS_TO_BOOKS_LIST)){
                    // link to books
                    name = String.format("<a href='%s?publisher=%s'>%s</a>",
                            reportConfig.getReportPath("books"), item.getId(), item.getName());
                    name = new FilledHTMLTemplate(reportConfig.getAppVersion()).span(String.format("publisher-details-%s", item.getId()), name);
                }else{
                    // link to thing
                    name = String.format("<a href='%s%s' title='View Details'>%s</a>",
                            reportConfig.withoutPostLink().withoutReportInPath().getReportPath("view/publisher?publisher="), item.getId(), item.getName());
                    name = new FilledHTMLTemplate(reportConfig.getAppVersion()).span(String.format("publisher-details-%s", item.getId()), name);
                }
            }

            String amend="";
            if(reportConfig.arePublisherAmendLinksShown()) {
                amend = String.format("[<a href='%s%s' alt='Amend'>amend</a>]",
                        reportConfig.withoutPostLink().withoutReportInPath().getReportPath("amend/publisher?publisher="), item.getId(), item.getName());
                amend = new FilledHTMLTemplate(reportConfig.getAppVersion()).span(String.format("publisher-amend-%s", item.getId()), amend);
            }

            String delete = "";
            if (reportConfig.allowDeletePublisher()){
                delete = new FilledHTMLTemplate(reportConfig.getAppVersion()).deletePublisherButton(item.getId(), "[x]", item.getName());
            }

            return (name + " " + faqs + " " + amend + " " + delete).trim();
        }else{
            return defaultNameOutput;
        }
    }
}
