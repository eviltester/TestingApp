package uk.co.compendiumdev.restlisticator.sparkrestserver.mock;

import org.junit.Test;
import uk.co.compendiumdev.restlisticator.sparkrestserver.integration.SparkIntegrationTest;
import uk.co.compendiumdev.restlisticator.sparkrestserver.restapi.http.HttpResponse;

/**
 * A Test which uses the app to create a set of Mock request versions
 */
public class CreateMockApiFilesTest extends SparkIntegrationTest {

    // used to create the Mock API files

    // get heartbeat
    @Test
    public void canMockGetHeartbeat(){


        MockRequestRule rule = new MockRequestRule();
        rule.whenUrlMatches("/heartbeat").
                returnResponse().
                withStatusCode(200);


        HttpResponse response = http.get("/heartbeat");

        System.out.println("REQUEST\n=======\n");
        System.out.println(http.getLastRequest().raw());
        System.out.println("RESPONSE\n=======\n");
        System.out.println(response.raw());
        System.out.println(response.asJson());
        

        // GET /lists
        // POST /lists to create
        // POST /lists to amend
        // PUT /lists/list to amend
        // PUT /lists/list to create

    }

}
