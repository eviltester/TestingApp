package com.seleniumsimplified.seleniumtestpages.php;

import spark.Request;
import spark.Response;

/**
 * Created by Alan on 15/06/2016.
 */
public class PhpRefresh {
    private final Request req;
    private final Response res;

    public PhpRefresh(Request req, Response res) {
        this.req=req;
        this.res=res;
    }

    public String get() {
        StringBuilder html = new StringBuilder();
        html.append("<html><head>");

        // php was using date(U) which provides seconds, so divide millis by 1000
        String refreshDate = String.valueOf(System.currentTimeMillis()/1000);

        html.append(String.format("<title>Refreshed Page on %s</title>", refreshDate));

        html.append("</head>\n" +
                "<body>\n" +
                "<p>Refresh me and I'll show a different title</p>\n" +
                "</body>\n" +
                "</html>");

        return html.toString();
    }
}
