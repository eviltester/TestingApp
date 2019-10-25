package com.seleniumsimplified.seleniumtestpages.php;

import com.seleniumsimplified.seleniumtestpages.ResourceReader;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PhpPrettySearch {
    private final Request req;
    private final Response res;
    private StringBuilder html;

    public PhpPrettySearch(Request req, Response res) {
        this.req = req;
        this.res = res;
    }


    public String post() {


        List<SearchUrl> urls = SearchUrls.get();


        this.html = new StringBuilder();

        MultiMap<String> params = new MultiMap<String>();
        UrlEncoded.decodeTo(req.body(), params, "UTF-8");

        List<SearchUrl> returnUrls = new ArrayList<>();

        String query = "";

        int urlsToReturn = 20;

        if (params.get("q") != null) {
            query = params.get("q").get(0);
        }

        // add the seleniumrc one to make sure that the exercises work
        SearchUrl seleniumrc = new SearchUrl("http://seleniumhq.org",
                "seleniumhq.org",
                "Selenium Remote-Control",
                "Selenium RC comes in two parts. A server which automatically launches and kills browsers, and acts as a HTTP proxy for web requests from them. ...");

        if (query.equalsIgnoreCase("selenium-rc")) {
            returnUrls.add(seleniumrc);
            urlsToReturn--;
        }

        // try and find some matching in the list

        for (SearchUrl sUrl : urls) {
            boolean addThis = false;

            if (sUrl.description.contains(query)) {
                addThis = true;
            }

            if (sUrl.title.contains(query)) {
                addThis = true;
            }

            if (sUrl.displayUrl.contains(query)) {
                addThis = true;
            }

            if (addThis) {
                returnUrls.add(sUrl);
                urlsToReturn--;
            }

            if (urlsToReturn == 0) {
                break;
            }
        }


        if (urlsToReturn > 0) {
            // randomly choose some urls
            while (urlsToReturn > 0) {
                int totalUrls = urls.size();
                Random r = new Random();
                int Low = 0;
                int rand = r.nextInt(totalUrls - Low) + Low;
                returnUrls.add(urls.get(rand));
                urlsToReturn--;
            }
        }


        return pageHtml(query, returnUrls);
    }

    private String pageHtml(String query, List<SearchUrl> urlsToReturn) {

        String htmlPage = new ResourceReader().asString("/web/styled/template.html");

        String theTitle = "";
        if(query.length()>0) {
            theTitle = String.format("%s  - ", query);
        }
        theTitle = theTitle + "Selenium Simplified Search Engine";


        htmlPage = htmlPage.replace("<!-- TITLE -->", theTitle);

        String theHead = "<script src=\"../cookies_search.js\"></script>";

        htmlPage = htmlPage.replace("<!-- HEAD -->", theHead);


        addLine("<div style=\"float:right\"><img src=\"/cover_small.gif\"><p>Note: This test page uses <br/>cookies as an example to<br/>count your visits and last<br/> search criteria.<br/> Do not use this page if you<br/> are not happy with this use<br/> of cookies.</p></div>\n");
        addLine("<h1>The \"Selenium Simplified\" Search Engine</h1>");

        if(urlsToReturn!=null) {
            addLine(String.format("<SCRIPT>setLastSearchCookie('%s');</SCRIPT>", query));
        }

        addLine("<form method='post' action='search'>");
        String defaultSearchValue="Selenium-RC";
        if(query.length()>0) {
            defaultSearchValue = query;
        }

        addLine("<p>");
        addLine(String.format("Type in a search : <input title='Search' type='text' name='q' value='%s'></p>", defaultSearchValue));
        addLine("<input type='submit' value='Search' name='btnG' />");
        addLine("</p>");

        if(urlsToReturn!=null){
            addLine("<ul ID=\"resultList\">");
            for(SearchUrl url : urlsToReturn){
                addLine(String.format("<li class='resultlistitem'><a href='%s'>%s</a>", url.url, url.displayUrl));
                addLine(String.format("<p><em>%s</em> %s</p></li>", url.title, url.description));
            }
        }

        addLine("</form>");

        addLine("<p class='smaller-text'>");
        addLine("<SCRIPT>document.write (GetLastVisit())</SCRIPT>");
        addLine("</p>");

        htmlPage = htmlPage.replace("<!-- BODY CONTENT -->", html.toString());

        return htmlPage;
    }


    private void addLine(String s) {
        html.append(s);
        html.append(System.lineSeparator());
    }

    public String get() {
        this.html = new StringBuilder();
        return pageHtml("", null);
    }
}
