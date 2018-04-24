package uk.co.compendiumdev.restlisticator.sparkrestserver.integration;

import org.junit.Assert;
import org.junit.Test;
import uk.co.compendiumdev.restlisticator.http.HttpStatusCode;
import uk.co.compendiumdev.restlisticator.sparkrestserver.restapi.ApiEndPointNames;
import uk.co.compendiumdev.restlisticator.sparkrestserver.restapi.http.HttpResponse;

public class HeartbeatEndpointTest extends SparkIntegrationTest{


    @Test
    public void heartBeatRespondsWith200(){

        HttpResponse response = http.get(ApiEndPointNames.HEARTBEAT);
        Assert.assertEquals(
                HttpStatusCode.OK_200,
                response.statusCode);
        Assert.assertEquals("", response.body);
    }

    @Test
    public void heartbeatSupportsOptions(){

        HttpResponse response = http.options(ApiEndPointNames.HEARTBEAT);
        Assert.assertEquals(
                    HttpStatusCode.OK_200,
                    response.statusCode);
        Assert.assertEquals("", response.body);
        Assert.assertEquals("GET", response.getHeaders().get("Allow"));
    }

    @Test
    public void cannotUseIncorrectVerbOnHeartbeat(){

        HttpResponse response = http.post(ApiEndPointNames.HEARTBEAT, "");
        Assert.assertEquals(
                    HttpStatusCode.METHOD_NOT_ALLOWED_405,
                    response.statusCode);

        response = http.put(ApiEndPointNames.HEARTBEAT, "");
        Assert.assertEquals(
                HttpStatusCode.METHOD_NOT_ALLOWED_405,
                response.statusCode);

        response = http.delete(ApiEndPointNames.HEARTBEAT);
        Assert.assertEquals(
                HttpStatusCode.METHOD_NOT_ALLOWED_405,
                response.statusCode);

        response = http.patch(ApiEndPointNames.HEARTBEAT, "");
        Assert.assertEquals(
                HttpStatusCode.METHOD_NOT_ALLOWED_405,
                response.statusCode);

        response = http.head(ApiEndPointNames.HEARTBEAT);
        Assert.assertEquals(
                HttpStatusCode.METHOD_NOT_ALLOWED_405,
                response.statusCode);

        response = http.trace(ApiEndPointNames.HEARTBEAT);
        Assert.assertEquals(
                HttpStatusCode.METHOD_NOT_ALLOWED_405,
                response.statusCode);

        response = http.connect(ApiEndPointNames.HEARTBEAT);
        Assert.assertEquals(
                HttpStatusCode.METHOD_NOT_ALLOWED_405,
                response.statusCode);
    }
}
