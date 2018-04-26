package uk.co.compendiumdev.restlisticator.sparkrestserver.integration.listicatorstarter;

import spark.Spark;
import uk.co.compendiumdev.http.HttpMessageSender;
import uk.co.compendiumdev.http.HttpResponse;
import uk.co.compendiumdev.restlisticator.sparkrestserver.RestServer;
import uk.co.compendiumdev.restlisticator.sparkrestserver.restapi.ApiEndPointNames;
import uk.co.compendiumdev.sparktesting.SparkStarter;

public class RestListicatorSparkStarter extends SparkStarter {

    private static RestListicatorSparkStarter starter;
    private final String host;
    private final String heartBeatPath;

    private RestListicatorSparkStarter(String host, String heartBeatPath){
        this.host = host;
        this.heartBeatPath = heartBeatPath;


    }

    public static RestListicatorSparkStarter get(String host){

        if(RestListicatorSparkStarter.starter==null) {
            RestListicatorSparkStarter.starter = new RestListicatorSparkStarter(host, ApiEndPointNames.HEARTBEAT);
        }
        return RestListicatorSparkStarter.starter;
    }



    // these should mainly test routing and SparkApiRequst/Response mapping
    static RestServer server;

    public boolean isRunning(){

        try{
            HttpResponse response = new HttpMessageSender("http://" + host + ":" + Spark.port()).get(heartBeatPath);
            return response.statusCode==200;
        }catch(Exception e){
            return false;
        }

    }

    @Override
    public void startServer() {
        //String [] args = {};
        //Main.main(args);
        server = new RestServer("");
        System.out.println("Run main to start");
    }


}
