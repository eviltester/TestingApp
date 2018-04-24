package com.seleniumsimplified.seleniumtestpages.php;

import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import spark.Request;
import spark.Response;

import java.text.DecimalFormat;

/**
 * Created by Alan on 15/06/2016.
 */
public class PhpCalculate {
    private final Request req;
    private final Response res;

    public PhpCalculate(Request req, Response res) {
        this.req = req;
        this.res = res;
    }

    public String get(){
        return getPage("","","");
    }

    public String post(){

        MultiMap<String> params = new MultiMap<String>();
        UrlEncoded.decodeTo(req.body(), params, "UTF-8");

        return getPage( params.get("number1").get(0),
                        params.get("function").get(0),
                        params.get("number2").get(0));
    }

    private String getPage(String number1, String function, String number2){
        StringBuilder html = new StringBuilder();
        html.append("<html> <head> <title>Selenium Simplified Calculator</title> </head>");
        html.append("<body>");
        html.append("<img src=\"cover_small.gif\" style=\"float:right\"/>");
        html.append("<h1>The \"Selenium Simplified\" Calculator</h1>");
        html.append("<form action=\"calculate.php\" method=\"post\">");
        html.append(String.format("<input type=\"text\" id=\"number1\" name=\"number1\" value=\"%s\" />", number1));

        html.append("<select id=\"function\" name=\"function\">");

        String options[] = {"plus", "times", "minus", "divide"};
        for(String anOption : options){
            html.append(String.format("<option value=\"%s\"", anOption));
            if(anOption.contentEquals(function)){
                html.append(" selected=\"selected\" ");
            }
            html.append(String.format(">%s</option>", anOption));
        }


        html.append("</select>");
        html.append(String.format("<input type=\"text\" id=\"number2\" name=\"number2\" value=\"%s\" />", number2));
        html.append("<input type=\"submit\" id=\"calculate\" value=\"Calculate\"/>");
        html.append("</form>");
        html.append(String.format("Answer : <span id=\"answer\">%s</span>",calculate(number1,function,number2)));
        html.append("</body></html>");

        return html.toString();
    }

    private String calculate(String number1, String function, String number2) {

        try {
            double a = Double.parseDouble(number1);
            double b = Double.parseDouble(number2);


            double val=0;

            if (function.equalsIgnoreCase("plus")) {
                val = a + b;
            }
            if (function.equalsIgnoreCase("times")) {
                val = a * b;
            }
            if (function.equalsIgnoreCase("minus")) {
                val = a - b;
            }
            if (function.equalsIgnoreCase("divide")) {
                if (b == 0) {
                   val = 0;
                } else {
                    val = a / b;
                }
            }

            return new DecimalFormat("#.######").format(val);

        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }

        return "ERR"; // "ERROR N/A";
    }
}
