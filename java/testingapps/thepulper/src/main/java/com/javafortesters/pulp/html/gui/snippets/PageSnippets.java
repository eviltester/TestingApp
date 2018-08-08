package com.javafortesters.pulp.html.gui.snippets;

import com.javafortesters.pulp.html.templates.MyTemplate;
import com.javafortesters.pulp.reader.ResourceReader;
import com.javafortesters.pulp.spark.AppVersion;

public class PageSnippets {


    // e.g. v001
    private final AppVersion appversion;
    private final String snippetsPathPrefix;

    public PageSnippets(final AppVersion appversionDetails) {
        this.appversion = appversionDetails;
        snippetsPathPrefix = String.format("/web/apps/pulp/%s/page-template/snippets/", this.appversion.getAppVersion());
    }

    public String getPageHead(String title){
        StringBuilder report = new StringBuilder();

        String pageToRender = new ResourceReader().asString(snippetsPathPrefix + "page-header.html");
        MyTemplate template = new MyTemplate(pageToRender);
        template.replace("<!-- TITLE -->", title);
        report.append(template.toString());
        return report.toString();
    }

    public String getDropDownMenu() {
        return getSnippet("dropdownmenu.html");
    }

    public String getPageFooter() {

        StringBuilder report = new StringBuilder();

        String pageToRender = new ResourceReader().asString(snippetsPathPrefix + "page-footer.html");
        MyTemplate template = new MyTemplate(pageToRender);
        template.replace("<!-- VERSION -->", appversion.getAppVersion());
        report.append(template.toString());
        return report.toString();
    }

    public String getSnippet(final String snippet) {

        try {
            final String pageToRender = new ResourceReader().asString( snippetsPathPrefix + snippet);
            final MyTemplate template = new MyTemplate(pageToRender);
            return template.toString();
        }catch(Exception e){
            System.out.println("SNIPPET: " + snippet);
            System.out.println(e.getMessage());
        }

        return "";
    }
}
