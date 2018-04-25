package uk.co.compendiumdev.restlisticator.sparkrestserver.integration;


import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;
import uk.co.compendiumdev.http.HttpMessageSender;
import uk.co.compendiumdev.http.HttpResponse;
import uk.co.compendiumdev.restlisticator.sparkrestserver.integration.listicatorstarter.RestListicatorSparkStarter;
import uk.co.compendiumdev.restlisticator.sparkrestserver.restapi.ApiEndPointNames;

import java.io.IOException;

public class HeaderAcceptTest{

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
    public void acceptHeaderDeterminesJSONFormatOfResponse() throws IOException {

        HttpResponse response;

        http.setHeader(http.HEADER_ACCEPT, http.CONTENT_JSON);
        response = http.get(ApiEndPointNames.LISTS);
        Assert.assertEquals(200, response.statusCode);
        Assert.assertEquals("{", response.body.substring(0,1));
    }

    @Test
    public void acceptHeaderDeterminesXMLFormatOfResponse() throws IOException {

        HttpResponse response;

        http.setHeader(http.HEADER_ACCEPT, http.CONTENT_XML);
        response = http.get(ApiEndPointNames.LISTS);
        Assert.assertEquals(200, response.statusCode);
        Assert.assertEquals("<", response.body.substring(0,1));
    }


    @Test
    public void lackOfAcceptHeaderDeterminesFormatOfDefaultJSONResponse() throws IOException {

        HttpResponse response;

        http.headers.remove(http.HEADER_ACCEPT);
        response = http.get(ApiEndPointNames.LISTS);
        Assert.assertEquals(200, response.statusCode);
        Assert.assertEquals("{", response.body.substring(0,1));
    }
}
