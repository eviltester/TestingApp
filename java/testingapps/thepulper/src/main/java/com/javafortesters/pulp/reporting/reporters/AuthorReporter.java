package com.javafortesters.pulp.reporting.reporters;

import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.html.templates.FilledHTMLTemplate;
import com.javafortesters.pulp.html.templates.MyUrlEncoder;
import com.javafortesters.pulp.reporting.ReportConfig;
import com.javafortesters.pulp.spark.app.versioning.AppVersionSettings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AuthorReporter {
    private final ReportConfig reportConfig;

    public AuthorReporter(ReportConfig reportConfig) {
        // TODO:this is a slightly different setup than other reports - they assign the reportConfig , rather than a copy - I kind of prefer copy, but which should we decide on?
        this.reportConfig = new ReportConfig(reportConfig);
    }


    public Collection<String> getAsStrings(Collection<PulpAuthor> authors) {
        List<String> report = new ArrayList<>();

        for(PulpAuthor author : authors) {

            report.add(getAuthorName(author));
        }

        return report;
    }

    public String getAuthorName(PulpAuthor author) {

        final String defaultNameOutput = new FilledHTMLTemplate(reportConfig.getAppVersion()).span(String.format("author-name-%s", author.getId()), author.getName());

        if(reportConfig!=null) {

            String faqs = "";
            if (reportConfig.includeFaqLinks()) {
                faqs = String.format(" [<a href='%s%s?searchterm=%s'>faqs</a>]", reportConfig.getReportPath(), "author/faqs", MyUrlEncoder.encode(author.getName()));
                faqs = new FilledHTMLTemplate(reportConfig.getAppVersion()).span(String.format("author-faqs-%s", author.getId()), faqs);
            }

            String name = defaultNameOutput;
            if (reportConfig.areAuthorNamesLinks()) {
                if(reportConfig.getAppVersion().are(AppVersionSettings.TITLE_LINKS_TO_BOOKS_LIST)){
                    // link to books
                    name = String.format("<a href='%s?author=%s'>%s</a>", reportConfig.getReportPath("books"), author.getId(), author.getName());
                    name = new FilledHTMLTemplate(reportConfig.getAppVersion()).span(String.format("author-details-%s", author.getId()), name);
                }else{
                    // link to thing
                    name = String.format("<a href='%s%s'>%s</a>",
                            reportConfig.withoutPostLink().withoutReportInPath().getReportPath("view/author?author="), author.getId(), author.getName());
                    name = new FilledHTMLTemplate(reportConfig.getAppVersion()).span(String.format("author-details-%s", author.getId()), name);
                }
            }

            String amend = "";
            if (reportConfig.areAuthorAmendLinksShown()) {
                amend = String.format("[<a href='%s%s' alt='Amend'>amend</a>]",
                        reportConfig.withoutPostLink().withoutReportInPath().getReportPath("amend/author?author="), author.getId(), author.getName());
                amend = new FilledHTMLTemplate(reportConfig.getAppVersion()).span(String.format("author-amend-%s", author.getId()), amend);
            }

            String delete = "";
            if (reportConfig.allowDeleteAuthor()){
                delete = new FilledHTMLTemplate(reportConfig.getAppVersion()).deleteAuthorButton(author.getId(), "[x]", author.getName());
            }



            return (name + " "  + faqs + " " + amend + " " + delete).trim();

        }else{
            return defaultNameOutput;
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
