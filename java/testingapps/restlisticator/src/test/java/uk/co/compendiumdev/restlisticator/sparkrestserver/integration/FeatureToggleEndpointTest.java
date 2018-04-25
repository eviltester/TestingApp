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


import static uk.co.compendiumdev.strings.Quoter.dbq;

public class FeatureToggleEndpointTest{

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
    public void featureTogglesSupportsOptions(){

        http.setHeader(http.HEADER_ACCEPT,http.CONTENT_XML);
        HttpResponse response = http.options(ApiEndPointNames.FEATURE_TOGGLES);
        //TODO should be 404 if it does not exist
        Assert.assertEquals(200, response.statusCode);
        Assert.assertEquals("", response.body);
        Assert.assertEquals("GET, POST", response.getHeaders().get("Allow"));
    }

    @Test
    public void onlySuperAdminCanToggleFeaturesWhenNotAuthorizedReturn403() {

        http.setHeader(http.HEADER_CONTENT_TYPE,http.CONTENT_XML);
        http.setBasicAuth("admin", "password");
        String xmlList = dbq("<toggles><toggle><key>BUG_001_FIXED</key><value>false</value></toggle></toggles>");
        HttpResponse response = http.post(ApiEndPointNames.FEATURE_TOGGLES, xmlList);

        // user is valid but not authorized
        Assert.assertEquals(403, response.statusCode);
    }
}
