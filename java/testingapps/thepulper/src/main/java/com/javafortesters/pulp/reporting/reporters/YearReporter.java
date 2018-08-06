package com.javafortesters.pulp.reporting.reporters;

import com.javafortesters.pulp.html.templates.FilledHTMLTemplate;
import com.javafortesters.pulp.html.templates.MyUrlEncoder;
import com.javafortesters.pulp.reporting.ReportConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class YearReporter {
    private final ReportConfig reportConfig;

    public YearReporter(ReportConfig reportConfig) {
        this.reportConfig = reportConfig;
    }

    public String getYear(int publicationYear) {

        final String defaultYearOutput = new FilledHTMLTemplate(reportConfig.getAppVersion()).span(String.format("year-%d", publicationYear), String.valueOf(publicationYear));

        if(reportConfig!=null){

            String faqs="";
            if(reportConfig.includeFaqLinks()) {
                faqs = String.format(" [<a href='%s%s?searchterm=%d'>faqs</a>]", reportConfig.getReportPath(),"year/faqs", publicationYear);
                faqs = new FilledHTMLTemplate(reportConfig.getAppVersion()).span(String.format("year-faqs-%d", publicationYear), faqs);
            }

            String yearlink = defaultYearOutput;
            if(reportConfig.areYearsLinks()) {
                yearlink = String.format("<a href='%s?year=%d'>%d</a>", reportConfig.getReportPath("books"), publicationYear, publicationYear);
                yearlink = new FilledHTMLTemplate(reportConfig.getAppVersion()).span(String.format("year-link-%d", publicationYear), yearlink);
            }

            return (yearlink + " " + faqs).trim();

        }else{
            return defaultYearOutput;
        }
    }

    public Collection<String> getAsLines(Collection<Integer> years) {
        List<String> report = new ArrayList<>();

        for(Integer year : years){
            report.add(getYear(year));
        }

        return report;
    }
}
