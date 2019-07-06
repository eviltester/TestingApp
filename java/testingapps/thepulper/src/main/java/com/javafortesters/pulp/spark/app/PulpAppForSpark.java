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


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.javafortesters.pulp.spark.app.versioning.AppVersionSettings.AMEND_LINKS_SHOWN_IN_LIST;
import static com.javafortesters.pulp.spark.app.versioning.AppVersionSettings.DELETE_LINKS_SHOWN_IN_LIST;
import static spark.Spark.*;


public class PulpAppForSpark {


    // TODO support multiple apps each with a different session

    private static final String SESSION_APP = "sessionPulpApp";
    private static final String SESSION_API_SECRET = "apisecret";

    // TODO: make max session configurable through environment variables
    private static final int MAX_SESSION_LENGTH = 60*5;  // set max session without interactivity to 5 minutes
    private static final long CHECK_FOR_EXPIRED_API_SESSIONS_EVERY_MILLIS = 60*3*1000;  // checkfor expired api sessions every 3 minutes
    private boolean allowsShutdown=false;

    private Map<String, Session> api_session_mapping= new ConcurrentHashMap<>();

    long lastDeleteExpiredCheck=0;

    private void deleteExpiredAPISessions() {

        long currentCheck = System.currentTimeMillis();
        long timeSinceLastCheck = currentCheck-lastDeleteExpiredCheck;
        long checkInXMilliseconds = timeSinceLastCheck-CHECK_FOR_EXPIRED_API_SESSIONS_EVERY_MILLIS;

        if(checkInXMilliseconds<0){
            System.out.println("Check for expired API sessions in milliseconds time " + (checkInXMilliseconds*-1));
            return;
        }

        lastDeleteExpiredCheck=currentCheck;

        for(String sessionKey : api_session_mapping.keySet()){
            Session session = api_session_mapping.get(sessionKey);
            System.out.println("checking session " + sessionKey);
            if(session==null){
                System.out.println("session is null removing... " + sessionKey);
                api_session_mapping.remove(sessionKey);
            }else {
                try {
                    System.out.println("checking session validity... " + sessionKey);
                    final PulpApp app = session.attribute(SESSION_APP);
                } catch (Exception e) {
                    System.out.println("automatically removed api session " + sessionKey);
                    api_session_mapping.remove(sessionKey);
                }
            }
        }
    }

    // TODO: add facility to create an API session /app/api/session
    // TODO: add facility to input an API session id into GUI to switch GUI to that session

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

            // For an API to be valid you have to access the GUI first
            String appApiKey = sessionPulpApp.getAPISecret();

            session.attribute(SESSION_API_SECRET, appApiKey);

            // System.out.println(appApiKey);
            api_session_mapping.put(appApiKey, session);

