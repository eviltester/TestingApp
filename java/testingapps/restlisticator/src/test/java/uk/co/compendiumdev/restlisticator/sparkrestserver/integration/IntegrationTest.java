package uk.co.compendiumdev.restlisticator.sparkrestserver.integration;


import org.junit.Assert;
import org.junit.Test;
import uk.co.compendiumdev.restlisticator.sparkrestserver.restapi.ApiEndPointNames;
import uk.co.compendiumdev.restlisticator.sparkrestserver.restapi.http.HttpResponse;

public class IntegrationTest extends SparkIntegrationTest{


    @Test
    public void a404IsReturned(){

        http.setHeader(http.HEADER_ACCEPT,http.CONTENT_XML);
        HttpResponse response = http.get("/noendpointhere");
        Assert.assertEquals(404, response.statusCode);
    }







}
