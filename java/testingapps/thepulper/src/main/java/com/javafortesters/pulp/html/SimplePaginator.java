package com.javafortesters.pulp.html;

import com.javafortesters.pulp.reporting.filtering.PaginationDetails;

import java.util.Collection;

public class SimplePaginator {
    public static PaginationDetails getPaginationDetails(final Collection<String> simpleReport, final String listOfWhat) {
        PaginationDetails pagination = new PaginationDetails();
        pagination.setPaginated(false);
        pagination.setTotalItems(simpleReport.size());
        pagination.setNameOfThings(listOfWhat);
        return pagination;
    }
}
