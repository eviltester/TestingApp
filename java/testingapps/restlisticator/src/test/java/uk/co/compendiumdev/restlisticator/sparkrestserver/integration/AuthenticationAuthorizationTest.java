package uk.co.compendiumdev.restlisticator.sparkrestserver.integration;


import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;
import uk.co.compendiumdev.http.HttpMessageSender;
import uk.co.compendiumdev.http.HttpResponse;
import uk.co.compendiumdev.restlisticator.sparkrestserver.integration.apicalls.IntegrationApiCall;
import uk.co.compendiumdev.restlisticator.sparkrestserver.integration.listicatorstarter.RestListicatorSparkStarter;
import uk.co.compendiumdev.restlisticator.sparkrestserver.restapi.ApiEndPointNames;
import uk.co.compendiumdev.restlisticator.sparkrestserver.utils.SimpleMessageProcessor;

import static uk.co.compendiumdev.strings.Quoter.dbq;

public class AuthenticationAuthorizationTest{


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
    public void wwwAuthenticateHeaderSentOn401() {

        // set toggle back to normal otherwise other tests might fail
        IntegrationApiCall.setFeatureToggleViaAPI("BUG_004_FIXED", "true");

        String xmlList;
        HttpResponse response;

        http.setHeader(http.HEADER_CONTENT_TYPE, http.CONTENT_XML);
        http.setBasicAuth("superadmin", "passward");
        xmlList = dbq("<toggles><toggle><key>BUG_001_FIXED</key><value>false</value></toggle></toggles>");
        response = http.post(ApiEndPointNames.FEATURE_TOGGLES, xmlList);

        // incorrect password is authentication failure
        Assert.assertEquals(401, response.statusCode);
        Assert.assertTrue(response.getHeaders().containsKey("WWW-Authenticate"));


        // SET toggle off
        IntegrationApiCall.setFeatureToggleViaAPI("BUG_004_FIXED", "false");

        http.setHeader(http.HEADER_CONTENT_TYPE, http.CONTENT_XML);
        http.setBasicAuth("superadmon", "passward");
        xmlList = dbq("<toggles><toggle><key>BUG_001_FIXED</key><value>false</value></toggle></toggles>");
        response = http.post(ApiEndPointNames.FEATURE_TOGGLES, xmlList);

        // incorrect user is authentication failure
        Assert.assertEquals(401, response.statusCode);
        Assert.assertFalse(response.getHeaders().containsKey("WWW-Authenticate"));



        // set toggle back to normal otherwise other tests might fail
        IntegrationApiCall.setFeatureToggleViaAPI("BUG_004_FIXED", "true");
    }


    @Test
    public void canUseUserAPIKeyInsteadOfBasicAuth() {

        http.setHeader(http.HEADER_ACCEPT,http.CONTENT_XML);
        http.setBasicAuth("superadmin", "password");
        HttpResponse response = http.get(ApiEndPointNames.USERS + "/superadmin");

        Assert.assertEquals(200, response.statusCode);
        Assert.assertTrue(response.body.contains("<apikey>"));

        String apiKey = new SimpleMessageProcessor().getXmlElement("apikey", response.body);

        System.out.println(apiKey);
        Assert.assertTrue(apiKey.length()>0);


        http.deleteHeader(http.HEADER_AUTHORIZATION);
        http.setHeader(http.HEADER_CONTENT_TYPE, http.CONTENT_XML);
        http.setHeader("X-API-AUTH", apiKey);
        String xmlList = dbq("<toggles><toggle><key>BUG_001_FIXED</key><value>false</value></toggle></toggles>");
        response = http.post(ApiEndPointNames.FEATURE_TOGGLES, xmlList);

        // incorrect user is authentication failure
        Assert.assertEquals(200, response.statusCode);

        IntegrationApiCall.setFeatureToggleViaAPI("BUG_001_FIXED", "true");
        http.deleteHeader("X-API-AUTH");


    }
}
