package com.javafortesters.thepulper;

import com.javafortesters.pulp.spark.app.PulpAppForSpark;
import spark.Spark;

import static spark.Spark.get;
import static spark.Spark.staticFileLocation;

public class MainApp {

    static boolean hasHerokuAssignedPort(){
        ProcessBuilder processBuilder = new ProcessBuilder();
        return (processBuilder.environment().get("PORT") != null);
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (hasHerokuAssignedPort()) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return -1; //return default port if heroku-port isn't set (i.e. on localhost)
    }

    public static void main(String[] args) {

        Integer proxyport = 4567;    // default for spark

        boolean allowShutdown = false;

        if(hasHerokuAssignedPort()) {
            proxyport = getHerokuAssignedPort();
        }

        for(String arg : args){
            if(arg.startsWith("-port")){
                String[]details = arg.split("=");
                if(details!=null && details.length>1){
                    proxyport = Integer.parseInt(details[1].trim());
                    System.out.println("Will configure web server to use port " + proxyport);
                }
            }
            if(arg.startsWith("-allowshutdown")){
                allowShutdown = true;
            }
        }

        Spark.port(proxyport);

        //port(4568); //for testing in case I forget to shutdown
        staticFileLocation("/web");


        if(allowShutdown){
            // add a shutdown url in case left running on port 4567
            get("/shutdown", (req, res) -> {System.exit(0); return "";});
        }

        // because Spark is a singleton - these just have to register their routings, they don't need to do anything else

        get("/", (req, res) -> { res.redirect("/apps/pulp/"); return "";});
        get("", (req, res) -> { res.redirect("/apps/pulp/"); return "";});

        PulpAppForSpark pulpapp = new PulpAppForSpark(allowShutdown);

        System.out.println("Running on " + proxyport);

    }
}
