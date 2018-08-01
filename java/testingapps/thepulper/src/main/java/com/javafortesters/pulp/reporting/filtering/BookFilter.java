package com.javafortesters.pulp.reporting.filtering;

public class BookFilter {
    private String authorId;
    private Integer year;
    private String publisherId;
    private String seriesId;
    private String titlePartialMatch;
    private Integer pageLimit;
    private Integer currentPage;
    private String bookId;

    public BookFilter where() {
        return this;
    }

    public BookFilter and() {
        return this;
    }

    public BookFilter author(String id) {
        this.authorId = id;
        return this;
    }

    public boolean isByAuthor() {
        return authorId!=null;
    }

    public String getAuthorId() {
        return authorId;
    }

    public BookFilter publishedInYear(int year) {
        this.year = Integer.valueOf(year);
        return this;
    }

    public boolean isByYear() {
        return year!=null;
    }

    public int getYear() {
        return year.intValue();
    }

    public BookFilter publishedBy(String id) {
        this.publisherId = id;
        return this;
    }

    public boolean isByPublisher() {
        return publisherId!=null;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public BookFilter partOfSeries(String id) {
        this.seriesId = id;
        return this;
    }

    public boolean isBySeries() {
        return seriesId!=null;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public BookFilter titleContains(String partialMatchTitle) {
        this.titlePartialMatch = partialMatchTitle;
        return this;
    }

    public boolean isByPartialTitleMatch() {
        return titlePartialMatch!=null;
    }

    public String getPartialTitleMatchString() {
        return titlePartialMatch;
    }

    public BookFilter numberPerPage(Integer pageLimit) {
        this.pageLimit = pageLimit;
        return this;
    }

    public BookFilter currentPage(Integer currentPage) {
        this.currentPage=currentPage;
        return this;
    }

    public boolean isPaging() {
        return !((pageLimit==null)||(currentPage==null));
    }

    public int getNumberPerPage() {

        if(pageLimit==null){
            return 20;
        }
        return pageLimit;
    }

    public int getCurrentPage() {
        if(currentPage==null){
            return 1;
        }

        return currentPage;
    }

    public BookFilter book(final String id) {
        this.bookId = id;
        return this;
    }

    public boolean isByBookId() {
        return bookId!=null;
    }

    public String getBookId() {
        return bookId;
    }
}
