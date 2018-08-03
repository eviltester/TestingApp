package co.uk.compendiumdev.testing;


import com.javafortesters.pulp.spark.app.PulpAppForSpark;
import com.seleniumsimplified.seleniumtestpages.spark.app.SeleniumTestPagesForSpark;
import spark.Spark;
import uk.co.compendiumdev.restlisticator.sparkrestserver.RestServer;
import uk.co.compendiumdev.spark.app.CompendiumDevAppsForSpark;

import java.nio.file.Paths;

import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.get;
import static spark.Spark.staticFileLocation;

public class CompilationApps {

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

        // required for file upload page in selenium test pages
        externalStaticFileLocation(Paths.get("upload").toAbsolutePath().toString());

        // add a shutdown url in case left running on port 4567
        get("/shutdown", (req, res) -> {System.exit(0); return "";});


        // because Spark is a singleton - these just have to register their routings, they don't need to do anything else



        get("/", (req, res) -> {
            res.redirect("/default.html"); return "";});
        get("", (req, res) -> {
            res.redirect("/default.html"); return "";});

        // All the apps compiled in one place
        CompendiumDevAppsForSpark appPages = new CompendiumDevAppsForSpark();
        SeleniumTestPagesForSpark seleniumTestPages = new SeleniumTestPagesForSpark();
        PulpAppForSpark pulpapp = new PulpAppForSpark();
        RestServer restServer = new RestServer(args, "/listicator");
        restServer.documentationDetails(proxyport);


        System.out.println("Running on " + proxyport);

    }
}
