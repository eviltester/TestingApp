package com.javafortesters.pulp.reporting.reporters;

import com.javafortesters.pulp.domain.objects.PulpSeries;
import com.javafortesters.pulp.reporting.ReportConfig;

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
        if(reportConfig!=null && reportConfig.areSeriesNamesLinks()){
            return String.format("<a href='%s?series=%s'>%s</a>", reportConfig.getReportPath("books"), item.getId(),item.getName());
        }else{
            return item.getName();
        }
    }
}
