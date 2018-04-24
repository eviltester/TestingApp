package uk.co.compendiumdev.restlisticator.sparkrestserver;

import org.junit.Assert;
import org.junit.Test;
import uk.co.compendiumdev.restlisticator.http.ApiEndPoint;
import uk.co.compendiumdev.restlisticator.sparkrestserver.restapi.ApiEndPointNames;


public class EndpointNamingTest {

    @Test
    public void endPointsAreConstantlyNamed(){

        Assert.assertEquals("/heartbeat", ApiEndPoint.HEARTBEAT.getPath());
        Assert.assertEquals("/lists", ApiEndPoint.LISTS.getPath());
        Assert.assertEquals("/feature-toggles", ApiEndPoint.FEATURE_TOGGLES.getPath());
        Assert.assertEquals("/users", ApiEndPoint.USERS.getPath());
    }

    @Test
    public void testApiEndPointsAreCorrectlyNamed(){

        Assert.assertEquals(ApiEndPointNames.HEARTBEAT, ApiEndPoint.HEARTBEAT.getPath());
        Assert.assertEquals(ApiEndPointNames.LISTS, ApiEndPoint.LISTS.getPath());
        Assert.assertEquals(ApiEndPointNames.FEATURE_TOGGLES, ApiEndPoint.FEATURE_TOGGLES.getPath());
        Assert.assertEquals(ApiEndPointNames.USERS, ApiEndPoint.USERS.getPath());
    }
}
