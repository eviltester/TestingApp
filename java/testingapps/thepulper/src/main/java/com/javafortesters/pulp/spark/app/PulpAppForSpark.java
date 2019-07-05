package com.javafortesters.pulp.spark.app;

import com.javafortesters.pulp.PulpApp;
import com.javafortesters.pulp.api.EntityResponse;
import com.javafortesters.pulp.reader.forseries.SavageReader;
import com.javafortesters.pulp.reader.forseries.SpiderReader;
import com.javafortesters.pulp.reader.forseries.TheAvengerReader;
import com.javafortesters.pulp.reporting.ReportConfig;
import com.javafortesters.pulp.reporting.filtering.BookFilter;
import com.javafortesters.pulp.spark.app.crudhandling.AmendFlowsHandler;
import com.javafortesters.pulp.spark.app.crudhandling.CreateFlowsHandler;
import spark.Request;
import spark.Response;
import spark.Session;


import static com.javafortesters.pulp.spark.app.versioning.AppVersionSettings.AMEND_LINKS_SHOWN_IN_LIST;
import static com.javafortesters.pulp.spark.app.versioning.AppVersionSettings.DELETE_LINKS_SHOWN_IN_LIST;
import static spark.Spark.*;


public class PulpAppForSpark {


    // TODO support multiple apps each with a different session

    private static final String SESSION_APP = "sessionPulpApp";
    private static final int MAX_SESSION_LENGTH = 60*5;  // set max session without interactivity to 5 minutes
    private boolean allowsShutdown=false;


    public PulpApp getPulpApp(Request req){

        PulpApp sessionPulpApp;
        // create a session based set of app data that is deleted after 5 minutes of inactivity
        Session session = req.session(false);

        if (session == null) {
            // user does not have a session create it
            session = req.session();
            session.maxInactiveInterval(MAX_SESSION_LENGTH);

            sessionPulpApp = new PulpApp();
            sessionPulpApp.getAppVersion().willAllowShutdown(this.allowsShutdown);
            sessionPulpApp.readData(new SavageReader("/data/pulp/doc_savage.csv"));
            sessionPulpApp.readData(new SpiderReader("/data/pulp/the_spider.csv"));
            sessionPulpApp.readData(new TheAvengerReader("/data/pulp/the_avenger.csv"));

            session.attribute(SESSION_APP, sessionPulpApp);


        } else {
            sessionPulpApp = session.attribute(SESSION_APP);
        }

        return sessionPulpApp;
    }

    public PulpAppForSpark() {
        // by default do not allow shutdown
        this(false);
    }

    public String apiEntityResponse(Response res, final EntityResponse response){
        res.status(response.getStatusCode());
        res.header("ContentType", response.getContentType());
        if(response.isError()){
            return response.getErrorMessage();
        }else{
            return response.getResponseBody();
        }
    }

    private String apiEmptyEntityResponse(final Response res, final EntityResponse response) {
        apiEntityResponse(res, response);
        return "";
    }


