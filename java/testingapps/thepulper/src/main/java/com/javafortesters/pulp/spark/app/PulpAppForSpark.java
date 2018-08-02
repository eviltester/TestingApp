package com.javafortesters.pulp.spark.app;

import com.javafortesters.pulp.PulpApp;
import com.javafortesters.pulp.html.gui.AlertSearchPage;
import com.javafortesters.pulp.html.gui.FaqRenderPage;
import com.javafortesters.pulp.html.gui.FilterTestPage;
import com.javafortesters.pulp.reader.forseries.SavageReader;
import com.javafortesters.pulp.reader.forseries.SpiderReader;
import com.javafortesters.pulp.reader.forseries.TheAvengerReader;
import com.javafortesters.pulp.reporting.ReportConfig;
import com.javafortesters.pulp.reporting.filtering.BookFilter;


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

        post("/apps/pulp/gui/create/author", (req, res) -> {
            return new CreateFlowsHandler(pulp).authorCreate(req, res);
        });

        ///apps/pulp/gui/amend/author?author=id
        get("/apps/pulp/gui/amend/author", (req, res) -> {
            return pulp.page().amendAuthorPage(req.queryParams("author")).asHTMLString();
        });

        post("/apps/pulp/gui/amend/author", (req, res) -> {
            return new AmendFlowsHandler(pulp).authorAmend(req, res);
        });

        get("/apps/pulp/gui/create/series", (req, res) -> {
            return pulp.page().createSeriesPage().asHTMLString();
        });

        post("/apps/pulp/gui/create/series", (req, res) -> {
            return new CreateFlowsHandler(pulp).seriesCreate(req, res);
        });

        ///apps/pulp/gui/amend/author?author=id
        get("/apps/pulp/gui/amend/series", (req, res) -> {
            return pulp.page().amendSeriesPage(req.queryParams("series")).asHTMLString();
        });

        post("/apps/pulp/gui/amend/series", (req, res) -> {
            return new AmendFlowsHandler(pulp).seriesAmend(req, res);
        });

        get("/apps/pulp/gui/create/publisher", (req, res) -> {
            return pulp.page().createPublisherPage().asHTMLString();
        });

        post("/apps/pulp/gui/create/publisher", (req, res) -> {
            return new CreateFlowsHandler(pulp).publisherCreate(req, res);
        });

        ///apps/pulp/gui/amend/publisher?publisher=id
        get("/apps/pulp/gui/amend/publisher", (req, res) -> {
            return pulp.page().amendPublisherPage(req.queryParams("publisher")).asHTMLString();
        });

        post("/apps/pulp/gui/amend/publisher", (req, res) -> {
            return new AmendFlowsHandler(pulp).publisherAmend(req, res);
        });


        get("/apps/pulp/gui/create/book", (req, res) -> {
            return pulp.page().createBookPage().asHTMLString();
        });

        post("/apps/pulp/gui/create/book", (req, res) -> {
            return new CreateFlowsHandler(pulp).bookCreate(req, res);
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




}
