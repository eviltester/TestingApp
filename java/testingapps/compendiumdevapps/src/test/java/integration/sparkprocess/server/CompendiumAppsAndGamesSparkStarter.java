package integration.sparkprocess.server;

import uk.co.compendiumdev.MainTestPages;
import uk.co.compendiumdev.spark.app.CompendiumDevAppsForSpark;
import uk.co.compendiumdev.sparktesting.SparkStarter;

import java.net.HttpURLConnection;
import java.net.URL;

public class CompendiumAppsAndGamesSparkStarter extends SparkStarter {

    private static CompendiumAppsAndGamesSparkStarter starter;
    private final String host;
    private final String heartBeatPath;

    private CompendiumAppsAndGamesSparkStarter(String host, String heartBeatPath){
        this.host = host;
        this.heartBeatPath = heartBeatPath;


    }

    public static CompendiumAppsAndGamesSparkStarter get(String host, String heartBeatPath){

        if(CompendiumAppsAndGamesSparkStarter.starter==null) {
            CompendiumAppsAndGamesSparkStarter.starter = new CompendiumAppsAndGamesSparkStarter(host, heartBeatPath);
        }
        return CompendiumAppsAndGamesSparkStarter.starter;
    }

    public boolean isRunning(){

        try{
            HttpURLConnection con = (HttpURLConnection)new URL("http",host, sparkport, heartBeatPath).openConnection();
            return con.getResponseCode()==200;
        }catch(Exception e){
            return false;
        }

    }

    @Override
    public void startServer() {
        // I sometimes use the main method
        // it is harder to start and stop reliably
        String[] args = {};
        MainTestPages.main(args);
        //final CompendiumDevAppsForSpark server = CompendiumDevAppsForSpark.runLocally(4567);
    }


}
