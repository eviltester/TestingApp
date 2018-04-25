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


        CompendiumDevAppsForSpark appPages = CompendiumDevAppsForSpark.runLocally(proxyport);

        System.out.println("Running on " + proxyport);

    }


}
