package com.javafortesters.pulp.reader.forseries;

import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpBook;
import com.javafortesters.pulp.reader.CsvReader;
import com.javafortesters.pulp.reader.PulpSeriesCSVReader;


import java.util.*;

public class SpiderReader implements PulpSeriesCSVReader {
    private final CsvReader reader;
    private String defaultSeriesName;



    public SpiderReader(String resourcePath) {
        reader = new CsvReader(resourcePath);
        reader.read();
        this.setSeriesName("The Spider");
    }

    private void setSeriesName(String defaultSeriesName) {
        this.defaultSeriesName = defaultSeriesName;
    }


    public int numberOfLines() {
        return reader.numberOfLines();
    }

    public PulpBook getBook(int atLine) {

        String houseAuthor = "Grant Stockbridge";
        String authors = reader.getLineField(atLine,0);

        if(!authors.contains(houseAuthor)){
            houseAuthor="";
        }

        return new PulpBook(   "unknown",
                                        defaultSeriesName,
                                        authors,
                                        houseAuthor,
                                        reader.getLineField(atLine,1),
                                        reader.getLineField(atLine,2),
                                        Integer.valueOf(reader.getLineField(atLine,3)),
                                        reader.getLineField(atLine,4)
                            );
    }

    public List<String> getAuthorNames() {

        Set<String> authorNames = new HashSet<>();

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
        publishers.add("Popular Publications");
        return publishers;
    }


    public List<String> getPulpSeries() {
        List<String> seriesnames = new ArrayList<>();
        seriesnames.add(defaultSeriesName);
        return seriesnames;
    }
}
