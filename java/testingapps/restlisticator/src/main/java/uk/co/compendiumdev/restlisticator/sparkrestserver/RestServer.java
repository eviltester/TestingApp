package uk.co.compendiumdev.restlisticator.sparkrestserver;

import spark.Request;
import spark.Response;
import spark.Session;
import uk.co.compendiumdev.Main;
import uk.co.compendiumdev.restlisticator.api.Api;
import uk.co.compendiumdev.restlisticator.domain.app.TheListicator;
import uk.co.compendiumdev.restlisticator.http.ApiEndPoint;
import uk.co.compendiumdev.restlisticator.testappconfig.FeatureToggles;

import java.util.Timer;
import java.util.TimerTask;

import static spark.Spark.*;

public class RestServer {

    Api theapi = new Api(new TheListicator());

    // by default running on port 4567
    private Integer theProxyPort=4567;

    // 30 seconds - this is settable with an environment variable or property or command line arg
    private Integer MAX_SESSION_INACTIVE_INTERVAL = 30; // in seconds




    private Api getApi(final Request request, final Response response) {
        // rather than multiuser and single user mode allow /sessionid to create new api within the session for the user
        Session session = request.session(false);

        final String headervalue = request.headers("X-SESSIONID");

        // if there is a current X-SESSIONID then check if it is valid and if so, return it
        if (headervalue != null && headervalue.length() > 0) {
            // There is a header, if there is no session then it may have expired or we may not have started it

            // if X-SESSIONID does not match current session then provide a 403
            if(session == null || !session.id().equalsIgnoreCase(headervalue)){
                response.header("WWW-Authenticate", "Bearer realm=\"restlisticator\", error=\"X-SESSIONID_header_mismatch\", error_description=\"X-SESSIONID header does not match session details, perhaps your session expired?\"");
                halt(403);
            }else{
                // if X-SESSIONID matches current session and there is a sessionapi then return it
                if(session.attribute("SESSIONAPI")!=null) {
                    return request.session().attribute("SESSIONAPI");
                }
            }
        }

        // if there is no X-SESSIONID then return default singleton theapi
        return theapi;

    }

