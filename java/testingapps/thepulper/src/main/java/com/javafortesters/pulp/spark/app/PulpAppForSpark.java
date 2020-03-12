package com.javafortesters.pulp.spark.app;

import com.google.gson.Gson;
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


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.javafortesters.pulp.spark.app.versioning.AppVersionSettings.AMEND_LINKS_SHOWN_IN_LIST;
import static com.javafortesters.pulp.spark.app.versioning.AppVersionSettings.DELETE_LINKS_SHOWN_IN_LIST;
import static spark.Spark.*;

/*

Basic plan

| Resource   |  GET (read)  | POST (create, amend)    | PUT (create, replace)        | DELETE          | PATCH                 |
|------------|--------------|-------------------------|------------------------------|-----------------|-----------------------|
| /plural    | list of X    | create/amend an item(s) | 405 NOT ALLOWED              | 405 NOT ALLOWED | 405 NOT ALLOWED       |
| /plural/id | specific X   | 405 NOT ALLOWED         | replace with full payload    | delete specific | partial field replace |


TODO: search/filtering by fields for GET lists  /plural?fieldname=value&fieldname2<=value
TODO: filtering of fields in response  /plural?fields=fieldname1,fieldname2
TODO: sorting on lists  /plural?sort=


Done:

HEAD plural/id
HEAD plural
GET /plural
GET /plural/id
DELETE /plural/id
POST /plural  (single, and multiple items)
PUT plural/id

*/

public class PulpAppForSpark {



    private boolean allowsShutdown=false;

    SessionMapping sessions = new SessionMapping(allowsShutdown);







    // TODO: add official GUI facility to input an API session id into GUI to switch GUI to that session






    public PulpAppForSpark() {
        // by default do not allow shutdown
        this(false);
    }

