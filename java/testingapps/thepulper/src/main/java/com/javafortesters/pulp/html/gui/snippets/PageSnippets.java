package com.javafortesters.pulp.html.gui.snippets;

import com.javafortesters.pulp.html.templates.MyTemplate;
import com.javafortesters.pulp.reader.ResourceReader;

public class PageSnippets {


    // e.g. v001
    private final String appversion;
    private final String snippetsPathPrefix;

    public PageSnippets(final String appversion_template_path) {
        this.appversion = appversion_template_path;
        snippetsPathPrefix = String.format("/web/apps/pulp/%s/page-template/snippets/", this.appversion);
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
        template.replace("<!-- VERSION -->", appversion);
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
