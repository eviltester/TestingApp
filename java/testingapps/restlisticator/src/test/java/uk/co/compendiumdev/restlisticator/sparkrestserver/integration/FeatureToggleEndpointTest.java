package uk.co.compendiumdev.restlisticator.sparkrestserver.integration;


import org.junit.Assert;
import org.junit.Test;
import uk.co.compendiumdev.restlisticator.sparkrestserver.restapi.ApiEndPointNames;
import uk.co.compendiumdev.restlisticator.sparkrestserver.restapi.http.HttpResponse;

import static uk.co.compendiumdev.strings.Quoter.dbq;

public class FeatureToggleEndpointTest extends SparkIntegrationTest {

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
