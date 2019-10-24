package com.seleniumsimplified.seleniumtestpages.php;

import com.seleniumsimplified.seleniumtestpages.ResourceReader;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import spark.Request;
import spark.Response;

import java.text.DecimalFormat;

public class PhpPrettyCalculate {
    private final Request req;
    private final Response res;

    public PhpPrettyCalculate(Request req, Response res) {
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

        String htmlPage = new ResourceReader().asString("/web/styled/calculate.html");

        htmlPage = htmlPage.replace("!NUMBER1VALUE!", number1);

        StringBuilder html = new StringBuilder();

        String options[] = {"plus", "times", "minus", "divide"};
        for(String anOption : options){
            html.append(String.format("<option value=\"%s\"", anOption));
            if(anOption.contentEquals(function)){
                html.append(" selected=\"selected\" ");
            }
            html.append(String.format(">%s</option>", anOption));
        }

        htmlPage = htmlPage.replace("<!-- options -->", html.toString());

        htmlPage = htmlPage.replace("!NUMBER2VALUE!", number2);

        htmlPage = htmlPage.replace("!ANSWERVALUE!", calculate(number1,function,number2));

        return htmlPage;
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
