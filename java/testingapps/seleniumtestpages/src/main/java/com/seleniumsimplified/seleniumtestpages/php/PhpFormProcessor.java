package com.seleniumsimplified.seleniumtestpages.php;

import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import spark.Request;
import spark.Response;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Alan on 15/06/2016.
 */
public class PhpFormProcessor {
    private final Request req;
    private final Response res;
    private StringBuilder html;

    public PhpFormProcessor(Request req, Response res) {
        this.req = req;
        this.res = res;
    }

    public String post() {

        html = new StringBuilder();

        html.append("<html><head><title>Processed Form Details</title></head>");
        html.append("<body>");

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

        addLine("<p>Submitted Values</p>");

        for(String param : theParamKeys){
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

        if(params.get("checkboxes[]")==null) {
            addLine("<p><strong>No Value for checkboxes</strong></p>");
        }

        if(params.get("multipleselect[]")==null) {
            addLine("<p><strong>No Value for multipleselect</strong></p>");
        }

        if(params.get("filename")==null) {
            addLine("<p><strong>No Value for filename</strong></p>");
        }


        if(req.queryParams("ajax")!=null){
            addLine("<a href='basic_ajax.html' id='back_to_form'>Go back to the Ajax form</a>");
        }else{
            addLine("<a href='basic_html_form.html' id='back_to_form'>Go back to the main form</a>");
        }

        html.append("</body>");
        html.append("</html>");
        return html.toString();
    }

    private void addLine(String s) {
        html.append(s);
        html.append(System.lineSeparator());
    }
}
