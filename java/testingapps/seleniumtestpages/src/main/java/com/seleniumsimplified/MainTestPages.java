package com.seleniumsimplified;

import com.seleniumsimplified.seleniumtestpages.spark.app.SeleniumTestPagesForSpark;
import spark.Spark;


import java.nio.file.Paths;

import static spark.Spark.*;

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
        externalStaticFileLocation(Paths.get("upload").toAbsolutePath().toString());


        // add a shutdown url in case left running on port 4567
        get("/shutdown", (req, res) -> {System.exit(0); return "";});


        // because Spark is a singleton - these just have to register their routings, they don't need to do anything else

        SeleniumTestPagesForSpark seleniumTestPages = new SeleniumTestPagesForSpark();

        System.out.println("Running on " + proxyport);

    }


}
