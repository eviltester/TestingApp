package uk.co.compendiumdev.spark.app;

import spark.Spark;
import uk.co.compendiumdev.spark.app.mock.MockApi;

import static spark.Spark.get;
import static spark.Spark.staticFileLocation;

public class CompendiumDevAppsForSpark {

    public MockApi mockApi = new MockApi();

    public CompendiumDevAppsForSpark(){
        // javascript games
        get("/games/", (req, res) -> {res.redirect("/games/buggygames/index.html"); return "";});
        get("/games", (req, res) -> {res.redirect("/games/buggygames/index.html"); return "";});
        get("/games/buggygames", (req, res) -> {res.redirect("/games/buggygames/index.html"); return "";});
        get("/games/buggygames/", (req, res) -> {res.redirect("/games/buggygames/index.html"); return "";});

        // apps
        get("/apps/eprimer/", (req, res) -> {res.redirect("/apps/eprimer/eprimer.html"); return "";});
        get("/apps/eprimer", (req, res) -> {res.redirect("/apps/eprimer/eprimer.html"); return "";});

        get("/apps/7charval/", (req, res) -> {res.redirect("/apps/7charval/simple7charvalidation.htm"); return "";});
        get("/apps/7charval", (req, res) -> {res.redirect("/apps/7charval/simple7charvalidation.htm"); return "";});


        //sloganizer header redirects to avoid going on the internet
        get("/page/dearEvilTester", (req, res) -> {res.redirect("/apps/pages/dear-evil-tester.html"); return "";});
        get("/contact.html", (req, res) -> {res.redirect("/apps/pages/contact.html"); return "";});

        get("/apps/reflect", (req, res) -> { res.status(200); res.type("text/html"); return mockApi.reflect(req);});
        get("/apps/reflect/*", (req, res) -> { res.status(200); res.type("text/html"); return mockApi.reflect(req);});

        // Backwards compatible url i.e. https://www.compendiumdev.co.uk/apps/api/mock/reflect
        get("/apps/api/mock/reflect", (req, res) -> { res.status(200); res.type("text/html"); return mockApi.reflect(req);});
        get("/apps/api/mock/reflect/*", (req, res) -> { res.status(200); res.type("text/html"); return mockApi.reflect(req);});

        
    }

    public static CompendiumDevAppsForSpark runLocally(Integer proxyport) {
        Spark.port(proxyport);

        //port(4568); //for testing in case I forget to shutdown
        staticFileLocation("/web");


        // add a shutdown url in case left running on port 4567
        get("/shutdown", (req, res) -> {System.exit(0); return "";});
        get("/heartbeat", (req, res) -> {res.status(200); return "";});


        // because Spark is a singleton - these just have to register their routings, they don't need to do anything else

        get("/", (req, res) -> {
            res.redirect("/index.html"); return "";});
        get("", (req, res) -> {res.redirect("/index.html"); return "";});

        return new CompendiumDevAppsForSpark();
    }
}
