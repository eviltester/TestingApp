package com.javafortesters.pulp.html.gui.snippets;

public class AppPageBuilder {

    private final String title;
    private final StringBuilder pageOutput;
    private final StringBuilder pageBody;
    private final String appversion;

    public AppPageBuilder(String aTitle, String app_version_template_path){
        this.title = aTitle;
        pageOutput = new StringBuilder();
        this.appversion = app_version_template_path;

        pageOutput.append(new PageSnippets(appversion).getPageHead(this.title));
        pageOutput.append(new PageSnippets(appversion).getDropDownMenu());

        pageBody = new StringBuilder();
    }


    public void appendToBody(final String stringToAdd) {
        pageBody.append(stringToAdd);
    }

    @Override
    public String toString() {
        StringBuilder renderedPage = new StringBuilder(pageOutput.toString());

        renderedPage.append(pageBody);

        renderedPage.append(new PageSnippets(appversion).getPageFooter());

        return renderedPage.toString();
    }

}
