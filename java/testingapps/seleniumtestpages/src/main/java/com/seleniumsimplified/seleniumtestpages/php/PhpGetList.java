package com.seleniumsimplified.seleniumtestpages.php;

import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Quick hack conversion of ajaxselect.php to Spark
 */
public class PhpGetList {
    private final Request req;
    private final Response res;

    public PhpGetList(Request req, Response res) {
        this.req = req;
        this.res = res;
    }

    public String get() {

        StringBuilder html = new StringBuilder();
        html.append("<html><head></head><body>");


        String theName = req.queryParams("name");
        String theListCount = req.queryParams("list");

        if(theName==null || theName.length()==0){
            theName="Heading";
        }

        html.append("<h1>" + theName + "</h1>");

        int aCount = 0;
        if(theListCount!=null){
            try{
                aCount = Integer.parseInt(theListCount);
            }catch(Exception e){

            }
        }

        if(aCount>0){
            html.append("<ul>");

            for(int x=0;x<aCount;x++){
                html.append(String.format("<li id='%s'>%s</li>", theName.toLowerCase()+x, theName + " List Item "+x));
            }

            html.append("<ul>");
        }


        html.append("</body></html>");
        return html.toString();
    }
}
