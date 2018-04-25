package uk.co.compendiumdev.restlisticator.sparkrestserver.mock;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import spark.Spark;
import uk.co.compendiumdev.http.HttpMessageSender;
import uk.co.compendiumdev.http.HttpResponse;
import uk.co.compendiumdev.restlisticator.sparkrestserver.integration.listicatorstarter.RestListicatorSparkStarter;


/**
 * A Test which uses the app to create a set of Mock request versions
 */
public class CreateMockApiFilesTest {

    // used to create the Mock API files

    protected HttpMessageSender http;

    @BeforeClass
    public static void startServer() {
        RestListicatorSparkStarter.get("localhost").startSparkAppIfNotRunning(4567);
    }

    @Before
    public void httpConnect() {
        http = new HttpMessageSender("http://" + "localhost:" + Spark.port());
    }

    // TODO: this looks unfinished and I don't think it does anything

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
