package com.javafortesters.pulp.reporting.filtering;

public class PaginationDetails {
    private boolean paginated;
    private int totalItemCount;
    private int numberPerPage;
    private int currentPage;
    private int numberOfPages;
    private BookFilter filterUsed;
    private String itemPlural = "books";

    public void setPaginated(boolean b) {
        this.paginated = b;
    }

    public void setTotalItems(int size) {
        this.totalItemCount = size;
        if(numberPerPage==0){
            this.numberOfPages=0;
        }else {
            this.numberOfPages = (size / numberPerPage) + 1;
        }
    }

    public void setFromFilter(BookFilter filter) {
        this.paginated = filter.isPaging();
        this.numberPerPage = filter.getNumberPerPage();
        this.currentPage = filter.getCurrentPage();
        this.filterUsed = filter;
    }

    public boolean isPaginated() {
        return paginated;
    }

    public int getTotalItems() {
        return totalItemCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return numberOfPages;
    }

    public int getNumberPerPage() {
        return numberPerPage;
    }

    public BookFilter getFilter() {
        return filterUsed;
    }

    public String getItemsName() {
        return itemPlural;
    }

    public void setNameOfThings(final String listOfWhat) {
        itemPlural = listOfWhat;
    }
}
