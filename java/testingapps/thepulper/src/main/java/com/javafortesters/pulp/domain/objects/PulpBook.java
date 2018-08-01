package com.javafortesters.pulp.domain.objects;

import com.javafortesters.pulp.reporting.filtering.BookFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class PulpBook {
    public static final PulpBook UNKNOWN_BOOK = new PulpBook("unknown", "unknown", "unknown", "unknown", "Unknown Title", "unknown", 0, "unknown");
    private final List<String> authorIndexNames;
    private final String seriesIndexName;
    private final String title;
    private final String seriesId;
    private final int publicationYear;
    private final String publisherIndexName;
    private String houseAuthorIndexName;
    private String id;

    public PulpBook(String id, String seriesIndexName, String authorIndexName, String houseAuthorIndex, String title, String seriesId, int publicationYear, String publisherIndexName) {
        this.id = id;
        this.seriesIndexName = seriesIndexName;
        this.authorIndexNames = new ArrayList<>();

        Collection<String> authorNames = PulpAuthor.parseNameAsMultipleAuthors(authorIndexName);
        authorIndexNames.addAll(authorNames);

        this.houseAuthorIndexName = houseAuthorIndex;
        this.title = title;
        this.seriesId = seriesId;
        this.publicationYear = publicationYear;
        this.publisherIndexName = publisherIndexName;
    }

    public String getSeriesIndex() {
        return this.seriesIndexName;
    }


    public List<String> getAuthorIndexes() {
        List<String> indexes = new ArrayList<>(authorIndexNames);
        return indexes;
    }

    public String getTitle() {
        return title;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public String getPublisherIndex() {
        return this.publisherIndexName;
    }

    public String getHouseAuthorIndex() {
        return this.houseAuthorIndexName;
    }

    public String getId() {
        return id;
    }

    public void addCoAuthor(String authorId) {
        if(!authorIndexNames.contains(authorId)){
            authorIndexNames.add(authorId);
        }
    }

    public String getAuthorIndexesAsString() {
        StringBuilder authors = new StringBuilder();
        int authorCount=0;

        for(String index : authorIndexNames){
            if(authorCount!=0 && authorCount <authorIndexNames.size()-1){
                authors.append(", ");
            }
            authors.append(index);
            authorCount++;
        }

        return authors.toString();
    }

    public Collection<String> getAllAuthorIndexes() {
        Collection<String> ids = new HashSet<>(getAuthorIndexes());

        if(houseAuthorIndexName!=null && houseAuthorIndexName.trim().length()!=0){
            ids.add(houseAuthorIndexName);
        }

        return ids;

    }

    public boolean isAuthoredBy(String authorId) {
        return getAllAuthorIndexes().contains(authorId);
    }

    public boolean isBookId(String aBookId) {
        return this.id.contentEquals(aBookId);
    }

    public boolean isPublishedBy(String publisherId) {
        return this.publisherIndexName.equalsIgnoreCase(publisherId);
    }

    public boolean wasPublishedIn(int year) {
        return this.publicationYear==year;
    }

    public boolean isPartOfSeries(String seriesId) {
        return this.seriesIndexName.equalsIgnoreCase(seriesId);
    }

    public boolean matches(BookFilter filter) {
        boolean ok = true;

        if(filter.isByBookId() && !isBookId(filter.getBookId())){
            ok = false;
        }

        if(filter.isByAuthor() && !isAuthoredBy(filter.getAuthorId())){
            ok = false;
        }

        if(filter.isByYear() && !wasPublishedIn(filter.getYear())){
            ok = false;
        }

        if(filter.isByPublisher() && !isPublishedBy(filter.getPublisherId())){
            ok = false;
        }

        if(filter.isBySeries() && !isPartOfSeries(filter.getSeriesId())){
            ok = false;
        }

        if(filter.isByPartialTitleMatch() && !title.toLowerCase().contains(filter.getPartialTitleMatchString().toLowerCase())){
            ok = false;
        }

        return ok;
    }

}
