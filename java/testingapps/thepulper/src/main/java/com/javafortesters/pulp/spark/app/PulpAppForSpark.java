package com.javafortesters.pulp.spark.app;

import com.javafortesters.pulp.PulpApp;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpBook;
import com.javafortesters.pulp.domain.objects.PulpPublisher;
import com.javafortesters.pulp.domain.objects.PulpSeries;
import com.javafortesters.pulp.html.gui.AlertSearchPage;
import com.javafortesters.pulp.html.gui.FaqRenderPage;
import com.javafortesters.pulp.html.gui.FilterTestPage;
import com.javafortesters.pulp.html.gui.entitycrud.createPages.CreateAuthorPage;
import com.javafortesters.pulp.html.gui.entitycrud.createPages.CreateBookPage;
import com.javafortesters.pulp.html.gui.entitycrud.createPages.CreateSeriesPage;
import com.javafortesters.pulp.html.gui.entitycrud.createPages.CreatePublisherPage;
import com.javafortesters.pulp.html.gui.entitycrud.updatePages.AmendAuthorPage;
import com.javafortesters.pulp.html.gui.entitycrud.updatePages.AmendSeriesPage;
import com.javafortesters.pulp.reader.forseries.SavageReader;
import com.javafortesters.pulp.reader.forseries.SpiderReader;
import com.javafortesters.pulp.reader.forseries.TheAvengerReader;
import com.javafortesters.pulp.reporting.ReportConfig;
import com.javafortesters.pulp.reporting.filtering.BookFilter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import static spark.Spark.get;
import static spark.Spark.post;


public class PulpAppForSpark {

    PulpApp pulp = new PulpApp();