    public String apiEntityResponse(Response res, final EntityResponse response){
        res.status(response.getStatusCode());
        res.header("content-type", response.getContentType());
        for(String headerkey : response.getHeaderNames()){
            res.header(headerkey, response.getHeaderValue(headerkey));
        }

        return response.getResponseBody();

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

        // enable for debugging to see all requests and responses

        /*
        before("*", (request, response) -> {
            System.out.println(request.requestMethod());
            System.out.println(request.pathInfo());
            // be careful, this will consume the body and params won't work
            System.out.println(request.body());
        });
        after((request, response) -> {
            System.out.println(response.status());
            System.out.println(response.body());
        });
        */




        before("/apps/pulp/api/*", (request, response) -> {
            // before any API request, check for an X-API-AUTH header
            // the value of the X-API-AUTH header must match the value displayed on the GUI

            boolean authNeeded = true;

            // by default return json - just in case there is an error
            response.header("content-type", "application/json");

            if(request.pathInfo().contentEquals("/apps/pulp/api/heartbeat")){
                // does not require auth
                authNeeded = false;
            }


            if(request.pathInfo().contentEquals("/apps/pulp/api/session")){
                // does not require auth... but probably should need an admin password or summat!
                authNeeded = false;
            }

            if(authNeeded) {
                if (request.headers("X-API-AUTH") == null) {
                    halt(401, apiEntityResponse(response, new EntityResponse().setErrorStatus(401, "You need to add the X-API-AUTH header with the secret key shown in the GUI")));
                }

                // check if the API AUTH is known to us
                Session session = sessions.getSessionForX_API_AUTH(request.headers("X-API-AUTH"));
                if (session == null) {
                    halt(401, apiEntityResponse(response, new EntityResponse().setErrorStatus(401, "X-API-AUTH header is invalid - check in the GUI")));
                }
            }

            // delete any expired sessions for tidy up
            sessions.deleteExpiredAPISessions();

            if(authNeeded) {
                final String jsessionid = sessions.getSessionCookieForApi(request.headers("X-API-AUTH"));
                if (jsessionid != null && jsessionid.length() > 0) {
                    Collection<String> cookieValues = sessions.getCookieValueFromRequest(request,"JSESSIONID");
                    if(!cookieValues.contains(jsessionid)) {
                        response.header("Set-Cookie", "JSESSIONID=" + jsessionid);
                    }
                }
            }
        });

        path("/apps/pulp/api/session", () -> {

            // shhh secret don't tell anyone about this end point
            options("", (req, res) -> {
                res.header("Allow", "OPTIONS, POST");
                return apiEntityResponse(res, new EntityResponse().setSuccessStatus(201, "{}"));
            });

            post("", (req, res) -> {
                String authCode = req.headers("X-API-AUTH");
                if(authCode==null || authCode.length()==0){
                    halt(401, apiEntityResponse(res, new EntityResponse().setErrorStatus(401, "Invalid X-API-AUTH header")));
                }
                Session session = null;
                session = sessions.getSessionForX_API_AUTH(authCode);
                if(session!=null){
                    // session exists
                    halt(204, "");
                }

                if(sessions.createSessionFor(req, authCode)!=null){
                    return apiEntityResponse(res, new EntityResponse().setSuccessStatus(201, ""));
                }else{
                    halt(500, apiEntityResponse(res, new EntityResponse().setErrorStatus(500, "Could not create session for " + authCode)));
                }

                return "";
            });

            get("",     (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            put("",     (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            delete("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            trace("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            patch("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
        });

        path("/apps/pulp/api/authors/:authorid", () -> {

            options("", (req, res) -> {
                res.header("Allow", "OPTIONS, GET, HEAD, DELETE, PUT, PATCH");
                return apiEntityResponse(res, new EntityResponse().setSuccessStatus(201, "{}"));
            });

            head("", (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().getAuthor(req.params(":authorid"), req.headers("Accept"));
                return apiEmptyEntityResponse(res, response);
            });

            get("", (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().getAuthor(req.params(":authorid"), req.headers("Accept"));
                return apiEntityResponse(res, response);
            });

            delete("",  (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().deleteAuthor(req.params(":authorid"),req.headers("Accept"));
                return apiEntityResponse(res, response);
            });

            put("",     (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().createReplaceAuthor(req.params(":authorid"),req.body(),req.headers("content-type"),req.headers("Accept"));
                return apiEntityResponse(res, response);
            });

            patch("",  (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().patchAuthor(req.params(":authorid"),req.body(),req.headers("content-type"),req.headers("Accept"));
                return apiEntityResponse(res, response);
            });

            post("",     (req, res) -> {  return apiEntityResponse(res, notAllowed);});  // TODO: POST would be the same as PUT - unless we add other fields to an author e.g. description, DOB, Date of Death
            trace("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});

        });

        path("/apps/pulp/api/authors", () -> {

            options("", (req, res) -> {
                res.header("Allow", "OPTIONS, GET, HEAD, POST");
                return apiEntityResponse(res, new EntityResponse().setSuccessStatus(200, "{}"));
            });

            head("", (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().getAuthors(req.headers("Accept"));
                return apiEmptyEntityResponse(res, response);
            });

            get("", (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().getAuthors(req.headers("Accept"));
                return apiEntityResponse(res, response);
            });

            post("",     (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().createAmendAuthor(req.body(),req.headers("content-type"),req.headers("Accept"));
                return apiEntityResponse(res, response);

            });
            put("",     (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            delete("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            trace("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            patch("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
        });

        path("/apps/pulp/api/books/:bookid", () -> {

            options("", (req, res) -> {
                res.header("Allow", "OPTIONS, GET, HEAD, DELETE, PUT, PATCH");
                return apiEntityResponse(res, new EntityResponse().setSuccessStatus(200, "{}"));
            });

            head("", (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().getBook(req.params(":bookid"),req.headers("Accept"));
                return apiEmptyEntityResponse(res, response);
            });

            get("", (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().getBook(req.params(":bookid"),req.headers("Accept"));
                return apiEntityResponse(res, response);
            });

            delete("",  (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().deleteBook(req.params(":bookid"),req.headers("Accept"));
                return apiEntityResponse(res, response);
            });

            put("",     (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().createReplaceBook(req.params(":bookid"),req.body(),req.headers("content-type"),req.headers("Accept"));
                return apiEntityResponse(res, response);
            });

            patch("",     (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().patchBook(req.params(":bookid"),req.body(),req.headers("content-type"),req.headers("Accept"));
                return apiEntityResponse(res, response);
            });

            post("",     (req, res) -> {  return apiEntityResponse(res, notAllowed);}); // TODO: a POST could partially amend a book
            trace("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
        });

        path("/apps/pulp/api/books", () -> {

            options("", (req, res) -> {
                res.header("Allow", "OPTIONS, GET, HEAD, POST");
                return apiEntityResponse(res, new EntityResponse().setSuccessStatus(200, "{}"));
            });

            head("", (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().getBooks(req.headers("Accept"));
                return apiEmptyEntityResponse(res, response);
            });

            get("", (req, res) -> {

                String how= "contains";
                String what="title";
                String forTerm="";

                if(req.queryMap().hasKeys() && req.queryMap().value("searchterm")!=null){
                    forTerm=req.queryMap().value("searchterm");
                }
                if(req.queryMap().hasKeys() && req.queryMap().value("what")!=null){
                    what=req.queryMap().value("what");
                }
                if(req.queryMap().hasKeys() && req.queryMap().value("how")!=null){
                    how=req.queryMap().value("how");
                }

                EntityResponse response;

                if(forTerm!=null && forTerm.length()>0){
                    response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().getBooks(req.headers("Accept"), what, how, forTerm);
                }else {
                    response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().getBooks(req.headers("Accept"));
                }
                return apiEntityResponse(res, response);
            });

            post("",     (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().createAmendBook(req.body(),req.headers("content-type"),req.headers("Accept"));
                return apiEntityResponse(res, response);

            });

            put("",     (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            delete("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            trace("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            patch("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
        });

        path("/apps/pulp/api/series/:seriesid", () -> {

            options("", (req, res) -> {
                res.header("Allow", "OPTIONS, GET, HEAD, DELETE, PUT, PATCH");
                return apiEntityResponse(res, new EntityResponse().setSuccessStatus(200, "{}"));
            });

            head("", (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().getSeries(req.params(":seriesid"),req.headers("Accept"));
                return apiEmptyEntityResponse(res, response);
            });

            get("", (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().getSeries(req.params(":seriesid"),req.headers("Accept"));
                return apiEntityResponse(res, response);
            });

            delete("",  (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().deleteSeries(req.params(":seriesid"),req.headers("Accept"));
                return apiEntityResponse(res, response);
            });

            put("",     (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().createReplaceSeries(req.params(":seriesid"),req.body(),req.headers("content-type"),req.headers("Accept"));
                return apiEntityResponse(res, response);
            });


            patch("",     (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().patchSeries(req.params(":seriesid"),req.body(),req.headers("content-type"),req.headers("Accept"));
                return apiEntityResponse(res, response);
            });

            post("",     (req, res) -> {  return apiEntityResponse(res, notAllowed);}); // TODO: POST would be the same as PUT - unless we add other fields to a series e.g. description
            trace("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            patch("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
        });

        path("/apps/pulp/api/series", () -> {

            head("", (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().getAllSeries(req.headers("Accept"));
                return apiEmptyEntityResponse(res, response);
            });

            get("", (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().getAllSeries(req.headers("Accept"));
                return apiEntityResponse(res, response);
            });

            options("", (req, res) -> {
                res.header("Allow", "OPTIONS, GET, HEAD, POST");
                return apiEntityResponse(res, new EntityResponse().setSuccessStatus(200, "{}"));
            });

            post("",     (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().createAmendSeries(req.body(),req.headers("content-type"),req.headers("Accept"));
                return apiEntityResponse(res, response);
            });
            put("",     (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            delete("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            trace("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            patch("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
        });


        path("/apps/pulp/api/publishers/:publisherid", () -> {

            options("", (req, res) -> {
                res.header("Allow", "OPTIONS, GET, HEAD, DELETE, PUT, PATCH");
                return apiEntityResponse(res, new EntityResponse().setSuccessStatus(200, "{}"));
            });


            head("", (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().getPublisher(req.params(":publisherid"),req.headers("Accept"));
                return apiEmptyEntityResponse(res, response);
            });

            get("", (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().getPublisher(req.params(":publisherid"),req.headers("Accept"));
                return apiEntityResponse(res, response);
            });

            delete("",  (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().deletePublisher(req.params(":publisherid"),req.headers("Accept"));
                return apiEntityResponse(res, response);
            });

            put("",     (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().createReplacePublisher(req.params(":publisherid"),req.body(),req.headers("content-type"),req.headers("Accept"));
                return apiEntityResponse(res, response);
            });

            patch("",     (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().patchPublisher(req.params(":publisherid"),req.body(),req.headers("content-type"),req.headers("Accept"));
                return apiEntityResponse(res, response);
            });

            post("",     (req, res) -> {  return apiEntityResponse(res, notAllowed);}); // TODO: post would be the same as put, unless it has more fields e.g. description, aliases, founded, closed, etc.
            trace("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});

        });

        path("/apps/pulp/api/publishers", () -> {

            head("", (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().getPublishers(req.headers("Accept"));
                return apiEmptyEntityResponse(res, response);
            });

            get("", (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().getPublishers(req.headers("Accept"));
                return apiEntityResponse(res, response);
            });

            options("", (req, res) -> {
                res.header("Allow", "OPTIONS, GET, HEAD, POST");
                return apiEntityResponse(res, new EntityResponse().setSuccessStatus(200, "{}"));
            });

            post("",     (req, res) -> {
                final EntityResponse response = sessions.getPulpAppForApi(req.headers("X-API-AUTH")).entities().createAmendPublisher(req.body(),req.headers("content-type"),req.headers("Accept"));
                return apiEntityResponse(res, response);
            });
            put("",     (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            delete("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            trace("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            patch("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
        });

        path("/apps/pulp/api/heartbeat", () -> {

            head("", (req, res) -> {
                res.status(200);
                return "";
            });

            get("", (req, res) -> {
                res.status(204);
                return "";
            });

            options("", (req, res) -> {
                res.header("Allow", "OPTIONS, GET, HEAD");
                res.status(204);
                return "";
            });

            post("",     (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            put("",     (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            delete("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            trace("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});
            patch("",  (req, res) -> {  return apiEntityResponse(res, notAllowed);});

        });

        // TODO: API Session handling (create from API, use from GUI)

        // TODO: xstream for xml to honour content-type and accept headers

        // TODO: Future API End Points

        // /apps/pulp/api/xapiauth/:auth does it exist? 204, 404
            // create a /gui link that allows us to set the JSESSION id and see a specific API session i.e. start with API and move to GUI
            // {"JSESSIONID":"sessionid","guiurl":"url"}  // also add a LOCATION header so can trigger from the browser
        // /apps/pulp/api/xapiauth get an API session

        // GET
        // /apps/pulp/api/search
        // /apps/pulp/api/publishers/id/series
        // /apps/pulp/api/publishers/id/authors
        // /apps/pulp/api/publishers/id/books
        // /apps/pulp/api/series/id/books
        // /apps/pulp/api/series/id/authors
        // /apps/pulp/api/authors/id/books
        // /apps/pulp/api/authors/id/publishers

        // TODO paging


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

        get("/apps/pulp/gui/view/author", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return pulpApp.page().viewAuthorPage(req.queryParams("author")).asHTMLString();
        });

        before("/apps/pulp/gui/*", (request, response) -> {

            final PulpApp pulpApp = sessions.getPulpApp(request);

            // make sure the session id is added as an x-api-auth cookie to allow api usage
            final String secret = pulpApp.getAPISecret();
            final Collection<String> x_api_auth = sessions.getCookieValueFromRequest(request, "X-API-AUTH");
            if(!x_api_auth.contains(secret)){
                response.header("Set-Cookie", "X-API-AUTH=" + secret + "; PATH=/");
            }
        });

        get("/apps/pulp/gui/clean/session", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            pulpApp.reset();
            pulpApp.readData(new SavageReader("/data/pulp/doc_savage.csv"));
            pulpApp.readData(new SpiderReader("/data/pulp/the_spider.csv"));
            pulpApp.readData(new TheAvengerReader("/data/pulp/the_avenger.csv"));
            res.redirect("/apps/pulp/");
            return "";
        });

        get("/apps/pulp/gui/create/author", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return pulpApp.page().createAuthorPage().asHTMLString();
        });

        post("/apps/pulp/gui/create/author", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return new CreateFlowsHandler(pulpApp).authorCreate(req, res);
        });

        ///apps/pulp/gui/amend/author?author=id
        get("/apps/pulp/gui/amend/author", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return pulpApp.page().amendAuthorPage(req.queryParams("author")).asHTMLString();
        });

        post("/apps/pulp/gui/amend/author", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return new AmendFlowsHandler(pulpApp).authorAmend(req, res);
        });

        post("/apps/pulp/gui/amend/deleteauthor", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            pulpApp.books().deleteAuthor(req.queryParams("authorid"));
            res.redirect("/apps/pulp/gui/reports/authors/list/navigation");
            return "";
        });

        get("/apps/pulp/gui/view/series", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return pulpApp.page().viewSeriesPage(req.queryParams("series")).asHTMLString();
        });

        get("/apps/pulp/gui/create/series", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return pulpApp.page().createSeriesPage().asHTMLString();
        });

        post("/apps/pulp/gui/create/series", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return new CreateFlowsHandler(pulpApp).seriesCreate(req, res);
        });

        ///apps/pulp/gui/amend/series?series=id
        get("/apps/pulp/gui/amend/series", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            String seriesid ="";
            seriesid=req.queryParams("series");
            return pulpApp.page().amendSeriesPage(seriesid).asHTMLString();
        });

        post("/apps/pulp/gui/amend/series", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return new AmendFlowsHandler(pulpApp).seriesAmend(req, res);
        });

        post("/apps/pulp/gui/amend/deleteseries", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            pulpApp.books().deleteSeries(req.queryParams("seriesid"));
            res.redirect("/apps/pulp/gui/reports/series/list/navigation");
            return "";
        });

        get("/apps/pulp/gui/view/publisher", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return pulpApp.page().viewPublisherPage(req.queryParams("publisher")).asHTMLString();
        });

        get("/apps/pulp/gui/create/publisher", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return pulpApp.page().createPublisherPage().asHTMLString();
        });

        post("/apps/pulp/gui/create/publisher", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return new CreateFlowsHandler(pulpApp).publisherCreate(req, res);
        });

        ///apps/pulp/gui/amend/publisher?publisher=id
        get("/apps/pulp/gui/amend/publisher", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return pulpApp.page().amendPublisherPage(req.queryParams("publisher")).asHTMLString();
        });

        post("/apps/pulp/gui/amend/publisher", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return new AmendFlowsHandler(pulpApp).publisherAmend(req, res);
        });

        post("/apps/pulp/gui/amend/deletepublisher", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            pulpApp.books().deletePublisher(req.queryParams("publisherid"));
            res.redirect("/apps/pulp/gui/reports/publishers/list/navigation");
            return "";
        });

        get("/apps/pulp/gui/view/book", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return pulpApp.page().viewBookPage(req.queryParams("book")).asHTMLString();
        });


        get("/apps/pulp/gui/create/book", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return pulpApp.page().createBookPage().asHTMLString();
        });

        post("/apps/pulp/gui/create/book", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return new CreateFlowsHandler(pulpApp).bookCreate(req, res);
        });

        ///apps/pulp/gui/amend/book?book=id
        get("/apps/pulp/gui/amend/book", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return pulpApp.page().amendBookPage(req.queryParams("book")).asHTMLString();
        });

        post("/apps/pulp/gui/amend/book", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return new AmendFlowsHandler(pulpApp).bookAmend(req, res);
        });

        post("/apps/pulp/gui/amend/deletebook", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            pulpApp.books().deleteBook(req.queryParams("bookid"));
            res.redirect("/apps/pulp/gui/reports/books/table/navigation");
            return "";
        });

        get("/apps/pulp/gui/reports/books/list/navigation", (req, res) -> {

            BookFilter filter = BookFilterFromQueryMap.getBookFilter(req.queryMap());
            final PulpApp pulpApp = sessions.getPulpApp(req);
            final ReportConfig config = new ReportConfig(pulpApp.reports().getReportConfig());

            if(req.queryMap().hasKeys() && req.queryMap().value("faqs")!=null){
                config.setIncludeFaqLinks(true);
            }else{
                config.setIncludeFaqLinks(false);
            }

            config.showAmendLinks(false);
            config.allowDelete(false);

            if(pulpApp.getAppVersion().are(AMEND_LINKS_SHOWN_IN_LIST)) {
                config.showBookAmendLink(true);
            }
            if(pulpApp.getAppVersion().are(DELETE_LINKS_SHOWN_IN_LIST)){
                // list doesn't have enough room - only show in table
                // config.setAllowDeleteBook(true);
            }


            // TODO if filtered at all then show authors, publishers, series and years as links

            config.setTitlesAreLinks(true);

            return pulpApp.reports(config).configurePostFixPath("/list/navigation").getBooksAsHtmlList(filter);
        });

        get("/apps/pulp/gui/reports/books/list/navigation/faqs", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);

            BookFilter filter = BookFilterFromQueryMap.getBookFilter(req.queryMap());
            final ReportConfig config = new ReportConfig(pulpApp.reports().getReportConfig());

            config.setIncludeFaqLinks(true);
            config.showAmendLinks(false);
            config.setTitlesAreLinks(false);
            config.allowDelete(false);

            return pulpApp.reports(config).configurePostFixPath("/list/navigation").getBooksAsHtmlList(filter);
        });

        get("/apps/pulp/gui/reports/books/list/static", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            BookFilter filter = BookFilterFromQueryMap.getBookFilter(req.queryMap());
            return pulpApp.stringReports().getBooksAsHtmlList(filter);
        });

        get("/apps/pulp/gui/reports/books/table/navigation", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            BookFilter filter = BookFilterFromQueryMap.getBookFilter(req.queryMap());

            final ReportConfig config = new ReportConfig(pulpApp.reports().getReportConfig());

            config.showAmendLinks(false);

            if(pulpApp.getAppVersion().are(AMEND_LINKS_SHOWN_IN_LIST)){
                config.showBookAmendLink(true);
            }
            if(pulpApp.getAppVersion().are(DELETE_LINKS_SHOWN_IN_LIST)){
                // table has enough space to allow delete button rendering
                config.setAllowDeleteBook(true);
            }

            config.setTitlesAreLinks(true);
            config.allowDelete(false);

            return pulpApp.reports(config).configurePostFixPath("/table/navigation").getBooksAsHtmlTable(filter);
        });

        get("/apps/pulp/gui/reports/books/table/static", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            BookFilter filter = BookFilterFromQueryMap.getBookFilter(req.queryMap());
            return pulpApp.stringReports().getBooksAsHtmlTable(filter);
        });

