package com.seleniumsimplified.seleniumtestpages.php;

import com.seleniumsimplified.seleniumtestpages.ResourceReader;
import spark.Request;
import spark.Response;

public class PhpPrettyRefresh {
    private final Request req;
    private final Response res;
    String htmlPage;

    public PhpPrettyRefresh(Request req, Response res) {
        this.req=req;
        this.res=res;
    }

    public String get() {

        htmlPage = new ResourceReader().asString("/web/styled/template.html");

        // php was using date(U) which provides seconds, so divide millis by 1000
        String refreshDate = String.valueOf(System.currentTimeMillis()/1000);

        htmlPage = htmlPage.replace("<!-- TITLE -->", String.format("Refreshed Page on %s", refreshDate) );

        /*  Copy and paste this into the string for auto formatting
    <h1>Refresh Page Test : !REFRESHDATE!</h1>

    <div class="explanation">
        <p>When this page is refreshed some of the content will change
          e.g the refresh date value <span id="embeddedrefreshdatevalue">!REFRESHDATE!</span>
        </p>
    </div>

    <div class="centered">
        <p id="refreshdisplay">This page refreshed at <span id="refreshdate">!REFRESHDATE!</span></p>
    </div>
         */

        String bodyContent = "    <h1>Refresh Page Test : !REFRESHDATE!</h1>\n" +
                "\n" +
                "    <div class=\"explanation\">\n" +
                "        <p>When this page is refreshed some of the content will change \n" +
                "          e.g the refresh date value <span id=\"embeddedrefreshdatevalue\">!REFRESHDATE!</span>\n" +
                "        </p>\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"centered\">\n" +
                "        <p id=\"refreshdisplay\">This page refreshed at <span id=\"refreshdate\">!REFRESHDATE!</span></p>\n" +
                "    </div>";

        bodyContent = bodyContent.replace("!REFRESHDATE!", refreshDate);

        htmlPage = htmlPage.replace("<!-- BODY CONTENT -->", bodyContent);


        return htmlPage;
    }
}
