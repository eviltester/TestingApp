package com.javafortesters.pulp.reporting.reporters;

import com.javafortesters.pulp.domain.groupings.PulpAuthors;
import com.javafortesters.pulp.domain.groupings.PulpPublishers;
import com.javafortesters.pulp.domain.groupings.PulpSeriesCollection;
import com.javafortesters.pulp.domain.objects.PulpBook;
import com.javafortesters.pulp.reporting.ReportConfig;

import java.util.*;

public class BookReporter {

    private final PulpAuthors authors;
    private final PulpPublishers publishers;
    private final PulpSeriesCollection series;
    private final SeriesReporter seriesReporter;
    private final PublisherReporter publisherReporter;
    private final YearReporter yearReporter;
    private final AuthorReporter authorReporter;
    private final ReportConfig reportConfig;

    public BookReporter(ReportConfig reportConfig, PulpAuthors authors, PulpPublishers publishers, PulpSeriesCollection series) {

        this.authors = authors;
        this.publishers = publishers;
        this.series = series;

        this.reportConfig = reportConfig;
        // todoL do not need the series in reporter for item only
        this.seriesReporter = new SeriesReporter(reportConfig);
        this.publisherReporter = new PublisherReporter(reportConfig);
        this.yearReporter = new YearReporter(reportConfig);
        this.authorReporter = new AuthorReporter(reportConfig);
    }

    public static BookReporter getEmpty() {
        return new BookReporter(ReportConfig.justStrings(), new PulpAuthors(), new PulpPublishers(), new PulpSeriesCollection());
    }

    public AuthorReporter getAuthorReporter(){
        return this.authorReporter;
    }

    public YearReporter getYearReporter(){
        return this.yearReporter;
    }

    public SeriesReporter getSeriesReporter(){
        return this.seriesReporter;
    }

    public PublisherReporter getPublisherReporter(){
        return this.publisherReporter;
    }


    public String getAsLine(PulpBook book) {

        StringBuilder line;

        line = new StringBuilder();

        line.append(getTitle(book));
        line.append(" | ");

        line.append(authorReporter.getConcatenated(authors.getAll(book.getAllAuthorIndexes()), ", "));

        line.append(" | ");

        line.append(yearReporter.getYear(book.getPublicationYear()));
        line.append(" | ");

        line.append(publisherReporter.getPublisher(publishers.get(book.getPublisherIndex())));
        line.append(" | ");

        line.append(seriesReporter.getSeries(series.get(book.getSeriesIndex())));

        return line.toString();
    }

    public Collection<String> getBooksAsLines(Collection<PulpBook> books) {
        List<String> report = new ArrayList<>();

        for(PulpBook book : books){
            report.add(getAsLine(book));
        }

        return report;
    }


    public String getBooksAsTable(List<PulpBook> books) {
        StringBuilder table=new StringBuilder();

        table.append("<table>");

        table.append("<tr>");
        table.append("<th>Title</th>");
        table.append("<th>Authors</th>");
        table.append("<th>Published</th>");
        table.append("<th>Publisher</th>");
        table.append("<th>Series</th>");
        table.append("</tr>");

        for(PulpBook book : books){
            table.append(getAsTr(book));
        }

        table.append("</table>");
        return table.toString();
    }

    public String getTitle(PulpBook book){
            if(reportConfig!=null && reportConfig.areTitlesLinks()){
                final String title = String.format("<a href='%s?book=%s'>%s</a>", reportConfig.getReportPath("books"), book.getId(), book.getTitle());
                String amend = "";

                if(reportConfig.areBookAmendLinksShown()) {
                    amend = String.format("[<a href='%s%s' alt='Amend'>amend</a>]",
                            reportConfig.withoutPostLink().withoutReportInPath().getReportPath("amend/book?book="), book.getId());
                }

                return title + " " + amend;
            }else{
                return book.getTitle();
            }
    }

    private String getAsTr(PulpBook book) {
        StringBuilder line;

        line = new StringBuilder();

        line.append("<tr>");
        line.append("<td>");
        line.append(getTitle(book));
        line.append("</td>");

        line.append("<td>");
        line.append(authorReporter.getConcatenated(authors.getAll(book.getAllAuthorIndexes()), ", "));
        line.append("</td>");

        line.append("<td>");
        line.append(yearReporter.getYear(book.getPublicationYear()));
        line.append("</td>");

        line.append("<td>");
        line.append(publisherReporter.getPublisher(publishers.get(book.getPublisherIndex())));
        line.append("</td>");

        line.append("<td>");
        line.append(seriesReporter.getSeries(series.get(book.getSeriesIndex())));
        line.append("</td>");

        line.append("</tr>");
        return line.toString();
    }
}