        get("/apps/pulp/gui/reports/:type/faqs", (req, res) -> {

            final PulpApp pulpApp = sessions.getPulpApp(req);
            String typeOfFaq = req.params(":type");
            String forTerm="";
            boolean showiframe=false;

            if(req.queryMap().hasKeys() && req.queryMap().value("searchterm")!=null){
                forTerm=req.queryMap().value("searchterm");
            }
            if(req.queryMap().hasKeys() && req.queryMap().value("iframe")!=null){
                showiframe=true;
            }

            return pulpApp.page().getFaqRenderPage(typeOfFaq, forTerm, showiframe).asHTMLString();

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

            final PulpApp pulpApp = sessions.getPulpApp(req);

            ReportConfig config = new ReportConfig(pulpApp.reports().getReportConfig());
            config.setPostFixPath("/list/navigation");
            config.showAmendLinks(false);
            config.allowDelete(false);
            config.setTitlesAreLinks(true);
            config.setYearsAsLinks(true);
            config.setSeriesNamesLinks(true);
            config.setPublisherNamesLinks(true);
            config.setAuthorNamesLinks(true);

            // TODO should be able to configure links to author, series, publisher to the Amend screen rather than a list filter

            return pulpApp.page().getAlertSearchPage().setConfirmSearch(confirmSearch)
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

            final PulpApp pulpApp = sessions.getPulpApp(req);

            BookFilter filter = BookFilterFromQueryMap.getBookFilter(req.queryMap());
            return pulpApp.page().getFilterTestPage(isList, navigation, canSearch, isPaginated).setFilter(filter).setShowData(req.queryMap().hasKeys()).
                    setShowThese(templateElements).
                    asHTMLString();
        });