    public void configureRestServerRouting(String nestedPath) {


        if(nestedPath!= null && nestedPath.length()>0){
            ApiEndPoint.setUrlPrefix(nestedPath);
        }


        //port(1234);

        // to support debugging show the path we were called with
//        path("*", () -> { before("", (request, response) -> {
//                // System.out.println("Received Request " + request.pathInfo());
//
//            });
//        });

        get(ApiEndPoint.getUrlPrefix()+"/sessionid", (request, response) -> {
            response.status(200);

            Session session = request.session(false); // get the current session

            if(session == null) {

                // we do not have a session so crate one of limited duration identified by the X-SESSIONID
                session = request.session();
                response.header("X-SESSIONID", request.session().id());

                session.maxInactiveInterval(MAX_SESSION_INACTIVE_INTERVAL);

                // Create app and add it to the session
                Api anApi = new Api(new TheListicator());
                anApi.setDocumentationDetails(theProxyPort, ApiEndPoint.getUrlPrefix());
                session.attribute("SESSIONAPI",  anApi);

            }else{
                // existing session - remind user of the session id
                response.header("X-SESSIONID", request.session().id());
            }

            return "";
        });

        // Documentation
        get(ApiEndPoint.DOCUMENTATION.getPath(), (request, response) -> {
            // can get documentation without an X-SESSIONID because it is through the browser
            return theapi.getDocumentation(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });


        // /heartbeat
        get(ApiEndPoint.HEARTBEAT.getPath(), (request, response) -> {return getApi(request,response).getHeartbeat(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
            });
        options(ApiEndPoint.HEARTBEAT.getPath(), (request, response) -> { response.header("Allow", "GET"); response.status(200); return "";});
        path(ApiEndPoint.HEARTBEAT.getPath(), () -> {
            before("", (request, response) -> {
                // TODO distinguish between 401 Unauthorized (no credentials supplied), 403 Forbidden and 405 Not Allowed
                if(!getApi(request,response).isMethodAllowed(ApiEndPoint.HEARTBEAT.getPath(),new SparkApiRequest(request))){
                    halt(405);
                }
            });
        });
        

        // /lists
        get(ApiEndPoint.LISTS.getPath(), (request, response) -> {
            return getApi(request,response).getLists(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        post(ApiEndPoint.LISTS.getPath(), (request, response) -> {
            return getApi(request,response).setLists(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        put(ApiEndPoint.LISTS.getPath(), (request, response) -> {
            return getApi(request,response).putList(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        options(ApiEndPoint.LISTS.getPath(), (request, response) -> { response.header("Allow", "GET, POST, PUT"); response.status(200); return "";});
        // catch unallowed verbs on /lists
        path(ApiEndPoint.LISTS.getPath(), () -> {
            before("", (request, response) -> {
                if(!getApi(request,response).isMethodAllowed(ApiEndPoint.LISTS.getPath(),new SparkApiRequest(request))){
                    halt(405);
                }
            });
        });

        // /lists/*
        get(ApiEndPoint.LISTS.getPath("*"), (request, response) -> {
            return getApi(request,response).getList(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        put(ApiEndPoint.LISTS.getPath("*"), (request, response) -> {
            return getApi(request,response).putList(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        post(ApiEndPoint.LISTS.getPath("*"), (request, response) -> {
            return getApi(request,response).partialAmendList(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        patch(ApiEndPoint.LISTS.getPath("*"), (request, response) -> {
            return getApi(request,response).patchAmendList(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        delete(ApiEndPoint.LISTS.getPath("*"), (request, response) -> {
            return getApi(request,response).deleteList(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        // since this doesn't check the value this is really a bug and should only return 200 when the entity exists, and 404 when it doesn't
        // TODO - POST here is a bug and should be feature toggle, able
        options(ApiEndPoint.LISTS.getPath("*"), (request, response) -> { response.header("Allow", "GET, POST, PUT, PATCH, DELETE"); response.status(200); return "";});
        path(ApiEndPoint.LISTS.getPath("*"), () -> {
            before("", (request, response) -> {
                if(!getApi(request,response).isMethodAllowed(ApiEndPoint.LISTS.getPath("*"),new SparkApiRequest(request))){
                    halt(405);
                }
            });
        });


        get(ApiEndPoint.USERS.getPath(), (request, response) -> {
            return getApi(request,response).getUsers(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        post(ApiEndPoint.USERS.getPath(), (request, response) -> {
            return getApi(request,response).createUserDetails(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        options(ApiEndPoint.USERS.getPath(), (request, response) -> { response.header("Allow", "GET, POST"); response.status(200); return "";});

        put(ApiEndPoint.USERS.getPath("*/password"), (request, response) -> {
            return getApi(request,response).setUserPassword(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        options(ApiEndPoint.USERS.getPath("*/password"), (request, response) -> { response.header("Allow", "GET, PUT"); response.status(200); return "";});

        put(ApiEndPoint.USERS.getPath("*/apikey"), (request, response) -> {
            return getApi(request,response).setUserApiKey(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        options(ApiEndPoint.USERS.getPath("*/apikey"), (request, response) -> { response.header("Allow", "GET, PUT"); response.status(200); return "";});


        get(ApiEndPoint.USERS.getPath("*"), (request, response) -> {
            return getApi(request,response).getUserDetails(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        options(ApiEndPoint.USERS.getPath("*"), (request, response) -> { response.header("Allow", "GET"); response.status(200); return "";});

        path(ApiEndPoint.USERS.getPath("*"), () -> {
            before("", (request, response) -> {
                String pathToCheck = "*";
                if(request.splat()!=null && request.splat()[0].endsWith("/password")){
                    pathToCheck = "*/password";
                }
                if(request.splat()!=null && request.splat()[0].endsWith("/apikey")){
                    pathToCheck = "*/apikey";
                }
                if(!getApi(request,response).isMethodAllowed(ApiEndPoint.USERS.getPath(pathToCheck),new SparkApiRequest(request))){
                    halt(405);
                }
            });
        });



        get(ApiEndPoint.FEATURE_TOGGLES.getPath(), (request, response) -> {
            return getApi(request,response).getFeatureToggles(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        // Super Admin only can amend feature toggles
        post(ApiEndPoint.FEATURE_TOGGLES.getPath(), (request, response) -> {
            return getApi(request,response).setFeatureToggles(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });

        options(ApiEndPoint.FEATURE_TOGGLES.getPath(), (request, response) -> { response.header("Allow", "GET, POST"); response.status(200); return "";});
        path(ApiEndPoint.FEATURE_TOGGLES.getPath(), () -> {
            before("", (request, response) -> {
                if(!getApi(request,response).isMethodAllowed(ApiEndPoint.FEATURE_TOGGLES.getPath(),new SparkApiRequest(request))){
                    halt(405);
                }
            });
        });
        // TODO: fix 404 bug, this should really return an error message in JSON or XML if the desired response is in xml or json - currently always uses HTML
    }




    public RestServer(String nestedPath){
        configureRestServerRouting(nestedPath);
    }

    public RestServer(String[] args, String nestedPath) {

        int resetTimer = 0;

        // process any command line arguments which apply to Rest listicator
        for(String arg : args){
            System.out.println("Args: " + arg);
            if(arg.startsWith("-bugfixes")){
                String[]details = arg.split("=");
                if(details!=null && details.length>1){
                    Boolean bugfixeson = Boolean.parseBoolean(details[1].trim());
                    FeatureToggles.toggleAll(bugfixeson);
                    System.out.println("All known bugs are now " + (bugfixeson ? "fixed" : "buggy"));
                }
            }
            if(arg.startsWith("-resettimer")){
                String[]details = arg.split("=");

                if(details!=null && details.length>1){
                    try {
                        resetTimer = Integer.parseInt(details[1]);
                    }catch(Exception e){
                        System.out.println("Could not reset timer to " + details[1]);
                    }
                }
            }
            if(arg.startsWith("-maxsessionseconds")){
                String[]details = arg.split("=");

                if(details!=null && details.length>1){
                    try {
                        MAX_SESSION_INACTIVE_INTERVAL = Integer.parseInt(details[1]);
                    }catch(Exception e){
                        System.out.println("Could not set max inactive interval to " + details[1]);
                    }
                }
            }

        }

        if(resetTimer>0) {
            System.out.println("Configured resetTimer every X milliseconds where 0 means timer is not set: " + resetTimer);
            scheduleResetEveryMillis(resetTimer); // 30 seconds //1000*60*3); // 3 minutes
        }

        configureRestServerRouting(nestedPath);

    }

    public void documentationDetails(Integer proxyport) {
        theapi.setDocumentationDetails(proxyport, ApiEndPoint.getUrlPrefix());
        theProxyPort = proxyport;
    }

    public Api getSingletonApi() {
        return theapi;
    }

    private void scheduleResetEveryMillis(final int milliseconds) {
        // use java in built task scheduler to reset the default api details ever X minutes
        // https://stackoverflow.com/questions/7814089/how-to-schedule-a-periodic-task-in-java
        // https://docs.oracle.com/javase/6/docs/api/java/util/Timer.html
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new APIResetTask(theapi), milliseconds, milliseconds);
    }


    private class APIResetTask extends TimerTask {

        private final Api managedApi;

        public APIResetTask(Api apiToReset){

            this.managedApi = apiToReset;

        }

        public void run() {
            try {

                System.out.println("Resetting listicator api");
                managedApi.resetApi(new TheListicator());

            } catch (Exception ex) {
                System.out.println("error running thread " + ex.getMessage());
            }
        }
    }
}
