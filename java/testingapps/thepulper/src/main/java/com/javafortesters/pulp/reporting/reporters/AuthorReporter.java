package com.javafortesters.pulp.reporting.reporters;

import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.html.templates.FilledHTMLTemplate;
import com.javafortesters.pulp.html.templates.MyUrlEncoder;
import com.javafortesters.pulp.reporting.ReportConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AuthorReporter {
    private final ReportConfig reportConfig;

    public AuthorReporter(ReportConfig reportConfig) {
        this.reportConfig = new ReportConfig(reportConfig);
    }

    public AuthorReporter(ReportConfig reportConfig, boolean includeFaqLinks) {
        this.reportConfig = new ReportConfig(reportConfig);
        this.reportConfig.setIncludeFaqLinks(includeFaqLinks);
    }

    public Collection<String> getAsStrings(Collection<PulpAuthor> authors) {
        List<String> report = new ArrayList<>();

        for(PulpAuthor author : authors) {

            report.add(getAuthorName(author));
        }

        return report;
    }

    public String getAuthorName(PulpAuthor author) {

        if(reportConfig!=null && reportConfig.areAuthorNamesLinks()){
            String faqs="";
            if(reportConfig.includeFaqLinks()) {
                 faqs = String.format(" [<a href='%s%s?searchterm=%s'>faqs</a>]", reportConfig.getReportPath(),"author/faqs", MyUrlEncoder.encode(author.getName()));
                 faqs = new FilledHTMLTemplate(reportConfig.getAppVersion()).span(String.format("author-faqs-%s", author.getId()), faqs);
            }


            String name =  String.format("<a href='%s?author=%s'>%s</a>", reportConfig.getReportPath("books"), author.getId(), author.getName());
            name = new FilledHTMLTemplate(reportConfig.getAppVersion()).span(String.format("author-details-%s", author.getId()), name);

            String amend="";
            if(reportConfig.areAuthorAmendLinksShown()) {
                amend = String.format("[<a href='%s%s' alt='Amend'>amend</a>]",
                        reportConfig.withoutPostLink().withoutReportInPath().getReportPath("amend/author?author="), author.getId(), author.getName());
                amend = new FilledHTMLTemplate(reportConfig.getAppVersion()).span(String.format("author-amend-%s", author.getId()), amend);
            }

            return name + " "  + faqs + " " + amend;

        }else{
            return new FilledHTMLTemplate(reportConfig.getAppVersion()).span(String.format("author-name-%s", author.getId()), author.getName());
        }

    }

    public String getConcatenated(Collection<PulpAuthor> authors, String concatonator) {
        StringBuilder line = new StringBuilder();
        int authorCount = authors.size();

        for(PulpAuthor author : authors){
            line.append(getAuthorName(author));
            authorCount--;
            if(authorCount!=0) {
                line.append(concatonator);
            }
        }

        return line.toString();
    }

}