        get("/apps/pulp/gui/admin/version/*", (req, res) -> {

            //request.splat()[0] is version
            try {
                int version = Integer.parseInt(req.splat()[0]);
                final PulpApp pulpApp = sessions.getPulpApp(req);
                pulpApp.setAppVersion(version);
            }catch(Exception e){
                res.status(400);
                // todo need an error page, handle 404, exceptions etc.
                return "<h1>What version was that?</h1>";
            }

            res.redirect("/apps/pulp/gui");
            return "";

        });

        get("/apps/pulp/gui/reports/authors/list/navigation", (req, res) -> {

            final PulpApp pulpApp = sessions.getPulpApp(req);

            final ReportConfig config = new ReportConfig(pulpApp.reports().getReportConfig());

            if(req.queryMap().hasKeys() && req.queryMap().value("faqs")!=null){
                config.setIncludeFaqLinks(true);
            }else{
                config.setIncludeFaqLinks(false);
            }


            config.setAuthorNamesLinks(true);
            config.allowDelete(false);

            if(pulpApp.getAppVersion().are(AMEND_LINKS_SHOWN_IN_LIST)) {
                config.showAmendLinks(true);
            }
            if(pulpApp.getAppVersion().are(DELETE_LINKS_SHOWN_IN_LIST)){
                config.setAllowDeleteAuthor(true);
            }


            // possibly if filtered then allow delete etc..


            return pulpApp.reports(config).configurePostFixPath("/list/navigation").getAuthorsAsHtmlList();
        });

