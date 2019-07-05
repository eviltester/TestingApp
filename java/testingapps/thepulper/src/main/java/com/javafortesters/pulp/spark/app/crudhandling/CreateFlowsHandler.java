package com.javafortesters.pulp.spark.app.crudhandling;

import com.javafortesters.pulp.PulpApp;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpBook;
import com.javafortesters.pulp.domain.objects.PulpPublisher;
import com.javafortesters.pulp.domain.objects.PulpSeries;
import com.javafortesters.pulp.html.gui.entitycrud.createPages.CreateAuthorPage;
import com.javafortesters.pulp.html.gui.entitycrud.createPages.CreateBookPage;
import com.javafortesters.pulp.html.gui.entitycrud.createPages.CreatePublisherPage;
import com.javafortesters.pulp.html.gui.entitycrud.createPages.CreateSeriesPage;
import com.javafortesters.pulp.spark.app.SparkUtils;
import spark.Request;
import spark.Response;

public class CreateFlowsHandler {
    private final PulpApp pulp;

    public CreateFlowsHandler(final PulpApp pulp) {
        this.pulp = pulp;
    }

    public String authorCreate(final Request req, final Response res) {
        final CreateAuthorPage page = pulp.page().createAuthorPage();

        String name = req.queryParams("name");
        if(name==null || name.trim().isEmpty()){
            res.status(400);

            page.setOutput("<h2>Error: Could not find a name to add</h2>");
            return page.asHTMLString();
        }


        try{
            final PulpAuthor author = pulp.books().authors().add(name);
            if(author==null){
                res.status(400);
                page.setOutput(String.format("<h2>Error Adding Author %s</h2>", name));
            }else {
                name = pulp.reports(pulp.reports().getReportConfig().setPostFixPath("/list/navigation")).
                        bookReporter().getAuthorReporter().getAuthorName(author);
                page.setOutput(String.format("<h2>Added Author %s</h2>", name));
            }
        }catch(Exception e){
            res.status(500);
            page.setOutput(String.format("<h2>%s</h2><p>%s</p>",e.getMessage(), SparkUtils.getMyStackTrace(e)));
        }


        return page.asHTMLString();
    }


    public String seriesCreate(final Request req, final Response res) {
        final CreateSeriesPage page = pulp.page().createSeriesPage();

        String name = req.queryParams("seriesname");
        if(name==null || name.trim().isEmpty()){
            res.status(400);

            page.setOutput("<h2>Error: Could not find a name to add</h2>");
            return page.asHTMLString();
        }

        try{

            final PulpSeries series = pulp.books().series().add(name);
            if(series==null){
                page.setOutput(String.format("<h2>Error Adding Series %s</h2>", name));
            }else {
                name = pulp.reports(pulp.reports().getReportConfig().setPostFixPath("/list/navigation")).
                        bookReporter().getSeriesReporter().getSeries(series);
                page.setOutput(String.format("<h2>Added Series %s</h2>", name));
            }
        }catch(Exception e){
            page.setOutput(String.format("<h2>%s</h2><p>%s</p>",e.getMessage(), SparkUtils.getMyStackTrace(e)));
        }


        return page.asHTMLString();
    }

    public String publisherCreate(final Request req, final Response res) {
        final CreatePublisherPage page = pulp.page().createPublisherPage();

        String name = req.queryParams("publishername");
        if(name==null || name.trim().isEmpty()){
            res.status(400);

            page.setOutput("<h2>Error: Could not find a name to add</h2>");
            return page.asHTMLString();
        }

        try{
            final PulpPublisher publisher = pulp.books().publishers().add(name);

            if(publisher==null){
                page.setOutput(String.format("<h2>Error Adding Publisher %s</h2>", name));
            }else {
                name = pulp.reports(pulp.reports().getReportConfig().setPostFixPath("/list/navigation")).
                        bookReporter().getPublisherReporter().getPublisher(publisher);
                page.setOutput(String.format("<h2>Added Publisher %s</h2>", name));
            }
        }catch(Exception e){
            page.setOutput(String.format("<h2>%s</h2><p>%s</p>",e.getMessage(), SparkUtils.getMyStackTrace(e)));
        }


        return page.asHTMLString();
    }

    public String bookCreate(final Request req, final Response res) {
        final CreateBookPage page = pulp.page().createBookPage();

        String title = req.queryParams("title");
        if(title==null || title.trim().isEmpty()){
            res.status(400);

            page.setOutput("<h2>Error: Could not find a title to add</h2>");
            return page.asHTMLString();
        }

        String authorid = req.queryParams("authorid");
        if(authorid==null || authorid.trim().isEmpty()){
            res.status(400);

            page.setOutput("<h2>Error: Could not find an Author to add</h2>");
            return page.asHTMLString();
        }

        String seriesid = req.queryParams("seriesid");
        if(seriesid==null || seriesid.trim().isEmpty()){
            res.status(400);

            page.setOutput("<h2>Error: Could not find a Series to add</h2>");
            return page.asHTMLString();
        }

        String publisherid = req.queryParams("publisherid");
        if(publisherid==null || publisherid.trim().isEmpty()){
            res.status(400);

            page.setOutput("<h2>Error: Could not find a Publisher to add</h2>");
            return page.asHTMLString();
        }

        //seriesidentifier
        String seriesidentifier = req.queryParams("seriesidentifier");
        if(seriesidentifier==null || seriesidentifier.trim().isEmpty()){
            res.status(400);

            page.setOutput("<h2>Error: Could not find a Series Identifier to add</h2>");
            return page.asHTMLString();
        }

        String yearofpublication = req.queryParams("yearofpub");

        if(yearofpublication==null || yearofpublication.trim().isEmpty()){
            res.status(400);

            page.setOutput("<h2>Error: Could not find a Year to add</h2>");
            return page.asHTMLString();
        }

        int year = -1;

        try {
            year = Integer.parseInt(yearofpublication);
        }catch(Exception e){

        }

        if(year < 0){
            res.status(400);

            page.setOutput("<h2>Error: Could not find a valid year to add</h2>");
            return page.asHTMLString();
        }

        try {
            final PulpBook book = pulp.books().books().add(seriesid, authorid, authorid, title, seriesidentifier, year, publisherid);

            if (book == null) {
                page.setOutput(String.format("<h2>Error Adding Book %s</h2>", title));
            } else {
                String settitle = pulp.reports(pulp.reports().getReportConfig().setPostFixPath("/list/navigation")).
                        bookReporter().getTitle(book);
                page.setOutput(String.format("<h2>Added Book %s</h2>", settitle));
            }
        }catch(Exception e){
            page.setOutput(String.format("<h2>%s</h2><p>%s</p>",e.getMessage(), SparkUtils.getMyStackTrace(e)));
        }

        return page.asHTMLString();
    }
}
