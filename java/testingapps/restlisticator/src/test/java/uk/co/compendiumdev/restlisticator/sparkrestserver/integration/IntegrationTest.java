package uk.co.compendiumdev.restlisticator.sparkrestserver.integration;


import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;
import uk.co.compendiumdev.http.HttpMessageSender;
import uk.co.compendiumdev.http.HttpResponse;
import uk.co.compendiumdev.restlisticator.sparkrestserver.integration.listicatorstarter.RestListicatorSparkStarter;


public class IntegrationTest{

    protected HttpMessageSender http;

    @BeforeClass
    public static void startServer() {
        RestListicatorSparkStarter.get("localhost").startSparkAppIfNotRunning(4567);
    }

    @Before
    public void httpConnect() {
        http = new HttpMessageSender("http://" + "localhost:" + Spark.port());
    }

    @Test
    public void a404IsReturned(){

        http.setHeader(http.HEADER_ACCEPT,http.CONTENT_XML);
        HttpResponse response = http.get("/noendpointhere");
        Assert.assertEquals(404, response.statusCode);
    }







}
