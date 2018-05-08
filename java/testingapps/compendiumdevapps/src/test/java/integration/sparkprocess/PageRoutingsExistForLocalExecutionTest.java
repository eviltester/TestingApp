package integration.sparkprocess;

import integration.sparkprocess.http.BasicHttp;
import integration.sparkprocess.server.CompendiumAppsAndGamesSparkStarter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class PageRoutingsExistForLocalExecutionTest {

    BasicHttp http;

    @BeforeClass
    public static void ensureAppIsRunning(){
        CompendiumAppsAndGamesSparkStarter.get("localhost", "/heartbeat" ).startSparkAppIfNotRunning(4567);

    }

    @Before
    public void basicHttpSetup(){
        http = new BasicHttp("http","localhost", 4567);
    }

    @Test
    public void localOnlyIndexPages(){
        Assert.assertTrue(http.isPageAt("/"));
        Assert.assertTrue(http.isPageAt(""));
    }

    @Test
    public void localIndexPageContainsURLForAppsIndex(){
        Assert.assertTrue(http.getPageContents("/").contains("href=\"/apps/\">"));
        Assert.assertTrue(http.getPageContents("/").contains("href=\"/games/buggygames/\">"));
    }

    @Test
    public void localIndexPageContainsURLForGamesIndex(){
        Assert.assertTrue(http.getPageContents("").contains("href=\"/apps/\">"));
        Assert.assertTrue(http.getPageContents("").contains("href=\"/games/buggygames/\">"));
    }



}
