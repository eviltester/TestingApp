package com.javafortesters.pulp.reader;

import com.javafortesters.pulp.domain.objects.PulpBook;

import java.util.List;

public interface PulpSeriesCSVReader {
    List<String> getPublisherNames();

    List<String> getAuthorNames();

    List<String> getPulpSeries();

    int numberOfLines();

    PulpBook getBook(int bookNumber);
}
