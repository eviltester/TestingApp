package integration.sparkprocess.server;

import integration.sparkprocess.http.BasicHttp;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class BasicConnectionTests {

    private BasicHttp http;

    @BeforeClass
    public static void ensureAppIsRunning(){
        CompendiumAppsAndGamesSparkStarter.get("localhost", "/heartbeat" ).startSparkAppIfNotRunning(4567);
    }

    @Before
    public void setupBasicHttp(){
        http = new BasicHttp("http","localhost", 4567);

    }
    @Test
    public void canAccessHeartbeat() throws IOException {
        HttpURLConnection con = (HttpURLConnection)new URL("http","localhost", 4567, "/heartbeat").openConnection();
        int status = con.getResponseCode();

        Assert.assertEquals(200, status);
    }

}
