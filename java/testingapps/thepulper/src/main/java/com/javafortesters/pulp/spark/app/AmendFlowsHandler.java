package com.javafortesters.pulp.spark.app;

import com.javafortesters.pulp.PulpApp;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpSeries;
import com.javafortesters.pulp.html.gui.entitycrud.updatePages.AmendAuthorPage;
import com.javafortesters.pulp.html.gui.entitycrud.updatePages.AmendSeriesPage;
import spark.Request;
import spark.Response;

public class AmendFlowsHandler {
    private final PulpApp pulp;

    public AmendFlowsHandler(final PulpApp aPulp) {
        this.pulp = aPulp;
    }

    public String authorAmend(final Request req, final Response res) {

        final PulpAuthor author = pulp.books().authors().get(req.queryParams("authorid"));

        String errorMessage="";

        if(author != PulpAuthor.UNKNOWN_AUTHOR){

            String newName = req.queryParams("name");
            author.amendName(newName);
            if(newName == null || !author.getName().contentEquals(newName)){
                errorMessage = "<h2>Could not amend author details to " + newName + "</h2>";
            }
        }else{
            res.status(404);
            errorMessage = "<h2>Cannot amend an unknown author</h2>";
        }


        final AmendAuthorPage page = pulp.page().amendAuthorPage(author.getId());
        if(!errorMessage.isEmpty()){
            res.status(400);
            page.setOutput(errorMessage);
        }else{
            page.setOutput("<h2>Author name amended</h2>");
        }

        return page.asHTMLString();

    }

    public String authorSeries(final Request req, final Response res) {

        final PulpSeries series = pulp.books().series().get(req.queryParams("seriesid"));

        String errorMessage="";

        if(series != PulpSeries.UNKNOWN_SERIES){

            String newName = req.queryParams("seriesname");
            series.amendName(newName);
            if(newName==null || !series.getName().contentEquals(newName)){
                errorMessage = "<h2>Could not amend series details to " + newName + "</h2>";
            }
        }else{
            res.status(404);
            errorMessage = "<h2>Cannot amend an unknown series</h2>";
        }


        final AmendSeriesPage page = pulp.page().amendSeriesPage(series.getId());
        if(!errorMessage.isEmpty()){
            res.status(400);
            page.setOutput(errorMessage);
        }else{
            page.setOutput("<h2>Series name amended</h2>");
        }

        return page.asHTMLString();
    }
}
