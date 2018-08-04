package com.javafortesters.pulp.html.templates;

public class FilledHTMLTemplate {
    private final String appversion;

    public FilledHTMLTemplate(final String appversion) {
        this.appversion = appversion;
    }

    public String searchResultMessage(final String message) {

        final MyTemplate template = new HtmlTemplates(appversion).getSearchResultMessage();
        template.replace("!!MESSAGE!!", message);

        return template.toString();
    }

    public String li(final String li_text, final String id, final String theClass) {
        final MyTemplate template = new HtmlTemplates(appversion).getLi();
        template.replace("!!TEXT!!", li_text);
        template.replace("!!ID!!", id);
        template.replace("!!CLASS!!", theClass);

        return template.toString();
    }

    public String error(final String errorMessage) {
        final MyTemplate template = new HtmlTemplates(appversion).getErrorMessage();
        template.replace("!!MESSAGE!!", errorMessage);
        return template.toString();
    }

    public String namedNewTabLink(final String href, final String linktext, final String name) {
        MyTemplate template = new HtmlTemplates(appversion).getNamedNewTabLink();
        template.replace("!!HREF!!", href);
        template.replace("!!LINKTEXT!!", linktext);
        template.replace("!!NAME!!", name);
        return template.toString();

    }
}