        get("/apps/pulp/gui/reports/authors/list/navigation/faqs", (req, res) -> {

            final PulpApp pulpApp = sessions.getPulpApp(req);

            final ReportConfig config = new ReportConfig(pulpApp.reports().getReportConfig());

            config.setIncludeFaqLinks(true);
            config.showAmendLinks(false);
            config.setTitlesAreLinks(false);

            return pulpApp.reports(config).configurePostFixPath("/list/navigation").getAuthorsAsHtmlList();
        });

        get("/apps/pulp/gui/reports/publishers/list/navigation", (req, res) -> {

            final PulpApp pulpApp = sessions.getPulpApp(req);
            final ReportConfig config = new ReportConfig(pulpApp.reports().getReportConfig());

            if(req.queryMap().hasKeys() && req.queryMap().value("faqs")!=null){
                config.setIncludeFaqLinks(true);
            }else{
                config.setIncludeFaqLinks(false);
            }


            config.setPublisherNamesLinks(true);
            config.allowDelete(false);

            if(pulpApp.getAppVersion().are(AMEND_LINKS_SHOWN_IN_LIST)) {
                config.showAmendLinks(true);
            }
            if(pulpApp.getAppVersion().are(DELETE_LINKS_SHOWN_IN_LIST)){
                config.setAllowDeletePublisher(true);
            }


            return pulpApp.reports(config).configurePostFixPath("/list/navigation").getPublishersAsHtmlList();
        });

