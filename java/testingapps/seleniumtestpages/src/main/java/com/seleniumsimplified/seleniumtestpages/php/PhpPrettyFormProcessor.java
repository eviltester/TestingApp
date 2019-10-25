package com.seleniumsimplified.seleniumtestpages.php;

import com.seleniumsimplified.seleniumtestpages.ResourceReader;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 An updated pretty version of the form processor
 */
public class PhpPrettyFormProcessor {
    private final Request req;
    private final Response res;
    private String htmlPage;
    private StringBuilder html;

    public PhpPrettyFormProcessor(Request req, Response res) {
        this.req = req;
        this.res = res;
    }

    public String post() {

        htmlPage = new ResourceReader().asString("/web/styled/template.html");

        htmlPage = htmlPage.replace("<!-- TITLE -->", "Processed Form Details" );

        html =new StringBuilder();

        html.append("    <h1>Processed Form Details</h1>\n" +
                "\n" +
                "    <div class=\"explanation\">\n" +
                "        <p>You submitted a form. The details below show the values you entered for processing.\n" +
                "        </p>\n" +
                "    </div>");


        addLine("<div class='centered form-results'>");

        // for backwards compatibility with PHP we should process the form fields in the order they are submitted
        String[] paramKeys = req.body().split("&");
        Set<String> theParamKeys = new LinkedHashSet<>();
        int index=0;
        for(String paramKey : paramKeys){
            int trimFrom = paramKey.indexOf("=");
            paramKeys[index] = paramKey.substring(0,trimFrom).replace("%5B%5D","[]");
            if(!theParamKeys.contains(paramKeys[index])){
                theParamKeys.add(paramKeys[index]);
            }
            index++;
        }

        //now decode the form into its name value,value pairs
        MultiMap<String> params = new MultiMap<String>();
        UrlEncoded.decodeTo(req.body(), params, "UTF-8");

        if(params.get("submitbutton")==null) {
            addLine("<p id='_valuesubmitbutton'>You did not click the submit button</p>");
        }

        addLine("<h2>Submitted Values</h2>");


        /**
         *
         * Hidden stuff in the form that we don't report
         * because it is just for our use
         *
         */
        List<String> skipKeys = new ArrayList();
        skipKeys.add("form_return");
        skipKeys.add("reportmissingparams");





        for(String param : theParamKeys){
            if(skipKeys.contains(param)){
                continue;
            }
            if(params.get(param) == null) {
                addLine(String.format("<p><strong>No Value for %s</strong></p>", param));
            }else{

                List<String> value = params.get(param);

                if(value.size()==0 || value.get(0).length()==0){
                    addLine(String.format("<p><strong>No Value for %s</strong></p>", param));
                }else{

                    boolean paramIsArray = param.contains("[]");
                    String paramDisplayName = param.replace("[]","");

                    addLine(String.format("<div id='_%s'>",paramDisplayName));
                    addLine(String.format("<p name='_%s'><strong>%s</strong></p>", paramDisplayName, paramDisplayName));

                    addLine("<ul>");

                    if(paramIsArray) {
                        int count=0;
                        for (String aValue : value) {
                            addLine(String.format("<li id='_value%s%d'>%s</li>", paramDisplayName, count, aValue));
                            count++;
                        }
                    }else{
                        addLine(String.format("<li id='_value%s'>%s</li>",paramDisplayName, value.get(0)));
                    }
                    addLine("</ul>");

                    addLine("</div>");
                }
            }

        }

        // the following controlled by a hidden field because this is only used for one form
        // reportmissingparams

        if(params.get("reportmissingparams")!=null) {

            System.out.println(params.get("reportmissingparams"));
            String[] splitnames = params.get("reportmissingparams").get(0).split(",");

            for(String paramname : splitnames){
                String checkName = paramname.trim();
                if (params.get(checkName) == null) {
                    addLine(String.format("<p><strong>No Value for %s</strong></p>", checkName.replace("[","").replace("]","")));
                }
            }
        }

        addLine("<div class='form-label'>");
        if(params.get("form_return")!=null){
            addLine(String.format("<a href='%s' class='styled-click-button' id='back_to_form'>Go back to the form</a>", params.get("form_return").get(0)));
        }else{
            // if no url provided then have generic back button
            addLine("<a class='styled-click-button' href='#' id='back_to_form' onclick='window.history.back()'>Go back</a>");
        }
        addLine("</div>");

        addLine("</div>");

        htmlPage = htmlPage.replace("<!-- BODY CONTENT -->", html.toString());

        return htmlPage;
    }

    private void addLine(String s) {
        html.append(s);
        html.append(System.lineSeparator());
    }
}
