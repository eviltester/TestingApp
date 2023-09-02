package com.seleniumsimplified.seleniumtestpages.spark.app;

import com.google.gson.Gson;
import com.seleniumsimplified.seleniumtestpages.ResourceReader;
import spark.Request;
import spark.Response;

import java.util.*;

public class TemplateContentPage {
    private final String appname;
    private final String content;

    public TemplateContentPage(final Request req, final Response res) {
        this.appname=req.queryMap("app").value();
        this.content=req.queryMap("t").value();
    }

    public String get() {

        String bodyText = "<p>Sorry, nothing to render for that page.</p>";
        String theTitle = "Default Page Details";

        String[] validPagesArray = {
                "basicwebpageexample",
                "dynamictableexample",
                "elementattributes",
                "findbyplayground",
                "htmltableexample",
                "othersites",
                "simplenotetaker",
                "webdriverexamplepage",
        };
        List<String> validPages = Arrays.asList(validPagesArray);

        String appNavHtml = "";

        if(this.appname!=null &&
            validPages.contains(this.appname.toLowerCase(Locale.ROOT))){

            String appjson = new ResourceReader().asString(
                    "/pagelinks/" +
                    this.appname.toLowerCase(Locale.ROOT) +
                    ".json");

            Map appdetails =
                    new Gson().fromJson(appjson, Map.class);

            if(appdetails.get("title")!=null){
                theTitle = (String)appdetails.get("title");
            }
            if(appdetails.get("urls")!=null){
                List<Map<String,String>> appurls = (List<Map<String,String>>)appdetails.get("urls");

                for (Map<String,String> url : appurls) {
                    appNavHtml = appNavHtml + " <a href='" + url.get("url") +  "'>" + url.get("title") + "</a> ";
                }
            }
            if(this.content != null && appdetails.get("content")!=null){
                Map<String,String> contentMapping = (Map<String,String>)appdetails.get("content");
                if(contentMapping.get(this.content.toLowerCase(Locale.ROOT))!=null){
                    String contentResource = contentMapping.get(this.content.toLowerCase(Locale.ROOT));
                    bodyText = new ResourceReader().asString(contentResource);
                    theTitle = theTitle + " - " + this.content;
                }
            }
        }

        String htmlPage = new ResourceReader().asString("/web/styled/template.html");

        String enableGoogleAds = "<script async src=\"https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js?client=ca-pub-7132305589272099\"\n" +
                "     crossorigin=\"anonymous\"></script>";

        String verticaladunit = "    <ins class=\"adsbygoogle\"\n" +
                "         style=\"display:inline-block;width:90px;height:728px\"\n" +
                "         data-ad-client=\"ca-pub-7132305589272099\"></ins>";

        htmlPage = htmlPage.replace("<!-- TITLE -->", theTitle);

        htmlPage = htmlPage.replace("<!-- HEAD -->", "<!-- HEAD -->" + "\n" + enableGoogleAds);
        htmlPage = htmlPage.replace("<!-- APPNAVIGATION CONTENT -->", appNavHtml);
        htmlPage = htmlPage.replace("<!-- VERTICALADUNIT -->", verticaladunit);
        htmlPage = htmlPage.replace("<!-- BODY CONTENT -->", bodyText);

        return htmlPage;
    }
}
