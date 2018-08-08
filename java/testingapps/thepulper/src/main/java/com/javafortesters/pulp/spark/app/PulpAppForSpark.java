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


        get("/apps/pulp/gui/view/author", (req, res) -> {
            return pulp.page().viewAuthorPage(req.queryParams("author")).asHTMLString();
        });

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

        post("/apps/pulp/gui/amend/deleteauthor", (req, res) -> {

            pulp.books().deleteAuthor(req.queryParams("authorid"));
            res.redirect("/apps/pulp/gui/reports/authors/list/navigation");
            return "";
        });

        get("/apps/pulp/gui/view/series", (req, res) -> {
            return pulp.page().viewSeriesPage(req.queryParams("series")).asHTMLString();
        });

        get("/apps/pulp/gui/create/series", (req, res) -> {
            return pulp.page().createSeriesPage().asHTMLString();
        });

        post("/apps/pulp/gui/create/series", (req, res) -> {
            return new CreateFlowsHandler(pulp).seriesCreate(req, res);
        });

        ///apps/pulp/gui/amend/series?series=id
        get("/apps/pulp/gui/amend/series", (req, res) -> {
            return pulp.page().amendSeriesPage(req.queryParams("series")).asHTMLString();
        });

        post("/apps/pulp/gui/amend/series", (req, res) -> {
            return new AmendFlowsHandler(pulp).seriesAmend(req, res);
        });

        post("/apps/pulp/gui/amend/deleteseries", (req, res) -> {
            pulp.books().deleteSeries(req.queryParams("seriesid"));
            res.redirect("/apps/pulp/gui/reports/series/list/navigation");
            return "";
        });

        get("/apps/pulp/gui/view/publisher", (req, res) -> {
            return pulp.page().viewPublisherPage(req.queryParams("publisher")).asHTMLString();
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

        post("/apps/pulp/gui/amend/deletepublisher", (req, res) -> {
            pulp.books().deletePublisher(req.queryParams("publisherid"));
            res.redirect("/apps/pulp/gui/reports/publishers/list/navigation");
            return "";
        });

        get("/apps/pulp/gui/view/book", (req, res) -> {
            return pulp.page().viewBookPage(req.queryParams("book")).asHTMLString();
        });


        get("/apps/pulp/gui/create/book", (req, res) -> {
            return pulp.page().createBookPage().asHTMLString();
        });

        post("/apps/pulp/gui/create/book", (req, res) -> {
            return new CreateFlowsHandler(pulp).bookCreate(req, res);
        });

        ///apps/pulp/gui/amend/book?book=id
        get("/apps/pulp/gui/amend/book", (req, res) -> {
            return pulp.page().amendBookPage(req.queryParams("book")).asHTMLString();
        });

        post("/apps/pulp/gui/amend/book", (req, res) -> {
            return new AmendFlowsHandler(pulp).bookAmend(req, res);
        });

        post("/apps/pulp/gui/amend/deletebook", (req, res) -> {

            pulp.books().deleteBook(req.queryParams("bookid"));
            res.redirect("/apps/pulp/gui/reports/books/table/navigation");
            return "";
        });

        get("/apps/pulp/gui/reports/books/list/navigation", (req, res) -> {

            BookFilter filter = BookFilterFromQueryMap.getBookFilter(req.queryMap());
            final ReportConfig config = new ReportConfig(pulp.reports().getReportConfig());

            if(req.queryMap().hasKeys() && req.queryMap().value("faqs")!=null){
                config.setIncludeFaqLinks(true);
            }else{
                config.setIncludeFaqLinks(false);
            }

            config.showAmendLinks(false);
            config.showBookAmendLink(true);

            // TODO if filtered at all then show authors, publishers, series and years as links

            config.setTitlesAreLinks(true);
            return pulp.reports(config).configurePostFixPath("/list/navigation").getBooksAsHtmlList(filter);
        });

        get("/apps/pulp/gui/reports/books/list/navigation/faqs", (req, res) -> {

            BookFilter filter = BookFilterFromQueryMap.getBookFilter(req.queryMap());
            final ReportConfig config = new ReportConfig(pulp.reports().getReportConfig());

            config.setIncludeFaqLinks(true);
            config.showAmendLinks(false);
            config.setTitlesAreLinks(false);
            config.allowDelete(false);

            return pulp.reports(config).configurePostFixPath("/list/navigation").getBooksAsHtmlList(filter);
        });

        get("/apps/pulp/gui/reports/books/list/static", (req, res) -> {
            BookFilter filter = BookFilterFromQueryMap.getBookFilter(req.queryMap());
            return pulp.stringReports().getBooksAsHtmlList(filter);
        });

        get("/apps/pulp/gui/reports/books/table/navigation", (req, res) -> {
            BookFilter filter = BookFilterFromQueryMap.getBookFilter(req.queryMap());

            final ReportConfig config = new ReportConfig(pulp.reports().getReportConfig());

            config.showAmendLinks(false);
            if(pulp.getAppVersionInt()==1) {
                config.showBookAmendLink(true);
                // table has enough space to allow delete button rendering
                // config.setAllowDeleteBook(true);
            }

            config.setTitlesAreLinks(true);
            config.allowDelete(false);

            return pulp.reports(config).configurePostFixPath("/table/navigation").getBooksAsHtmlTable(filter);
        });

        get("/apps/pulp/gui/reports/books/table/static", (req, res) -> {
            BookFilter filter = BookFilterFromQueryMap.getBookFilter(req.queryMap());
            return pulp.stringReports().getBooksAsHtmlTable(filter);
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

            return pulp.page().getFaqRenderPage(typeOfFaq, forTerm, showiframe).asHTMLString();

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

            ReportConfig config = new ReportConfig(pulp.reports().getReportConfig());
            config.setPostFixPath("/list/navigation");
            config.showAmendLinks(false);
            config.allowDelete(false);
            config.setTitlesAreLinks(true);
            config.setYearsAsLinks(true);
            config.setSeriesNamesLinks(true);
            config.setPublisherNamesLinks(true);
            config.setAuthorNamesLinks(true);

            // TODO should be able to configure links to author, series, publisher to the Amend screen rather than a list filter

            return pulp.page().getAlertSearchPage().setConfirmSearch(confirmSearch)
                    .setSearchTerms(what, how, forTerm)
                    .asHTMLString(config);

        });

        get("/apps/pulp/gui/admin/filtertest", (req, res) -> {

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
            return pulp.page().getFilterTestPage(isList, navigation, canSearch, isPaginated).setFilter(filter).setShowData(req.queryMap().hasKeys()).
                    setShowThese(templateElements).
                    asHTMLString();
        });

        get("/apps/pulp/gui/admin/version/*", (req, res) -> {

            //request.splat()[0] is version
            try {
                int version = Integer.parseInt(req.splat()[0]);
                pulp.setAppVersion(version);
            }catch(Exception e){
                res.status(400);
                // todo need an error page, handle 404, exceptions etc.
                return "<h1>What version was that?</h1>";
            }

            res.redirect("/apps/pulp/gui");
            return "";

        });

        get("/apps/pulp/gui/reports/authors/list/navigation", (req, res) -> {

            final ReportConfig config = new ReportConfig(pulp.reports().getReportConfig());

            if(req.queryMap().hasKeys() && req.queryMap().value("faqs")!=null){
                config.setIncludeFaqLinks(true);
            }else{
                config.setIncludeFaqLinks(false);
            }


            config.showAmendLinks(true);
            config.setAuthorNamesLinks(true);
            config.allowDelete(false);

            // possibly if filtered then allow delete tec.
            // config.setAllowDeleteAuthor(true);


            return pulp.reports(config).configurePostFixPath("/list/navigation").getAuthorsAsHtmlList();
        });

        get("/apps/pulp/gui/reports/authors/list/navigation/faqs", (req, res) -> {

            final ReportConfig config = new ReportConfig(pulp.reports().getReportConfig());

            config.setIncludeFaqLinks(true);
            config.showAmendLinks(false);
            config.setTitlesAreLinks(false);

            return pulp.reports(config).configurePostFixPath("/list/navigation").getAuthorsAsHtmlList();
        });

        get("/apps/pulp/gui/reports/publishers/list/navigation", (req, res) -> {

            final ReportConfig config = new ReportConfig(pulp.reports().getReportConfig());

            if(req.queryMap().hasKeys() && req.queryMap().value("faqs")!=null){
                config.setIncludeFaqLinks(true);
            }else{
                config.setIncludeFaqLinks(false);
            }

            config.showAmendLinks(true);
            config.setPublisherNamesLinks(true);
            config.allowDelete(false);

            if(pulp.getAppVersionInt()>=2){
            //    config.setAllowDeletePublisher(true);
            }


            return pulp.reports(config).configurePostFixPath("/list/navigation").getPublishersAsHtmlList();
        });

        get("/apps/pulp/gui/reports/publishers/list/navigation/faqs", (req, res) -> {

            final ReportConfig config = new ReportConfig(pulp.reports().getReportConfig());

            config.setIncludeFaqLinks(true);
            config.showAmendLinks(false);
            config.setTitlesAreLinks(false);

            return pulp.reports(config).configurePostFixPath("/list/navigation").getPublishersAsHtmlList();
        });

        get("/apps/pulp/gui/reports/years/list/navigation", (req, res) -> {

            final ReportConfig config = new ReportConfig(pulp.reports().getReportConfig());

            if(req.queryMap().hasKeys() && req.queryMap().value("faqs")!=null){
                config.setIncludeFaqLinks(true);
            }else{
                config.setIncludeFaqLinks(false);
            }

            config.showAmendLinks(false);
            config.setYearsAsLinks(true);

            return pulp.reports(config).configurePostFixPath("/list/navigation").getYearsAsHtmlList();
        });

        get("/apps/pulp/gui/reports/years/list/navigation/faqs", (req, res) -> {

            final ReportConfig config = new ReportConfig(pulp.reports().getReportConfig());

            config.setIncludeFaqLinks(true);
            config.showAmendLinks(false);
            config.setTitlesAreLinks(false);

            return pulp.reports(config).configurePostFixPath("/list/navigation").getYearsAsHtmlList();
        });

        get("/apps/pulp/gui/reports/series/list/navigation", (req, res) -> {
            final ReportConfig config = new ReportConfig(pulp.reports().getReportConfig());

            if(req.queryMap().hasKeys() && req.queryMap().value("faqs")!=null){
                config.setIncludeFaqLinks(true);
            }else{
                config.setIncludeFaqLinks(false);
            }

            config.showAmendLinks(true);
            config.setSeriesNamesLinks(true);
            config.allowDelete(false);

            if(pulp.getAppVersionInt()>=2){
            //    config.setAllowDeleteSeries(true);
            }


            return pulp.reports(config).configurePostFixPath("/list/navigation").getSeriesNamesAsHtmlList();
        });

        get("/apps/pulp/gui/reports/series/list/navigation/faqs", (req, res) -> {

            final ReportConfig config = new ReportConfig(pulp.reports().getReportConfig());

            config.setIncludeFaqLinks(true);
            config.showAmendLinks(false);

            config.setTitlesAreLinks(false);
            return pulp.reports(config).configurePostFixPath("/list/navigation").getSeriesNamesAsHtmlList();
        });

        get("/apps/pulp/gui/reports/authors/list/static", (req, res) -> { return pulp.stringReports().getAuthorsAsHtmlList();});
        get("/apps/pulp/gui/reports/publishers/list/static", (req, res) -> { return pulp.stringReports().getPublishersAsHtmlList();});
        get("/apps/pulp/gui/reports/years/list/static", (req, res) -> { return pulp.stringReports().getYearsAsHtmlList();});
        get("/apps/pulp/gui/reports/series/list/static", (req, res) -> { return pulp.stringReports().getSeriesNamesAsHtmlList();});


        get("/apps/pulp/", (req, res) -> { return pulp.reports().getIndexPage();});
        get("/apps/pulp", (req, res) -> { res.redirect("/apps/pulp/"); return "";});
        get("/apps/pulp/gui", (req, res) -> { res.redirect("/apps/pulp/gui/"); return "";});
        get("/apps/pulp/gui/", (req, res) -> { return pulp.reports().getIndexPage();});
        get("/apps/pulp/gui/help", (req, res) -> { return pulp.reports().getHelpPage();});
        get("/apps/pulp/gui/menu/books", (req, res) -> { return pulp.reports().getSnippetPage("Books Menu", "menu-screen-books-reports-list.html");});
        get("/apps/pulp/gui/menu/authors", (req, res) -> { return pulp.reports().getSnippetPage("Authors Menu", "menu-screen-authors-reports-list.html");});
        get("/apps/pulp/gui/menu/publishers", (req, res) -> { return pulp.reports().getSnippetPage("Publishers Menu", "menu-screen-publishers-reports-list.html");});
        get("/apps/pulp/gui/menu/series", (req, res) -> { return pulp.reports().getSnippetPage("Series Menu", "menu-screen-series-reports-list.html");});
        get("/apps/pulp/gui/menu/years", (req, res) -> { return pulp.reports().getSnippetPage("Years Menu", "menu-screen-years-reports-list.html");});
        get("/apps/pulp/gui/menu/create", (req, res) -> { return pulp.reports().getSnippetPage("Create Menu", "menu-screen-create.html");});
        get("/apps/pulp/gui/menu/reports", (req, res) -> { return pulp.reports().getSnippetPage("Reports Menu", "menu-screen-static-reports.html");});
        get("/apps/pulp/gui/menu/admin", (req, res) -> { return pulp.reports().getSnippetPage("Admin Menu", "menu-screen-admin.html");});


        get("/apps/pulp/gui/reports", (req, res) -> { res.redirect("/apps/pulp/gui/reports/"); return "";});
        get("/apps/pulp/gui/reports/", (req, res) -> { return pulp.reports().getIndexPage();});
        get("/apps/pulp/gui/reports/books", (req, res) -> { res.redirect("/apps/pulp/gui/reports/"); return "";});
    }




}
