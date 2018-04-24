package uk.co.compendiumdev.spark.app;

import static spark.Spark.get;

public class CompendiumDevAppsForSpark {

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
    }
}
