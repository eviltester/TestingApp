package com.seleniumsimplified.seleniumtestpages.php;

import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Quick hack conversion of ajaxselect.php to Spark
 */
public class PhpAjaxSelect {
    private final Request req;
    private final Response res;

    public PhpAjaxSelect(Request req, Response res) {
        this.req = req;
        this.res = res;
    }

    public String get() {

        List<String> jsonResponses = new ArrayList<>();
        jsonResponses.add(("[ {'optionValue': 0, 'optionDisplay': 'Javascript'}, \n" +
                "  {'optionValue':1, 'optionDisplay': 'VBScript'}, \n" +
                "  {'optionValue':2, 'optionDisplay': 'Flash'}]").replaceAll("\'","\""));

        jsonResponses.add(("[{'optionValue':10, 'optionDisplay': 'C++'}, \n" +
                "  {'optionValue':11, 'optionDisplay': 'Assembler'}, \n" +
                "  {'optionValue':12, 'optionDisplay': 'C'},\n" +
                "  {'optionValue':13, 'optionDisplay': 'Visual Basic'}]").replaceAll("\'","\""));

        jsonResponses.add(("[{'optionValue':20, 'optionDisplay': 'Cobol'}, \n" +
                "{'optionValue':21, 'optionDisplay':'Fortran'},\n" +
                "{'optionValue':22, 'optionDisplay':'C++'},\n" +
                "{'optionValue':23, 'optionDisplay':'Java'}]").replaceAll("\'","\""));

        String theId = req.queryParams("id");


        int id=0;

        if(theId!=null) {
            id = Integer.decode(theId);
        }

        String output = "";

        switch(id){
            case 1:
            case 2:
            case 3:
                output = jsonResponses.get(id-1);
                break;
        }

        // make the Ajax request pause for 2 seconds to simulate a slightly long ajax call
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return output;
    }
}
