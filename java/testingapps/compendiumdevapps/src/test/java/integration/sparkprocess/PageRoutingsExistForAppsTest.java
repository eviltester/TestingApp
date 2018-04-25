package integration.sparkprocess;

import integration.sparkprocess.http.BasicHttp;
import integration.sparkprocess.server.CompendiumAppsAndGamesSparkStarter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class PageRoutingsExistForAppsTest {

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
    public void canAccess7Charval(){
        Assert.assertTrue(http.isPageAt("/apps/7charval/simple7charvalidation.htm"));
        Assert.assertTrue(http.isPageAt("/apps/7charval/"));
        Assert.assertTrue(http.isPageAt("/apps/7charval"));
    }

    @Test
    public void canAccessBookmarkletsPage(){
        Assert.assertTrue(http.isPageAt("/apps/bookmarklet/version/1/bookmarklets.html"));
    }

    @Test
    public void canAccessEPrimerPage(){
        Assert.assertTrue(http.isPageAt("/apps/eprimer/eprimer.html"));
        Assert.assertTrue(http.isPageAt("/apps/eprimer/"));
        Assert.assertTrue(http.isPageAt("/apps/eprimer"));
    }

    @Test
    public void canAccessiframeSearch(){
        Assert.assertTrue(http.isPageAt("/apps/iframe-search/iframe-search.html"));
    }

    @Test
    public void contactPage(){
        Assert.assertTrue(http.isPageAt("/apps/pages/contact.html"));
        // should also redirect to
        Assert.assertTrue(http.isPageAt("/contact.html"));
    }

    @Test
    public void dearEvilTesterPage(){
        Assert.assertTrue(http.isPageAt("/apps/pages/dear-evil-tester.html"));
        // should also redirect to
        Assert.assertTrue(http.isPageAt("/page/dearEvilTester"));
    }

    @Test
    public void responsiveTestTool(){
        Assert.assertTrue(http.isPageAt("/apps/responsive/v1/responsiveTestTool.html"));
        Assert.assertTrue(http.isPageAt("/apps/responsive/v2/responsiveTestTool.html"));
    }

    @Test
    public void sloganizer(){
        Assert.assertTrue(http.isPageAt("/apps/sloganizer/version/1/sloganizer.html"));
        Assert.assertTrue(http.isPageAt("/apps/sloganizer/version/2/sloganizer.html"));
        Assert.assertTrue(http.isPageAt("/apps/sloganizer/version/3/sloganizer.html"));
    }

    @Test
    public void appsIndex(){
        Assert.assertTrue(http.isPageAt("/apps/index.html"));
    }

    @Test
    public void testWith(){
        Assert.assertTrue(http.isPageAt("/apps/testwith/version/1/testwith.html"));
    }


}
