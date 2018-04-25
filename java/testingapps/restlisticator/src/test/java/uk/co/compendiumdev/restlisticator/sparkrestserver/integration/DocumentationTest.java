package uk.co.compendiumdev.restlisticator.sparkrestserver.integration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;
import uk.co.compendiumdev.http.HttpMessageSender;
import uk.co.compendiumdev.http.HttpResponse;
import uk.co.compendiumdev.restlisticator.sparkrestserver.integration.listicatorstarter.RestListicatorSparkStarter;


/**
 * Created by Alan on 18/08/2017.
 */
public class DocumentationTest{

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
    public void canCallRootAndGetHTMLInstructions(){

        http.setHeader(http.HEADER_ACCEPT,http.CONTENT_XML);
        HttpResponse response = http.get("");
        Assert.assertEquals(200, response.statusCode);
        Assert.assertTrue(response.body.startsWith("<html><head><title>REST Listicator"));
    }

    @Test
    public void canCallRootAndGetHTMLInstructionsRoot(){

        http.setHeader(http.HEADER_ACCEPT,http.CONTENT_XML);
        HttpResponse response = http.get("/");
        Assert.assertEquals(200, response.statusCode);
        Assert.assertTrue(response.body.startsWith("<html><head><title>REST Listicator"));
    }


}
