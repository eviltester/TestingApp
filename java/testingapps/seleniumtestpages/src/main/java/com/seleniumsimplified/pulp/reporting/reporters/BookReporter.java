package com.seleniumsimplified.pulp.reporting.reporters;

import com.seleniumsimplified.pulp.domain.groupings.PulpAuthors;
import com.seleniumsimplified.pulp.domain.groupings.PulpPublishers;
import com.seleniumsimplified.pulp.domain.groupings.PulpSeriesCollection;
import com.seleniumsimplified.pulp.domain.objects.PulpBook;
import com.seleniumsimplified.pulp.reporting.ReportConfig;

import java.util.*;

public class BookReporter {

    private final PulpAuthors authors;
    private final PulpPublishers publishers;
    private final PulpSeriesCollection series;
    private final SeriesReporter seriesReporter;
    private final PublisherReporter publisherReporter;
    private final YearReporter yearReporter;
    private final AuthorReporter authorReporter;

    public BookReporter(ReportConfig reportConfig, PulpAuthors authors, PulpPublishers publishers, PulpSeriesCollection series) {

        this.authors = authors;
        this.publishers = publishers;
        this.series = series;

        // todoL do not need the series in reporter for item only
        this.seriesReporter = new SeriesReporter(reportConfig);
        this.publisherReporter = new PublisherReporter(reportConfig);
        this.yearReporter = new YearReporter(reportConfig);
        this.authorReporter = new AuthorReporter(reportConfig);
    }

    public static BookReporter getEmpty() {
        return new BookReporter(ReportConfig.justStrings(), new PulpAuthors(), new PulpPublishers(), new PulpSeriesCollection());
    }

    public String getAsLine(PulpBook book) {

        StringBuilder line;

        line = new StringBuilder();

        line.append(book.getTitle());
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

    private String getAsTr(PulpBook book) {
        StringBuilder line;

        line = new StringBuilder();

        line.append("<tr>");
        line.append("<td>");
        line.append(book.getTitle());
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
