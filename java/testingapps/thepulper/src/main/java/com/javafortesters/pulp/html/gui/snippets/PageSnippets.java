package com.javafortesters.pulp.html.gui.snippets;

import com.javafortesters.pulp.html.templates.MyTemplate;
import com.javafortesters.pulp.reader.ResourceReader;

public class PageSnippets {

    public String getPageHead(String title){
        StringBuilder report = new StringBuilder();

        String pageToRender = new ResourceReader().asString("/web/apps/pulp/v001/page-template/snippets/page-header.html");
        MyTemplate template = new MyTemplate(pageToRender);
        template.replace("<!-- TITLE -->", title);
        report.append(template.toString());
        return report.toString();
    }

    public String getDropDownMenu() {
        return getSnippet("dropdownmenu.html");
    }

    public String getPageFooter() {
        return getSnippet("page-footer.html");
    }

    public String getSnippet(final String snippet) {

        try {
            final String pageToRender = new ResourceReader().asString("/web/apps/pulp/v001/page-template/snippets/" + snippet);
            final MyTemplate template = new MyTemplate(pageToRender);
            return template.toString();
        }catch(Exception e){
            System.out.println("SNIPPET: " + snippet);
            System.out.println(e.getMessage());
        }

        return "";
    }
}
