package uk.co.compendiumdev.restlisticator.sparkrestserver;

import uk.co.compendiumdev.restlisticator.api.Api;
import uk.co.compendiumdev.restlisticator.domain.app.TheListicator;
import uk.co.compendiumdev.restlisticator.http.ApiEndPoint;
import uk.co.compendiumdev.restlisticator.testappconfig.FeatureToggles;

import static spark.Spark.*;

public class RestServer {

    Api api = new Api(new TheListicator());

    // by default running on port 4567

    public RestServer() {

        //port(1234);

        // to support debugging show the path we were called with
//        path("*", () -> {
//            before("", (request, response) -> {
//                System.out.println("Received Request " + request.pathInfo());
//            });
//        });

        // Documentation
        get("/", (request, response) -> {return api.getDocumentation(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });


        // /heartbeat
        get(ApiEndPoint.HEARTBEAT.getPath(), (request, response) -> {return api.getHeartbeat(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
            });
        options(ApiEndPoint.HEARTBEAT.getPath(), (request, response) -> { response.header("Allow", "GET"); response.status(200); return "";});
        path(ApiEndPoint.HEARTBEAT.getPath(), () -> {
            before("", (request, response) -> {
                // TODO distinguish between 401 Unauthorized (no credentials supplied), 403 Forbidden and 405 Not Allowed
                if(!api.isMethodAllowed(ApiEndPoint.HEARTBEAT.getPath(),new SparkApiRequest(request))){
                    halt(405);
                }
            });
        });
        

        // /lists
        get(ApiEndPoint.LISTS.getPath(), (request, response) -> {
            return api.getLists(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        post(ApiEndPoint.LISTS.getPath(), (request, response) -> {
            return api.setLists(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        put(ApiEndPoint.LISTS.getPath(), (request, response) -> {
            return api.putList(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        options(ApiEndPoint.LISTS.getPath(), (request, response) -> { response.header("Allow", "GET, POST, PUT"); response.status(200); return "";});
        // catch unallowed verbs on /lists
        path(ApiEndPoint.LISTS.getPath(), () -> {
            before("", (request, response) -> {
                if(!api.isMethodAllowed(ApiEndPoint.LISTS.getPath(),new SparkApiRequest(request))){
                    halt(405);
                }
            });
        });

        // /lists/*
        get(ApiEndPoint.LISTS.getPath("*"), (request, response) -> {
            return api.getList(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        put(ApiEndPoint.LISTS.getPath("*"), (request, response) -> {
            return api.putList(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        post(ApiEndPoint.LISTS.getPath("*"), (request, response) -> {
            return api.partialAmendList(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        patch(ApiEndPoint.LISTS.getPath("*"), (request, response) -> {
            return api.patchAmendList(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        delete(ApiEndPoint.LISTS.getPath("*"), (request, response) -> {
            return api.deleteList(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        // since this doesn't check the value this is really a bug and should only return 200 when the entity exists, and 404 when it doesn't
        // TODO - POST here is a bug and should be feature toggle, able
        options(ApiEndPoint.LISTS.getPath("*"), (request, response) -> { response.header("Allow", "GET, POST, PUT, PATCH, DELETE"); response.status(200); return "";});
        path(ApiEndPoint.LISTS.getPath("*"), () -> {
            before("", (request, response) -> {
                if(!api.isMethodAllowed(ApiEndPoint.LISTS.getPath("*"),new SparkApiRequest(request))){
                    halt(405);
                }
            });
        });


        get(ApiEndPoint.USERS.getPath(), (request, response) -> {
            return api.getUsers(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        post(ApiEndPoint.USERS.getPath(), (request, response) -> {
            return api.createUserDetails(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        options(ApiEndPoint.USERS.getPath(), (request, response) -> { response.header("Allow", "GET, POST"); response.status(200); return "";});

        put(ApiEndPoint.USERS.getPath("*/password"), (request, response) -> {
            return api.setUserPassword(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        options(ApiEndPoint.USERS.getPath("*/password"), (request, response) -> { response.header("Allow", "GET, PUT"); response.status(200); return "";});

        put(ApiEndPoint.USERS.getPath("*/apikey"), (request, response) -> {
            return api.setUserApiKey(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        options(ApiEndPoint.USERS.getPath("*/apikey"), (request, response) -> { response.header("Allow", "GET, PUT"); response.status(200); return "";});


        get(ApiEndPoint.USERS.getPath("*"), (request, response) -> {
            return api.getUserDetails(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
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
                if(!api.isMethodAllowed(ApiEndPoint.USERS.getPath(pathToCheck),new SparkApiRequest(request))){
                    halt(405);
                }
            });
        });



        get(ApiEndPoint.FEATURE_TOGGLES.getPath(), (request, response) -> {
            return api.getFeatureToggles(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });
        // Super Admin only can amend feature toggles
        post(ApiEndPoint.FEATURE_TOGGLES.getPath(), (request, response) -> {
            return api.setFeatureToggles(new SparkApiRequest(request),new SparkApiResponse(response)).getBody();
        });

        options(ApiEndPoint.FEATURE_TOGGLES.getPath(), (request, response) -> { response.header("Allow", "GET, POST"); response.status(200); return "";});
        path(ApiEndPoint.FEATURE_TOGGLES.getPath(), () -> {
            before("", (request, response) -> {
                if(!api.isMethodAllowed(ApiEndPoint.FEATURE_TOGGLES.getPath(),new SparkApiRequest(request))){
                    halt(405);
                }
            });
        });
        // TODO: fix 404 bug, this should really return an error message in JSON or XML if the desired response is in xml or json - currently always uses HTML
    }


    public RestServer(String[] args) {
        this();
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
        }

    }
}