        get("/apps/pulp/gui/reports/publishers/list/navigation/faqs", (req, res) -> {

            final PulpApp pulpApp = sessions.getPulpApp(req);
            final ReportConfig config = new ReportConfig(pulpApp.reports().getReportConfig());

            config.setIncludeFaqLinks(true);
            config.showAmendLinks(false);
            config.setTitlesAreLinks(false);

            return pulpApp.reports(config).configurePostFixPath("/list/navigation").getPublishersAsHtmlList();
        });

        get("/apps/pulp/gui/reports/years/list/navigation", (req, res) -> {

            final PulpApp pulpApp = sessions.getPulpApp(req);
            final ReportConfig config = new ReportConfig(pulpApp.reports().getReportConfig());

            if(req.queryMap().hasKeys() && req.queryMap().value("faqs")!=null){
                config.setIncludeFaqLinks(true);
            }else{
                config.setIncludeFaqLinks(false);
            }

            config.showAmendLinks(false);
            config.setYearsAsLinks(true);

            return pulpApp.reports(config).configurePostFixPath("/list/navigation").getYearsAsHtmlList();
        });

        get("/apps/pulp/gui/reports/years/list/navigation/faqs", (req, res) -> {

            final PulpApp pulpApp = sessions.getPulpApp(req);
            final ReportConfig config = new ReportConfig(pulpApp.reports().getReportConfig());

            config.setIncludeFaqLinks(true);
            config.showAmendLinks(false);
            config.setTitlesAreLinks(false);

            return pulpApp.reports(config).configurePostFixPath("/list/navigation").getYearsAsHtmlList();
        });