            // System.out.println("Session count " + api_session_mapping.size());

        } else {
            sessionPulpApp = session.attribute(SESSION_APP);
        }

        return sessionPulpApp;
    }

    public PulpApp getPulpAppForApi(String api_auth_header){
        final Session session = api_session_mapping.get(api_auth_header);
        PulpApp sessionPulpApp=null;
        try{
            sessionPulpApp = session.attribute(SESSION_APP);
        }catch(Exception e){
            // session is not valid
            if(session!=null) {
                session.invalidate();
            }
            api_session_mapping.remove(api_auth_header);
            halt(401, new EntityResponse().setErrorStatus(401, "X-API-AUTH header is invalid - check in the GUI").getErrorMessage());
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


    private class PulpAppReferencer{
        private PulpApp reference;

        public PulpAppReferencer(){

        }

        public PulpAppReferencer app(PulpApp referenced){
            this.reference = referenced;
            return this;
        }

        public PulpApp app(){
            return reference;
        }
    }

    public PulpAppForSpark(boolean allowsShutdown){

        this.allowsShutdown = allowsShutdown;

        //
        // BASIC API
        //
        final EntityResponse notAllowed = new EntityResponse().setErrorStatus(405, "Not allowed, sorry");
        final EntityResponse unknown = new EntityResponse().setErrorStatus(404, "Unknown API EndPoint");


        final PulpAppReferencer requestPulpApp = new PulpAppReferencer();

        before("/apps/pulp/api/*", (request, response) -> {
            // before any API request, check for an X-API-AUTH header
            // the value of the X-API-AUTH header must match the value displayed on the GUI
            if(request.headers("X-API-AUTH")==null) {
                halt(401, apiEntityResponse(response,new EntityResponse().setErrorStatus(401, "You need to add the X-API-AUTH header with the secret key shown in the GUI")));
            }

            // check if the API AUTH is known to us
            Session session = api_session_mapping.get(request.headers("X-API-AUTH"));
            if(session==null){
                halt(401, apiEntityResponse(response,new EntityResponse().setErrorStatus(401, "X-API-AUTH header is invalid - check in the GUI")));
            }

            // delete any expired sessions for tidy up
            deleteExpiredAPISessions();

            requestPulpApp.app(getPulpAppForApi(request.headers("X-API-AUTH")));

        });

        path("/apps/pulp/api/authors/:authorid", () -> {

            options("", (req, res) -> {
                res.header("Allow", "OPTIONS, GET, HEAD, DELETE");
                return apiEntityResponse(res, new EntityResponse().setSuccessStatus(200, "{}"));
            });

            head("", (req, res) -> {
                final EntityResponse response = requestPulpApp.app().entities().getAuthor(req.params(":authorid"), req.headers("Accept"));
                return apiEmptyEntityResponse(res, response);
            });

            get("", (req, res) -> {
                final EntityResponse response = requestPulpApp.app().entities().getAuthor(req.params(":authorid"), req.headers("Accept"));
                return apiEntityResponse(res, response);
            });

            delete("",  (req, res) -> {
                final EntityResponse response = requestPulpApp.app().entities().deleteAuthor(req.params(":authorid"),req.headers("Accept"));
                return apiEntityResponse(res, response);
            });

            post("",     (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            put("",     (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            trace("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            patch("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
        });

        path("/apps/pulp/api/authors", () -> {

            options("", (req, res) -> {
                res.header("Allow", "OPTIONS, GET, HEAD");
                return apiEntityResponse(res, new EntityResponse().setSuccessStatus(200, "{}"));
            });

            head("", (req, res) -> {
                final EntityResponse response = requestPulpApp.app().entities().getAuthors(req.headers("Accept"));
                return apiEmptyEntityResponse(res, response);
            });

            get("", (req, res) -> {
                final EntityResponse response = requestPulpApp.app().entities().getAuthors(req.headers("Accept"));
                return apiEntityResponse(res, response);
            });


            post("",     (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            put("",     (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            delete("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            trace("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            patch("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
        });

        path("/apps/pulp/api/books/:bookid", () -> {

            options("", (req, res) -> {
                res.header("Allow", "OPTIONS, GET, HEAD, DELETE");
                return apiEntityResponse(res, new EntityResponse().setSuccessStatus(200, "{}"));
            });

            head("", (req, res) -> {
                final EntityResponse response = requestPulpApp.app().entities().getBook(req.params(":bookid"),req.headers("Accept"));
                return apiEmptyEntityResponse(res, response);
            });

            get("", (req, res) -> {
                final EntityResponse response = requestPulpApp.app().entities().getBook(req.params(":bookid"),req.headers("Accept"));
                return apiEntityResponse(res, response);
            });

            delete("",  (req, res) -> {
                final EntityResponse response = requestPulpApp.app().entities().deleteBook(req.params(":bookid"),req.headers("Accept"));
                return apiEntityResponse(res, response);
            });

            post("",     (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            put("",     (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            trace("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            patch("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
        });

        path("/apps/pulp/api/books", () -> {

            options("", (req, res) -> {
                res.header("Allow", "OPTIONS, GET, HEAD");
                return apiEntityResponse(res, new EntityResponse().setSuccessStatus(200, "{}"));
            });

            head("", (req, res) -> {
                final EntityResponse response = requestPulpApp.app().entities().getBooks(req.headers("Accept"));
                return apiEmptyEntityResponse(res, response);
            });

            get("", (req, res) -> {
                final EntityResponse response = requestPulpApp.app().entities().getBooks(req.headers("Accept"));
                return apiEntityResponse(res, response);
            });

            post("",     (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            put("",     (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            delete("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            trace("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            patch("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
        });

        path("/apps/pulp/api/series/:seriesid", () -> {

            options("", (req, res) -> {
                res.header("Allow", "OPTIONS, GET, HEAD, DELETE");
                return apiEntityResponse(res, new EntityResponse().setSuccessStatus(200, "{}"));
            });

            head("", (req, res) -> {
                final EntityResponse response = requestPulpApp.app().entities().getSeries(req.params(":seriesid"),req.headers("Accept"));
                return apiEmptyEntityResponse(res, response);
            });

            get("", (req, res) -> {
                final EntityResponse response = requestPulpApp.app().entities().getSeries(req.params(":seriesid"),req.headers("Accept"));
                return apiEntityResponse(res, response);
            });

            delete("",  (req, res) -> {
                final EntityResponse response = requestPulpApp.app().entities().deleteSeries(req.params(":seriesid"),req.headers("Accept"));
                return apiEntityResponse(res, response);
            });

            post("",     (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            put("",     (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            trace("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            patch("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
        });

        path("/apps/pulp/api/series", () -> {

            head("", (req, res) -> {
                final EntityResponse response = requestPulpApp.app().entities().getAllSeries(req.headers("Accept"));
                return apiEmptyEntityResponse(res, response);
            });

            get("", (req, res) -> {
                final EntityResponse response = requestPulpApp.app().entities().getAllSeries(req.headers("Accept"));
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


        path("/apps/pulp/api/publishers/:publisherid", () -> {

            head("", (req, res) -> {
                final EntityResponse response = requestPulpApp.app().entities().getPublisher(req.params(":publisherid"),req.headers("Accept"));
                return apiEmptyEntityResponse(res, response);
            });

            get("", (req, res) -> {
                final EntityResponse response = requestPulpApp.app().entities().getPublisher(req.params(":publisherid"),req.headers("Accept"));
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

        path("/apps/pulp/api/publishers", () -> {

            head("", (req, res) -> {
                final EntityResponse response = requestPulpApp.app().entities().getPublishers(req.headers("Accept"));
                return apiEmptyEntityResponse(res, response);
            });

            get("", (req, res) -> {
                final EntityResponse response = requestPulpApp.app().entities().getPublishers(req.headers("Accept"));
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


        // TODO: API Session handling
        // TODO: CxUD interaction [R] is done at this point in todo list
        // TODO: xstream for xml to honour content-type and accept headers

        // TODO: Future API End Points
        // /apps/pulp/api/publishers/id/series
        // /apps/pulp/api/publishers/id/authors
        // /apps/pulp/api/publishers/id/books
        // /apps/pulp/api/series/id/books
        // /apps/pulp/api/series/id/authors
        // /apps/pulp/api/authors/id/books
        // /apps/pulp/api/authors/id/publishers

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




        /*

                GUI Processing section


         */

        get("/apps/pulp", (req, res) -> { res.redirect("/apps/pulp/"); return "";});

        before("/apps/pulp/*", (request, response) -> {
            // only check using request if null,
            // because it may have been set by X-API-AUTH header
            if(requestPulpApp.app()==null) {
                requestPulpApp.app(getPulpApp(request));
            }
        });

        get("/apps/pulp/gui/view/author", (req, res) -> {
            return requestPulpApp.app().page().viewAuthorPage(req.queryParams("author")).asHTMLString();
        });

        get("/apps/pulp/gui/create/author", (req, res) -> {
            return requestPulpApp.app().page().createAuthorPage().asHTMLString();
        });

        post("/apps/pulp/gui/create/author", (req, res) -> {
            return new CreateFlowsHandler(requestPulpApp.app()).authorCreate(req, res);
        });

        ///apps/pulp/gui/amend/author?author=id
        get("/apps/pulp/gui/amend/author", (req, res) -> {
            return requestPulpApp.app().page().amendAuthorPage(req.queryParams("author")).asHTMLString();
        });

        post("/apps/pulp/gui/amend/author", (req, res) -> {
            return new AmendFlowsHandler(requestPulpApp.app()).authorAmend(req, res);
        });

        post("/apps/pulp/gui/amend/deleteauthor", (req, res) -> {

            requestPulpApp.app().books().deleteAuthor(req.queryParams("authorid"));
            res.redirect("/apps/pulp/gui/reports/authors/list/navigation");
            return "";
        });

        get("/apps/pulp/gui/view/series", (req, res) -> {
            return requestPulpApp.app().page().viewSeriesPage(req.queryParams("series")).asHTMLString();
        });

        get("/apps/pulp/gui/create/series", (req, res) -> {
            return requestPulpApp.app().page().createSeriesPage().asHTMLString();
        });

        post("/apps/pulp/gui/create/series", (req, res) -> {
            return new CreateFlowsHandler(requestPulpApp.app()).seriesCreate(req, res);
        });

        ///apps/pulp/gui/amend/series?series=id
        get("/apps/pulp/gui/amend/series", (req, res) -> {
            return requestPulpApp.app().page().amendSeriesPage(req.queryParams("series")).asHTMLString();
        });

        post("/apps/pulp/gui/amend/series", (req, res) -> {
            return new AmendFlowsHandler(requestPulpApp.app()).seriesAmend(req, res);
        });

        post("/apps/pulp/gui/amend/deleteseries", (req, res) -> {
            requestPulpApp.app().books().deleteSeries(req.queryParams("seriesid"));
            res.redirect("/apps/pulp/gui/reports/series/list/navigation");
            return "";
        });

        get("/apps/pulp/gui/view/publisher", (req, res) -> {
            return requestPulpApp.app().page().viewPublisherPage(req.queryParams("publisher")).asHTMLString();
        });

        get("/apps/pulp/gui/create/publisher", (req, res) -> {
            return requestPulpApp.app().page().createPublisherPage().asHTMLString();
        });

        post("/apps/pulp/gui/create/publisher", (req, res) -> {
            return new CreateFlowsHandler(requestPulpApp.app()).publisherCreate(req, res);
        });

        ///apps/pulp/gui/amend/publisher?publisher=id
        get("/apps/pulp/gui/amend/publisher", (req, res) -> {
            return requestPulpApp.app().page().amendPublisherPage(req.queryParams("publisher")).asHTMLString();
        });

        post("/apps/pulp/gui/amend/publisher", (req, res) -> {
            return new AmendFlowsHandler(requestPulpApp.app()).publisherAmend(req, res);
        });

        post("/apps/pulp/gui/amend/deletepublisher", (req, res) -> {
            requestPulpApp.app().books().deletePublisher(req.queryParams("publisherid"));
            res.redirect("/apps/pulp/gui/reports/publishers/list/navigation");
            return "";
        });

        get("/apps/pulp/gui/view/book", (req, res) -> {
            return requestPulpApp.app().page().viewBookPage(req.queryParams("book")).asHTMLString();
        });


        get("/apps/pulp/gui/create/book", (req, res) -> {
            return requestPulpApp.app().page().createBookPage().asHTMLString();
        });

        post("/apps/pulp/gui/create/book", (req, res) -> {
            return new CreateFlowsHandler(requestPulpApp.app()).bookCreate(req, res);
        });

        ///apps/pulp/gui/amend/book?book=id
        get("/apps/pulp/gui/amend/book", (req, res) -> {
            return requestPulpApp.app().page().amendBookPage(req.queryParams("book")).asHTMLString();
        });

        post("/apps/pulp/gui/amend/book", (req, res) -> {
            return new AmendFlowsHandler(requestPulpApp.app()).bookAmend(req, res);
        });

        post("/apps/pulp/gui/amend/deletebook", (req, res) -> {

            requestPulpApp.app().books().deleteBook(req.queryParams("bookid"));
            res.redirect("/apps/pulp/gui/reports/books/table/navigation");
            return "";
        });

        get("/apps/pulp/gui/reports/books/list/navigation", (req, res) -> {

            BookFilter filter = BookFilterFromQueryMap.getBookFilter(req.queryMap());
            final ReportConfig config = new ReportConfig(requestPulpApp.app().reports().getReportConfig());

            if(req.queryMap().hasKeys() && req.queryMap().value("faqs")!=null){
                config.setIncludeFaqLinks(true);
            }else{
                config.setIncludeFaqLinks(false);
            }

            config.showAmendLinks(false);
            config.allowDelete(false);

            if(requestPulpApp.app().getAppVersion().are(AMEND_LINKS_SHOWN_IN_LIST)) {
                config.showBookAmendLink(true);
            }
            if(requestPulpApp.app().getAppVersion().are(DELETE_LINKS_SHOWN_IN_LIST)){
                // list doesn't have enough room - only show in table
                // config.setAllowDeleteBook(true);
            }


            // TODO if filtered at all then show authors, publishers, series and years as links

            config.setTitlesAreLinks(true);

            return requestPulpApp.app().reports(config).configurePostFixPath("/list/navigation").getBooksAsHtmlList(filter);
        });

        get("/apps/pulp/gui/reports/books/list/navigation/faqs", (req, res) -> {

            BookFilter filter = BookFilterFromQueryMap.getBookFilter(req.queryMap());
            final ReportConfig config = new ReportConfig(requestPulpApp.app().reports().getReportConfig());

            config.setIncludeFaqLinks(true);
            config.showAmendLinks(false);
            config.setTitlesAreLinks(false);
            config.allowDelete(false);

            return requestPulpApp.app().reports(config).configurePostFixPath("/list/navigation").getBooksAsHtmlList(filter);
        });

        get("/apps/pulp/gui/reports/books/list/static", (req, res) -> {
            BookFilter filter = BookFilterFromQueryMap.getBookFilter(req.queryMap());
            return requestPulpApp.app().stringReports().getBooksAsHtmlList(filter);
        });

        get("/apps/pulp/gui/reports/books/table/navigation", (req, res) -> {
            BookFilter filter = BookFilterFromQueryMap.getBookFilter(req.queryMap());

            final ReportConfig config = new ReportConfig(requestPulpApp.app().reports().getReportConfig());

            config.showAmendLinks(false);

            if(requestPulpApp.app().getAppVersion().are(AMEND_LINKS_SHOWN_IN_LIST)){
                config.showBookAmendLink(true);
            }
            if(requestPulpApp.app().getAppVersion().are(DELETE_LINKS_SHOWN_IN_LIST)){
                // table has enough space to allow delete button rendering
                config.setAllowDeleteBook(true);
            }

            config.setTitlesAreLinks(true);
            config.allowDelete(false);

            return requestPulpApp.app().reports(config).configurePostFixPath("/table/navigation").getBooksAsHtmlTable(filter);
        });

        get("/apps/pulp/gui/reports/books/table/static", (req, res) -> {
            BookFilter filter = BookFilterFromQueryMap.getBookFilter(req.queryMap());
            return requestPulpApp.app().stringReports().getBooksAsHtmlTable(filter);
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

            return requestPulpApp.app().page().getFaqRenderPage(typeOfFaq, forTerm, showiframe).asHTMLString();

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

            ReportConfig config = new ReportConfig(requestPulpApp.app().reports().getReportConfig());
            config.setPostFixPath("/list/navigation");
            config.showAmendLinks(false);
            config.allowDelete(false);
            config.setTitlesAreLinks(true);
            config.setYearsAsLinks(true);
            config.setSeriesNamesLinks(true);
            config.setPublisherNamesLinks(true);
            config.setAuthorNamesLinks(true);

            // TODO should be able to configure links to author, series, publisher to the Amend screen rather than a list filter

            return requestPulpApp.app().page().getAlertSearchPage().setConfirmSearch(confirmSearch)
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
            return requestPulpApp.app().page().getFilterTestPage(isList, navigation, canSearch, isPaginated).setFilter(filter).setShowData(req.queryMap().hasKeys()).
                    setShowThese(templateElements).
                    asHTMLString();
        });

        get("/apps/pulp/gui/admin/version/*", (req, res) -> {

            //request.splat()[0] is version
            try {
                int version = Integer.parseInt(req.splat()[0]);
                requestPulpApp.app().setAppVersion(version);
            }catch(Exception e){
                res.status(400);
                // todo need an error page, handle 404, exceptions etc.
                return "<h1>What version was that?</h1>";
            }

            res.redirect("/apps/pulp/gui");
            return "";

        });

        get("/apps/pulp/gui/reports/authors/list/navigation", (req, res) -> {

            final ReportConfig config = new ReportConfig(requestPulpApp.app().reports().getReportConfig());

            if(req.queryMap().hasKeys() && req.queryMap().value("faqs")!=null){
                config.setIncludeFaqLinks(true);
            }else{
                config.setIncludeFaqLinks(false);
            }


            config.setAuthorNamesLinks(true);
            config.allowDelete(false);

            if(requestPulpApp.app().getAppVersion().are(AMEND_LINKS_SHOWN_IN_LIST)) {
                config.showAmendLinks(true);
            }
            if(requestPulpApp.app().getAppVersion().are(DELETE_LINKS_SHOWN_IN_LIST)){
                config.setAllowDeleteAuthor(true);
            }


            // possibly if filtered then allow delete etc..


            return requestPulpApp.app().reports(config).configurePostFixPath("/list/navigation").getAuthorsAsHtmlList();
        });

        get("/apps/pulp/gui/reports/authors/list/navigation/faqs", (req, res) -> {

            final ReportConfig config = new ReportConfig(requestPulpApp.app().reports().getReportConfig());

            config.setIncludeFaqLinks(true);
            config.showAmendLinks(false);
            config.setTitlesAreLinks(false);

            return requestPulpApp.app().reports(config).configurePostFixPath("/list/navigation").getAuthorsAsHtmlList();
        });

        get("/apps/pulp/gui/reports/publishers/list/navigation", (req, res) -> {

            final ReportConfig config = new ReportConfig(requestPulpApp.app().reports().getReportConfig());

            if(req.queryMap().hasKeys() && req.queryMap().value("faqs")!=null){
                config.setIncludeFaqLinks(true);
            }else{
                config.setIncludeFaqLinks(false);
            }


            config.setPublisherNamesLinks(true);
            config.allowDelete(false);

            if(requestPulpApp.app().getAppVersion().are(AMEND_LINKS_SHOWN_IN_LIST)) {
                config.showAmendLinks(true);
            }
            if(requestPulpApp.app().getAppVersion().are(DELETE_LINKS_SHOWN_IN_LIST)){
                config.setAllowDeletePublisher(true);
            }


            return requestPulpApp.app().reports(config).configurePostFixPath("/list/navigation").getPublishersAsHtmlList();
        });

        get("/apps/pulp/gui/reports/publishers/list/navigation/faqs", (req, res) -> {

            final ReportConfig config = new ReportConfig(requestPulpApp.app().reports().getReportConfig());

            config.setIncludeFaqLinks(true);
            config.showAmendLinks(false);
            config.setTitlesAreLinks(false);

            return requestPulpApp.app().reports(config).configurePostFixPath("/list/navigation").getPublishersAsHtmlList();
        });

        get("/apps/pulp/gui/reports/years/list/navigation", (req, res) -> {

            final ReportConfig config = new ReportConfig(requestPulpApp.app().reports().getReportConfig());

            if(req.queryMap().hasKeys() && req.queryMap().value("faqs")!=null){
                config.setIncludeFaqLinks(true);
            }else{
                config.setIncludeFaqLinks(false);
            }

            config.showAmendLinks(false);
            config.setYearsAsLinks(true);

            return requestPulpApp.app().reports(config).configurePostFixPath("/list/navigation").getYearsAsHtmlList();
        });

        get("/apps/pulp/gui/reports/years/list/navigation/faqs", (req, res) -> {

            final ReportConfig config = new ReportConfig(requestPulpApp.app().reports().getReportConfig());

            config.setIncludeFaqLinks(true);
            config.showAmendLinks(false);
            config.setTitlesAreLinks(false);

            return requestPulpApp.app().reports(config).configurePostFixPath("/list/navigation").getYearsAsHtmlList();
        });

        get("/apps/pulp/gui/reports/series/list/navigation", (req, res) -> {
            final ReportConfig config = new ReportConfig(requestPulpApp.app().reports().getReportConfig());

            if(req.queryMap().hasKeys() && req.queryMap().value("faqs")!=null){
                config.setIncludeFaqLinks(true);
            }else{
                config.setIncludeFaqLinks(false);
            }

            config.showAmendLinks(false);
            config.setSeriesNamesLinks(true);
            config.allowDelete(false);

            if(requestPulpApp.app().getAppVersion().are(AMEND_LINKS_SHOWN_IN_LIST)) {
                config.showAmendLinks(true);
            }
            if(requestPulpApp.app().getAppVersion().are(DELETE_LINKS_SHOWN_IN_LIST)){
                config.setAllowDeleteSeries(true);
            }

            return requestPulpApp.app().reports(config).configurePostFixPath("/list/navigation").getSeriesNamesAsHtmlList();
        });

        get("/apps/pulp/gui/reports/series/list/navigation/faqs", (req, res) -> {

            final ReportConfig config = new ReportConfig(requestPulpApp.app().reports().getReportConfig());

            config.setIncludeFaqLinks(true);
            config.showAmendLinks(false);

            config.setTitlesAreLinks(false);
            return requestPulpApp.app().reports(config).configurePostFixPath("/list/navigation").getSeriesNamesAsHtmlList();
        });

        get("/apps/pulp/gui/reports/authors/list/static", (req, res) -> { return requestPulpApp.app().stringReports().getAuthorsAsHtmlList();});
        get("/apps/pulp/gui/reports/publishers/list/static", (req, res) -> { return requestPulpApp.app().stringReports().getPublishersAsHtmlList();});
        get("/apps/pulp/gui/reports/years/list/static", (req, res) -> { return requestPulpApp.app().stringReports().getYearsAsHtmlList();});
        get("/apps/pulp/gui/reports/series/list/static", (req, res) -> { return requestPulpApp.app().stringReports().getSeriesNamesAsHtmlList();});


        get("/apps/pulp/", (req, res) -> { return requestPulpApp.app().reports().getIndexPage();});
        get("/apps/pulp/gui", (req, res) -> { res.redirect("/apps/pulp/gui/"); return "";});
        get("/apps/pulp/gui/", (req, res) -> { return requestPulpApp.app().reports().getIndexPage();});
        get("/apps/pulp/gui/help", (req, res) -> { return requestPulpApp.app().reports().getHelpPage();});
        get("/apps/pulp/gui/menu/books", (req, res) -> { return requestPulpApp.app().reports().getSnippetPage("Books Menu", "menu-screen-books-reports-list.html");});
        get("/apps/pulp/gui/menu/authors", (req, res) -> { return requestPulpApp.app().reports().getSnippetPage("Authors Menu", "menu-screen-authors-reports-list.html");});
        get("/apps/pulp/gui/menu/publishers", (req, res) -> { return requestPulpApp.app().reports().getSnippetPage("Publishers Menu", "menu-screen-publishers-reports-list.html");});
        get("/apps/pulp/gui/menu/series", (req, res) -> { return requestPulpApp.app().reports().getSnippetPage("Series Menu", "menu-screen-series-reports-list.html");});
        get("/apps/pulp/gui/menu/years", (req, res) -> { return requestPulpApp.app().reports().getSnippetPage("Years Menu", "menu-screen-years-reports-list.html");});
        get("/apps/pulp/gui/menu/create", (req, res) -> { return requestPulpApp.app().reports().getSnippetPage("Create Menu", "menu-screen-create.html");});
        get("/apps/pulp/gui/menu/reports", (req, res) -> { return requestPulpApp.app().reports().getSnippetPage("Reports Menu", "menu-screen-static-reports.html");});
        get("/apps/pulp/gui/menu/admin", (req, res) -> { return requestPulpApp.app().reports().getSnippetPage("Admin Menu", "menu-screen-admin.html");});


        get("/apps/pulp/gui/reports", (req, res) -> { res.redirect("/apps/pulp/gui/reports/"); return "";});
        get("/apps/pulp/gui/reports/", (req, res) -> { return requestPulpApp.app().reports().getIndexPage();});
        get("/apps/pulp/gui/reports/books", (req, res) -> { res.redirect("/apps/pulp/gui/reports/"); return "";});
    }



}
