package uk.co.compendiumdev;

import spark.Spark;
import uk.co.compendiumdev.spark.app.CompendiumDevAppsForSpark;

import static spark.Spark.get;
import static spark.Spark.staticFileLocation;

/**
 * Created by Alan on 15/06/2016.
 */
public class MainTestPages {

    public static void main(String[] args) {

        Integer proxyport = 4567;    // default for spark

        for(String arg : args){
            if(arg.startsWith("-port")){
                String[]details = arg.split("=");
                if(details!=null && details.length>1){
                    proxyport = Integer.parseInt(details[1].trim());
                    System.out.println("Will configure web server to use port " + proxyport);
                }
            }
        }

        Spark.port(proxyport);

        //port(4568); //for testing in case I forget to shutdown
        staticFileLocation("/web");

        // add a shutdown url in case left running on port 4567
        get("/shutdown", (req, res) -> {System.exit(0); return "";});


        // because Spark is a singleton - these just have to register their routings, they don't need to do anything else

        get("/", (req, res) -> {res.redirect("/index.html"); return "";});
        get("", (req, res) -> {res.redirect("/index.html"); return "";});

        CompendiumDevAppsForSpark appPages = new CompendiumDevAppsForSpark();

        System.out.println("Running on " + proxyport);

    }


}
