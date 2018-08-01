package com.javafortesters.pulp.spark.app;

import com.javafortesters.pulp.reporting.filtering.BookFilter;
import spark.QueryParamsMap;

public class BookFilterFromQueryMap {
    public static BookFilter getBookFilter(QueryParamsMap queryParamsMap) {
        BookFilter filter = new BookFilter();
        if(queryParamsMap.hasKeys() && queryParamsMap.value("book")!=null){
            filter.book(queryParamsMap.value("book"));
        }
        if(queryParamsMap.hasKeys() && queryParamsMap.value("author")!=null){
            filter.author(queryParamsMap.value("author"));
        }
        if(queryParamsMap.hasKeys() && queryParamsMap.value("year")!=null){
            filter.publishedInYear(Integer.valueOf(queryParamsMap.value("year")));
        }
        if(queryParamsMap.hasKeys() && queryParamsMap.value("publisher")!=null){
            filter.publishedBy(queryParamsMap.value("publisher"));
        }
        if(queryParamsMap.hasKeys() && queryParamsMap.value("series")!=null){
            filter.partOfSeries(queryParamsMap.value("series"));
        }
        if(queryParamsMap.hasKeys() && queryParamsMap.value("page")!=null){
            filter.currentPage(Integer.valueOf(queryParamsMap.value("page")));
        }
        if(queryParamsMap.hasKeys() && queryParamsMap.value("pagelimit")!=null){
            filter.numberPerPage(Integer.valueOf(queryParamsMap.value("pagelimit")));
        }
        if(queryParamsMap.hasKeys() && queryParamsMap.value("searchterm")!=null){
            filter.titleContains(queryParamsMap.value("searchterm"));
        }
        return filter;
    }
}
