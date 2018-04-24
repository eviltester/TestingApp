package uk.co.compendiumdev.restlisticator.api;


import org.junit.Assert;
import org.junit.Test;
import uk.co.compendiumdev.restlisticator.domain.app.TheListicator;
import uk.co.compendiumdev.restlisticator.http.HttpStatusCode;
import uk.co.compendiumdev.restlisticator.testappconfig.FeatureToggles;

public class FeatureTogglesTest {

    // can get list of feature toggles
    // can set a feature toggle to false
    // can set a feature toggle to true

    @Test
    public void canGetFeatureToggles() {

        Api api = new Api(new TheListicator());

        ApiRequest request = new TestApiRequest();
        ApiResponse response = new TestApiResponse();

        request.setUserDetails("user", "password");
        ApiResponse calledResponse = api.getFeatureToggles(request, response);

        System.out.println(calledResponse.getBody());
        Assert.assertNotEquals("",
                calledResponse.getBody());

        Assert.assertEquals(HttpStatusCode.OK_200, response.getStatus());
    }


    @Test
    public void bug006_canGetFeatureTogglesWhenNotAuthenticated() {

        Api api = new Api(new TheListicator());

        ApiRequest request = new TestApiRequest();
        ApiResponse response = new TestApiResponse();

        // BUG 006 feature toggle get is not an authenticated request
        FeatureToggles.BUG_006_FIXED.setState(false);
        //request.setUserDetails("user", "password");
        ApiResponse calledResponse = api.getFeatureToggles(request, response);

        System.out.println(calledResponse.getBody());
        Assert.assertNotEquals("",
                calledResponse.getBody());

        Assert.assertEquals(HttpStatusCode.OK_200, response.getStatus());


        // fix the bug and if you try to do that you are unauthorized
        FeatureToggles.BUG_006_FIXED.setState(true);

        calledResponse = api.getFeatureToggles(request, response);

        System.out.println(calledResponse.getBody());
        Assert.assertNotEquals("",
                calledResponse.getBody());

        Assert.assertEquals(HttpStatusCode.UNAUTHORIZED_401, response.getStatus());
    }


    @Test
    public void canSetFeatureToggles() {

        Api api = new Api(new TheListicator());

        ApiRequest request = new TestApiRequest();
        ApiResponse response = new TestApiResponse();

        ApiResponse getResponse = api.getFeatureToggles(request, response);
        System.out.println(getResponse.getBody());

        request.setUserDetails("superadmin", "password");

        request.setRequestFormat(ApiRequestResponseFormat.JSON);
        request.setBody("{\"toggles\":[{\"key\":\"BUG_001_FIXED\",\"value\":\"false\"},{\"key\":\"BUG_002_FIXED\",\"value\":\"false\"},{\"key\":\"BUG_003_FIXED\",\"value\":\"true\"}]}");
        request.setVerb("post");
        
        ApiResponse calledResponse = api.setFeatureToggles(request, response);

        System.out.println(calledResponse.getBody());
        Assert.assertNotEquals("",
                calledResponse.getBody());

        Assert.assertEquals(200, response.getStatus());

        // put back to normal otherwise other tests will fail
        FeatureToggles.BUG_001_FIXED.setState(true);
        FeatureToggles.BUG_002_FIXED.setState(true);


    }

}
