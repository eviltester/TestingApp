package com.javafortesters.pulp.domain.objects;

import com.javafortesters.pulp.reporting.filtering.BookFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class PulpBook {
    public static final PulpBook UNKNOWN_BOOK = new PulpBook("unknown", "unknown", "unknown", "unknown", "Unknown Title", "unknown", 0, "unknown");
    private List<String> authorIndexNames;

    /**
     * The id of the series
     */
    private String seriesIndexName;
    private String title;
    /**
     * The human readable index name i.e. the issue number etc.
     */
    private String seriesId;
    private int publicationYear;
    private String publisherIndexName;
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

    /**
     * The id of the series
     */
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

    /**
     * The human readable index name i.e. the issue number etc.
     */
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

    public PulpBook amendTitle(final String newValue) {
        if(!hasContent(newValue)){
            return this;
        }

        this.title = newValue;

        return this;
    }

    private boolean hasContent(final String newValue) {
        if(newValue == null){
            return false;
        }

        if(newValue.trim().isEmpty()){
            return false;
        }

        return true;
    }

    public PulpBook amendSeriesIdentifier(final String newValue) {
        if(!hasContent(newValue)){
            return this;
        }

        this.seriesId = newValue;

        return this;
    }

    public PulpBook amendPublicationYear(final String newValue) {
        if(!hasContent(newValue)){
            return this;
        }

        int newyear = 0;
        try{
            newyear = Integer.parseInt(newValue);
        }catch(Exception e){
            return this;
        }

        this.publicationYear = newyear;

        return this;
    }

    public PulpBook amendPublisher(final String newValue) {
        if(!hasContent(newValue)){
            return this;
        }


        this.publisherIndexName = newValue;

        return this;
    }

    public PulpBook amendSeries(final String newValue) {
        if(!hasContent(newValue)){
            return this;
        }


        this.seriesIndexName = newValue;

        return this;
    }

    public PulpBook amendHouseAuthor(final String newValue) {
        if(!hasContent(newValue)){
            return this;
        }

        this.houseAuthorIndexName = newValue;

        return this;
    }

    /*
        This amend replaces the existing authors with the supplied list
     */
    public boolean amendAuthors(final List<String> authorIds) {

        if(authorIds==null){
            return false;
        }

        if(authorIds.size()==0){
            return false;
        }

        authorIndexNames.clear();
        authorIndexNames.addAll(authorIds);
        return true;
    }

    /*
        This amend adds the supplied authors to the list.
     */
    public boolean amendPatchAuthors(final List<String> authorIds) {

        if(authorIds==null){
            return false;
        }

        if(authorIds.size()==0){
            return false;
        }

        // TODO: perhaps authorIndexNames should be a Set instead of a list?
        for(String authorId : authorIds){
            if(!authorIndexNames.contains(authorId)){
                authorIndexNames.add(authorId);
            }
        }

        return true;
    }

    public void removeAuthor(final String id) {
        if(id==null){
            return;
        }

        if(id.isEmpty()){
            return;
        }

        authorIndexNames.remove(id);
        if(houseAuthorIndexName.contentEquals(id)){
            houseAuthorIndexName=null;
        }

    }
}
