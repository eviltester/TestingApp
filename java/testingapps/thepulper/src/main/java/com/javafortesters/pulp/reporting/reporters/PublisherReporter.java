package com.javafortesters.pulp.reporting.reporters;

import com.javafortesters.pulp.domain.objects.PulpPublisher;
import com.javafortesters.pulp.reporting.ReportConfig;

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
        if(reportConfig!=null && reportConfig.arePublishersLinks()){
            final String name = String.format("<a href='%s?publisher=%s'>%s</a>",
                    reportConfig.getReportPath("books"), item.getId(), item.getName());

            String amend="";
            if(reportConfig.arePublisherAmendLinksShown()) {
                amend = String.format("[<a href='%s%s' alt='Amend'>amend</a>]",
                        reportConfig.withoutPostLink().withoutReportInPath().getReportPath("amend/publisher?publisher="), item.getId(), item.getName());
            }
//            final String delete = String.format("[<a href='%s%s' alt='Delete'>x</a>]",
//                                reportConfig.withoutPostLink().getReportPath("amend/publisher?publisher="), item.getId(), item.getName());
            return name + " " + amend;
        }else{
            return item.getName();
        }
    }
}
