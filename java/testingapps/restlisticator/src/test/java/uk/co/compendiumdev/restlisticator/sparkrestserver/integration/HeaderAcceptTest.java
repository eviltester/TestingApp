package uk.co.compendiumdev.restlisticator.sparkrestserver.integration;


import org.junit.Assert;
import org.junit.Test;
import uk.co.compendiumdev.restlisticator.sparkrestserver.restapi.ApiEndPointNames;
import uk.co.compendiumdev.restlisticator.sparkrestserver.restapi.http.HttpResponse;

import java.io.IOException;

public class HeaderAcceptTest extends SparkIntegrationTest{



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
