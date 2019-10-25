package com.seleniumsimplified.seleniumtestpages.spark.app;

import com.seleniumsimplified.seleniumtestpages.ResourceReader;
import com.seleniumsimplified.seleniumtestpages.php.*;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

public class SeleniumTestPagesForSpark {

    public SeleniumTestPagesForSpark(){

        // create backwards compatibility with selenium page on compendiumdev.co.uk
        // avoid redirects

        get("/selenium", (req, res) -> {res.redirect("/selenium.html"); return "";});
        get("/selenium/", (req, res) -> {res.redirect("/selenium.html"); return "";});
        get("/selenium/testpages/", (req, res) -> {res.redirect("/selenium.html"); return "";});


        get("/selenium/ajaxselect.php", (req, res) -> {return new PhpAjaxSelect(req,res).get();});
        get("/selenium/calculate.php", (req, res) -> {return new PhpCalculate(req,res).get();});
        post("/selenium/calculate.php", (req, res) -> {return new PhpCalculate(req,res).post();});
        get("/selenium/refresh.php", (req, res) -> {return new PhpRefresh(req,res).get();});
        post("/selenium/form_processor.php", (req, res) -> {return new PhpFormProcessor(req,res).post();});
        get("/selenium/find_by_playground.php", (req, res) -> {return new ResourceReader().asString("/web/find_by_playground.html");});

        // some tests check the url of the asked for page so don't redirect this one
        get("/selenium/basic_web_page.html", (req, res) -> {return new ResourceReader().asString("/web/basic_web_page.html");});
        get("/selenium/gui_user_interactions.html", (req, res) -> {return new ResourceReader().asString("/web/gui_user_interactions.html");});


        get("/ajaxselect.php", (req, res) -> {return new PhpAjaxSelect(req,res).get();});
        get("/calculate.php", (req, res) -> {return new PhpCalculate(req,res).get();});
        post("/calculate.php", (req, res) -> {return new PhpCalculate(req,res).post();});
        get("/refresh.php", (req, res) -> {return new PhpRefresh(req,res).get();});
        post("/form_processor.php", (req, res) -> {return new PhpFormProcessor(req,res).post();});
        get("/find_by_playground.php", (req, res) -> {return new ResourceReader().asString("/web/find_by_playground.html");});

        // pretty versions
        get("/styled/calculator", (req, res) -> {return new PhpPrettyCalculate(req,res).get();});
        post("/styled/calculator", (req, res) -> {return new PhpPrettyCalculate(req,res).post();});
        post("/the_form_processor.php", (req, res) -> {return new PhpPrettyFormProcessor(req,res).post();}); // pretty template version
        get("/styled/refresh", (req, res) -> {return new PhpPrettyRefresh(req,res).get();});
        get("/styled/frames/get-list", (req, res) -> {return new PhpGetList(req,res).get();});

        post("/styled/search", (req, res) -> {return new PhpPrettySearch(req,res).post();});
        get("/styled/search", (req, res) -> {return new PhpPrettySearch(req,res).get();});

        //search.php  - do not use a search engine, just have a set of random urls that we put up so it looks like a search
        // testing, java, related
        post("/selenium/search.php", (req, res) -> {return new PhpSearch(req,res).post();});
        get("/selenium/search.php", (req, res) -> {return new PhpSearch(req,res).get();});
        post("/search.php", (req, res) -> {return new PhpSearch(req,res).post();});
        get("/search.php", (req, res) -> {return new PhpSearch(req,res).get();});
        // I Was going to rewrite the find_by_playground.php but then realised that the html is actually static now
        // as I just added the output of the old php to the resources and serve it up as a static html page
        //get("/find_by_playground.php", (req, res) -> {return new PhpFindByPlayground(req,res).get();});

        // the upload functionality makes this insecure for external sites - do not release with this active
        post("/uploads/fileprocessor", (req, res) -> {
            Boolean allowUploads = true; // assume working locally

            String envVar = System.getenv("SELENIUM_TEST_PAGES_DISALLOW_UPLOAD");
            if(envVar != null && envVar.length()>0){
                allowUploads = false;
            }
            return new FileUploadProcessor(req,res, allowUploads).prettyOutput().post();
        });
        get("/upload/NoFileUploadsAllowed.txt", (req, res) -> {return new ResourceReader().asString("/web/NoFileUploadsAllowed.txt");});
        get("/upload/*", (req, res) -> {return new UploadedFile(req,res).get("upload/"+req.splat()[0]);});

        // everything else just redirect
        get("/selenium/testpages/*", (req, res) -> {res.redirect("/" + req.splat()[0]); return "";});
        get("/selenium/*", (req, res) -> {res.redirect("/" + req.splat()[0]); return "";});

    }
}
