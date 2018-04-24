package com.javafortesters.pulp.reader.forseries;

import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpBook;
import com.javafortesters.pulp.reader.CsvReader;
import com.javafortesters.pulp.reader.PulpSeriesCSVReader;


import java.util.*;

public class SavageReader implements PulpSeriesCSVReader {
    private final CsvReader reader;
    private String defaultHouseAuthor;
    private String defaultSeriesName;
    private String defaultPublisherName;

    public SavageReader(String resourcePath) {
        reader = new CsvReader(resourcePath);
        reader.read();
        this.setHouseAuthor("Kenneth Robeson");
        this.setSeriesName("Doc Savage");
        this.setPublisher("Street And Smith");
    }

    private void setSeriesName(String defaultSeriesName) {
        this.defaultSeriesName = defaultSeriesName;
    }


    private void setPublisher(String defaultPublisherName) {
        this.defaultPublisherName = defaultPublisherName;
    }

    private void setHouseAuthor(String defaultHouseAuthor) {
        this.defaultHouseAuthor = defaultHouseAuthor;
    }

    public int numberOfLines() {
        return reader.numberOfLines();
    }

    public PulpBook getBook(int atLine) {

        return new PulpBook(   "unknown",
                                        defaultSeriesName,
                                        reader.getLineField(atLine,0),
                                        defaultHouseAuthor,
                                        reader.getLineField(atLine,1),
                                        reader.getLineField(atLine,2),
                                        Integer.valueOf(reader.getLineField(atLine,3)),
                                        defaultPublisherName
                            );
    }

    public List<String> getAuthorNames() {

        Set<String> authorNames = new HashSet<>();
        authorNames.add(defaultHouseAuthor);

        for(int line=0; line<reader.numberOfLines(); line++){
            authorNames.addAll(getAuthorsFromLine(line));
        }

        List<String> names = new ArrayList<>(authorNames);

        return names;
    }

    private Collection<String> getAuthorsFromLine(int line) {

        String authorsField = reader.getLineField(line,0).trim();

        return PulpAuthor.parseNameAsMultipleAuthors(authorsField);

    }

    public List<String> getPublisherNames() {
        List<String> publishers = new ArrayList<>();
        publishers.add(defaultPublisherName);
        return publishers;
    }

    public List<String> getPulpSeries() {
        List<String> seriesnames = new ArrayList<>();
        seriesnames.add(defaultSeriesName);
        return seriesnames;
    }
}