    public PulpAppForSpark(){
        // pulp app

        pulp.readData(new SavageReader("/data/pulp/doc_savage.csv"));
        pulp.readData(new SpiderReader("/data/pulp/the_spider.csv"));
        pulp.readData(new TheAvengerReader("/data/pulp/the_avenger.csv"));

        pulp.reports().configure(ReportConfig.allHTML("/apps/pulp/gui/reports/"));

        get("/apps/pulp/gui/create/author", (req, res) -> {
            return pulp.page().createAuthorPage().asHTMLString();
        });

        // TODO: clearly this needs validation and refactoring
        post("/apps/pulp/gui/create/author", (req, res) -> {

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
                    page.setOutput(String.format("<h2>Error Adding Author %s</h2>", name));
                }else {
                    name = pulp.reports(pulp.reports().getReportConfig().setPostFixPath("/list/navigation")).
                            bookReporter().getAuthorReporter().getAuthorName(author);
                    page.setOutput(String.format("<h2>Added Author %s</h2>", name));
                }
            }catch(Exception e){
                page.setOutput(String.format("<h2>%s</h2><p>%s</p>",e.getMessage(), getMyStackTrace(e)));
            }


            return page.asHTMLString();
        });

        ///apps/pulp/gui/amend/author?author=id
        get("/apps/pulp/gui/amend/author", (req, res) -> {
            return pulp.page().amendAuthorPage(req.queryParams("author")).asHTMLString();
        });

        post("/apps/pulp/gui/amend/author", (req, res) -> {

            // TODO move this out of the spark page processing
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
        });

        get("/apps/pulp/gui/create/series", (req, res) -> {
            return pulp.page().createSeriesPage().asHTMLString();
        });

        // TODO: clearly this needs validation and refactoring
        post("/apps/pulp/gui/create/series", (req, res) -> {

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
                page.setOutput(String.format("<h2>%s</h2><p>%s</p>",e.getMessage(), getMyStackTrace(e)));
            }


        return page.asHTMLString();
        });

        ///apps/pulp/gui/amend/author?author=id
        get("/apps/pulp/gui/amend/series", (req, res) -> {
            return pulp.page().amendSeriesPage(req.queryParams("series")).asHTMLString();
        });

        post("/apps/pulp/gui/amend/series", (req, res) -> {

            // TODO move this out of the spark page processing
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
        });



        get("/apps/pulp/gui/create/publisher", (req, res) -> {
            return pulp.page().createPublisherPage().asHTMLString();
        });

        // TODO: clearly this needs validation and refactoring
        post("/apps/pulp/gui/create/publisher", (req, res) -> {

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
                page.setOutput(String.format("<h2>%s</h2><p>%s</p>",e.getMessage(), getMyStackTrace(e)));
            }


        return page.asHTMLString();

        });


        get("/apps/pulp/gui/create/book", (req, res) -> {
            return pulp.page().createBookPage().asHTMLString();
        });

        // TODO: clearly this needs validation and refactoring
        post("/apps/pulp/gui/create/book", (req, res) -> {

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
                page.setOutput(String.format("<h2>%s</h2><p>%s</p>",e.getMessage(), getMyStackTrace(e)));
            }

            return page.asHTMLString();
        });



        get("/apps/pulp/gui/reports/books/list/navigation", (req, res) -> {
            BookFilter filter = BookFilterFromQueryMap.getBookFilter(req.queryMap());
            return pulp.reports().configurePostFixPath("/list/navigation").getBooksAsHtmlList(filter);
        });
        get("/apps/pulp/gui/reports/books/list/static", (req, res) -> {
            BookFilter filter = BookFilterFromQueryMap.getBookFilter(req.queryMap());
            return pulp.reports(ReportConfig.justStrings()).getBooksAsHtmlList(filter);
        });
        get("/apps/pulp/gui/reports/books/table/navigation", (req, res) -> {
            BookFilter filter = BookFilterFromQueryMap.getBookFilter(req.queryMap());
            return pulp.reports().configurePostFixPath("/table/navigation").getBooksAsHtmlTable(filter);
        });
        get("/apps/pulp/gui/reports/books/table/static", (req, res) -> {
            BookFilter filter = BookFilterFromQueryMap.getBookFilter(req.queryMap());
            return pulp.reports(ReportConfig.justStrings()).getBooksAsHtmlTable(filter);
        });

        get("/apps/pulp/gui/reports/:type/faqs", (req, res) -> {

            String typeOfFaq = req.params(":type");
            String forTerm="";
            boolean showiframe=false;

            if(req.queryMap().hasKeys() && req.queryMap().value("searchterm")!=null){
                forTerm=req.queryMap().value("searchterm");
            }
            if(req.queryMap().hasKeys() && req.queryMap().value("iframe")!=null){
                showiframe=true;
            }
            return new FaqRenderPage(typeOfFaq, forTerm, showiframe).asHTMLString();
        });


        get("/apps/pulp/gui/reports/books/search", (req, res) -> {
            String how= "";
            String what="";
            String forTerm="";
            String source="";
            boolean confirmSearch=false;

            if(req.queryMap().hasKeys() && req.queryMap().value("searchterm")!=null){
                forTerm=req.queryMap().value("searchterm");
            }
            if(req.queryMap().hasKeys() && req.queryMap().value("what")!=null){
                what=req.queryMap().value("what");
            }
            if(req.queryMap().hasKeys() && req.queryMap().value("how")!=null){
                how=req.queryMap().value("how");
            }
            if(req.queryMap().hasKeys() && req.queryMap().value("mode")!=null){
                source=req.queryMap().value("mode");
            }
            // confirmsearchcheck=on (or missing if not checked)
            if(source.length()==0){
                confirmSearch=true;
            }
            if(source.length()>0 && req.queryMap().hasKeys() && req.queryMap().value("confirmsearchcheck")!=null){
                confirmSearch=true;
            }

            return new AlertSearchPage().setConfirmSearch(confirmSearch)
                    .setSearchTerms(what, how, forTerm)
                    .setDataFrom(pulp.books())
                    .asHTMLString();

        });

        get("/apps/pulp/gui/reports/filtertest", (req, res) -> {

            String listOrTable="list";
            String navigationOrStatic="navigation";
            boolean canSearch=false;
            boolean isPaginated=false;

            String[] templateElements = {};

            if(req.queryMap().hasKeys() && req.queryMap().value("style")!=null){
                listOrTable=req.queryMap().value("style");
            }
            if(req.queryMap().hasKeys() && req.queryMap().value("navigation")!=null){
                navigationOrStatic=req.queryMap().value("navigation");
            }
            if(req.queryMap().hasKeys() && req.queryMap().value("search")!=null){
                canSearch=true; //req.queryMap().value("search");
            }
            if(req.queryMap().hasKeys() && req.queryMap().value("paginate")!=null){
                isPaginated=true; //req.queryMap().value("search");
            }
            if(req.queryMap().hasKeys() && req.queryMap().value("template")!=null){
                templateElements = req.queryMap().toMap().get("template");
            }


            boolean isList = listOrTable.equalsIgnoreCase("list");
            boolean navigation = navigationOrStatic.equalsIgnoreCase("navigation");

            BookFilter filter = BookFilterFromQueryMap.getBookFilter(req.queryMap());
            return new FilterTestPage(isList, navigation, canSearch, isPaginated).
                    setFilter(filter).setData(pulp.books()).
                    setShowData(req.queryMap().hasKeys()).
                    setShowThese(templateElements).
                    asHTMLString();
        });

        get("/apps/pulp/gui/reports/authors/list/navigation", (req, res) -> {
            boolean includeFaqs = false;
            if(req.queryMap().hasKeys() && req.queryMap().value("faqs")!=null){
                includeFaqs=true;
            }
            return pulp.reports().configurePostFixPath("/list/navigation").getAuthorsAsHtmlList(includeFaqs);
        });

        get("/apps/pulp/gui/reports/publishers/list/navigation", (req, res) -> { return pulp.reports().configurePostFixPath("/list/navigation").getPublishersAsHtmlList();});
        get("/apps/pulp/gui/reports/years/list/navigation", (req, res) -> { return pulp.reports().configurePostFixPath("/list/navigation").getYearsAsHtmlList();});
        get("/apps/pulp/gui/reports/series/list/navigation", (req, res) -> { return pulp.reports().configurePostFixPath("/list/navigation").getSeriesNamesAsHtmlList();});

        get("/apps/pulp/gui/reports/authors/list/static", (req, res) -> { return pulp.reports(ReportConfig.justStrings()).getAuthorsAsHtmlList(false);});
        get("/apps/pulp/gui/reports/publishers/list/static", (req, res) -> { return pulp.reports(ReportConfig.justStrings()).getPublishersAsHtmlList();});
        get("/apps/pulp/gui/reports/years/list/static", (req, res) -> { return pulp.reports(ReportConfig.justStrings()).getYearsAsHtmlList();});
        get("/apps/pulp/gui/reports/series/list/static", (req, res) -> { return pulp.reports(ReportConfig.justStrings()).getSeriesNamesAsHtmlList();});


        get("/apps/pulp/", (req, res) -> { return pulp.reports().getIndexPage();});
        get("/apps/pulp", (req, res) -> { res.redirect("/apps/pulp/"); return "";});
        get("/apps/pulp/gui", (req, res) -> { res.redirect("/apps/pulp/gui/"); return "";});
        get("/apps/pulp/gui/", (req, res) -> { return pulp.reports().getIndexPage();});
        get("/apps/pulp/gui/reports", (req, res) -> { res.redirect("/apps/pulp/gui/reports/"); return "";});
        get("/apps/pulp/gui/reports/", (req, res) -> { return pulp.reports().getIndexPage();});
        get("/apps/pulp/gui/reports/books", (req, res) -> { res.redirect("/apps/pulp/gui/reports/"); return "";});
    }

    private String getMyStackTrace(final Exception e) {
            Writer result = new StringWriter();
            PrintWriter printWriter = new PrintWriter(result);
            e.printStackTrace(printWriter);
            return result.toString();
    }


}
