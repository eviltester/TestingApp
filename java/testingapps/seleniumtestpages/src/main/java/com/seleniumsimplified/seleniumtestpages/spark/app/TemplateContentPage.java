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
                "alerttest",
                "basicwebpageexample",
                "dynamictableexample",
                "elementattributes",
                "fakealerttest",
                "findbyplayground",
                "htmltableexample",
                "othersites",
                "simplenotetaker",
                "webdriverexamplepage",
        };
        List<String> validPages = Arrays.asList(validPagesArray);

        Boolean isValidPage = validPages.contains(this.appname.toLowerCase(Locale.ROOT));

        String appNameToUse = this.appname.toLowerCase(Locale.ROOT);

        Map appdetails = new HashMap();

        if(!isValidPage){
            // sanitise and try and find
            // this is just to make it faster to add pages
            appNameToUse = appNameToUse.replaceAll("[^a-z0-9]","");
        }

        // try to find the json defn, if not use defaults
        try{
            String appjson = new ResourceReader().asString(
                    "/pagelinks/" +
                            appNameToUse +
                            ".json");

            appdetails = new Gson().fromJson(appjson, Map.class);
        }catch (Exception e){
            appdetails.put("title", appNameToUse);
        }


        String appNavHtml = "";

        if(appdetails.get("title")!=null){
            theTitle = (String)appdetails.get("title");
        }

        if(appdetails.get("urls")!=null){
            List<Map<String,String>> appurls = (List<Map<String,String>>)appdetails.get("urls");

            for (Map<String,String> url : appurls) {
                appNavHtml = appNavHtml + " <a href='" + url.get("url") +  "'>" + url.get("title") + "</a> ";
            }
        }

        Boolean contentGivenInUrl = this.content != null && this.content.length()>0;


        if(contentGivenInUrl && appdetails.get("content")!=null){
            Map<String,String> contentMapping = (Map<String,String>)appdetails.get("content");
            if(contentMapping.get(this.content.toLowerCase(Locale.ROOT))!=null){
                String contentResource = contentMapping.get(this.content.toLowerCase(Locale.ROOT));
                try {
                    bodyText = new ResourceReader().asString(contentResource);
                }catch(Exception e){

                }
                theTitle = theTitle + " - " + this.content;
            }
        }else{
            // try to find default content without configuration through json
            String contentResource = "/pagehtml/" + appNameToUse + "/index.html";
            try{
                bodyText = new ResourceReader().asString(contentResource);
            }catch(Exception e){
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

        // template must have included toc.js
        htmlPage = htmlPage.replace("<!-- TOC -->","<div id='toc'></div>");

        return htmlPage;
    }
}
