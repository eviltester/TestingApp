package com.javafortesters.pulp.reporting.reporters;

import com.javafortesters.pulp.html.templates.FilledHTMLTemplate;
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
        if(reportConfig!=null && reportConfig.areYearsLinks()){
            String yearlink = String.format("<a href='%s?year=%d'>%d</a>", reportConfig.getReportPath("books"), publicationYear, publicationYear);
            return new FilledHTMLTemplate(reportConfig.getAppVersion()).span(String.format("year-link-%d", publicationYear), yearlink);
        }else{
            return new FilledHTMLTemplate(reportConfig.getAppVersion()).span(String.format("year-%d", publicationYear), String.valueOf(publicationYear));
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
