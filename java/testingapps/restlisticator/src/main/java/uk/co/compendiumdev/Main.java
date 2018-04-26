package uk.co.compendiumdev;

import spark.Spark;
import uk.co.compendiumdev.restlisticator.sparkrestserver.RestServer;
import uk.co.compendiumdev.restlisticator.testappconfig.FeatureToggles;

/**
 * Created by Alan on 04/07/2017.
 */
public class Main {


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

        // added to support heroku as per https://sparktutorials.github.io/2015/08/24/spark-heroku.html
        // environment can override config for port
        if(hasHerokuAssignedPort()) {
            proxyport = getHerokuAssignedPort();
        }

        for(String arg : args){
            System.out.println("Args: " + arg);

            if(arg.startsWith("-port")){
                String[]details = arg.split("=");
                if(details!=null && details.length>1){
                    proxyport = Integer.parseInt(details[1].trim());
                    System.out.println("Will configure web server to use port " + proxyport);
                }
            }
        }

        // TODO: add a shutdown verb as configurable through arguments e.g. -shutdownable=false

        Spark.port(proxyport);
        
        RestServer restServer = new RestServer(args, "");

        System.out.println("Running on " + Spark.port());

    }
}
