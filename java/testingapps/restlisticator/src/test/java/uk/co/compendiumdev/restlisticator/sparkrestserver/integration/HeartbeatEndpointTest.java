package uk.co.compendiumdev.restlisticator.sparkrestserver.integration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;
import uk.co.compendiumdev.http.HttpMessageSender;
import uk.co.compendiumdev.http.HttpResponse;
import uk.co.compendiumdev.restlisticator.http.HttpStatusCode;
import uk.co.compendiumdev.restlisticator.sparkrestserver.integration.listicatorstarter.RestListicatorSparkStarter;
import uk.co.compendiumdev.restlisticator.sparkrestserver.restapi.ApiEndPointNames;


public class HeartbeatEndpointTest{


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
