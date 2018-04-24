package com.javafortesters.pulp.reader;

import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpBook;
import com.javafortesters.pulp.domain.objects.PulpPublisher;
import com.javafortesters.pulp.domain.objects.PulpSeries;
import java.util.List;

public class PulpDataPopulator {
    private final PulpData data;
    private PulpSeriesCSVReader reader;

    public PulpDataPopulator(PulpData data) {
        this.data = data;
    }

    public void populateFrom(PulpSeriesCSVReader reader) {
        this.reader = reader;

        addTheAuthorNames(reader, data);
        addThePublisherNames(reader, data);
        addTheSeriesNames(reader, data);
        addTheBookData(reader, data);

    }

    private void addThePublisherNames(PulpSeriesCSVReader reader, PulpData data) {
        List<String> names = reader.getPublisherNames();

        for(String publisherName : names){
            data.publishers().add(publisherName);
        }

    }

    private void addTheAuthorNames(PulpSeriesCSVReader reader, PulpData data) {
        List<String> names = reader.getAuthorNames();

        for(String authorName : names){
            data.authors().add(authorName);
        }
    }

    private void addTheSeriesNames(PulpSeriesCSVReader reader, PulpData data) {
        List<String> names = reader.getPulpSeries();

        for(String seriesName : names){
            data.series().add(seriesName);
        }
    }


    private void addTheBookData(PulpSeriesCSVReader reader, PulpData data) {

        for(int bookNumber=0; bookNumber < reader.numberOfLines(); bookNumber++){

            PulpBook aDraftBook = reader.getBook(bookNumber);

            String title = aDraftBook.getTitle();
            int publicationYear = aDraftBook.getPublicationYear();
            String seriesId = aDraftBook.getSeriesId();

            // find the series index
            PulpSeries series = data.series().findByName(aDraftBook.getSeriesIndex());

            // find the author indexes
            List<String> authors = aDraftBook.getAuthorIndexes();
            PulpAuthor author = data.authors().findByName(authors.get(0)); // use first author as main author

            // find the house author index
            PulpAuthor houseAuthor;
            if(aDraftBook.getHouseAuthorIndex().trim().length()==0) {
                // there is no house author
                houseAuthor = author;
            }else{
                houseAuthor = data.authors().findByName(aDraftBook.getHouseAuthorIndex());
            }

            // find the publisher index
            PulpPublisher publisher = data.publishers().findByName(aDraftBook.getPublisherIndex());

            if(allIndexesArePresent(series, author, houseAuthor, publisher )){
                PulpBook book = data.books().add(series.getId(), author.getId(), houseAuthor.getId(), title, seriesId, publicationYear, publisher.getId());
                for(String authorName : authors){
                    PulpAuthor anAuthor = data.authors().findByName(authorName);
                    book.addCoAuthor(anAuthor.getId());
                }
            }else{
                System.out.println(String.format("Warning: Could not add SERIES %s, %s, %s, by %s (published as %s) by publisher %s",
                                                    aDraftBook.getSeriesIndex(), title, seriesId, aDraftBook.getAuthorIndexesAsString(), aDraftBook.getHouseAuthorIndex(), aDraftBook.getPublisherIndex()));
                System.out.println(String.format("Because: %s", whichIndexesAreMissing(series, author, houseAuthor, publisher)));
            }

        }
    }

    private String whichIndexesAreMissing(PulpSeries series, PulpAuthor author, PulpAuthor houseAuthor, PulpPublisher publisher) {

        String output = "";

        if(series==PulpSeries.UNKNOWN_SERIES){
            output +=" Unknown Series,";
        }
        if(author==PulpAuthor.UNKNOWN_AUTHOR){
            output +=" Unknown Author,";
        }
        if(houseAuthor==PulpAuthor.UNKNOWN_AUTHOR){
            output +=" Unknown House Author,";
        }
        if(publisher==PulpPublisher.UNKNOWN_PUBLISHER){
            output +=" Unknown Publisher,";
        }

        return output;
    }


    private boolean allIndexesArePresent(PulpSeries series, PulpAuthor author, PulpAuthor houseAuthor, PulpPublisher publisher) {
        if(series==PulpSeries.UNKNOWN_SERIES){
            return false;
        }
        if(author==PulpAuthor.UNKNOWN_AUTHOR){
            return false;
        }
        if(houseAuthor==PulpAuthor.UNKNOWN_AUTHOR){
            return false;
        }
        if(publisher==PulpPublisher.UNKNOWN_PUBLISHER){
            return false;
        }


        return true;
    }

}
