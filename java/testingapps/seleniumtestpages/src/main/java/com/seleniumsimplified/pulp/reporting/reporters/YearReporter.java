package com.seleniumsimplified.pulp.reporting.reporters;

import com.seleniumsimplified.pulp.reporting.ReportConfig;

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
            return String.format("<a href='%s?year=%d'>%d</a>", reportConfig.getReportPath("books"), publicationYear, publicationYear);
        }else{
            return String.valueOf(publicationYear);
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
