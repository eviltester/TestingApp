package uk.co.compendiumdev.restlisticator.api;

import org.junit.Assert;
import org.junit.Test;
import uk.co.compendiumdev.restlisticator.domain.app.TheListicator;

public class GeneralApiTest {

    @Test
    public void canGetHeartbeatViaAPI() {

        Api api = new Api(new TheListicator());

        ApiRequest request = new TestApiRequest();
        ApiResponse response = new TestApiResponse();

        Assert.assertEquals("",
                api.getHeartbeat(request, response).getBody());

        Assert.assertEquals(200, response.getStatus());
    }
}
