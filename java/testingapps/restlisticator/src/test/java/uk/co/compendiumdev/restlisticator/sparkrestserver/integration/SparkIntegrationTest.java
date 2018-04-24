package uk.co.compendiumdev.restlisticator.sparkrestserver.integration;


import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import spark.Spark;
import uk.co.compendiumdev.Main;
import uk.co.compendiumdev.restlisticator.sparkrestserver.RestServer;
import uk.co.compendiumdev.restlisticator.sparkrestserver.restapi.ApiEndPointNames;
import uk.co.compendiumdev.restlisticator.sparkrestserver.restapi.http.HttpMessageSender;
import uk.co.compendiumdev.restlisticator.sparkrestserver.restapi.http.HttpResponse;

import static uk.co.compendiumdev.strings.Quoter.dbq;

public class SparkIntegrationTest {

    public static HttpMessageSender http;


    // these should mainly test routing and SparkApiRequst/Response mapping
    static RestServer server;

    @BeforeClass
    public static void createServer(){

        String [] args = {};



        try {
            System.out.println("Checking if running for integration tests");
            if(!isRunning()) {
                System.out.println("Not running - starting");
                //Main.main(args);
                server = new RestServer();
                System.out.println("Run main to start");
            }
        }catch(IllegalStateException e){
            e.printStackTrace();
            System.out.println("TODO: Investigate - Illegal state exception thrown starting up Spark when testing");
        }


        // use main instead of server and get full coverage
        //server = new RestServer();

        String host = "localhost:" + Spark.port();

        http = new HttpMessageSender("http://" + host);

        //http.setProxy("localhost", 8080);

        waitForServerToRun();

    }

    public static boolean isRunning(){

        try{
            HttpResponse response = new HttpMessageSender("http://" + "localhost:" + Spark.port()).get(ApiEndPointNames.HEARTBEAT);
            return response.statusCode==200;
        }catch(Exception e){
            return false;
        }

    }


    private static void waitForServerToRun() {
        int tries = 10;
        while(tries>0) {
            try {
                System.out.println("Checking if server has started");
                HttpResponse response = http.get(ApiEndPointNames.HEARTBEAT);
                Assert.assertEquals(200, response.statusCode);
                return;
            }catch(Exception e){
                System.out.println("Server has not started");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
            tries --;
        }

        System.out.println("Server might not have started");
    }

    //@AfterClass
    // even better, just don't switch the server off,
    // and when the tests stop and the static closes down the server will be stopped
    public static void killServer(){

        Spark.stop();

        // wait until server has stopped
        int tries = 10;
        while(tries>0) {
            try {
                System.out.println("Checking if server has stopped");
                HttpResponse response = http.get(ApiEndPointNames.HEARTBEAT);
                Assert.assertEquals(200, response.statusCode);
                try {
                    //Spark.halt();
                    System.out.println("Spark threads " + Spark.activeThreadCount());
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }catch(Exception e){
                System.out.println("Server has stopped");
                return;

            }
            tries --;
        }

        System.out.println("Server might not have stopped");
    }


    protected void setFeatureToggleViaAPI(String toggleName, String trueFalseValue) {
        http.setHeader(http.HEADER_CONTENT_TYPE,http.CONTENT_XML);
        http.setBasicAuth("superadmin", "password");
        String xmlList = dbq(String.format("<toggles><toggle><key>%s</key><value>%s</value></toggle></toggles>", toggleName, trueFalseValue));
        HttpResponse response = http.post(ApiEndPointNames.FEATURE_TOGGLES, xmlList);
        Assert.assertEquals(200, response.statusCode);
    }
}
