package com.seleniumsimplified.seleniumtestpages.spark.app;

import com.seleniumsimplified.seleniumtestpages.ResourceReader;
import com.seleniumsimplified.seleniumtestpages.php.*;

import java.util.Locale;

import static spark.Spark.*;

public class SeleniumTestPagesForSpark {

    public SeleniumTestPagesForSpark(){

        // handle migration from testpages.herokuapp.com
        // to eviltester subdomain
        before("*", ((request, response) -> {

            // only perform redirects on GET requests
            if(!request.requestMethod().toLowerCase(Locale.ROOT).equals("GET")){
                return;
            }

            String url = request.url();

            if(request.queryString()!=null){
                url = url + "?" + request.queryString();
            }

            String redirectTo = "";

            if(url.contains("//testpages.herokuapp.com")){
                url = url.replace("//testpages.herokuapp.com",
                        "//testpages.eviltester.com");
                redirectTo = url;
            }

            if (url.startsWith("http://testpages.herokuapp.com") ||
                url.startsWith("http://testpages.eviltester.com"))
            {
                final String[] split = url.split("http://");
                redirectTo = "https://" + split[1];
            }

            if(!redirectTo.equals("")){
                response.redirect(redirectTo);
            }
        }));

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
        post("/legacy/form_processor.php", (req, res) -> {return new PhpFormProcessor(req,res).post();});
        get("/selenium/find_by_playground.php", (req, res) -> {return new ResourceReader().asString("/web/legacy/find_by_playground.html");});

        // some tests check the url of the asked for page so don't redirect this one
        get("/selenium/basic_web_page.html", (req, res) -> {return new ResourceReader().asString("/web/legacy/basic_web_page.html");});
        get("/selenium/gui_user_interactions.html", (req, res) -> {return new ResourceReader().asString("/web/legacy/gui_user_interactions.html");});

        get("/ajaxselect.php", (req, res) -> {return new PhpAjaxSelect(req,res).get();});
        get("/legacy/ajaxselect.php", (req, res) -> {return new PhpAjaxSelect(req,res).get();});
        get("/calculate.php", (req, res) -> {return new PhpCalculate(req,res).get();});
        get("/legacy/calculate.php", (req, res) -> {return new PhpCalculate(req,res).get();});
        post("/calculate.php", (req, res) -> {return new PhpCalculate(req,res).post();});
        post("/legacy/calculate.php", (req, res) -> {return new PhpCalculate(req,res).post();});
        get("/refresh.php", (req, res) -> {return new PhpRefresh(req,res).get();});
        get("/legacy/refresh.php", (req, res) -> {return new PhpRefresh(req,res).get();});
        post("/form_processor.php", (req, res) -> {return new PhpFormProcessor(req,res).post();});
        get("/find_by_playground.php", (req, res) -> {return new ResourceReader().asString("/web/legacy/find_by_playground.html");});
        get("/legacy/find_by_playground.php", (req, res) -> {return new ResourceReader().asString("/web/legacy/find_by_playground.html");});

        // pretty versions
        get("/styled/calculator", (req, res) -> {return new PhpPrettyCalculate(req,res).get();});
        post("/styled/calculator", (req, res) -> {return new PhpPrettyCalculate(req,res).post();});
        post("/styled/the_form_processor.php", (req, res) -> {return new PhpPrettyFormProcessor(req,res).post();}); // pretty template version
        get("/styled/refresh", (req, res) -> {return new PhpPrettyRefresh(req,res).get();});
        get("/styled/frames/get-list", (req, res) -> {return new PhpGetList(req,res).get();});

        post("/styled/search", (req, res) -> {return new PhpPrettySearch(req,res).post();});
        get("/styled/search", (req, res) -> {return new PhpPrettySearch(req,res).get();});

        get("/styled/redirect/user-agent-redirect-test", (req, res) -> {
            if(req.userAgent()!=null && req.userAgent().length()>0){
                // this is actually better than I thought "(?i:.*(mobile|blackberry|mini).*)"
                // so using "(.*(Mobile).*)" misses more
                if(req.userAgent().matches(("(.*(Mobile).*)"))){
                    res.redirect("/styled/redirect/mobile/user-agent-mobile-test"); return "";
                }
            }
            return new ResourceReader().asString("/web/styled/redirect/user-agent-redirect-test.html");
        });

        get("/styled/redirect/mobile/user-agent-mobile-test", (req, res) -> {
            return new ResourceReader().asString("/web/styled/redirect/mobile/user-agent-mobile-test.html").replace("<!-- USERAGENT -->", req.userAgent());
        });

        get("/styled/webdriver-example-page", (req, res) -> {
            String numstxt=req.queryMap("number-entry").value();
            String text = new NumTextParser().parse(numstxt);
            String page = new ResourceReader().asString("/web/styled/webdriver-example-page.html").
                    replace("class=\"message\"></p>", "class=\"message\">" + text + "</p>");
            return page;
        });


        get("/styled/page", (req, res) -> {
            {return new TemplateContentPage(req,res).get();}
        });

        //search.php  - do not use a search engine, just have a set of random urls that we put up so it looks like a search
        // testing, java, related
        post("/selenium/search.php", (req, res) -> {return new PhpSearch(req,res).post();});
        get("/selenium/search.php", (req, res) -> {return new PhpSearch(req,res).get();});
        post("/search.php", (req, res) -> {return new PhpSearch(req,res).post();});
        get("/search.php", (req, res) -> {return new PhpSearch(req,res).get();});
        post("/legacy/search.php", (req, res) -> {return new PhpSearch(req,res).post();});
        get("/legacy/search.php", (req, res) -> {return new PhpSearch(req,res).get();});
        // I Was going to rewrite the find_by_playground.php but then realised that the html is actually static now
        // as I just added the output of the old php to the resources and serve it up as a static html page
        //get("/find_by_playground.php", (req, res) -> {return new PhpFindByPlayground(req,res).get();});


        /*
            When migrating compendiumdev.co.uk to static site, we have some
            'loose' pages which were used in the book API Testing a REST API
            redirect these to the testing app.
            compendiumdev.co.uk/apps/mocktracks/projectsjson.php
         */
        get("/apps/mocktracks/projectsjson.php", (req, res) -> {
            res.header("Content-Type","application/json");
            return new ResourceReader().
                asString(
                        "/web/mocktracks/projects.json");
        });
        get("/apps/mocktracks/projectsxml.php", (req, res) -> {
            res.header("Content-Type","application/xml");
            return new ResourceReader().
                    asString(
                            "/web/mocktracks/projects.xml");
        });
        get("/apps/mocktracks/reflect.php", (req, res) -> {
            return "GET\n\n"+req.body();
        });
        post("/apps/mocktracks/reflect.php", (req, res) -> {
            return "POST\n\n"+req.body();
        });
        put("/apps/mocktracks/reflect.php", (req, res) -> {
            return "PUT\n\n"+req.body();
        });


        // the upload functionality makes this insecure for external sites - do not release with this active
        post("/uploads/fileprocessor", (req, res) -> {
            Boolean allowUploads = true; // assume working locally
            Boolean allowSaving = false; // assume care about security

            String envVar = System.getenv("SELENIUM_TEST_PAGES_DISALLOW_UPLOAD");
            if(envVar != null && envVar.length()>0){
                allowUploads = false;
            }
            String envSaveVar = System.getenv("SELENIUM_TEST_PAGES_ALLOW_UPLOAD_FILE_SAVING");
            if(envSaveVar != null && envSaveVar.length()>0){
                if(envSaveVar.equalsIgnoreCase("TRUE")) {
                    allowSaving = true;
                }
            }
            // add configuration to allow saving which is currently not enabled
            return new FileUploadProcessor(req,res, allowUploads, allowSaving).prettyOutput().post();
        });

        post("/validate/input-validation", (req, res) -> {return new InputValidationProcessor(req,res).post();});

        get("/uploads/fileprocessor", (req, res) -> {res.redirect("/styled/file-upload-test.html"); return "";});
        get("/upload/NoFileUploadsAllowed.txt", (req, res) -> {return new ResourceReader().asString("/web/NoFileUploadsAllowed.txt");});
        get("/upload/*", (req, res) -> {return new UploadedFile(req,res).get("upload/"+req.splat()[0]);});

        get("/download/*", (req, res) -> {return new FileDownloadProcessor(req, res).get(req.splat()[0]);});
        post("/download/*", (req, res) -> {res.redirect("/download/"+req.splat()[0]); return "";});

        // everything else just redirect
        get("/selenium/testpages/*", (req, res) -> {res.redirect("/" + req.splat()[0]); return "";});
        get("/selenium/*", (req, res) -> {res.redirect("/" + req.splat()[0]); return "";});

    }
}