        get("/apps/pulp/gui/reports/series/list/navigation", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            final ReportConfig config = new ReportConfig(pulpApp.reports().getReportConfig());

            if(req.queryMap().hasKeys() && req.queryMap().value("faqs")!=null){
                config.setIncludeFaqLinks(true);
            }else{
                config.setIncludeFaqLinks(false);
            }

            config.showAmendLinks(false);
            config.setSeriesNamesLinks(true);
            config.allowDelete(false);

            if(pulpApp.getAppVersion().are(AMEND_LINKS_SHOWN_IN_LIST)) {
                config.showAmendLinks(true);
            }
            if(pulpApp.getAppVersion().are(DELETE_LINKS_SHOWN_IN_LIST)){
                config.setAllowDeleteSeries(true);
            }

            return pulpApp.reports(config).configurePostFixPath("/list/navigation").getSeriesNamesAsHtmlList();
        });

        get("/apps/pulp/gui/reports/series/list/navigation/faqs", (req, res) -> {

            final PulpApp pulpApp = sessions.getPulpApp(req);
            final ReportConfig config = new ReportConfig(pulpApp.reports().getReportConfig());

            config.setIncludeFaqLinks(true);
            config.showAmendLinks(false);

            config.setTitlesAreLinks(false);
            return pulpApp.reports(config).configurePostFixPath("/list/navigation").getSeriesNamesAsHtmlList();
        });

        get("/apps/pulp/gui/reports/authors/list/static", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return pulpApp.stringReports().getAuthorsAsHtmlList();});
        get("/apps/pulp/gui/reports/publishers/list/static", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return pulpApp.stringReports().getPublishersAsHtmlList();});
        get("/apps/pulp/gui/reports/years/list/static", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return pulpApp.stringReports().getYearsAsHtmlList();});
        get("/apps/pulp/gui/reports/series/list/static", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return pulpApp.stringReports().getSeriesNamesAsHtmlList();});


        get("/apps/pulp/", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return pulpApp.reports().getIndexPage();});
        get("/apps/pulp/gui", (req, res) -> { res.redirect("/apps/pulp/gui/"); return "";});
        get("/apps/pulp/gui/", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return pulpApp.reports().getIndexPage();});
        get("/apps/pulp/gui/help", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return pulpApp.reports().getHelpPage();});
        get("/apps/pulp/gui/menu/books", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return pulpApp.reports().getSnippetPage("Books Menu", "menu-screen-books-reports-list.html");});
        get("/apps/pulp/gui/menu/authors", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return pulpApp.reports().getSnippetPage("Authors Menu", "menu-screen-authors-reports-list.html");});
        get("/apps/pulp/gui/menu/publishers", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return pulpApp.reports().getSnippetPage("Publishers Menu", "menu-screen-publishers-reports-list.html");});
        get("/apps/pulp/gui/menu/series", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return pulpApp.reports().getSnippetPage("Series Menu", "menu-screen-series-reports-list.html");});
        get("/apps/pulp/gui/menu/years", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return pulpApp.reports().getSnippetPage("Years Menu", "menu-screen-years-reports-list.html");});
        get("/apps/pulp/gui/menu/create", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return pulpApp.reports().getSnippetPage("Create Menu", "menu-screen-create.html");});
        get("/apps/pulp/gui/menu/reports", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return pulpApp.reports().getSnippetPage("Reports Menu", "menu-screen-static-reports.html");});
        get("/apps/pulp/gui/menu/admin", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return pulpApp.reports().getSnippetPage("Admin Menu", "menu-screen-admin.html");});


        get("/apps/pulp/gui/reports", (req, res) -> { res.redirect("/apps/pulp/gui/reports/"); return "";});
        get("/apps/pulp/gui/reports/", (req, res) -> {
            final PulpApp pulpApp = sessions.getPulpApp(req);
            return pulpApp.reports().getIndexPage();});
        get("/apps/pulp/gui/reports/books", (req, res) -> { res.redirect("/apps/pulp/gui/reports/"); return "";});
    }



}
