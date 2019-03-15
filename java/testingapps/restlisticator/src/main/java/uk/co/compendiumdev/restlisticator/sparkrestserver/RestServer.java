package uk.co.compendiumdev.restlisticator.sparkrestserver;

import spark.Request;
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
    Boolean singleUserMode = true;
    private Integer theProxyPort=4567;

    // by default running on port 4567


    private Api getApi(final Request request) {
        // rather than multiuser and single user mode allow /sessionid to create new api within the session for the user
        Session session = request.session(false);
        if(session == null){
            return theapi;
        }else{

            // TODO: if X-SESSIONID matches current session and there is a sessionapi then return it
            // TODO: if X-SESSIONID does not match current session then provide a 403
            // TODO: if there is no X-SESSIONID then return theapi
            if(session.attribute("SESSIONAPI")!=null){
                return request.session().attribute("SESSIONAPI");
            }else{
                return theapi;
            }
        }


        // for a single user multi user toggle then this would be used
//        if(singleUserMode){
//            return theapi;
//        }
//        return request.session().attribute("SESSIONAPI");
    }

    public void configureRestServerRouting(String nestedPath) {


        if(nestedPath!= null && nestedPath.length()>0){
            ApiEndPoint.setUrlPrefix(nestedPath);
        }


        //port(1234);

        // to support debugging show the path we were called with
        path("*", () -> { before("", (request, response) -> {
                // System.out.println("Received Request " + request.pathInfo());

            // TODO: we might not need this... we could allow multiple users on default to make it easier for people to get started, but if you use /sessionid you get your own session
                // If not in single user mode
                    // If no X-SESSION header then not authenticated
                    // If no session matches X-SESSION then not authenticated
//                if(!singleUserMode){
//
//                    if(!request.pathInfo().equalsIgnoreCase("/sessionid") && !request.pathInfo().equalsIgnoreCase(ApiEndPoint.DOCUMENTATION.getPath())) {
//                        Session session = request.session(false); // get the current session
//                        final String headervalue = request.headers("X-SESSIONID");
//                        if (headervalue == null || headervalue.length() == 0) {
//                            response.header("WWW-Authenticate", "Bearer realm=\"restlisticator\", error=\"no_X-SESSIONID_header\", error_description=\"Message needs to have a valid X-SESSIONID header with a value that matches a valid session\"");
//                            halt(403);
//                        } else {
//                            if (session == null || !(request.session().id().equalsIgnoreCase(headervalue)) || request.session().isNew()) {
//                                response.header("WWW-Authenticate", "Bearer realm=\"restlisticator\", error=\"bad_X-SESSIONID_header\", error_description=\"X-SESSIONID header not recognised\"");
//                                halt(403);
//                            }
//                        }
//                    }
//                }

            });
        });

        get(ApiEndPoint.getUrlPrefix()+"/sessionid", (request, response) -> {
            response.status(200);

            Session session = request.session(false); // get the current session

            if(session == null) {

                session = request.session();
                response.header("X-SESSIONID", request.session().id());
                if(!singleUserMode) {

                    session.maxInactiveInterval(30); // 30 seconds - TODO: make this settable with an environment variable or property or command line arg

                    // Create app and add it to the session
                    Api anApi = new Api(new TheListicator());
                    anApi.setDocumentationDetails(theProxyPort, ApiEndPoint.getUrlPrefix());
                    session.attribute("SESSIONAPI",  anApi);
                }

            }else{
                // existing session
                response.header("X-SESSIONID", request.session().id());
            }

            return "";
        });

        // Documentation
        get(ApiEndPoint.DOCUMENTATION.getPath(), (request, response) -> {
            // TODO: generate different documentation for the multiuser mode
            // can get documentation without an X-SESSIONID because it is through the browser
            return theapi.getDocumentation(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });


        // /heartbeat
        get(ApiEndPoint.HEARTBEAT.getPath(), (request, response) -> {return getApi(request).getHeartbeat(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
            });
        options(ApiEndPoint.HEARTBEAT.getPath(), (request, response) -> { response.header("Allow", "GET"); response.status(200); return "";});
        path(ApiEndPoint.HEARTBEAT.getPath(), () -> {
            before("", (request, response) -> {
                // TODO distinguish between 401 Unauthorized (no credentials supplied), 403 Forbidden and 405 Not Allowed
                if(!getApi(request).isMethodAllowed(ApiEndPoint.HEARTBEAT.getPath(),new SparkApiRequest(request))){
                    halt(405);
                }
            });
        });
        

        // /lists
        get(ApiEndPoint.LISTS.getPath(), (request, response) -> {
            return getApi(request).getLists(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        post(ApiEndPoint.LISTS.getPath(), (request, response) -> {
            return getApi(request).setLists(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        put(ApiEndPoint.LISTS.getPath(), (request, response) -> {
            return getApi(request).putList(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        options(ApiEndPoint.LISTS.getPath(), (request, response) -> { response.header("Allow", "GET, POST, PUT"); response.status(200); return "";});
        // catch unallowed verbs on /lists
        path(ApiEndPoint.LISTS.getPath(), () -> {
            before("", (request, response) -> {
                if(!getApi(request).isMethodAllowed(ApiEndPoint.LISTS.getPath(),new SparkApiRequest(request))){
                    halt(405);
                }
            });
        });

        // /lists/*
        get(ApiEndPoint.LISTS.getPath("*"), (request, response) -> {
            return getApi(request).getList(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        put(ApiEndPoint.LISTS.getPath("*"), (request, response) -> {
            return getApi(request).putList(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        post(ApiEndPoint.LISTS.getPath("*"), (request, response) -> {
            return getApi(request).partialAmendList(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        patch(ApiEndPoint.LISTS.getPath("*"), (request, response) -> {
            return getApi(request).patchAmendList(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        delete(ApiEndPoint.LISTS.getPath("*"), (request, response) -> {
            return getApi(request).deleteList(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        // since this doesn't check the value this is really a bug and should only return 200 when the entity exists, and 404 when it doesn't
        // TODO - POST here is a bug and should be feature toggle, able
        options(ApiEndPoint.LISTS.getPath("*"), (request, response) -> { response.header("Allow", "GET, POST, PUT, PATCH, DELETE"); response.status(200); return "";});
        path(ApiEndPoint.LISTS.getPath("*"), () -> {
            before("", (request, response) -> {
                if(!getApi(request).isMethodAllowed(ApiEndPoint.LISTS.getPath("*"),new SparkApiRequest(request))){
                    halt(405);
                }
            });
        });


        get(ApiEndPoint.USERS.getPath(), (request, response) -> {
            return getApi(request).getUsers(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        post(ApiEndPoint.USERS.getPath(), (request, response) -> {
            return getApi(request).createUserDetails(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        options(ApiEndPoint.USERS.getPath(), (request, response) -> { response.header("Allow", "GET, POST"); response.status(200); return "";});

        put(ApiEndPoint.USERS.getPath("*/password"), (request, response) -> {
            return getApi(request).setUserPassword(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        options(ApiEndPoint.USERS.getPath("*/password"), (request, response) -> { response.header("Allow", "GET, PUT"); response.status(200); return "";});

        put(ApiEndPoint.USERS.getPath("*/apikey"), (request, response) -> {
            return getApi(request).setUserApiKey(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        options(ApiEndPoint.USERS.getPath("*/apikey"), (request, response) -> { response.header("Allow", "GET, PUT"); response.status(200); return "";});


        get(ApiEndPoint.USERS.getPath("*"), (request, response) -> {
            return getApi(request).getUserDetails(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
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
                if(!getApi(request).isMethodAllowed(ApiEndPoint.USERS.getPath(pathToCheck),new SparkApiRequest(request))){
                    halt(405);
                }
            });
        });



        get(ApiEndPoint.FEATURE_TOGGLES.getPath(), (request, response) -> {
            return getApi(request).getFeatureToggles(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        // Super Admin only can amend feature toggles
        post(ApiEndPoint.FEATURE_TOGGLES.getPath(), (request, response) -> {
            return getApi(request).setFeatureToggles(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });

        options(ApiEndPoint.FEATURE_TOGGLES.getPath(), (request, response) -> { response.header("Allow", "GET, POST"); response.status(200); return "";});
        path(ApiEndPoint.FEATURE_TOGGLES.getPath(), () -> {
            before("", (request, response) -> {
                if(!getApi(request).isMethodAllowed(ApiEndPoint.FEATURE_TOGGLES.getPath(),new SparkApiRequest(request))){
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
            // in Heroku this would be set by adding a Config Vars RestListicatorMultiUser
            if(arg.startsWith("-multiuser")){
                singleUserMode=false;
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
