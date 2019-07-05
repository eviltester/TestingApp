package com.javafortesters.pulp.spark.app.crudhandling;

import com.javafortesters.pulp.PulpApp;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpBook;
import com.javafortesters.pulp.domain.objects.PulpPublisher;
import com.javafortesters.pulp.domain.objects.PulpSeries;
import com.javafortesters.pulp.html.gui.entitycrud.updatePages.AmendAuthorPage;
import com.javafortesters.pulp.html.gui.entitycrud.updatePages.AmendBookPage;
import com.javafortesters.pulp.html.gui.entitycrud.updatePages.AmendPublisherPage;
import com.javafortesters.pulp.html.gui.entitycrud.updatePages.AmendSeriesPage;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public String seriesAmend(final Request req, final Response res) {

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

    public String publisherAmend(final Request req, final Response res) {
        final PulpPublisher publisher = pulp.books().publishers().get(req.queryParams("publisherid"));

        String errorMessage="";

        if(publisher != PulpPublisher.UNKNOWN_PUBLISHER){

            String newName = req.queryParams("name");
            publisher.amendName(newName);
            if(newName==null || !publisher.getName().contentEquals(newName)){
                errorMessage = "<h2>Could not amend publisher details to " + newName + "</h2>";
            }
        }else{
            res.status(404);
            errorMessage = "<h2>Cannot amend an unknown publisher</h2>";
        }


        final AmendPublisherPage page = pulp.page().amendPublisherPage(publisher.getId());
        if(!errorMessage.isEmpty()){
            res.status(400);
            page.setOutput(errorMessage);
        }else{
            page.setOutput("<h2>Publisher name amended</h2>");
        }

        return page.asHTMLString();
    }

    public String bookAmend(final Request req, final Response res) {
        final PulpBook book = pulp.books().books().get(req.queryParams("bookid"));

        StringBuilder errorMessage = new StringBuilder();

        if(book != PulpBook.UNKNOWN_BOOK){

            // TODO should really set book back to start if it fails validation
            // e.g. PulpBook cloneDetails = book.cloneThis();
            //      do all amendments on clone
            //      if(errorMessages.isEmpty())
            //          book.setFromClone(cloneDetails);

            String newTitle = req.queryParams("title");

            // TODO: handle multiple authors with a multi select
            //String newAuthorID = req.queryParams("authorid");
            String[] authorIds = req.queryParamsValues("authorid");

            String newSeriesIdentifier = req.queryParams("seriesidentifier");
            String yearOfPublication = req.queryParams("yearofpub");
            String seriesId = req.queryParams("seriesid");
            String publisherId = req.queryParams("publisherid");
            String houseAuthorId = req.queryParams("houseauthorid");

            book.amendTitle(newTitle);
            if(newTitle==null || !book.getTitle().contentEquals(newTitle)){
                res.status(400);
                errorMessage.append("<h2>Could not amend title to " + newTitle + "</h2>");
            }


            List<String> givenAuthors = Arrays.asList(authorIds);
            if(givenAuthors.size()==0) {
                res.status(400);
                errorMessage.append("<h2>A book must have at least one author</h2>");
            }else {

                // check that each author exists
                List<String> foundAuthors = new ArrayList();

                for (PulpAuthor author : pulp.books().authors().getAllOrderedByName()) {
                    if (givenAuthors.contains(author.getId())) {
                        foundAuthors.add(author.getId());
                    }
                }
                if (foundAuthors.size() != givenAuthors.size()) {
                    res.status(404);
                    errorMessage.append("<h2>Could not find all authors</h2>");
                } else {

                    if (!book.amendAuthors(foundAuthors)) {
                        res.status(400);
                        errorMessage.append("<h2>Could not amend authors</h2>");
                    }
                }
            }


            book.amendSeriesIdentifier(newSeriesIdentifier);
            if(newTitle==null || !book.getSeriesId().contentEquals(newSeriesIdentifier)){
                res.status(400);
                errorMessage.append("<h2>Could not amend series index to " + newSeriesIdentifier + "</h2>");
            }
            book.amendPublicationYear(yearOfPublication);
            if(yearOfPublication==null || !String.valueOf(book.getPublicationYear()).contentEquals(yearOfPublication)){
                res.status(400);
                errorMessage.append("<h2>Could not amend publication year " + yearOfPublication + "</h2>");
            }

            final PulpSeries series = pulp.books().series().get(seriesId);
            if(series != PulpSeries.UNKNOWN_SERIES){
                book.amendSeries(series.getId());
            }else{
                res.status(404);
                errorMessage.append("<h2>Cannot amend to an unknown series</h2>");
            }

            final PulpPublisher publisher = pulp.books().publishers().get(publisherId);
            if(publisher != PulpPublisher.UNKNOWN_PUBLISHER){
                book.amendPublisher(publisher.getId());
            }else{
                res.status(404);
                errorMessage.append("<h2>Cannot amend to an unknown publisher</h2>");
            }

            final PulpAuthor houseAuthor = pulp.books().authors().get(houseAuthorId);
            if(houseAuthor != PulpAuthor.UNKNOWN_AUTHOR){
                book.amendHouseAuthor(houseAuthor.getId());
            }else{
                res.status(404);
                errorMessage.append("<h2>Cannot amend to an unknown house author</h2>");
            }

        }else{
            res.status(404);
            errorMessage.append("<h2>Cannot amend an unknown book</h2>");
        }


        final AmendBookPage page = pulp.page().amendBookPage(book.getId());
        if(!errorMessage.toString().isEmpty()){
            page.setOutput(errorMessage.toString());
        }else{
            page.setOutput("<h2>Book amended</h2>");
        }

        return page.asHTMLString();
    }
}
