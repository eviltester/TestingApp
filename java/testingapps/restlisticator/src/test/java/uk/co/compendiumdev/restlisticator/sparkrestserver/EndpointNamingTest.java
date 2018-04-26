package uk.co.compendiumdev.restlisticator.sparkrestserver;

import org.junit.After;
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

    @Test
    public void canMoveApi(){
        // api might be nested in another app so we need the ability to nest the api
        ApiEndPoint.setUrlPrefix("listicator");
        Assert.assertEquals("/listicator/heartbeat", ApiEndPoint.HEARTBEAT.getPath());
        Assert.assertEquals("/listicator/lists", ApiEndPoint.LISTS.getPath());
        ApiEndPoint.clearUrlPrefix();
    }

    @Test
    public void canMoveApiStartsWith(){
        // api might be nested in another app so we need the ability to nest the api
        ApiEndPoint.setUrlPrefix("/listicator");
        Assert.assertEquals("/listicator/heartbeat", ApiEndPoint.HEARTBEAT.getPath());
        Assert.assertEquals("/listicator/lists", ApiEndPoint.LISTS.getPath());
        ApiEndPoint.clearUrlPrefix();
    }

    @Test
    public void canMoveApiEndsWith(){
        // api might be nested in another app so we need the ability to nest the api
        ApiEndPoint.setUrlPrefix("listicator/");
        Assert.assertEquals("/listicator/heartbeat", ApiEndPoint.HEARTBEAT.getPath());
        Assert.assertEquals("/listicator/lists", ApiEndPoint.LISTS.getPath());
        ApiEndPoint.clearUrlPrefix();
    }

    @Test
    public void canMoveApiBoth(){
        // api might be nested in another app so we need the ability to nest the api
        ApiEndPoint.setUrlPrefix("/listicator/");
        Assert.assertEquals("/listicator/heartbeat", ApiEndPoint.HEARTBEAT.getPath());
        Assert.assertEquals("/listicator/lists", ApiEndPoint.LISTS.getPath());
        ApiEndPoint.clearUrlPrefix();
    }

    @Test
    public void canMoveApiBack(){
        // api might be nested in another app so we need the ability to nest the api
        ApiEndPoint.setUrlPrefix("/listicator/");
        Assert.assertEquals("/listicator/heartbeat", ApiEndPoint.HEARTBEAT.getPath());
        Assert.assertEquals("/listicator/lists", ApiEndPoint.LISTS.getPath());
        ApiEndPoint.clearUrlPrefix();

        Assert.assertEquals("/heartbeat", ApiEndPoint.HEARTBEAT.getPath());
        Assert.assertEquals("/lists", ApiEndPoint.LISTS.getPath());

    }

    @After
    public void clearThePrefix(){
        ApiEndPoint.clearUrlPrefix();
    }

}