    public PulpAppForSpark(boolean allowsShutdown){

        this.allowsShutdown = allowsShutdown;

        //
        // BASIC API
        //
        final EntityResponse notAllowed = new EntityResponse().setErrorStatus(405, "Not allowed, sorry");
        final EntityResponse unknown = new EntityResponse().setErrorStatus(404, "Unknown API EndPoint");


        path("/apps/pulp/api/authors/:authorid", () -> {

            head("", (req, res) -> {
                final EntityResponse response = getPulpApp(req).entities().getAuthor(req.params(":authorid"), req.headers("Accept"));
                return apiEmptyEntityResponse(res, response);
            });

            get("", (req, res) -> {
                final EntityResponse response = getPulpApp(req).entities().getAuthor(req.params(":authorid"), req.headers("Accept"));
                return apiEntityResponse(res, response);
            });

            options("", (req, res) -> {
                res.header("Allow", "OPTIONS, GET, HEAD");
                return apiEntityResponse(res, new EntityResponse().setSuccessStatus(200, "{}"));
            });

            post("",     (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            put("",     (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            delete("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            trace("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            patch("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
        });

        path("/apps/pulp/api/authors", () -> {

            head("", (req, res) -> {
                final EntityResponse response = getPulpApp(req).entities().getAuthor(req.params(":authorid"), req.headers("Accept"));
                return apiEmptyEntityResponse(res, response);
            });

            get("", (req, res) -> {
                final EntityResponse response = getPulpApp(req).entities().getAuthors(req.headers("Accept"));
                return apiEntityResponse(res, response);
            });

            options("", (req, res) -> {
                res.header("Allow", "OPTIONS, GET, HEAD");
                return apiEntityResponse(res, new EntityResponse().setSuccessStatus(200, "{}"));
            });

            post("",     (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            put("",     (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            delete("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            trace("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            patch("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
        });



        // End API processing with unknown end points
        path("/apps/pulp/api/*", () -> {
            get("", (req, res) -> {  return apiEntityResponse(res, unknown);});
            post("", (req, res) -> {  return apiEntityResponse(res, unknown);});
            delete("", (req, res) -> {  return apiEntityResponse(res, unknown);});
            put("", (req, res) -> {  return apiEntityResponse(res, unknown);});
            trace("", (req, res) -> {  return apiEntityResponse(res, unknown);});
            options("", (req, res) -> {  return apiEntityResponse(res, unknown);});
            patch("", (req, res) -> {  return apiEntityResponse(res, unknown);});
            head("", (req, res) -> {  return apiEntityResponse(res, unknown);});
        });





        get("/apps/pulp/gui/view/author", (req, res) -> {
            return getPulpApp(req).page().viewAuthorPage(req.queryParams("author")).asHTMLString();
        });

        get("/apps/pulp/gui/create/author", (req, res) -> {
            return getPulpApp(req).page().createAuthorPage().asHTMLString();
        });

        post("/apps/pulp/gui/create/author", (req, res) -> {
            return new CreateFlowsHandler(getPulpApp(req)).authorCreate(req, res);
        });

        ///apps/pulp/gui/amend/author?author=id
        get("/apps/pulp/gui/amend/author", (req, res) -> {
            return getPulpApp(req).page().amendAuthorPage(req.queryParams("author")).asHTMLString();
        });

        post("/apps/pulp/gui/amend/author", (req, res) -> {
            return new AmendFlowsHandler(getPulpApp(req)).authorAmend(req, res);
        });

        post("/apps/pulp/gui/amend/deleteauthor", (req, res) -> {

            getPulpApp(req).books().deleteAuthor(req.queryParams("authorid"));
            res.redirect("/apps/pulp/gui/reports/authors/list/navigation");
            return "";
        });

        get("/apps/pulp/gui/view/series", (req, res) -> {
            return getPulpApp(req).page().viewSeriesPage(req.queryParams("series")).asHTMLString();
        });

        get("/apps/pulp/gui/create/series", (req, res) -> {
            return getPulpApp(req).page().createSeriesPage().asHTMLString();
        });

        post("/apps/pulp/gui/create/series", (req, res) -> {
            return new CreateFlowsHandler(getPulpApp(req)).seriesCreate(req, res);
        });

        ///apps/pulp/gui/amend/series?series=id
        get("/apps/pulp/gui/amend/series", (req, res) -> {
            return getPulpApp(req).page().amendSeriesPage(req.queryParams("series")).asHTMLString();
        });

        post("/apps/pulp/gui/amend/series", (req, res) -> {
            return new AmendFlowsHandler(getPulpApp(req)).seriesAmend(req, res);
        });

        post("/apps/pulp/gui/amend/deleteseries", (req, res) -> {
            getPulpApp(req).books().deleteSeries(req.queryParams("seriesid"));
            res.redirect("/apps/pulp/gui/reports/series/list/navigation");
            return "";
        });

        get("/apps/pulp/gui/view/publisher", (req, res) -> {
            return getPulpApp(req).page().viewPublisherPage(req.queryParams("publisher")).asHTMLString();
        });

        get("/apps/pulp/gui/create/publisher", (req, res) -> {
            return getPulpApp(req).page().createPublisherPage().asHTMLString();
        });

        post("/apps/pulp/gui/create/publisher", (req, res) -> {
            return new CreateFlowsHandler(getPulpApp(req)).publisherCreate(req, res);
        });

        ///apps/pulp/gui/amend/publisher?publisher=id
        get("/apps/pulp/gui/amend/publisher", (req, res) -> {
            return getPulpApp(req).page().amendPublisherPage(req.queryParams("publisher")).asHTMLString();
        });

        post("/apps/pulp/gui/amend/publisher", (req, res) -> {
            return new AmendFlowsHandler(getPulpApp(req)).publisherAmend(req, res);
        });

        post("/apps/pulp/gui/amend/deletepublisher", (req, res) -> {
            getPulpApp(req).books().deletePublisher(req.queryParams("publisherid"));
            res.redirect("/apps/pulp/gui/reports/publishers/list/navigation");
            return "";
        });

        get("/apps/pulp/gui/view/book", (req, res) -> {
            return getPulpApp(req).page().viewBookPage(req.queryParams("book")).asHTMLString();
        });


        get("/apps/pulp/gui/create/book", (req, res) -> {
            return getPulpApp(req).page().createBookPage().asHTMLString();
        });

        post("/apps/pulp/gui/create/book", (req, res) -> {
            return new CreateFlowsHandler(getPulpApp(req)).bookCreate(req, res);
        });

        ///apps/pulp/gui/amend/book?book=id
        get("/apps/pulp/gui/amend/book", (req, res) -> {
            return getPulpApp(req).page().amendBookPage(req.queryParams("book")).asHTMLString();
        });

        post("/apps/pulp/gui/amend/book", (req, res) -> {
            return new AmendFlowsHandler(getPulpApp(req)).bookAmend(req, res);
        });

        post("/apps/pulp/gui/amend/deletebook", (req, res) -> {

            getPulpApp(req).books().deleteBook(req.queryParams("bookid"));
            res.redirect("/apps/pulp/gui/reports/books/table/navigation");
            return "";
        });

        get("/apps/pulp/gui/reports/books/list/navigation", (req, res) -> {

            BookFilter filter = BookFilterFromQueryMap.getBookFilter(req.queryMap());
            final ReportConfig config = new ReportConfig(getPulpApp(req).reports().getReportConfig());

            if(req.queryMap().hasKeys() && req.queryMap().value("faqs")!=null){
                config.setIncludeFaqLinks(true);
            }else{
                config.setIncludeFaqLinks(false);
            }

            config.showAmendLinks(false);
            config.allowDelete(false);

            if(getPulpApp(req).getAppVersion().are(AMEND_LINKS_SHOWN_IN_LIST)) {
                config.showBookAmendLink(true);
            }
            if(getPulpApp(req).getAppVersion().are(DELETE_LINKS_SHOWN_IN_LIST)){
                // list doesn't have enough room - only show in table
                // config.setAllowDeleteBook(true);
            }


            // TODO if filtered at all then show authors, publishers, series and years as links

            config.setTitlesAreLinks(true);

            return getPulpApp(req).reports(config).configurePostFixPath("/list/navigation").getBooksAsHtmlList(filter);
        });

        get("/apps/pulp/gui/reports/books/list/navigation/faqs", (req, res) -> {

            BookFilter filter = BookFilterFromQueryMap.getBookFilter(req.queryMap());
            final ReportConfig config = new ReportConfig(getPulpApp(req).reports().getReportConfig());

            config.setIncludeFaqLinks(true);
            config.showAmendLinks(false);
            config.setTitlesAreLinks(false);
            config.allowDelete(false);

            return getPulpApp(req).reports(config).configurePostFixPath("/list/navigation").getBooksAsHtmlList(filter);
        });

        get("/apps/pulp/gui/reports/books/list/static", (req, res) -> {
            BookFilter filter = BookFilterFromQueryMap.getBookFilter(req.queryMap());
            return getPulpApp(req).stringReports().getBooksAsHtmlList(filter);
        });

        get("/apps/pulp/gui/reports/books/table/navigation", (req, res) -> {
            BookFilter filter = BookFilterFromQueryMap.getBookFilter(req.queryMap());

            final ReportConfig config = new ReportConfig(getPulpApp(req).reports().getReportConfig());

            config.showAmendLinks(false);

            if(getPulpApp(req).getAppVersion().are(AMEND_LINKS_SHOWN_IN_LIST)){
                config.showBookAmendLink(true);
            }
            if(getPulpApp(req).getAppVersion().are(DELETE_LINKS_SHOWN_IN_LIST)){
                // table has enough space to allow delete button rendering
                config.setAllowDeleteBook(true);
            }

            config.setTitlesAreLinks(true);
            config.allowDelete(false);

            return getPulpApp(req).reports(config).configurePostFixPath("/table/navigation").getBooksAsHtmlTable(filter);
        });

        get("/apps/pulp/gui/reports/books/table/static", (req, res) -> {
            BookFilter filter = BookFilterFromQueryMap.getBookFilter(req.queryMap());
            return getPulpApp(req).stringReports().getBooksAsHtmlTable(filter);
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

            return getPulpApp(req).page().getFaqRenderPage(typeOfFaq, forTerm, showiframe).asHTMLString();

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

            ReportConfig config = new ReportConfig(getPulpApp(req).reports().getReportConfig());
            config.setPostFixPath("/list/navigation");
            config.showAmendLinks(false);
            config.allowDelete(false);
            config.setTitlesAreLinks(true);
            config.setYearsAsLinks(true);
            config.setSeriesNamesLinks(true);
            config.setPublisherNamesLinks(true);
            config.setAuthorNamesLinks(true);

            // TODO should be able to configure links to author, series, publisher to the Amend screen rather than a list filter

            return getPulpApp(req).page().getAlertSearchPage().setConfirmSearch(confirmSearch)
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
            return getPulpApp(req).page().getFilterTestPage(isList, navigation, canSearch, isPaginated).setFilter(filter).setShowData(req.queryMap().hasKeys()).
                    setShowThese(templateElements).
                    asHTMLString();
        });

        get("/apps/pulp/gui/admin/version/*", (req, res) -> {

            //request.splat()[0] is version
            try {
                int version = Integer.parseInt(req.splat()[0]);
                getPulpApp(req).setAppVersion(version);
            }catch(Exception e){
                res.status(400);
                // todo need an error page, handle 404, exceptions etc.
                return "<h1>What version was that?</h1>";
            }

            res.redirect("/apps/pulp/gui");
            return "";

        });

        get("/apps/pulp/gui/reports/authors/list/navigation", (req, res) -> {

            final ReportConfig config = new ReportConfig(getPulpApp(req).reports().getReportConfig());

            if(req.queryMap().hasKeys() && req.queryMap().value("faqs")!=null){
                config.setIncludeFaqLinks(true);
            }else{
                config.setIncludeFaqLinks(false);
            }


            config.setAuthorNamesLinks(true);
            config.allowDelete(false);

            if(getPulpApp(req).getAppVersion().are(AMEND_LINKS_SHOWN_IN_LIST)) {
                config.showAmendLinks(true);
            }
            if(getPulpApp(req).getAppVersion().are(DELETE_LINKS_SHOWN_IN_LIST)){
                config.setAllowDeleteAuthor(true);
            }


            // possibly if filtered then allow delete etc..


            return getPulpApp(req).reports(config).configurePostFixPath("/list/navigation").getAuthorsAsHtmlList();
        });

        get("/apps/pulp/gui/reports/authors/list/navigation/faqs", (req, res) -> {

            final ReportConfig config = new ReportConfig(getPulpApp(req).reports().getReportConfig());

            config.setIncludeFaqLinks(true);
            config.showAmendLinks(false);
            config.setTitlesAreLinks(false);

            return getPulpApp(req).reports(config).configurePostFixPath("/list/navigation").getAuthorsAsHtmlList();
        });

        get("/apps/pulp/gui/reports/publishers/list/navigation", (req, res) -> {

            final ReportConfig config = new ReportConfig(getPulpApp(req).reports().getReportConfig());

            if(req.queryMap().hasKeys() && req.queryMap().value("faqs")!=null){
                config.setIncludeFaqLinks(true);
            }else{
                config.setIncludeFaqLinks(false);
            }


            config.setPublisherNamesLinks(true);
            config.allowDelete(false);

            if(getPulpApp(req).getAppVersion().are(AMEND_LINKS_SHOWN_IN_LIST)) {
                config.showAmendLinks(true);
            }
            if(getPulpApp(req).getAppVersion().are(DELETE_LINKS_SHOWN_IN_LIST)){
                config.setAllowDeletePublisher(true);
            }


            return getPulpApp(req).reports(config).configurePostFixPath("/list/navigation").getPublishersAsHtmlList();
        });

        get("/apps/pulp/gui/reports/publishers/list/navigation/faqs", (req, res) -> {

            final ReportConfig config = new ReportConfig(getPulpApp(req).reports().getReportConfig());

            config.setIncludeFaqLinks(true);
            config.showAmendLinks(false);
            config.setTitlesAreLinks(false);

            return getPulpApp(req).reports(config).configurePostFixPath("/list/navigation").getPublishersAsHtmlList();
        });

        get("/apps/pulp/gui/reports/years/list/navigation", (req, res) -> {

            final ReportConfig config = new ReportConfig(getPulpApp(req).reports().getReportConfig());

            if(req.queryMap().hasKeys() && req.queryMap().value("faqs")!=null){
                config.setIncludeFaqLinks(true);
            }else{
                config.setIncludeFaqLinks(false);
            }

            config.showAmendLinks(false);
            config.setYearsAsLinks(true);

            return getPulpApp(req).reports(config).configurePostFixPath("/list/navigation").getYearsAsHtmlList();
        });

        get("/apps/pulp/gui/reports/years/list/navigation/faqs", (req, res) -> {

            final ReportConfig config = new ReportConfig(getPulpApp(req).reports().getReportConfig());

            config.setIncludeFaqLinks(true);
            config.showAmendLinks(false);
            config.setTitlesAreLinks(false);

            return getPulpApp(req).reports(config).configurePostFixPath("/list/navigation").getYearsAsHtmlList();
        });

        get("/apps/pulp/gui/reports/series/list/navigation", (req, res) -> {
            final ReportConfig config = new ReportConfig(getPulpApp(req).reports().getReportConfig());

            if(req.queryMap().hasKeys() && req.queryMap().value("faqs")!=null){
                config.setIncludeFaqLinks(true);
            }else{
                config.setIncludeFaqLinks(false);
            }

            config.showAmendLinks(false);
            config.setSeriesNamesLinks(true);
            config.allowDelete(false);

            if(getPulpApp(req).getAppVersion().are(AMEND_LINKS_SHOWN_IN_LIST)) {
                config.showAmendLinks(true);
            }
            if(getPulpApp(req).getAppVersion().are(DELETE_LINKS_SHOWN_IN_LIST)){
                config.setAllowDeleteSeries(true);
            }

            return getPulpApp(req).reports(config).configurePostFixPath("/list/navigation").getSeriesNamesAsHtmlList();
        });

        get("/apps/pulp/gui/reports/series/list/navigation/faqs", (req, res) -> {

            final ReportConfig config = new ReportConfig(getPulpApp(req).reports().getReportConfig());

            config.setIncludeFaqLinks(true);
            config.showAmendLinks(false);

            config.setTitlesAreLinks(false);
            return getPulpApp(req).reports(config).configurePostFixPath("/list/navigation").getSeriesNamesAsHtmlList();
        });

        get("/apps/pulp/gui/reports/authors/list/static", (req, res) -> { return getPulpApp(req).stringReports().getAuthorsAsHtmlList();});
        get("/apps/pulp/gui/reports/publishers/list/static", (req, res) -> { return getPulpApp(req).stringReports().getPublishersAsHtmlList();});
        get("/apps/pulp/gui/reports/years/list/static", (req, res) -> { return getPulpApp(req).stringReports().getYearsAsHtmlList();});
        get("/apps/pulp/gui/reports/series/list/static", (req, res) -> { return getPulpApp(req).stringReports().getSeriesNamesAsHtmlList();});


        get("/apps/pulp/", (req, res) -> { return getPulpApp(req).reports().getIndexPage();});
        get("/apps/pulp", (req, res) -> { res.redirect("/apps/pulp/"); return "";});
        get("/apps/pulp/gui", (req, res) -> { res.redirect("/apps/pulp/gui/"); return "";});
        get("/apps/pulp/gui/", (req, res) -> { return getPulpApp(req).reports().getIndexPage();});
        get("/apps/pulp/gui/help", (req, res) -> { return getPulpApp(req).reports().getHelpPage();});
        get("/apps/pulp/gui/menu/books", (req, res) -> { return getPulpApp(req).reports().getSnippetPage("Books Menu", "menu-screen-books-reports-list.html");});
        get("/apps/pulp/gui/menu/authors", (req, res) -> { return getPulpApp(req).reports().getSnippetPage("Authors Menu", "menu-screen-authors-reports-list.html");});
        get("/apps/pulp/gui/menu/publishers", (req, res) -> { return getPulpApp(req).reports().getSnippetPage("Publishers Menu", "menu-screen-publishers-reports-list.html");});
        get("/apps/pulp/gui/menu/series", (req, res) -> { return getPulpApp(req).reports().getSnippetPage("Series Menu", "menu-screen-series-reports-list.html");});
        get("/apps/pulp/gui/menu/years", (req, res) -> { return getPulpApp(req).reports().getSnippetPage("Years Menu", "menu-screen-years-reports-list.html");});
        get("/apps/pulp/gui/menu/create", (req, res) -> { return getPulpApp(req).reports().getSnippetPage("Create Menu", "menu-screen-create.html");});
        get("/apps/pulp/gui/menu/reports", (req, res) -> { return getPulpApp(req).reports().getSnippetPage("Reports Menu", "menu-screen-static-reports.html");});
        get("/apps/pulp/gui/menu/admin", (req, res) -> { return getPulpApp(req).reports().getSnippetPage("Admin Menu", "menu-screen-admin.html");});


        get("/apps/pulp/gui/reports", (req, res) -> { res.redirect("/apps/pulp/gui/reports/"); return "";});
        get("/apps/pulp/gui/reports/", (req, res) -> { return getPulpApp(req).reports().getIndexPage();});
        get("/apps/pulp/gui/reports/books", (req, res) -> { res.redirect("/apps/pulp/gui/reports/"); return "";});
    }



}
